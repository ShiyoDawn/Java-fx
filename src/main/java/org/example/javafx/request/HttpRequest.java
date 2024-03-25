package org.example.javafx.request;

import java.net.http.HttpClient;

public class HttpRequest {
    public static boolean isLocal = false;
    private static HttpClient client = HttpClient.newHttpClient();
    public static String serverUrl = "http://localhost:9090";
}
