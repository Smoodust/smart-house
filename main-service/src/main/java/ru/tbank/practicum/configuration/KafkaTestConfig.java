package ru.tbank.practicum.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaTestConfig {
  @Bean
  public NewTopic myTopic() {
    System.out.println("Creating topic...");
    return TopicBuilder.name("test-topic").partitions(1).replicas(1).build();
  }

  @Bean
  CommandLineRunner testKafka(KafkaTemplate<String, String> kafkaTemplate) {
    return args -> {
      System.out.println("Sending test message...");
      kafkaTemplate.send("test-topic", "hello kafka");
    };
  }
}
