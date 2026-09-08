package com.example.tiendaLibros.revista.repository;

import java.util.List;
import java.util.Optional;

import com.example.tiendaLibros.revista.model.Revista;

public interface RevistaRepository {

	Revista save(Revista revista);

	List<Revista> findAll();

	Optional<Revista> findById(Long id);

	void deleteById(Long id);
}
