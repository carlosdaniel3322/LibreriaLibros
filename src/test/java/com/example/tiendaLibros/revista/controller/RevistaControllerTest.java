package com.example.tiendaLibros.revista.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.tiendaLibros.libro.exception.RestExceptionHandler;
import com.example.tiendaLibros.revista.dto.RevistaRequest;
import com.example.tiendaLibros.revista.dto.RevistaResponse;
import com.example.tiendaLibros.revista.exception.RevistaNotFoundException;
import com.example.tiendaLibros.revista.service.RevistaService;
import com.fasterxml.jackson.databind.ObjectMapper;

class RevistaControllerTest {

	private MockMvc mockMvc;
	private FakeRevistaService revistaService;

	@BeforeEach
	void setUp() {
		revistaService = new FakeRevistaService();
		mockMvc = MockMvcBuilders.standaloneSetup(new RevistaController(revistaService))
				.setControllerAdvice(new RestExceptionHandler())
				.setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
				.setValidator(new LocalValidatorFactoryBean())
				.build();
	}

	@Test
	void debeCrearRevistaYResponder201() throws Exception {
		revistaService.responseToReturn = new RevistaResponse(1L, "National Geographic", "National Geographic Partners", "0027-9358", new BigDecimal("18.00"), 8);

		mockMvc.perform(post("/api/revistas")
						.contentType("application/json")
						.content("""
								{
								  "titulo": "National Geographic",
								  "editorial": "National Geographic Partners",
								  "issn": "0027-9358",
								  "precio": 18.00,
								  "stock": 8
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.titulo").value("National Geographic"));
	}

	@Test
	void debeResponder404CuandoNoExisteLaRevista() throws Exception {
		revistaService.notFoundId = 99L;

		mockMvc.perform(get("/api/revistas/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	private static class FakeRevistaService implements RevistaService {

		private RevistaResponse responseToReturn;
		private Long notFoundId;

		@Override
		public RevistaResponse crearRevista(RevistaRequest request) {
			return responseToReturn;
		}

		@Override
		public java.util.List<RevistaResponse> listarRevistas() {
			return java.util.List.of();
		}

		@Override
		public RevistaResponse obtenerRevistaPorId(Long id) {
			if (notFoundId != null && notFoundId.equals(id)) {
				throw new RevistaNotFoundException(id);
			}
			return responseToReturn;
		}

		@Override
		public RevistaResponse actualizarRevista(Long id, RevistaRequest request) {
			return responseToReturn;
		}

		@Override
		public void eliminarRevista(Long id) {
			// No-op for standalone controller tests.
		}
	}
}
