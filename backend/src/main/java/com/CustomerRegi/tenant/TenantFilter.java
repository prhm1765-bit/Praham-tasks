package com.CustomerRegi.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter extends OncePerRequestFilter {

	private static final String TENANT_HEADER = "X-TENANT-ID";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String tenantId = request.getHeader(TENANT_HEADER);

		if (tenantId != null && !tenantId.isBlank()) {
			TenantContext.setTenant(tenantId);
			log.debug("Tenant set to: {}", tenantId);
		} else {
			log.debug("No tenant header found, using default");
		}

		try {
			filterChain.doFilter(request, response);
		} finally {
			TenantContext.clear();
		}
	}

}
