package com.CustomerRegi.security;

import com.CustomerRegi.model.Customer;
import com.CustomerRegi.repository.CustomerRepo;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomerRepo customerRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

	String authHeader = request.getHeader("Authorization");

	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		filterChain.doFilter(request, response);
		return;
	}

	String jwt = authHeader.substring(7);
	String email = jwtService.extractUsername(jwt);

	if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		Customer customer = customerRepo
			.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));

		if (jwtService.isTokenValid(jwt, customer.getEmail())) {
			String role = jwtService.extractRole(jwt);
			List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customer, null, authorities);

			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
	}
	filterChain.doFilter(request, response);
	}

}

