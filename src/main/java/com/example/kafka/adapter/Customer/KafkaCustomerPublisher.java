package com.example.kafka.adapter.Customer;

import com.example.kafka.adapter.Customer.model.CustomerEvent;
import com.example.kafka.domain.models.Customer;
import com.example.kafka.domain.ports.CustomerPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventData;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaCustomerPublisher implements CustomerPublisher {

  private final KafkaTemplate<String, CloudEvent> cloudEventTemplate;
  private final ObjectMapper objectMapper;

  public KafkaCustomerPublisher(KafkaTemplate<String, CloudEvent> kafkaCloudEventTemplate, ObjectMapper objectMapper) {
    this.cloudEventTemplate = kafkaCloudEventTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public void publish(Customer customer) {
    final CustomerEvent customerEvent = CustomerEvent.from(customer);
    try {
      CloudEventData data = PojoCloudEventData.wrap(customerEvent, objectMapper::writeValueAsBytes);
      final CloudEvent cloudEvent = CloudEventBuilder.v1()
              .withId(UUID.randomUUID().toString())
              .withType("CustomerEvent")
              .withSource(new URI("CustomerTopic"))
              .withTime(Instant.now().atOffset(ZoneOffset.UTC))
              .withDataContentType("application/json")
              .withData(data).build();

      cloudEventTemplate.send("CustomerTopic", cloudEvent);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
