package com.example.tiendaLibros.revista.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tiendaLibros.revista.exception.RevistaNotFoundException;
import com.example.tiendaLibros.revista.model.Revista;
import com.example.tiendaLibros.revista.repository.RevistaRepository;

@ExtendWith(MockitoExtension.class)
class RevistaServiceImplTest {

	@Mock
	private RevistaRepository revistaRepository;

	private RevistaService revistaService;

	@BeforeEach
	void setUp() {
		revistaService = new RevistaServiceImpl(revistaRepository);
	}

	@Test
	void debeCrearYListarRevistas() {
		Revista revista = new Revista(1L, "National Geographic", "National Geographic Partners", "0027-9358", new BigDecimal("18.00"), 8);
		org.mockito.Mockito.when(revistaRepository.findAll()).thenReturn(java.util.List.of(revista));

		assertEquals(1, revistaService.listarRevistas().size());
	}

	@Test
	void debeLanzarErrorCuandoLaRevistaNoExiste() {
		assertThrows(RevistaNotFoundException.class, () -> revistaService.obtenerRevistaPorId(99L));
	}
}
