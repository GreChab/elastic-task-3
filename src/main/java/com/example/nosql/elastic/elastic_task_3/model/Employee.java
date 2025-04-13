package com.example.nosql.elastic.elastic_task_3.model;

import java.util.List;

public record Employee(
        String name,
        String dob,
        Address address,
        String email,
        List<String> skills,
        int experience,
        double rating,
        String description,
        boolean verified,
        int salary) {
}

