package com.example.part2_1_firstactivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpUtils {

    // get 호출
    public static String get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    // Post -> 아직 테스트 안함
    public static String post(String url, Map<String, String> body, MediaType mediaType) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(new Gson().toJson(body), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /*public static String post(String url, Map<String, String> paramHeader, Map<String, String> body, MediaType mediaType) {
//        if (ObjectUtils.isEmpty(paramHeader)) {
//            throw new IllegalArgumentException("paramHeader is null");
//        }
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(new Gson().toJson(body), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(Headers.of(paramHeader))
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }*/

    // put -> 안해도 될듯

    // patch
    public static String patch(String url, Map<String, String> body, MediaType mediaType) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(new Gson().toJson(body), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .patch(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // delete
    public static String delete(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
