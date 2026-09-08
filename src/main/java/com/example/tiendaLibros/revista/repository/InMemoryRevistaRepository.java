package com.example.tiendaLibros.revista.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.tiendaLibros.revista.model.Revista;

@Repository
public class InMemoryRevistaRepository implements RevistaRepository {

	private final ConcurrentHashMap<Long, Revista> storage = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0L);

	@Override
	public Revista save(Revista revista) {
		Long id = revista.id();
		if (id == null) {
			id = sequence.incrementAndGet();
		} else {
			Long existingId = id;
			sequence.updateAndGet(current -> Math.max(current, existingId));
		}

		Revista persisted = new Revista(id, revista.titulo(), revista.editorial(), revista.issn(), revista.precio(), revista.stock());
		storage.put(id, persisted);
		return persisted;
	}

	@Override
	public List<Revista> findAll() {
		List<Revista> revistas = new ArrayList<>(storage.values());
		revistas.sort(Comparator.comparing(Revista::id));
		return revistas;
	}

	@Override
	public Optional<Revista> findById(Long id) {
		return Optional.ofNullable(storage.get(id));
	}

	@Override
	public void deleteById(Long id) {
		storage.remove(id);
	}
}
