package com.kamesh.empmgmt.employee.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kamesh.empmgmt.employee.model.Employee;

public interface EmployeeMongoRepository extends MongoRepository<Employee, String> {
	List<Employee> findByStatus(boolean status);
}
