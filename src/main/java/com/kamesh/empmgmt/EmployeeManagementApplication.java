/**
 * 
 */
package com.kamesh.empmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
/**
 * Main spring boot application starter/initializer
 * @author KAMESHC
 *
 */
@ComponentScan(basePackages = "com.kamesh.empmgmt")
@SpringBootApplication
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class EmployeeManagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}
	
}
