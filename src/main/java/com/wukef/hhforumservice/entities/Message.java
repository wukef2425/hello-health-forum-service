package com.wukef.hhforumservice.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Message {
    private int errorCode; // Error code used to determine if the operation was successful
    private String message; // Message content
    private Map<String, Object> data = new HashMap<>(); // Data returned to the frontend

    // Constructor initializes defaults
    public Message() {
        this.message = "";
        this.errorCode = 200; // Default to 200, if the backend does not receive or cannot process, then code=300
    }

    // Method to pass the class as a JSON string to the frontend
    public String returnJson(int errorCodeNew) {
        this.errorCode = errorCodeNew;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Appropriate exception handling logic should be here
            return "{\"errorCode\": 500, \"message\": \"Error serializing message.\"}";
        }
    }
}