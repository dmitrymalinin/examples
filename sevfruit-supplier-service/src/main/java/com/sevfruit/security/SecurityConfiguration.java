package com.sevfruit.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
	
	private final ApiKeyAuthFilter authFilter;
	private final UnauthorizedHandler unauthorizedHandler;
		
	
	public SecurityConfiguration(ApiKeyAuthFilter authFilter, UnauthorizedHandler unauthorizedHandler) {
		super();
		logger.debug("SecurityConfiguration.ctor(): authFilter: {}", authFilter);
		this.authFilter = authFilter;
		this.unauthorizedHandler = unauthorizedHandler;
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		logger.debug("SecurityConfiguration.securityFilterChain()");
		return http
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(AbstractHttpConfigurer::disable)
				.exceptionHandling(configurer -> configurer.authenticationEntryPoint(unauthorizedHandler))
				.securityMatcher("/**")
				.authorizeHttpRequests(registry -> registry
						.requestMatchers("/error").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

}
