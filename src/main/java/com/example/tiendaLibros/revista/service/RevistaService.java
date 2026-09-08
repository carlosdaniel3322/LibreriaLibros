package com.example.tiendaLibros.revista.service;

import java.util.List;

import com.example.tiendaLibros.revista.dto.RevistaRequest;
import com.example.tiendaLibros.revista.dto.RevistaResponse;

public interface RevistaService {

	RevistaResponse crearRevista(RevistaRequest request);

	List<RevistaResponse> listarRevistas();

	RevistaResponse obtenerRevistaPorId(Long id);

	RevistaResponse actualizarRevista(Long id, RevistaRequest request);

	void eliminarRevista(Long id);
}
