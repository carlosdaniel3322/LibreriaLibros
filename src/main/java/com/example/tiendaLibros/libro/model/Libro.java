package com.example.tiendaLibros.libro.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 120)
	private String titulo;
	@Column(nullable = false, length = 120)
	private String autor;
	@Column(nullable = false, unique = true, length = 17)
	private String isbn;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;
	@Column(nullable = false)
	private Integer stock;

	protected Libro() { }

	public Libro(Long id, String titulo, String autor, String isbn, BigDecimal precio, Integer stock) {
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.precio = precio;
		this.stock = stock;
	}

	public Long id() { return id; }
	public String titulo() { return titulo; }
	public String autor() { return autor; }
	public String isbn() { return isbn; }
	public BigDecimal precio() { return precio; }
	public Integer stock() { return stock; }
}
