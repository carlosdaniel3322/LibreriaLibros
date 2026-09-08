package com.example.tiendaLibros.revista.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.tiendaLibros.revista.dto.RevistaRequest;
import com.example.tiendaLibros.revista.exception.RevistaNotFoundException;
import com.example.tiendaLibros.revista.repository.InMemoryRevistaRepository;

class RevistaServiceImplTest {

	private RevistaService revistaService;

	@BeforeEach
	void setUp() {
		revistaService = new RevistaServiceImpl(new InMemoryRevistaRepository());
	}

	@Test
	void debeCrearYListarRevistas() {
		revistaService.crearRevista(new RevistaRequest("National Geographic", "National Geographic Partners", "0027-9358", new BigDecimal("18.00"), 8));

		assertEquals(1, revistaService.listarRevistas().size());
	}

	@Test
	void debeLanzarErrorCuandoLaRevistaNoExiste() {
		assertThrows(RevistaNotFoundException.class, () -> revistaService.obtenerRevistaPorId(99L));
	}
}
