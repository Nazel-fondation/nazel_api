package fr.thibault.pterodactyl.controlers;

import fr.thibault.pterodactyl.firestore.FirestoreService;
import fr.thibault.pterodactyl.server.ServerData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class GetMiniGameServerInfo {
    @GetMapping(value = ("/minigames/userserverinfo/{pseudo}"))
    public @ResponseBody ResponseEntity<String> getUserServerInfo(@PathVariable("pseudo") String pseudo) throws IOException, InterruptedException, ExecutionException {  // Remarquez ici l'utilisation de "userid" en minuscule.
        ServerData serverData = new ServerData();
        FirestoreService firestoreService = new FirestoreService();
        String serverId = firestoreService.getUserIdFromPseudo(pseudo);
        String ip = serverData.getUserIdAllocationData(serverId);
        if (Objects.equals(ip, "0.0.0.0")){
            return new ResponseEntity<String> ("{\"status\" : \"server_not_found\"}", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String> ("{\"status\" : \"success\", \"ip\": \"" + ip + "\"}", HttpStatus.OK);
    }

    @GetMapping(value = ("/minigames/serveridentifierinfo/{serveridentifier}"))
    public @ResponseBody ResponseEntity<String> getServerUidInfo(@PathVariable("serveridentifier") String serverIdentifier) throws IOException, InterruptedException {  // Remarquez ici l'utilisation de "userid" en minuscule.
        ServerData serverData = new ServerData();
        String ip = serverData.getServerIdentifierAllocationData(serverIdentifier);
        if (Objects.equals(ip, "0.0.0.0")){
            return new ResponseEntity<String> ("{\"status\" : \"server_not_found\"}", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String> ("{\"status\" : \"success\", \"ip\": \"" + ip + "\"}", HttpStatus.OK);
    }
}
