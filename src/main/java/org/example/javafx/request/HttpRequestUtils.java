package org.example.javafx.request;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import com.google.gson.Gson;
import org.example.javafx.pojo.Result;

import static java.net.http.HttpRequest.newBuilder;

public class HttpRequestUtils {
    public static boolean isLocal = false;
    private static HttpClient client = HttpClient.newHttpClient();
    public static String serverUrl = "http://localhost:8080";
    private static Gson gson = new Gson();

    public Result login(LoginRequest request) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Result result = new Gson().fromJson(response.body(),Result.class);
            System.out.println(result.getCode());
            if(result.getCode().equals(200) == false){
                throw new RuntimeException();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Result(400,null,"未知原因失败");
    }

    public Integer loginAgain(Integer id) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/loginAgain"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(id)))
                .headers("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Integer id0 = new Gson().fromJson(response.body(),Integer.class);
        return id0;
    }


}
