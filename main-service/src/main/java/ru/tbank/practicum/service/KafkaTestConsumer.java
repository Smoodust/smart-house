package ru.tbank.practicum.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTestConsumer {
  @KafkaListener(topics = "test-topic", groupId = "test-group")
  public void listen(String message) {
    System.out.println("Received: " + message);
  }
}
