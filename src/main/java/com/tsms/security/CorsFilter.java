package com.tsms.security;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("customCorsFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	@Value("${cors.allow.domain:*}")
	private String origin;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Allow", "OPTIONS");
		response.setHeader("Access-Control-Allow-Methods", "HEAD,GET,POST,OPTIONS,PUT,DELETE,PATCH");
		response.setHeader("Access-Control-Max-Age", "3600");

		response.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Origin, Origin, Accept, X-ACCESS_TOKEN, "
						+ "Authorization, Content-Type, X-Requested-With, X-Custom-Header, "
						+ "Content-Range, Content-Disposition, Content-Description, "
						+ "Access-Control-Request-Method, Access-Control-Request-Headers, "
						+ "Pragma, Cache-Control, If-Modified-Since, isAdminUser, auth-head, "
						+ "moduleKey, app_version, device_token");

		response.setHeader("Access-Control-Allow-Credentials", "true");

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig filterConfig) {

	}

	public void destroy() {
	}
}