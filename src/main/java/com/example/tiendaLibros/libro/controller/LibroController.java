package com.example.tiendaLibros.libro.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.tiendaLibros.libro.dto.LibroRequest;
import com.example.tiendaLibros.libro.dto.LibroResponse;
import com.example.tiendaLibros.libro.service.LibroService;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

	private final LibroService libroService;

	public LibroController(LibroService libroService) {
		this.libroService = libroService;
	}

	@PostMapping
	public ResponseEntity<LibroResponse> crear(@Valid @RequestBody LibroRequest request) {
		LibroResponse creado = libroService.crearLibro(request);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(creado.id())
				.toUri();
		return ResponseEntity.created(location).body(creado);
	}

	@GetMapping
	public ResponseEntity<List<LibroResponse>> listar() {
		return ResponseEntity.ok(libroService.listarLibros());
	}

	@GetMapping("/{id}")
	public ResponseEntity<LibroResponse> obtener(@PathVariable Long id) {
		return ResponseEntity.ok(libroService.obtenerLibroPorId(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<LibroResponse> actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequest request) {
		return ResponseEntity.ok(libroService.actualizarLibro(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		libroService.eliminarLibro(id);
		return ResponseEntity.noContent().build();
	}
}
