package com.taimi.mq.comm.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class HttpSender {

    private final int timeout = 12;
    private CloseableHttpClient httpClient;

    private ResponseHandler<ErrorCode> defaultResponseHandler = httpResponse -> {
        int code = httpResponse.getStatusLine().getStatusCode();
        if(code == HttpStatus.SC_OK){
            return ErrorCode.Success;
        }else{
            if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                return ErrorCode.HttpInternalError;
            }
            return ErrorCode.HttpFailException;
        }
    };

    public HttpSender(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(5);

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(config)
                .build();
    }

    public ErrorCode get(String url, String token){
        return get(url, null, token);
    }

    public ErrorCode get(String url){
        return get(url, "");
    }

    public ErrorCode get(String url, ResponseHandler<ErrorCode> handler, String token){
        try{
            HttpGet httpGet = new HttpGet(url);
            if(token != null && !token.isEmpty()){
                httpGet.setHeader("Authorization", token);
            }
            if(handler == null){
                return httpClient.execute(httpGet, defaultResponseHandler);
            }else{
                return httpClient.execute(httpGet, handler);
            }
        }catch (IOException e){
            e.printStackTrace();
            return ErrorCode.HttpFailException;
        }
    }

    public ErrorCode get(String url, ResponseHandler<ErrorCode> handler){
        return get(url, handler, null);
    }

    public ErrorCode post(String url, List<NameValuePair> params, String token){
        try{
            HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            return postCore(url, entity, token);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return ErrorCode.HttpEncodingPayloadException;
        }
    }

    public ErrorCode post(String url, List<NameValuePair> params){
        return post(url, params, null);
    }

    public ErrorCode postJson(String url, String jsonString, String token){
        return postJson(url, jsonString, null, token);
    }

    public ErrorCode postJson(String url, String jsonString){
        return postJson(url, jsonString, null);
    }

    public ErrorCode postJson(String url, String jsonString, ResponseHandler<ErrorCode> handler, String token){
        HttpEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON.withCharset("UTF-8"));
        return postCore(url, entity, handler, token);
    }

    private ErrorCode postCore(String url, HttpEntity entity, String token) {
        return postCore(url, entity, null, token);
    }

    private ErrorCode postCore(String url, HttpEntity entity, ResponseHandler<ErrorCode> handler, String token){
        try {
            HttpPost httpPost = new HttpPost(url);
            if (token != null && !token.isEmpty()) {
                httpPost.setHeader("Authorization", token);
            }
            httpPost.setEntity(entity);
            if(handler == null) {
                return httpClient.execute(httpPost, defaultResponseHandler);
            }else{
                return httpClient.execute(httpPost, handler);
            }
        }catch (HttpHostConnectException e){
            return ErrorCode.HostDownException;
        }
        catch (IOException e) {
            e.printStackTrace();
            return ErrorCode.HttpFailException;
        }
    }

    enum ErrorCode{
        Success,
        HttpFailException,
        HostDownException,
        HttpEncodingPayloadException,
        HttpInternalError
    }
}