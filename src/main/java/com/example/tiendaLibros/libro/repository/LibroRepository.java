package com.example.tiendaLibros.libro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tiendaLibros.libro.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
