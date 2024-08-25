package fr.thibault.pterodactyl.server;

import com.google.firebase.internal.FirebaseService;
import fr.thibault.pterodactyl.firestore.FirestoreService;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class ServerAction {
    Dotenv dotenv = Dotenv.load();

    public HttpResponse<String> createServer(String userId, int egg, String docker_image, String maxRam, String disk, int allocationId) throws IOException, InterruptedException, ExecutionException {
        FirestoreService firestoreService = new FirestoreService();
        String pseudo = firestoreService.getPseudoFromUserId(userId);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/servers"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"" + userId + "\", \"user\": 1,  \"egg\": \"" + egg + "\",  \"docker_image\": \"" + docker_image +  "\", \"startup\": \"java -Xms128M -Xmx" + maxRam + "M -jar server.jar\",  \"environment\": { \"OWNER_ID\": \"" + userId + "\", \"OWNER_PSEUDO\": \"" + pseudo + "\" },  \"limits\": { \"memory\": \"" + maxRam + "\", \"swap\": 0, \"disk\": \"" + disk + "\", \"io\": 500, \"cpu\": 400 }, \"feature_limits\": { \"databases\": 5,    \"backups\": 1  },  \"allocation\": {    \"default\": " + allocationId + "  }}"))
                .build();


        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> startServer(String serverId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/client/servers/" + serverId + "/power"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"signal\": \"start\"}"))
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    }

    public HttpResponse<String> renameServer(int serverId, String newServerName) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/servers/" + serverId + "/details"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"name\": \"" + newServerName + "\", \"user\": 1}"))
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    }

    public HttpResponse<String> removeServer(int serverId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/servers/" + serverId + "/force"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .DELETE()
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    }
}
