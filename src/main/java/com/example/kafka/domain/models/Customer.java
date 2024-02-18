package com.example.kafka.domain.models;

public record Customer(
    String firstName,
    String lastName,
    String email,
    String phoneNo,
    String city,
    String zipCode,
    String country) {}
