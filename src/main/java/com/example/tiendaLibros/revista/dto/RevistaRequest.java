package com.example.tiendaLibros.revista.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record RevistaRequest(
		@NotBlank
		@Size(min = 2, max = 120)
		String titulo,

		@NotBlank
		@Size(min = 2, max = 120)
		String editorial,

		@NotBlank
		@Size(min = 8, max = 9)
		String issn,

		@NotNull
		@PositiveOrZero
		BigDecimal precio,

		@NotNull
		@PositiveOrZero
		Integer stock) {
}
