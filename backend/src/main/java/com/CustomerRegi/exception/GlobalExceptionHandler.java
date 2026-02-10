package com.CustomerRegi.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of("errors", errors));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntime(RuntimeException ex) {
		Throwable root = ex;
		while (root.getCause() != null) {
			root = root.getCause();
		}
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of(
						"errors",
						Map.of("message", root.getMessage())
				));
	}


	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<?> handleEmailExists(EmailAlreadyExistsException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of(
						"errors",
						Map.of("email", ex.getMessage())
				));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(Map.of(
						"errors",
						Map.of("message", ex.getMessage())
				));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity
				.badRequest()
				.body(Map.of(
						"errors",
						Map.of("message", ex.getMessage())
				));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(v -> {
			String field = v.getPropertyPath().toString();
			errors.put(field.substring(field.lastIndexOf('.') + 1), v.getMessage());
		});
		return ResponseEntity
				.badRequest()
				.body(Map.of("errors", errors));
	}


	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDuplicate(DataIntegrityViolationException ex) {
		String message = ex.getMostSpecificCause().getMessage();
		Map<String, String> errors = new HashMap<>();
		if (message != null) {
			if (message.contains("UK2d74g7acpo0pgsm3fbqlxiwjt")) {
				errors.put("email", "Email already exists");
			}
			else if (message.contains("UK9cioy9iex0a45lmruuo3dlswc")) {
				errors.put("companyCode", "Company code already exists");
			}
			else {
				errors.put("message", "Duplicate value violates unique constraint");
			}
		}
		return ResponseEntity
				.badRequest()
				.body(Map.of("errors", errors));
	}

}
