package com.taimi.mq.persistence.mysql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by superttmm on 31/07/2018.
 */
public class PayloadJsonConverter implements AttributeConverter<HashMap<String, String>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashMap<String, String> stringStringHashMap) {
        String modules = "";
        try{
            modules = objectMapper.writeValueAsString(stringStringHashMap);
        }catch (IOException e){
            e.printStackTrace();
        }
        return modules;
    }

    @Override
    public HashMap<String, String> convertToEntityAttribute(String s) {
        HashMap<String, String> payload = null;
        try {
            payload = objectMapper.readValue(s, new TypeReference<HashMap<String, String>>(){});
        }catch (IOException e){
            e.printStackTrace();
        }
        return payload;
    }
}
