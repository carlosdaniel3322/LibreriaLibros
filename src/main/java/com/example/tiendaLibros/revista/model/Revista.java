package com.example.tiendaLibros.revista.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "revistas")
public class Revista {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 120)
	private String titulo;
	@Column(nullable = false, length = 120)
	private String editorial;
	@Column(nullable = false, unique = true, length = 9)
	private String issn;
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;
	@Column(nullable = false)
	private Integer stock;

	protected Revista() { }

	public Revista(Long id, String titulo, String editorial, String issn, BigDecimal precio, Integer stock) {
		this.id = id;
		this.titulo = titulo;
		this.editorial = editorial;
		this.issn = issn;
		this.precio = precio;
		this.stock = stock;
	}

	public Long id() { return id; }
	public String titulo() { return titulo; }
	public String editorial() { return editorial; }
	public String issn() { return issn; }
	public BigDecimal precio() { return precio; }
	public Integer stock() { return stock; }
}
