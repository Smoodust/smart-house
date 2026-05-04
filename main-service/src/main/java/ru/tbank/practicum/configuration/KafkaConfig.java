package ru.tbank.practicum.configuration;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class KafkaConfig {
  private final KafkaTopicsProperties kafkaTopicsProperties;

  @Bean
  public List<NewTopic> kafkaTopics() {
    System.out.println("Creating Kafka topics...");

    return kafkaTopicsProperties.getTopics().stream()
        .map(topic -> new NewTopic(topic.getName(), topic.getPartitions(), topic.getReplicas()))
        .collect(Collectors.toList());
  }
}
