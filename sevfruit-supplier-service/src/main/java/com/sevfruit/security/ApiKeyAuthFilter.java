package com.sevfruit.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);
	
	private final ApiKeyExtractor extractor;
	
		
	public ApiKeyAuthFilter(ApiKeyExtractor extractor) {
		super();
		logger.debug("ApiKeyAuthFilter.ctor()");
		this.extractor = extractor;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		extractor.extract(request).ifPresent(SecurityContextHolder.getContext()::setAuthentication);
		
		filterChain.doFilter(request, response);		
	}

}
