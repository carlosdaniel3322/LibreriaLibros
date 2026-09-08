package com.example.tiendaLibros.libro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.tiendaLibros.libro.dto.LibroRequest;
import com.example.tiendaLibros.libro.exception.LibroNotFoundException;
import com.example.tiendaLibros.libro.repository.InMemoryLibroRepository;

class LibroServiceImplTest {

	private LibroService libroService;

	@BeforeEach
	void setUp() {
		libroService = new LibroServiceImpl(new InMemoryLibroRepository());
	}

	@Test
	void debeCrearYListarLibros() {
		libroService.crearLibro(new LibroRequest("Clean Code", "Robert C. Martin", "9780132350884", new BigDecimal("50.00"), 3));
		libroService.crearLibro(new LibroRequest("Effective Java", "Joshua Bloch", "9780134685991", new BigDecimal("70.00"), 2));

		assertEquals(2, libroService.listarLibros().size());
	}

	@Test
	void debeActualizarYEliminarUnLibro() {
		var creado = libroService.crearLibro(new LibroRequest("Spring", "Autor", "1234567890123", new BigDecimal("40.00"), 5));

		var actualizado = libroService.actualizarLibro(creado.id(), new LibroRequest("Spring Boot", "Autor", "1234567890123", new BigDecimal("45.00"), 4));
		assertEquals("Spring Boot", actualizado.titulo());
		assertEquals(new BigDecimal("45.00"), actualizado.precio());

		libroService.eliminarLibro(creado.id());

		assertThrows(LibroNotFoundException.class, () -> libroService.obtenerLibroPorId(creado.id()));
		assertFalse(libroService.listarLibros().stream().anyMatch(libro -> libro.id().equals(creado.id())));
	}
}
