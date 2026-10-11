package com.example.tiendaLibros.libro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tiendaLibros.libro.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

	@Query("""
			SELECT l FROM Libro l
			WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
			   OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :texto, '%'))
			ORDER BY l.titulo
			""")
	List<Libro> buscarPorTituloOAutor(@Param("texto") String texto);
}
