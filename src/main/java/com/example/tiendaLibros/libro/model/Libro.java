package com.example.tiendaLibros.libro.model;

import java.math.BigDecimal;

public record Libro(
		Long id,
		String titulo,
		String autor,
		String isbn,
		BigDecimal precio,
		Integer stock) {
}
