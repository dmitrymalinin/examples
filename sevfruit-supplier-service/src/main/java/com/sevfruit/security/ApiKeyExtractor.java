package com.sevfruit.security;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Извлекает apiKey из заголовка HTTP запроса
 * @author dm
 *
 */
@Component
public class ApiKeyExtractor {
	private static final Logger logger = LoggerFactory.getLogger(ApiKeyExtractor.class);
	
	@Value("${app.security.api-key}")
	private String apiKey;
	
	public Optional<Authentication> extract(HttpServletRequest request)
	{
		final String providedKey = request.getHeader("Api-Key");
		logger.debug("apiKey: '{}'  providedKey: '{}'", apiKey, providedKey);
		if (providedKey == null || !providedKey.equals(apiKey))
		{
			return Optional.empty();
		} else
		{
			return Optional.of(new ApiKeyAuthToken(providedKey, AuthorityUtils.NO_AUTHORITIES));
		}
	}
}
