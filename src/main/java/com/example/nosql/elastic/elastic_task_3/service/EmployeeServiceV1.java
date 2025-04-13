package com.example.nosql.elastic.elastic_task_3.service;

import com.example.nosql.elastic.elastic_task_3.model.Employee;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeServiceV1 implements EmployeeService{
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public List<Employee> getAllEmployees() {
        Request request = new Request(
                "GET",
                "/employees/_search");
        try {
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            List<Employee> employees = new ArrayList<>();
            for (JsonNode employeeNode : jsonNode.get("hits").get("hits")) {
                String source = employeeNode.get("_source").toString();
                Employee employee = objectMapper.readValue(source, Employee.class);
                employees.add(employee);
            }
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee getEmployeeById(String id) {
        Request request = new Request(
                "GET",
                "/employees/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            String employee = objectMapper.readTree(responseBody).get("_source").toString();
            return objectMapper.readValue(employee, Employee.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createEmployee(String id, Employee employee) {
        Request request = new Request(
                "POST",
                "/employees/_doc/" + id);
        try {
            String newEmployee = objectMapper.writeValueAsString(employee);
            request.setJsonEntity(newEmployee);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            String employeeId = objectMapper.readTree(responseBody).get("_id").toString();
            return String.format("Employee created. Id: %s", employeeId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(String id) {
        Request request = new Request(
                "DELETE",
                "/employees/_doc/" + id);
        try {
            restClient.performRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Employee> searchBy(String field, String value) {
        Request request = new Request(
                "GET",
                "/employees/_search/");
        try {
            String requestBody = String.format("{\"query\" : {\"match\" : {\"%s\" : {\"query\" : \"%s\"}}}}", field, value);
            request.setJsonEntity(requestBody);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            List<Employee> employees = new ArrayList<>();
            for (JsonNode employeeNode : jsonNode.get("hits").get("hits")) {
                String source = employeeNode.get("_source").toString();
                Employee employee = objectMapper.readValue(source, Employee.class);
                employees.add(employee);
            }
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String aggregateBy(String aggregationField, String metricType, String metricField) {
        Request request = new Request(
                "GET",
                "/employees/_search/");
        try {
            String requestBody = String.format("""
                    {
                        "size": 0,
                        "aggs": {
                            "%s": {
                                "terms": {
                                    "field": "%s.keyword"
                                },
                                "aggs": {
                                    "operation": {
                                        "%s": {
                                            "field": "%s"
                                        }
                                    }
                                }
                            }
                        }
                    }
                    """, aggregationField, aggregationField, metricType, metricField);

            request.setJsonEntity(requestBody);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode responseNode = new ObjectMapper().readTree(responseBody);
            return responseNode.get("aggregations").get(String.format("%s", aggregationField)).get("buckets").toPrettyString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
