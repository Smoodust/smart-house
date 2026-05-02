package ru.tbank.practicum.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
  @Bean
  public NewTopic myTopic() {
    System.out.println("Creating topic...");
    return TopicBuilder.name("hubs.command").partitions(1).replicas(1).build();
  }
}
