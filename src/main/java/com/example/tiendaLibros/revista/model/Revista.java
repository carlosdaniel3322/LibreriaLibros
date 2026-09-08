package com.example.tiendaLibros.revista.model;

import java.math.BigDecimal;

public record Revista(
		Long id,
		String titulo,
		String editorial,
		String issn,
		BigDecimal precio,
		Integer stock) {
}
