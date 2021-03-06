package com.kamesh.empmgmt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web security configuration to ensure the security entry points and authentication.
 * @author KAMESHC
 *
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthEntryPoint authEntryPoint;
	/**
	 *  
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
		  	.csrf().disable()
		 	.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/employees/*").hasRole("ADMIN")
		 	.and()		    
		    .httpBasic().authenticationEntryPoint(authEntryPoint);        
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("user").roles("USER")
				.and()
				.withUser("admin").password("admin").roles("USER","ADMIN");
	}

}
