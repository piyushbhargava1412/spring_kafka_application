package com.example.kafka.adapter.config;

import io.cloudevents.CloudEvent;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {
  private final KafkaProperties kafkaProperties;

  public KafkaProducerConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public KafkaTemplate<String, CloudEvent> kafkaCloudEventTemplate() {
    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(initProducerProps(true)));
  }

  @Bean
  public KafkaTemplate<String, String> kafkaStringTemplate() {
    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(initProducerProps(false)));
  }

  private Map<String, Object> initProducerProps(boolean isCloudEvent) {
    final Map<String, Object> props = kafkaProperties.buildProducerProperties(null);

    // Any custom properties can be set here
    if (isCloudEvent)
      props.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
          "io.cloudevents.kafka.CloudEventSerializer");
    else
      props.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
          "org.apache.kafka.common.serialization.StringSerializer");
    return props;
  }
}
