package cl.bootcamp.springedumanager_2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.bootcamp.springedumanager_2.exception.RecursoNoEncontradoException;
import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Curso;
import cl.bootcamp.springedumanager_2.repository.CursoRepository;

@Service
public class CursoService {

	private final CursoRepository cursoRepository;

	public CursoService(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public List<Curso> listar() {
		return cursoRepository.findAll();
	}

	public Curso buscarPorId(int id) {
		return cursoRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado"));
	}

	public Curso guardar(Curso curso) {
		if (curso.getId() == 0) {
			if (cursoRepository.existsByNombreIgnoreCase(curso.getNombre())) {
				throw new ReglasNegocioException("El nombre del curso ya existe");
			}
		} else {
			buscarPorId(curso.getId());
			if (cursoRepository.existsByNombreIgnoreCaseAndIdNot(curso.getNombre(), curso.getId())) {
				throw new ReglasNegocioException("El nombre del curso ya existe");
			}
		}
		return cursoRepository.save(curso);
	}

	public void eliminar(int id) {
		Curso curso = buscarPorId(id);
		cursoRepository.delete(curso);
	}
}
