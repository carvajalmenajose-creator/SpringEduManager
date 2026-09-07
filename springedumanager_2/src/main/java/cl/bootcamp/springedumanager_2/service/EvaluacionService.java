package cl.bootcamp.springedumanager_2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.bootcamp.springedumanager_2.exception.RecursoNoEncontradoException;
import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Evaluacion;
import cl.bootcamp.springedumanager_2.repository.EvaluacionRepository;

@Service
public class EvaluacionService {

	private final EvaluacionRepository evaluacionRepository;

	public EvaluacionService(EvaluacionRepository evaluacionRepository) {
		this.evaluacionRepository = evaluacionRepository;
	}

	public List<Evaluacion> listar() {
		return evaluacionRepository.findAll();
	}

	public Evaluacion buscarPorId(int id) {
		return evaluacionRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Evaluación no encontrada"));
	}

	public Evaluacion guardar(Evaluacion evaluacion) {
		if (evaluacion.getNota() < 1.0 || evaluacion.getNota() > 7.0) {
			throw new ReglasNegocioException("La nota debe estar entre 1.0 y 7.0");
		}
		if (evaluacion.getId() != 0) {
			buscarPorId(evaluacion.getId());
		}
		return evaluacionRepository.save(evaluacion);
	}

	public void eliminar(int id) {
		Evaluacion evaluacion = buscarPorId(id);
		evaluacionRepository.delete(evaluacion);
	}
}
