package com.kamesh.empmgmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {
	/**
	 * Can implement URI based security only. Not based on HTTP Action (e.g. DELETE).  
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
		  .csrf().disable()
		 	//.httpBasic()
		    //.and()
		  .authorizeRequests().antMatchers("/api/**").permitAll();	        
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER")
				.and()
				.withUser("kamesh").password("kamesh").roles("USER","ADMIN");
	}

}
