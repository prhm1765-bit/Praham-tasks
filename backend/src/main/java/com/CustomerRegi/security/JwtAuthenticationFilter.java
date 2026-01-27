package com.CustomerRegi.security;

import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.CustomerRepo;
import com.CustomerRegi.repository.TenantRepo;
import com.CustomerRegi.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;


/**
 * This filter runs once for every request.It validates JWT, sets authentication, and also sets tenant id for database routing.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomerRepo customerRepo;
<<<<<<< Updated upstream
	private final TenantRepo tenantRepo;
=======
>>>>>>> Stashed changes

	/**
	 * @param request     is all data which is sent by browser to server
	 * @param response    is all data which is sent by server to browser
	 * @param filterChain moves the request forward after this check is completed
	 * {@inheritDoc}
	 * @return it is returning customer response DTO
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		// If token is missing or invalid format, skip authentication
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		// Remove "Bearer " prefix to get actual JWT
		String jwt = authHeader.substring(7);
		try {
			String email = jwtService.extractUsername(jwt);
			String tenantId = jwtService.extractTenantId(jwt);
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
<<<<<<< Updated upstream
				Tenant tenant = tenantRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Tenant not found"));
				if (jwtService.isTokenValid(jwt, tenant.getEmail())) {
					TenantContext.setTenant(tenantId);
=======
				if (jwtService.isTokenValid(jwt,email)) {
>>>>>>> Stashed changes
					Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));
					String role = jwtService.extractRole(jwt);
					List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customer, null, authorities);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
		} finally {
			// Always clear tenant context after request is done. This prevents tenant data leaking to next request
			TenantContext.clear();
		}
	}

}
