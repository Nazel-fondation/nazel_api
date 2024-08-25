package fr.thibault.pterodactyl.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class ServerData {
    Dotenv dotenv = Dotenv.load();
    public record AllocationData(String ip, int id) {}

    public AllocationData obtainAvailablePort() throws IOException, InterruptedException {
        String ip = "0.0.0.0";
        int id = -1;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/nodes/1/allocations"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode rootNode = jsonMapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");

        for (JsonNode allocationNode : dataArray) {
            JsonNode attributes = allocationNode.get("attributes");
            boolean isAssigned = attributes.get("assigned").asBoolean();
            if (!isAssigned) {
                ip = dotenv.get("SERVER_IP") + ":" + attributes.get("port").asText();
                id = attributes.get("id").asInt();
            }
        }

        return new AllocationData(ip, id);
    }

    public int getUserServerId(String userId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/servers"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode rootNode = jsonMapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");

        for (JsonNode allocationNode : dataArray) {
            JsonNode attributes = allocationNode.get("attributes");
            String serverName = attributes.get("name").asText();
            if (Objects.equals(serverName, userId)) {
                return attributes.get("id").asInt();
            }
        }

        return -1;
    }

    public String getUserIdAllocationData(String userId) throws IOException, InterruptedException {
        String ip = "0.0.0.0";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/servers"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode rootNode = jsonMapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");

        for (JsonNode serverNode : dataArray) {
            JsonNode attributes = serverNode.get("attributes");
            String serverName = attributes.get("name").asText();
            if (Objects.equals(serverName, userId)) {
                ip = getAllocationIp(attributes.get("allocation").asInt());
            }
        }

        return ip;
    }

    public String getServerIdentifierAllocationData(String identifier) throws IOException, InterruptedException {
        String ip = "0.0.0.0";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/servers"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode rootNode = jsonMapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");

        for (JsonNode serverNode : dataArray) {
            JsonNode attributes = serverNode.get("attributes");
            String serverUuid = attributes.get("identifier").asText();
            if (Objects.equals(serverUuid, identifier)) {
                ip = getAllocationIp(attributes.get("allocation").asInt());
            }
        }

        return ip;
    }

    public int getEggIdFromName(String eggName) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/nests/5/eggs"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode rootNode = jsonMapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");

        for (JsonNode eggNode : dataArray) {
            JsonNode attributes = eggNode.get("attributes");
            String name = attributes.get("name").asText();
            if (Objects.equals(name, eggName)) {
                return attributes.get("id").asInt();
            }
        }

        return -1;
    }

    private String getAllocationIp(int allocationId) throws IOException, InterruptedException {
        String ip = "0.0.0.0";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + dotenv.get("PTERODACTYL_API_IP") + "/api/application/nodes/1/allocations"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("PTERODACTYL_API_TOKEN"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode rootNode = jsonMapper.readTree(response.body());
        JsonNode dataArray = rootNode.get("data");

        for (JsonNode allocationNode : dataArray) {
            JsonNode attributes = allocationNode.get("attributes");
            int id = attributes.get("id").asInt();
            if (Objects.equals(allocationId, id)) {
                ip = dotenv.get("SERVER_IP") + ":" + attributes.get("port").asText();
            }
        }

        return ip;
    }
}
