package com.example.tiendaLibros.libro.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.tiendaLibros.libro.model.Libro;

@Repository
public class InMemoryLibroRepository implements LibroRepository {

	private final ConcurrentHashMap<Long, Libro> storage = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0L);

	@Override
	public Libro save(Libro libro) {
		Long id = libro.id();
		if (id == null) {
			id = sequence.incrementAndGet();
		} else {
			Long existingId = id;
			sequence.updateAndGet(current -> Math.max(current, existingId));
		}

		Libro persisted = new Libro(
				id,
				libro.titulo(),
				libro.autor(),
				libro.isbn(),
				libro.precio(),
				libro.stock());
		storage.put(id, persisted);
		return persisted;
	}

	@Override
	public List<Libro> findAll() {
		List<Libro> libros = new ArrayList<>(storage.values());
		libros.sort(Comparator.comparing(Libro::id));
		return libros;
	}

	@Override
	public Optional<Libro> findById(Long id) {
		return Optional.ofNullable(storage.get(id));
	}

	@Override
	public void deleteById(Long id) {
		storage.remove(id);
	}
}
