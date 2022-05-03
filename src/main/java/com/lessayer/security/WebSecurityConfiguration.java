package com.lessayer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "style.css");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated()
		.and().formLogin()
				/*
				 * .loginPage("/login") .usernameParameter("email")
				 * .passwordParameter("password")
				 */
			.permitAll()
		.and().logout()
			.permitAll()
		.and().rememberMe()
			.key("testing_key")
			.tokenValiditySeconds(7 * 24 * 60 * 60);
		
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	private DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvicer = new DaoAuthenticationProvider();
		authenticationProvicer.setUserDetailsService(userDetailsService());
		authenticationProvicer.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvicer;
	}
	
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	protected UserDetailsService userDetailsService() {
		return new LessayerUserDetailsService();
	}
	
	
}
