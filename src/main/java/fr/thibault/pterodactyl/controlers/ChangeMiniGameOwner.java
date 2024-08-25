package fr.thibault.pterodactyl.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.thibault.pterodactyl.firestore.FirestoreService;
import fr.thibault.pterodactyl.server.ServerAction;
import fr.thibault.pterodactyl.server.ServerData;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@RestController
public class ChangeMiniGameOwner {

    //Just change the server name work like that
    @PostMapping("/minigames/changeowner")
    public @ResponseBody ResponseEntity<String> post(@RequestBody String requestBody) throws IOException, InterruptedException, ExecutionException {
        ServerAction serverAction = new ServerAction();
        ServerData serverData = new ServerData();
        ObjectMapper objectMapper = new ObjectMapper();
        FirestoreService firestoreService = new FirestoreService();
        String serverName = objectMapper.readTree(requestBody).get("serverName").asText();
        String newOwnerPseudo = objectMapper.readTree(requestBody).get("newOwnerPseudo").asText();
        String newServerName = firestoreService.getUserIdFromPseudo(newOwnerPseudo);
        HttpResponse<String> response = serverAction.renameServer(serverData.getUserServerId(serverName), newServerName);
        if (response.statusCode() == 404){
            return new ResponseEntity<String>("{\"status\" : \"error_not_found\"}", HttpStatus.NOT_FOUND);
        }else if(response.statusCode() != 200){
            return new ResponseEntity<String>("{\"status\" : \"error\"}", HttpStatusCode.valueOf(response.statusCode()));
        }
        return new ResponseEntity<String>("{\"status\" : \"success\", \"serverName\" : \"" + newServerName + "\"}", HttpStatus.OK);
    }
}