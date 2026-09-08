package com.example.tiendaLibros.libro.exception;

public class LibroNotFoundException extends RuntimeException {

	public LibroNotFoundException(Long id) {
		super("No existe un libro con id " + id);
	}
}
