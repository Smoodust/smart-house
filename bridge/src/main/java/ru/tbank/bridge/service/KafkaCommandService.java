package ru.tbank.bridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tbank.bridge.dto.DeviceCommandMQTTDTO;
import ru.tbank.common.dto.DeviceCommandKafkaDTO;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCommandService {
    private final MqttClient mqttClient;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "hubs.command", groupId = "bridge")
    public void listen(DeviceCommandKafkaDTO message) {
        DeviceCommandMQTTDTO result = new DeviceCommandMQTTDTO();
        result.setData(message.getData());
        result.setDeviceId(message.getDeviceId());

        String serializedMessage;
        try {
            serializedMessage = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize DeviceCommandMQTTDTO for device {}: {}",
                    message.getDeviceId(), e.getMessage(), e);
            return;
        }

        String topic = "hubs/" + message.getLogin() + "/command";
        try {
            mqttClient.publish(topic,
                    new MqttMessage(serializedMessage.getBytes(StandardCharsets.UTF_8)));
            log.debug("Published command to topic {} for device {}", topic, message.getDeviceId());
        } catch (MqttException e) {
            log.error("Failed to publish MQTT message to topic {} for device {}: {}",
                    topic, message.getDeviceId(), e.getMessage(), e);
        }
    }
}