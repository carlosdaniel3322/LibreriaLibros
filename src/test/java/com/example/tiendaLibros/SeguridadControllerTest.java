package com.example.tiendaLibros;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tiendaLibros.config.SecurityConfig;
import com.example.tiendaLibros.controller.AuthController;
import com.example.tiendaLibros.libro.controller.LibroController;
import com.example.tiendaLibros.libro.dto.LibroResponse;
import com.example.tiendaLibros.libro.service.LibroService;
import com.example.tiendaLibros.service.TokenService;
import com.example.tiendaLibros.service.UsuarioDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@WebMvcTest(controllers = { AuthController.class, LibroController.class }, properties = "jwt.secret=clave-prueba-jwt-segura-para-ejecutar-tests-automaticos-123456789")
@Import({ SecurityConfig.class, TokenService.class, SeguridadControllerTest.TestConfig.class })
class SeguridadControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private LibroService libroService;

	@Test
	void debeGenerarTokenJwtConCredencialesValidas() throws Exception {
		mockMvc.perform(post("/api/auth/login")
					.contentType("application/json")
					.content("""
							{
							  "username": "lector",
							  "password": "lector123"
							}
							"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").isNotEmpty());
	}

	@Test
	void debeRechazarRutaProtegidaSinToken() throws Exception {
		mockMvc.perform(get("/api/libros"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void debePermitirConsultaAlRolLector() throws Exception {
		when(libroService.listarLibros()).thenReturn(List.<LibroResponse>of());

		mockMvc.perform(get("/api/libros").with(user("lector").roles("LECTOR")))
				.andExpect(status().isOk());
	}

	@Test
	void debeProhibirCreacionAlRolLector() throws Exception {
		mockMvc.perform(post("/api/libros")
					.with(user("lector").roles("LECTOR"))
					.contentType("application/json")
					.content("""
							{
							  "titulo": "Libro no autorizado",
							  "autor": "Autor",
							  "isbn": "9780132350884",
							  "precio": 25.00,
							  "stock": 1
							}
							"""))
				.andExpect(status().isForbidden());
	}

	@TestConfiguration
	static class TestConfig {
		@Bean
		public UserDetailsService userDetailsService() {
			org.springframework.security.crypto.password.PasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
			UserDetails lector = User.builder()
					.username("lector")
					.password(encoder.encode("lector123"))
					.roles("LECTOR")
					.build();
			UserDetails editor = User.builder()
					.username("editor")
					.password(encoder.encode("editor123"))
					.roles("LECTOR", "EDITOR")
					.build();
			UserDetails admin = User.builder()
					.username("admin")
					.password(encoder.encode("admin123"))
					.roles("LECTOR", "EDITOR", "ADMIN")
					.build();
			return new InMemoryUserDetailsManager(lector, editor, admin);
		}
	}
}
