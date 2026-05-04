package ru.tbank.practicum.configuration;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaTopicsProperties {
  private List<TopicConfig> topics;

  @Data
  public static class TopicConfig {
    private String name;
    private int partitions;
    private short replicas;
  }
}
