package com.example.tiendaLibros.libro.dto;

import java.math.BigDecimal;

public record LibroResponse(
		Long id,
		String titulo,
		String autor,
		String isbn,
		BigDecimal precio,
		Integer stock) {
}
