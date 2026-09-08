package com.example.tiendaLibros.revista.exception;

public class RevistaNotFoundException extends RuntimeException {

	public RevistaNotFoundException(Long id) {
		super("No existe una revista con id " + id);
	}
}
