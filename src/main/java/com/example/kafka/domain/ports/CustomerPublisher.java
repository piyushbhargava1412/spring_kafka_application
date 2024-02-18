package com.example.kafka.domain.ports;

import com.example.kafka.domain.models.Customer;

public interface CustomerPublisher {
  void publish(Customer customer);
}
