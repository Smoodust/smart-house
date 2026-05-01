package ru.tbank.bridge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class MessageValidator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String normalizeDataMessage(String payload) {
        try {
            // 1. Parse the whole payload into a tree (data remains a generic node)
            JsonNode root = objectMapper.readTree(payload);

            // 2. Validate the envelope
            if (!root.has("login") || !root.get("login").isTextual()) {
                throw new IllegalArgumentException("Field 'login' missing or not a string");
            }
            if (!root.has("pass") || !root.get("pass").isTextual()) {
                throw new IllegalArgumentException("Field 'pass' missing or not a string");
            }
            if (!root.has("deviceId") || !root.get("deviceId").isTextual()) {
                throw new IllegalArgumentException("Field 'deviceId' missing or not a string");
            }
            if (!root.has("data")) {
                throw new IllegalArgumentException("Field 'data' missing");
            }

            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message from MQTT topic: " + e.getMessage());
        }
    }
}
