package com.example.kafka.adapter.Customer.model;

import com.example.kafka.domain.models.Customer;

public record CustomerEvent(
    String firstName,
    String lastName,
    String email,
    String phoneNo,
    String city,
    String zipCode,
    String country) {

  public static CustomerEvent from(Customer customer) {
    return new CustomerEvent(
        customer.firstName(),
        customer.lastName(),
        customer.email(),
        customer.phoneNo(),
        customer.city(),
        customer.zipCode(),
        customer.country());
  }
}
