package com.ark.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class Main {


    private static String openId = "oSQQY0f7Dju6REuFKpSo9gkexqyk";

    private static String pubAppId = "wx90d6c677a8bd4d08";


    private static String appId = "wx0ec00d27f988d2e0";
    private static String appSecret = "6868844b72ab577f2480ff8c519442e1";
    private static String token = "65_HFgLPT2lURKiJ8dqZhwHX4h6Wo1GJJobP1KIPq9CfX5LSJIkeL6itrews_dZkZp2bHi9GfKNX6nACBUqjgAo3fkhsF7uIT-Ivmf3Z-UjiFo2Z17JSe6xKq0N5jIVTQcAFAJHE";

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
//        getToken(httpClient);
        login(httpClient);

    }

    private static void login(HttpClient httpClient) {
        try {
            String str = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid=%s&secret=%s&js_code=%s&access_token=%s";
            HttpRequest buildGet = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(str.formatted(appId, appSecret, "003mxS0w3pVy803Mme4w34Nbi03mxS0w", token)))
                    .build();
            HttpResponse<String> response = httpClient.send(buildGet, HttpResponse.BodyHandlers.ofString());
            log.info("登录：{}", response.body());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getToken(HttpClient httpClient) {
        try {
            String str = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
            HttpRequest buildGet = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(str.formatted(appId, appSecret)))
                    .build();
            HttpResponse<String> response = httpClient.send(buildGet, HttpResponse.BodyHandlers.ofString());
            log.info("获取Token：{}", response.body());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
