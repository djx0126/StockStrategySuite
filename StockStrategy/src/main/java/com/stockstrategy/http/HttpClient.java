package com.stockstrategy.http;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dave on 2016/12/13.
 */
public class HttpClient {

    private HttpClient(){}

    public static HttpClient getInstance() {
        return new HttpClient();
    }

    public <T> List<T> getList(String uriStr, Class<T> clazz) {
        return getList(uriStr, new HashMap<>(), clazz);
    }

    public <T> List<T> getList(String uriStr, Map<String, Object> queryParams, Class<T> clazz) {
        URI uri;
        try {
            String uriToSend = uriStr;
            if (queryParams != null && queryParams.size() > 0) {
                String queryParamStr = queryParams.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
                uriToSend += "?" + queryParamStr;
            }
            uri = new URI(uriToSend);
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            ClientHttpRequest req = factory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse resp = req.execute();
            if (resp.getStatusCode().is2xxSuccessful()) {
                String bodyStr = "";
                InputStream is = resp.getBody(); //获得返回数据,注意这里是个流
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String str;
                while((str = br.readLine())!=null){
                    bodyStr += str;
                }

                if (clazz !=null) {
                    JavaType type = JsonMapper.buildNormalMapper().constructParametricType(ArrayList.class, clazz);
                    List<T> entity = JsonMapper.buildNormalMapper().fromJson(bodyStr, type);
                    return entity;
                }
            }
        } catch (URISyntaxException|IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
