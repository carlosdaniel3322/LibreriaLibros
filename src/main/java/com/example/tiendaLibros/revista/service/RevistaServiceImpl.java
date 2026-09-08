package com.example.tiendaLibros.revista.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tiendaLibros.revista.dto.RevistaRequest;
import com.example.tiendaLibros.revista.dto.RevistaResponse;
import com.example.tiendaLibros.revista.exception.RevistaNotFoundException;
import com.example.tiendaLibros.revista.model.Revista;
import com.example.tiendaLibros.revista.repository.RevistaRepository;

@Service
public class RevistaServiceImpl implements RevistaService {

	private final RevistaRepository revistaRepository;

	public RevistaServiceImpl(RevistaRepository revistaRepository) {
		this.revistaRepository = revistaRepository;
	}

	@Override
	public RevistaResponse crearRevista(RevistaRequest request) {
		return toResponse(revistaRepository.save(toModel(null, request)));
	}

	@Override
	public List<RevistaResponse> listarRevistas() {
		return revistaRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public RevistaResponse obtenerRevistaPorId(Long id) {
		return revistaRepository.findById(id).map(this::toResponse)
				.orElseThrow(() -> new RevistaNotFoundException(id));
	}

	@Override
	public RevistaResponse actualizarRevista(Long id, RevistaRequest request) {
		revistaRepository.findById(id).orElseThrow(() -> new RevistaNotFoundException(id));
		return toResponse(revistaRepository.save(toModel(id, request)));
	}

	@Override
	public void eliminarRevista(Long id) {
		revistaRepository.findById(id).orElseThrow(() -> new RevistaNotFoundException(id));
		revistaRepository.deleteById(id);
	}

	private Revista toModel(Long id, RevistaRequest request) {
		return new Revista(id, request.titulo(), request.editorial(), request.issn(), request.precio(), request.stock());
	}

	private RevistaResponse toResponse(Revista revista) {
		return new RevistaResponse(revista.id(), revista.titulo(), revista.editorial(), revista.issn(), revista.precio(), revista.stock());
	}
}
