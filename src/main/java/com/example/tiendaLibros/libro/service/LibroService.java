package com.example.tiendaLibros.libro.service;

import java.util.List;

import com.example.tiendaLibros.libro.dto.LibroRequest;
import com.example.tiendaLibros.libro.dto.LibroResponse;

public interface LibroService {

	LibroResponse crearLibro(LibroRequest request);

	List<LibroResponse> listarLibros();

	LibroResponse obtenerLibroPorId(Long id);

	LibroResponse actualizarLibro(Long id, LibroRequest request);

	void eliminarLibro(Long id);
}
