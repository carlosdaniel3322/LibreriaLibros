package com.example.tiendaLibros.libro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tiendaLibros.libro.dto.LibroRequest;
import com.example.tiendaLibros.libro.dto.LibroResponse;
import com.example.tiendaLibros.libro.exception.LibroNotFoundException;
import com.example.tiendaLibros.libro.model.Libro;
import com.example.tiendaLibros.libro.repository.LibroRepository;

@Service
public class LibroServiceImpl implements LibroService {

	private final LibroRepository libroRepository;

	public LibroServiceImpl(LibroRepository libroRepository) {
		this.libroRepository = libroRepository;
	}

	@Override
	public LibroResponse crearLibro(LibroRequest request) {
		Libro guardado = libroRepository.save(toModel(null, request));
		return toResponse(guardado);
	}

	@Override
	public List<LibroResponse> listarLibros() {
		return libroRepository.findAll()
				.stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	public LibroResponse obtenerLibroPorId(Long id) {
		return libroRepository.findById(id)
				.map(this::toResponse)
				.orElseThrow(() -> new LibroNotFoundException(id));
	}

	@Override
	public LibroResponse actualizarLibro(Long id, LibroRequest request) {
		libroRepository.findById(id)
				.orElseThrow(() -> new LibroNotFoundException(id));

		Libro actualizado = libroRepository.save(toModel(id, request));
		return toResponse(actualizado);
	}

	@Override
	public void eliminarLibro(Long id) {
		libroRepository.findById(id)
				.orElseThrow(() -> new LibroNotFoundException(id));

		libroRepository.deleteById(id);
	}

	private Libro toModel(Long id, LibroRequest request) {
		return new Libro(
				id,
				request.titulo(),
				request.autor(),
				request.isbn(),
				request.precio(),
				request.stock());
	}

	private LibroResponse toResponse(Libro libro) {
		return new LibroResponse(
				libro.id(),
				libro.titulo(),
				libro.autor(),
				libro.isbn(),
				libro.precio(),
				libro.stock());
	}
}
