package ua.edu.ukma.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.ukma.exceptions.BadRequestException;

public class JsonMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T parseObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to parse json: " + json + " to " + clazz);
            throw new BadRequestException("Bad Request body", e);
        }
    }

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to serialize object: " + object);
            throw new RuntimeException(e);
        }
    }
}
