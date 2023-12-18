package com.sevfruit.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	
	private final String apiKey;
	
	public ApiKeyAuthToken(String apiKey, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		
		this.apiKey = apiKey;
		
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return apiKey;
	}

}
