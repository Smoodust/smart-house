package ru.tbank.bridge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tbank.bridge.service.MessageValidator;

import static org.junit.jupiter.api.Assertions.*;

public class MessageValidatorTest {
    private MessageValidator validator;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        validator = new MessageValidator(objectMapper);
    }

    // ------------------- valid scenarios -------------------

    @Test
    void shouldReturnNormalizedJsonWhenAllFieldsPresentAndCorrect() throws Exception {
        String payload = """
            {
              "login": "user",
              "pass": "secret",
              "deviceId": "dev-123",
              "data": { "key": "value" }
            }""";

        String result = validator.normalizeDataMessage(payload);

        JsonNode original = objectMapper.readTree(payload);
        JsonNode normalized = objectMapper.readTree(result);

        assertEquals(original, normalized, "Normalized JSON should match original structure");
    }

    @Test
    void shouldAcceptDataFieldOfAnyType() throws Exception {
        // data = array
        String arrayPayload = """
            { "login": "u", "pass": "p", "deviceId": "d", "data": [1,2,3] }""";
        // data = plain number
        String numberPayload = """
            { "login": "u", "pass": "p", "deviceId": "d", "data": 42 }""";
        // data = string
        String stringPayload = """
            { "login": "u", "pass": "p", "deviceId": "d", "data": "hello" }""";

        assertDoesNotThrow(() -> validator.normalizeDataMessage(arrayPayload));
        assertDoesNotThrow(() -> validator.normalizeDataMessage(numberPayload));
        assertDoesNotThrow(() -> validator.normalizeDataMessage(stringPayload));
    }

    // ------------------- missing field scenarios -------------------

    @Test
    void shouldThrowWhenLoginFieldMissing() {
        String payload = """
            { "pass": "p", "deviceId": "d", "data": {} }""";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'login' missing or not a string"));
    }

    @Test
    void shouldThrowWhenLoginIsNotString() {
        String payload = """
            { "login": 123, "pass": "p", "deviceId": "d", "data": {} }""";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'login' missing or not a string"));
    }

    @Test
    void shouldThrowWhenPassFieldMissing() {
        String payload = """
            { "login": "u", "deviceId": "d", "data": {} }""";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'pass' missing or not a string"));
    }

    @Test
    void shouldThrowWhenPassIsNotString() {
        String payload = """
            { "login": "u", "pass": true, "deviceId": "d", "data": {} }""";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'pass' missing or not a string"));
    }

    @Test
    void shouldThrowWhenDeviceIdFieldMissing() {
        String payload = """
            { "login": "u", "pass": "p", "data": {} }""";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'deviceId' missing or not a string"));
    }

    @Test
    void shouldThrowWhenDeviceIdIsNotString() {
        String payload = """
            { "login": "u", "pass": "p", "deviceId": null, "data": {} }""";
        // Jackson represents null as JsonNode of type NullNode, not TextNode
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'deviceId' missing or not a string"));
    }

    @Test
    void shouldThrowWhenDataFieldMissing() {
        String payload = """
            { "login": "u", "pass": "p", "deviceId": "d" }""";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().contains("Field 'data' missing"));
    }

    // ------------------- malformed payload scenarios -------------------

    @Test
    void shouldThrowWhenPayloadIsInvalidJson() {
        String payload = "not a json";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().startsWith("Invalid message from MQTT topic: "));
    }

    @Test
    void shouldThrowWhenPayloadIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(null));
        assertTrue(ex.getMessage().startsWith("Invalid message from MQTT topic: "));
    }

    @Test
    void shouldThrowWhenPayloadIsEmpty() {
        String payload = "";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.normalizeDataMessage(payload));
        assertTrue(ex.getMessage().startsWith("Invalid message from MQTT topic: "));
    }
}
