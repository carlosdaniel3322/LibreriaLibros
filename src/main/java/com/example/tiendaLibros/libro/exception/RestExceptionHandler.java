package com.example.tiendaLibros.libro.exception;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.tiendaLibros.revista.exception.RevistaNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(LibroNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(LibroNotFoundException ex, HttpServletRequest request) {
		return notFoundResponse(ex, request);
	}

	@ExceptionHandler(RevistaNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(RevistaNotFoundException ex, HttpServletRequest request) {
		return notFoundResponse(ex, request);
	}

	private ResponseEntity<ApiError> notFoundResponse(RuntimeException ex, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiError(
						OffsetDateTime.now().toString(),
						HttpStatus.NOT_FOUND.value(),
						HttpStatus.NOT_FOUND.getReasonPhrase(),
						ex.getMessage(),
						request.getRequestURI()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<String> errores = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.toList();

		return ResponseEntity.badRequest()
				.body(new ApiError(
						OffsetDateTime.now().toString(),
						HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.getReasonPhrase(),
						String.join(" | ", errores),
						request.getRequestURI()));
	}
}
