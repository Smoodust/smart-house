package ru.tbank.bridge.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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
    private final Counter hubsDataSucessfulRequest;
    private final Counter hubsDataTotalRequest;

    @Value("${kafka.topic.data}")
    private String kafkaTopic;

    public MQTTMessageHandler(KafkaTemplate<String, String> kafkaTemplate, MessageValidator messageValidator, MeterRegistry registry) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageValidator = messageValidator;
        this.hubsDataSucessfulRequest = registry.counter("mqtt_hubs_data_sucessful_requests");
        this.hubsDataTotalRequest = registry.counter("mqtt_hubs_data_total_requests");
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection lost: " + cause.getMessage());
    }

    public void handleHubsData(MqttMessage message) {
        hubsDataTotalRequest.increment();

        String payload = new String(message.getPayload());

        try {
            payload = messageValidator.normalizeDataMessage(payload);
        } catch (Exception e) {
            System.out.println("Error parsing: "+e.getMessage());
            return;
        }
        kafkaTemplate.send(kafkaTopic, payload);
        hubsDataSucessfulRequest.increment();
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
