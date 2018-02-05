package com.kamesh.empmgmt.employee.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
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

	/**
	 * 
	 * @return
	 */
	@GetMapping("/employees")
	public List<Employee> getActiveEmployees() {
		System.out.println("Get all Employees...");
		return employeeRepository.findByStatus(EmployeeStatus.ACTIVE.getValue());
	}

	/**
	 * Getting employee by id and return only if status is ACTIVE.
	 * 
	 * @return
	 */
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getActiveEmployee(@PathVariable("id") Long id) {
		System.out.println("Search Employee with ID = " + id + "...");
		Employee employeeToSearch = new Employee();
		employeeToSearch.setEmployeeId(id);
		Example<Employee> example = Example.of(employeeToSearch);
		Employee employeeData = employeeRepository.findOne(example);
		
		if (employeeData == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(employeeData, HttpStatus.OK);
		}
	}

	/**
	 * 
	 * @param customer
	 * @return
	 */
	@PostMapping("/employee")
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
		System.out.println("Create Employee: " + employee.toString() + "...");
		return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param customer
	 * @return
	 */
	@PostMapping("/employees")
	public ResponseEntity<String> createEmployees(@Valid @RequestBody List<Employee> employeesList) {
		System.out.println("Creating " + employeesList.size() + " Employees in bulk...");
		List<Employee> savedEmployees = employeeRepository.save(employeesList);
		return new ResponseEntity<>("Total " + savedEmployees.size() + "Employess created in bulk.",
				HttpStatus.CREATED);
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
	@DeleteMapping("/actual/employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") String id) {
		System.out.println("Delete Employee with ID = " + id + "...");
		employeeRepository.delete(id);
		return new ResponseEntity<>("Employee has been deleted!", HttpStatus.OK);
	}

	/**
	 * 
	 * @return
	 */
	@DeleteMapping("/actual/employees")
	public ResponseEntity<String> deleteAllEmployees() {
		System.out.println("Delete All Employees...");
		employeeRepository.deleteAll();
		return new ResponseEntity<>("All employees have been deleted!", HttpStatus.OK);
	}

	
	@PostMapping("/employees/injest")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			List<Employee> employees = this.extractEntities(this.readCSV(file));
			this.createEmployees(employees);
		} catch (SecurityException se) {
			return new ResponseEntity<>("Could not process file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("All employees have been deleted!", HttpStatus.OK);
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private List<List<String>> readCSV(MultipartFile fileName) {
		List<List<String>> records = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileName.getInputStream()))) {
			records = reader.lines().map(line -> Arrays.asList(line.split(","))).collect(Collectors.toList());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		records.remove(0);
		return records;
	}

	/**
	 * 
	 * @param values
	 * @return
	 */
	private List<Employee> extractEntities(List<List<String>> values) {
		List<Employee> employeesTobeCreated = new ArrayList<Employee>();
		DateFormat dateFormat = new ISO8601DateFormat();
		values.forEach(value -> {
			System.out.println(value);
			Employee employee = new Employee();
			String str = value.get(0);
			str = str.trim().substring(1);
			//employee.setId();  // Not to save
			employee.setEmployeeId(Long.valueOf(str));
			employee.setFirstName(value.get(1).trim());
			employee.setMiddleInitial(value.get(2).trim());
			employee.setLastName(value.get(3).trim());
			try {
				employee.setDateOfBirth(dateFormat.parse(value.get(4).trim()));
				str = value.get(5);
				str = str.trim().substring(0, str.length() - 1);
				employee.setDateOfEmployment(dateFormat.parse(str));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			employee.setStatus(EmployeeStatus.ACTIVE.getValue());
			employeesTobeCreated.add(employee);
		});
		return employeesTobeCreated;
	}
}
