package org.example.javafx.request;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import lombok.Data;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.w3c.dom.ls.LSInput;

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
            Result result = new Gson().fromJson(response.body(), Result.class);
            if (result.getCode().equals(200) == false) {
                throw new RuntimeException();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Result(400, null, "未知原因失败");
    }

    public Result getCourse(DataRequest request) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/course/selectAll"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .build();
        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Result result = new Gson().fromJson(httpResponse.body(), Result.class);
//        System.out.println();
//        Iterator<String> it = course.keySet().iterator();
//        while (it.hasNext()){
//            String str = it.next();
//            System.out.println(str);
//        }
        return result;

    }
    public static Result courseField(String url, DataRequest request) throws IOException, InterruptedException{
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Result result = gson.fromJson(response.body(), Result.class);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Result(400 ,null, "未获取到信息");
    }

    public static Result request(String url, DataRequest request) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                //                System.out.println(response.body());
                Result result = gson.fromJson(response.body(), Result.class);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List getMenu(Integer userType) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/menu/get"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userType)))
                .headers("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Result result = new Gson().fromJson(response.body(), Result.class);
            List menuList = new Gson().fromJson(result.getData().toString(),List.class);
            if (result.getCode().equals(200) == false) {
                throw new RuntimeException();
            }else return menuList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List searchMenu(Integer userType, String str) {
        Map param = new HashMap();
        param.put("type", userType);
        param.put("str", str);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/menu/searchMenu"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(param)))
                .headers("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Result result = new Gson().fromJson(response.body(), Result.class);
            List menuList = new Gson().fromJson(result.getData().toString(),List.class);
            if (result.getCode().equals(200) == false) {
                throw new RuntimeException();
            }else return menuList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Result uploadFile(String uri,String filePath,String remoteFile,String person_num)  {
        try {
            Path file = Path.of(filePath);
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl+uri+"?remoteFile="+remoteFile + "&fileName="
                            + person_num+".jpg"))
                    .POST(HttpRequest.BodyPublishers.ofFile(file))
                    .build();
            HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                Result dataResponse = gson.fromJson(response.body(), Result.class);
                return dataResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
