package com.example.tiendaLibros.libro.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record LibroRequest(
		@NotBlank
		@Size(min = 2, max = 120)
		String titulo,

		@NotBlank
		@Size(min = 2, max = 120)
		String autor,

		@NotBlank
		@Size(min = 10, max = 17)
		String isbn,

		@NotNull
		@PositiveOrZero
		BigDecimal precio,

		@NotNull
		@PositiveOrZero
		Integer stock) {
}
