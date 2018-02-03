package com.kamesh.empmgmt.employee.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamesh.empmgmt.employee.model.Employee;
import com.kamesh.empmgmt.employee.model.EmployeeStatus;
import com.kamesh.empmgmt.employee.repo.EmployeeMongoRepository;

@CrossOrigin(origins = "http://localhost:4100")
@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeMongoRepository employeeRepository;

	@GetMapping("/employees/all")
	public List<Employee> getAllEmployees() {
		System.out.println("Get all Employees...");
		return employeeRepository.findAll();
	}
	
	@GetMapping("/employees")
	public List<Employee> getActiveEmployees() {
		System.out.println("Get all Employees...");
	    return employeeRepository.findByStatus(EmployeeStatus.ACTIVE.getValue());		
	}
	/**
	 * 
	 * @param customer
	 * @return
	 */
	@PostMapping("/employee")
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee customer) {
		System.out.println("Create Employee: " + customer.toString() + "...");
		return new ResponseEntity<>(employeeRepository.save(customer), HttpStatus.CREATED);
	}
	/**
	 * 
	 * @param id
	 * @param employee
	 * @return
	 */
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String id, @RequestBody Employee employee) {
		System.out.println("Update Employee with ID = " + id + "...");

		Employee employeeData = employeeRepository.findOne(id);
		if (employee == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		employeeData.setFirstName(employee.getFirstName());
		employeeData.setLastName(employee.getLastName());
		employeeData.setMiddleInitial(employee.getMiddleInitial());
		employeeData.setDateOfBirth(employee.getDateOfBirth());
		employeeData.setDateOfEmployment(employee.getDateOfEmployment());
		employeeData.setStatus(employee.getStatus());
		
		Employee updatedcustomer = employeeRepository.save(employeeData);
		return new ResponseEntity<>(updatedcustomer, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Employee> deleteByDeactivateEmployee(@PathVariable("id") String id) {
		System.out.println("Delete Employee with ID = " + id + "...");
		Employee employeeData = employeeRepository.findOne(id);
		if (employeeData == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		employeeData.setStatus(EmployeeStatus.INACTIVE.getValue());
		Employee deactivatedCustomer = employeeRepository.save(employeeData);
		return new ResponseEntity<>(deactivatedCustomer, HttpStatus.OK);		
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/employees1/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") String id) {
		System.out.println("Delete Employee with ID = " + id + "...");
		employeeRepository.delete(id);
		return new ResponseEntity<>("Employee has been deleted!", HttpStatus.OK);
	}
	/**
	 * 
	 * @return
	 */
	@DeleteMapping("/employees")
	public ResponseEntity<String> deleteAllEmployees() {
		System.out.println("Delete All Employees...");
		employeeRepository.deleteAll();
		return new ResponseEntity<>("All employees have been deleted!", HttpStatus.OK);
	}
}
