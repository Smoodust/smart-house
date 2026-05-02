package ru.tbank.bridge.configuration;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.tbank.bridge.service.MessageValidator;

import java.util.Objects;

@Component
public class MQTTMessageHandler implements MqttCallback {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageValidator messageValidator;

    @Value("${kafka.topic.data}")
    private String kafkaTopic;

    public MQTTMessageHandler(KafkaTemplate<String, String> kafkaTemplate, MessageValidator messageValidator) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageValidator = messageValidator;
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection lost: " + cause.getMessage());
    }

    public void handleHubsData(MqttMessage message) {
        String payload = new String(message.getPayload());

        try {
            payload = messageValidator.normalizeDataMessage(payload);
        } catch (Exception e) {
            System.out.println("Error parsing: "+e.getMessage());
            return;
        }
        kafkaTemplate.send(kafkaTopic, payload);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (Objects.equals(topic, "hubs/data")) {
            handleHubsData(message);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // not needed for inbound
    }
}
