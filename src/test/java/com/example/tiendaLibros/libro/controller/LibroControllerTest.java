package com.example.tiendaLibros.libro.controller;

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

import com.example.tiendaLibros.libro.dto.LibroRequest;
import com.example.tiendaLibros.libro.dto.LibroResponse;
import com.example.tiendaLibros.libro.exception.LibroNotFoundException;
import com.example.tiendaLibros.libro.exception.RestExceptionHandler;
import com.example.tiendaLibros.libro.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;

class LibroControllerTest {

	private MockMvc mockMvc;
	private FakeLibroService libroService;

	@BeforeEach
	void setUp() {
		libroService = new FakeLibroService();
		mockMvc = MockMvcBuilders.standaloneSetup(new LibroController(libroService))
				.setControllerAdvice(new RestExceptionHandler())
				.setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
				.setValidator(new LocalValidatorFactoryBean())
				.build();
	}

	@Test
	void debeCrearLibroYResponder201() throws Exception {
		libroService.responseToReturn = new LibroResponse(1L, "Clean Code", "Robert C. Martin", "9780132350884", new BigDecimal("50.00"), 3);

		mockMvc.perform(post("/api/libros")
						.contentType("application/json")
						.content("""
								{
								  "titulo": "Clean Code",
								  "autor": "Robert C. Martin",
								  "isbn": "9780132350884",
								  "precio": 50.00,
								  "stock": 3
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("Clean Code"));
	}

	@Test
	void debeResponder400CuandoElPayloadEsInvalido() throws Exception {
		mockMvc.perform(post("/api/libros")
						.contentType("application/json")
						.content("""
								{
								  "titulo": "",
								  "autor": "",
								  "isbn": "123",
								  "precio": -1,
								  "stock": -2
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void debeResponder404CuandoNoExisteElLibro() throws Exception {
		libroService.notFoundId = 99L;

		mockMvc.perform(get("/api/libros/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	private static class FakeLibroService implements LibroService {

		private LibroResponse responseToReturn;
		private Long notFoundId;

		@Override
		public LibroResponse crearLibro(LibroRequest request) {
			return responseToReturn;
		}

		@Override
		public java.util.List<LibroResponse> listarLibros() {
			return java.util.List.of();
		}

		@Override
		public LibroResponse obtenerLibroPorId(Long id) {
			if (notFoundId != null && notFoundId.equals(id)) {
				throw new LibroNotFoundException(id);
			}
			return responseToReturn;
		}

		@Override
		public LibroResponse actualizarLibro(Long id, LibroRequest request) {
			return responseToReturn;
		}

		@Override
		public void eliminarLibro(Long id) {
			// No-op for standalone controller tests.
		}
	}
}
