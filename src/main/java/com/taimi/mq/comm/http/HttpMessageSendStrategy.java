package com.taimi.mq.comm.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taimi.mq.comm.MessageSendStrategy;
import com.taimi.mq.comm.MessageSentCallback;
import com.taimi.mq.message.Message;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by superttmm on 25/07/2018.
 */
public class HttpMessageSendStrategy implements MessageSendStrategy {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String MESSAGE_ID = "MessageId";
    private static final String MESSAGE_URL = "url";
    private static final String MESSAGE_PAYLOAD = "payload";
    private static final String MESSAGE_TOKEN = "token";
    private static final String MESSAGE_METHOD = "method";

    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_GET = "GET";

    private ObjectMapper mapper = new ObjectMapper();
    private HttpSender httpSender;

    public HttpMessageSendStrategy(){
        httpSender = new HttpSender();
    }

    @Override
    public void sendMessage(final Message message, final MessageSentCallback callback) {
        Map<String, String> payload = message.getPayload();
        Assert.isTrue(payload.containsKey(MESSAGE_URL));
        Assert.isTrue(payload.containsKey(MESSAGE_METHOD));
        try {
            String method = payload.get(MESSAGE_METHOD);
            ResponseHandler<HttpSender.ErrorCode> responseHandler = httpResponse -> {
                try {
                    String body = EntityUtils.toString(httpResponse.getEntity());
                    int code = httpResponse.getStatusLine().getStatusCode();
                    if(code == HttpStatus.SC_OK) {
                        logger.info("Send successfully: " + message.getId());
                        if(body == null || body.isEmpty()){
                            String messageId = message.getId();
                            callback.handle(messageId);
                        }
                        else {
                            Map<String, Object> map = mapper.readValue(body, new TypeReference<HashMap<String, Object>>() {});
                            if (map.containsKey(MESSAGE_ID)) {
                                String messageId = (String) map.get(MESSAGE_ID);
                                callback.handle(messageId);
                            } else {
                                String messageId = message.getId();
                                callback.handle(messageId);
                            }
                        }
                        return HttpSender.ErrorCode.Success;
                    }else{
                        logger.info(String.format("Send message: %s, with error: %d", message.getId(), code));
                        if(code == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                            return HttpSender.ErrorCode.HttpInternalError;
                        }
                        return HttpSender.ErrorCode.HttpFailException;
                    }
                } catch (IOException e) {
                    logger.error(e.toString());
                    return HttpSender.ErrorCode.HttpEncodingPayloadException;
                }
            };
            if(method.equals(HTTP_METHOD_POST)){
                logger.info("Sending message: " + payload.get(MESSAGE_URL) + ", method: " + payload.get(MESSAGE_METHOD));
                httpSender.postJson(payload.get(MESSAGE_URL), payload.get(MESSAGE_PAYLOAD), responseHandler, payload.get(MESSAGE_TOKEN));
            }
            else if(method.equals(HTTP_METHOD_GET)){
                logger.info("Sending message: " + payload.get(MESSAGE_URL) + ", method: " + payload.get(MESSAGE_METHOD));
                httpSender.get(payload.get(MESSAGE_URL), responseHandler, payload.get(MESSAGE_TOKEN));
            }
        }catch (IllegalArgumentException e){

        }
    }
}
