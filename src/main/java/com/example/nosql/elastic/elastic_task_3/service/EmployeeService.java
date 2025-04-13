package com.example.nosql.elastic.elastic_task_3.service;

import com.example.nosql.elastic.elastic_task_3.model.Employee;
import java.util.List;

public interface EmployeeService {

     List<Employee> getAllEmployees();

    Employee getEmployeeById(String id);

    String createEmployee(String id, Employee employee);

    void deleteById(String id);

    List<Employee> searchBy(String field, String value);

    String aggregateBy(String aggregationField, String metricType, String metricField);
}
