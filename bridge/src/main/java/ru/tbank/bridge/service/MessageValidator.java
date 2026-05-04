package ru.tbank.bridge.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageValidator {
    private final ObjectMapper objectMapper;

    public String normalizeDataMessage(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);

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
