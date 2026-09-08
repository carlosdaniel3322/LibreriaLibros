package com.example.tiendaLibros.libro.repository;

import java.util.List;
import java.util.Optional;

import com.example.tiendaLibros.libro.model.Libro;

public interface LibroRepository {

	Libro save(Libro libro);

	List<Libro> findAll();

	Optional<Libro> findById(Long id);

	void deleteById(Long id);
}
