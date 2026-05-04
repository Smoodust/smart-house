package ru.tbank.bridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.bridge.dto.DeviceCommandMQTTDTO;
import ru.tbank.bridge.service.KafkaCommandService;
import ru.tbank.common.dto.DeviceCommandKafkaDTO;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaCommandServiceTest {

    @Mock
    private MqttClient mqttClient;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private KafkaCommandService kafkaCommandService;

    @Captor
    private ArgumentCaptor<DeviceCommandMQTTDTO> dtoCaptor;

    @Captor
    private ArgumentCaptor<MqttMessage> messageCaptor;

    @Test
    void shouldPublishMessageSuccessfully() throws Exception {
        // Given
        DeviceCommandKafkaDTO kafkaDto = new DeviceCommandKafkaDTO();
        kafkaDto.setLogin("user123");
        kafkaDto.setDeviceId("device456");
        kafkaDto.setData(Map.of("command", "turn_on", "value", 1));

        String expectedJson = "{\"deviceId\":\"device456\",\"data\":{\"command\":\"turn_on\",\"value\":1}}";
        when(mapper.writeValueAsString(any(DeviceCommandMQTTDTO.class))).thenReturn(expectedJson);

        // When
        kafkaCommandService.listen(kafkaDto);

        // Then
        // Verify the MQTT DTO was created with correct fields
        verify(mapper).writeValueAsString(dtoCaptor.capture());
        DeviceCommandMQTTDTO mqttDto = dtoCaptor.getValue();
        assertEquals("device456", mqttDto.getDeviceId());
        assertEquals(Map.of("command", "turn_on", "value", 1), mqttDto.getData());

        // Verify MQTT publish was called with correct topic and payload
        verify(mqttClient).publish(eq("hubs/user123/command"), messageCaptor.capture());
        MqttMessage mqttMessage = messageCaptor.getValue();
        assertEquals(expectedJson, new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
        // Default QoS and retain can be whatever the MqttMessage defaults are; no need to check further.
    }

    @Test
    void shouldHandleJsonProcessingExceptionAndNotPublish() throws Exception {
        // Given
        DeviceCommandKafkaDTO kafkaDto = new DeviceCommandKafkaDTO();
        kafkaDto.setLogin("user");
        kafkaDto.setDeviceId("dev");
        kafkaDto.setData(Map.of());

        when(mapper.writeValueAsString(any(DeviceCommandMQTTDTO.class)))
                .thenThrow(new JsonProcessingException("Serialization error") {});

        // When
        kafkaCommandService.listen(kafkaDto);

        // Then
        // Verify MQTT publish was never attempted
        verify(mqttClient, never()).publish(anyString(), any(MqttMessage.class));
        // The method should not throw, error is just logged.
    }

    @Test
    void shouldCatchMqttExceptionAndNotThrow() throws Exception {
        // Given
        DeviceCommandKafkaDTO kafkaDto = new DeviceCommandKafkaDTO();
        kafkaDto.setLogin("user");
        kafkaDto.setDeviceId("dev");
        kafkaDto.setData(Map.of("action", "reboot"));

        String json = "{}";
        when(mapper.writeValueAsString(any(DeviceCommandMQTTDTO.class))).thenReturn(json);
        doThrow(new MqttException(MqttException.REASON_CODE_BROKER_UNAVAILABLE))
                .when(mqttClient).publish(anyString(), any(MqttMessage.class));

        // When & Then - no exception should propagate
        assertDoesNotThrow(() -> kafkaCommandService.listen(kafkaDto));

        // Verify that publish was indeed attempted
        verify(mqttClient).publish(eq("hubs/user/command"), any(MqttMessage.class));
    }
}