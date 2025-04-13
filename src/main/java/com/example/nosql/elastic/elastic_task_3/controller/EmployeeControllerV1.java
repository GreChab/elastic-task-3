package com.example.nosql.elastic.elastic_task_3.controller;

import com.example.nosql.elastic.elastic_task_3.model.Employee;
import com.example.nosql.elastic.elastic_task_3.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employee API v1", description = "Java Low Level REST Client for retrieval of employees info")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeControllerV1 {

    private final EmployeeService employeeService;

    public EmployeeControllerV1(@Qualifier("employeeServiceV1") EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the employees",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employees not found",
                    content = @Content)})
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Get an employee by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public Employee getEmployeeById(@Parameter(description = "id of employee to be searched")
                                    @PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @Operation(summary = "Search employees by any field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @GetMapping("/searchBy")
    public List<Employee> searchBy(@RequestParam String field, @RequestParam String value) {
        return employeeService.searchBy(field, value);
    }

    @Operation(summary = "Aggregation by any numeric field")
    @GetMapping("/aggregateBy")
    public String aggregate(@RequestParam String aggregationField,
                            @RequestParam String metricType,
                            @RequestParam String metricField) {
        return employeeService.aggregateBy(aggregationField, metricType, metricField);
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")})
    @PostMapping("/{id}")
    public String addEmployee(@PathVariable String id, @RequestBody Employee employee) {
        return employeeService.createEmployee(id, employee);
    }

    @Operation(summary = "Delete an employee by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied") })
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        employeeService.deleteById(id);
    }
}
