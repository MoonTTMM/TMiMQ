package com.taimi.mq.comm.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by superttmm on 26/07/2018.
 */
public class HttpAsyncSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int timeout = 5;

    private CloseableHttpAsyncClient httpAsyncClient;

    private FutureCallback<HttpResponse> defaultFutureCallback = new FutureCallback<HttpResponse>() {
        @Override
        public void completed(HttpResponse httpResponse) {
            HttpEntity resEntity = httpResponse.getEntity();
            if (resEntity != null) {
                logger.info("Response content length: " + resEntity.getContentLength());
                try{
                    logger.info("Response content: " + EntityUtils.toString(resEntity, "UTF-8"));
                }catch (IOException e){
                    logger.info("Response content io exception!");
                }
            }
        }

        @Override
        public void failed(Exception e) {
            logger.info("Http async request failed!");
        }

        @Override
        public void cancelled() {
            logger.info("Http request cancelled!");
        }
    };

    public HttpAsyncSender(){
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        httpAsyncClient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(config).build();
        httpAsyncClient.start();
    }

    public void postJson(String url, String jsonString, String token){
        postJson(url, jsonString, null, token);
    }

    public void postXml(String url, String xmlString) {
        HttpEntity entity = new StringEntity(xmlString, ContentType.APPLICATION_XML.withCharset("UTF-8"));
        post(url, entity, null, null);

    }

    public void postJson(String url, String jsonString, FutureCallback<HttpResponse> callback, String token){
        HttpEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON.withCharset("UTF-8"));
        post(url, entity, callback, token);
    }

    public void post(String url, HttpEntity entity, FutureCallback<HttpResponse> callback, String token){
        HttpPost httpPost = new HttpPost(url);
        if (token != null && !token.isEmpty()) {
            httpPost.setHeader("Authorization", token);
        }
        httpPost.setEntity(entity);
        callback = callback == null ? defaultFutureCallback : callback;
        httpAsyncClient.execute(httpPost, callback);
    }

    public void get(String url, String token){
        get(url, null, token);
    }

    public void get(String url, FutureCallback<HttpResponse> callback, String token){
        HttpGet httpGet = new HttpGet(url);
        if (token != null && !token.isEmpty()) {
            httpGet.setHeader("Authorization", token);
        }
        callback = callback == null ? defaultFutureCallback : callback;
        httpAsyncClient.execute(httpGet, callback);
    }
}
