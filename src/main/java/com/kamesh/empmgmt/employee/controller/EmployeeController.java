/**
 * 
 */
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
import java.util.Random;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
/**
 * An Employee ReST controller class supporting HTTP Methods GET, POST, PUT, DELETE on employee resource.
 * @author KAMESHC
 *
 */
@CrossOrigin(origins = "https://empmgmtweb.herokuapp.com/")
@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeMongoRepository employeeRepository;
	@Autowired
	private Environment env;
	/**
	 * API to get random number to set for employee ID.
	 * @return
	 */
	private long getNextEmpId(){
		Random r = new Random();
		int low = 1;
		int high = 100000;
		return r.nextInt(high-low) + low;		
	}
	/**
	 * GET ReST endpoint for getting all the employees whether Status is ACTIVE or INACTIVE.
	 * @return
	 */
	@GetMapping("/employees/all")
	public List<Employee> getAllEmployees() {
		System.out.println("Get all Employees...");
		return employeeRepository.findAll();
	}

	/**
	 * GET ReST endpoint for getting all the employees which are with status ACTIVE.
	 * @return
	 */
	@GetMapping("/employees")
	public List<Employee> getActiveEmployees() {
		System.out.println("Get all Employees...");
		return employeeRepository.findByStatus(EmployeeStatus.ACTIVE.getValue());
	}

	/**
	 * GET ReST endpoint for getting employee by id and return only if status is ACTIVE.
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
	 * POST ReST end point to create a new active employee.
	 * @param customer
	 * @return
	 */
	@PostMapping("/employee")
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
		System.out.println("Create Employee: " + employee.toString() + "...");
		employee.setEmployeeId(getNextEmpId());
		return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
	}
	
	/**
	 * Put ReST end point to update the specific employee.
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
	 * Delete end point to update the status of the employee as "INACTIVE" in database. 
	 * This ReST is used to achieve delete as inactive employees are not retruned in the resposne.
	 * @param id
	 * @return
	 */

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<String> deleteByDeactivateEmployee(@PathVariable("id") String id) {
		System.out.println("Delete Employee with ID = " + id + "...");
		Employee employeeData = employeeRepository.findOne(id);
		if (employeeData == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		employeeData.setStatus(EmployeeStatus.INACTIVE.getValue());
		employeeRepository.save(employeeData);
		return new ResponseEntity<>("The employee has been deleted!", HttpStatus.OK);
	}

	/**
	 * Delete end point to ACTUALLY delete the single employee from database. This ReST is not directly used.
	 * @param id
	 * @return
	 */
	@DeleteMapping("/actual/employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") String id) {
		System.out.println("Delete Employee with ID = " + id + "...");
		employeeRepository.delete(id);
		return new ResponseEntity<>("The employee has been deleted!", HttpStatus.OK);
	}

	/**
	 * Delete end point to ACTUALLY delete all the employees from database. This ReST is not directly used.
	 * @return
	 */
	@DeleteMapping("/actual/employees")
	public ResponseEntity<String> deleteAllEmployees() {
		System.out.println("Delete All Employees...");
		employeeRepository.deleteAll();
		return new ResponseEntity<>("All employees have been deleted!", HttpStatus.OK);
	}
	/**
	 * ReST end point to support injesting employees via upload of csv file.  
	 * @param file
	 * @return
	 */
	@PostMapping("/employees/injest")
	public ResponseEntity<InjestionResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		List<Employee> savedEmployees = null;
		List<Employee> employees = null;
		List<Employee> finalList = new ArrayList<Employee>();
		try {
			List<Employee> existingEmployees = null;
			ArrayList currentIds = new ArrayList();
			ArrayList newIds = new ArrayList();
		
			employees = this.extractEntities(this.readCSV(file));
			String idStrategy = env.getProperty("employee.injest.id.strategy").trim();
			// ignoreInput strategy means, ignore ID column in file
			if(idStrategy.equalsIgnoreCase("ignoreInput")) {
				employees.forEach(item->{
					item.setEmployeeId(getNextEmpId());
					finalList.add(item);
				});
			}else {  
				// in this strategy, the application will consider employee id from file and
				//duplicate id entry will not be created
				existingEmployees = employeeRepository.findAll();
				existingEmployees.forEach(item->{
					currentIds.add(item.getEmployeeId());
				});
				employees.forEach(item->{
					newIds.add(item.getEmployeeId());
				});
				newIds.removeAll(currentIds);
				
				employees.forEach(item->{
					if(newIds.contains(item.getEmployeeId())) {
						finalList.add(item);
					}
				});
			}
			System.out.println("Creating " + finalList.size() + " Employees in bulk...");
			savedEmployees = employeeRepository.save(finalList);
		} catch (SecurityException se) {
			return new ResponseEntity<>(new InjestionResponse(employees==null?0:employees.size()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		InjestionResponse injestResponse = new InjestionResponse(employees.size(), finalList.size(), savedEmployees.size(), employees.size() - savedEmployees.size());
		
		return new ResponseEntity<>(injestResponse,HttpStatus.OK);
	}
	
	/**
	 * API to read the CSV file of list of employees
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
		records.remove(0); // Remove header read from file to prevent adding it.
		return records;
	}

	/**
	 * API to create Employee entity out of the data read from CSV file
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
			// employee.setId(); // Not to save
			employee.setEmployeeId(Long.valueOf(str));
			employee.setFirstName(value.get(1).trim());
			employee.setMiddleInitial(value.get(2).trim());
			employee.setLastName(value.get(3).trim());
			try {
				employee.setDateOfBirth(value.get(4).trim());
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
	
	/**
	 * Inner class to generate employee file injest response.
	 * 
	 *
	 */
	class InjestionResponse {
		int requested;		
		int success;
		int failure;
		String message;
		public InjestionResponse(int requested) {
			super();
			this.requested = requested;			
			this.success = 0;
			this.failure = 0;
			this.message ="Error, Employees not created!";
		}
		public InjestionResponse(int requested, int processed, int success, int failure) {
			super();
			this.requested = requested;			
			this.success = success;
			this.failure = failure;
			this.message = new String("Total " + this.success + " out of " + this.requested + "new Employees created!");
		}

		public int getRequested() {
			return requested;
		}

		public void setRequested(int requested) {
			this.requested = requested;
		}
		
		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		public int getFailure() {
			return failure;
		}

		public void setFailure(int failure) {
			this.failure = failure;
		}

		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
