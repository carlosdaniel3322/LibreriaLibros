package com.example.tiendaLibros.revista.dto;

import java.math.BigDecimal;

public record RevistaResponse(
		Long id,
		String titulo,
		String editorial,
		String issn,
		BigDecimal precio,
		Integer stock) {
}
