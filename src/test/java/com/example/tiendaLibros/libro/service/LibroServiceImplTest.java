package com.example.tiendaLibros.libro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tiendaLibros.libro.dto.LibroRequest;
import com.example.tiendaLibros.libro.exception.LibroNotFoundException;
import com.example.tiendaLibros.libro.model.Libro;
import com.example.tiendaLibros.libro.repository.LibroRepository;

@ExtendWith(MockitoExtension.class)
class LibroServiceImplTest {

	@Mock
	private LibroRepository libroRepository;

	private LibroService libroService;

	@BeforeEach
	void setUp() {
		libroService = new LibroServiceImpl(libroRepository);
	}

	@Test
	void debeCrearYListarLibros() {
		Libro primero = new Libro(1L, "Clean Code", "Robert C. Martin", "9780132350884", new BigDecimal("50.00"), 3);
		Libro segundo = new Libro(2L, "Effective Java", "Joshua Bloch", "9780134685991", new BigDecimal("70.00"), 2);
		when(libroRepository.findAll()).thenReturn(java.util.List.of(primero, segundo));

		assertEquals(2, libroService.listarLibros().size());
	}

	@Test
	void debeActualizarYReportarLibroInexistente() {
		Libro existente = new Libro(1L, "Spring", "Autor", "1234567890123", new BigDecimal("40.00"), 5);
		Libro actualizadoEnRepositorio = new Libro(1L, "Spring Boot", "Autor", "1234567890123", new BigDecimal("45.00"), 4);
		when(libroRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
		when(libroRepository.save(any(Libro.class))).thenReturn(actualizadoEnRepositorio);

		var actualizado = libroService.actualizarLibro(1L, new LibroRequest("Spring Boot", "Autor", "1234567890123", new BigDecimal("45.00"), 4));
		assertEquals("Spring Boot", actualizado.titulo());
		assertThrows(LibroNotFoundException.class, () -> libroService.obtenerLibroPorId(99L));
	}
}
