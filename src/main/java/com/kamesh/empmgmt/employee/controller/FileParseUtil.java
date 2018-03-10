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
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.kamesh.empmgmt.employee.model.Employee;
import com.kamesh.empmgmt.employee.model.EmployeeStatus;

public class FileParseUtil{

    public FileParseUtil(){
        
    }
    public static List<Employee> getEmployeesFrom(MultipartFile fileName){       
        return extractEntitiesFromFile(readCSV(fileName));
    }
	/**
	 * API to read the CSV file of list of employees
	 * @param fileName
	 * @return
	 */
	private static List<List<String>> readCSV(MultipartFile fileName) {
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
	public static List<Employee> extractEntitiesFromFile(List<List<String>> values) {
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
}