package com.example.tiendaLibros.revista.controller;

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

import com.example.tiendaLibros.revista.dto.RevistaRequest;
import com.example.tiendaLibros.revista.dto.RevistaResponse;
import com.example.tiendaLibros.revista.service.RevistaService;

@RestController
@RequestMapping("/api/revistas")
public class RevistaController {

	private final RevistaService revistaService;

	public RevistaController(RevistaService revistaService) {
		this.revistaService = revistaService;
	}

	@PostMapping
	public ResponseEntity<RevistaResponse> crear(@Valid @RequestBody RevistaRequest request) {
		RevistaResponse creada = revistaService.crearRevista(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(creada.id()).toUri();
		return ResponseEntity.created(location).body(creada);
	}

	@GetMapping
	public ResponseEntity<List<RevistaResponse>> listar() {
		return ResponseEntity.ok(revistaService.listarRevistas());
	}

	@GetMapping("/{id}")
	public ResponseEntity<RevistaResponse> obtener(@PathVariable Long id) {
		return ResponseEntity.ok(revistaService.obtenerRevistaPorId(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RevistaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody RevistaRequest request) {
		return ResponseEntity.ok(revistaService.actualizarRevista(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		revistaService.eliminarRevista(id);
		return ResponseEntity.noContent().build();
	}
}
