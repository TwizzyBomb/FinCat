package hesh.zone.fincat.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Service
public class ChatGptWebClient {

    private static final String API_KEY = "sk-rUEFknreR2fn7B_IAqP69ADsprKwfe3uy2QEQo-aBRT3BlbkFJtdNhQERCIB_5C2SM1a1EWnyZbKKbw2BPczSSPQg90A";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * sendGptHttpRequest
     * Uses HttpClient (standard in j11 java.net.http) can't do reactive streams, complex
     * authentication or WebSocket support. No reactive support
     * @param gptQuery
     */
    public void sendGptHttpRequest(String gptQuery) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Build the JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode message = objectMapper.createObjectNode();
            message.put("role", "user");
            message.put("content", gptQuery);

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.putArray("messages").add(message);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response body
            if (response.statusCode() == 200){
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("Error: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String categorizeTransaction(String description) {
        return "";
    }


}