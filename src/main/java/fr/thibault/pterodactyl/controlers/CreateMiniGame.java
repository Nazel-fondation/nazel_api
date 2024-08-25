package fr.thibault.pterodactyl.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.thibault.pterodactyl.firestore.FirestoreService;
import fr.thibault.pterodactyl.server.ServerAction;
import fr.thibault.pterodactyl.server.ServerData;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@RestController
public class CreateMiniGame {

	@PostMapping("/minigames/createserver")
	public @ResponseBody ResponseEntity<String> post(@RequestBody String requestBody) throws IOException, InterruptedException, ExecutionException {
		ServerAction serverAction = new ServerAction();
		ServerData serverData = new ServerData();
		ObjectMapper objectMapper = new ObjectMapper();
		FirestoreService firestoreService = new FirestoreService();
		String userId = objectMapper.readTree(requestBody).get("userId").asText();
		String eggName = objectMapper.readTree(requestBody).get("eggName").asText();

		//Verify if userExist
		if (!firestoreService.userIdExist(userId)){
			return new ResponseEntity<String> ("{\"status\" : \"unknown_user\"}", HttpStatus.NOT_FOUND);
		}

		//Verify if user has already a server if there is a server we remove server
		int lastUserServerId = serverData.getUserServerId(userId);
		if ( lastUserServerId != -1){
			HttpResponse<String> removeServerRequest = serverAction.removeServer(lastUserServerId);
			if (removeServerRequest.statusCode() != 204){
				return new ResponseEntity<String> ("{\"status\" : \"impossible_to_remove_last_user_server\", \"pteroResponse\" : " + removeServerRequest.body() + "}", HttpStatusCode.valueOf(removeServerRequest.statusCode()));
			}
		}

		int eggId = serverData.getEggIdFromName(eggName);
		if (eggId == -1){
			return new ResponseEntity<String> ("{\"status\" : \"egg_not_found\"}", HttpStatus.NOT_FOUND);
		}

		//Verify if an allocation is available
		ServerData.AllocationData allocation = serverData.obtainAvailablePort();
		if (allocation.id() == -1){
			return new ResponseEntity<String> ("{\"status\" : \"allocations_already_use\"}", HttpStatus.CONFLICT);
		}

		//Create and startServer
		HttpResponse<String> response = serverAction.createServer(userId, eggId, "ghcr.io/pterodactyl/yolks:java_8", "4096", "4096", allocation.id());
		String jsonBody = response.body();
		String serverId = objectMapper.readTree(jsonBody).path("attributes").path("identifier").asText();
		Thread.sleep(6000); //The time to set up server
		HttpResponse<String> startServer = serverAction.startServer(serverId);
		if (startServer.statusCode() != 204){
			return new ResponseEntity<String> ("{\"status\" : \"impossible_to_start_server\", \"pteroResponse\" : " + startServer.body() + "}", HttpStatusCode.valueOf(startServer.statusCode()));
		}

		return new ResponseEntity<String> ("{\"status\" : \"success\", \"ip\": \"" + allocation.ip() + "\"}", HttpStatus.OK);
	}
}
