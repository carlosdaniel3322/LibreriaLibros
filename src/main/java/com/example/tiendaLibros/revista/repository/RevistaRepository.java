package com.example.tiendaLibros.revista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tiendaLibros.revista.model.Revista;

public interface RevistaRepository extends JpaRepository<Revista, Long> {
}
