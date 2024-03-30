package org.example.javafx.request;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import org.example.javafx.AppStore;

public class HttpRequest {
    public static boolean isLocal = false;
    private static HttpClient client = HttpClient.newHttpClient();
    public static String serverUrl = "http://localhost:8080";

//    private static Gson gson = new Gson();

    public void login() {
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/login"))
//                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("response.statusCode===="+response.statusCode());
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
