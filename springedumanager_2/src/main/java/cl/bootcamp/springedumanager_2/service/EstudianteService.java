package cl.bootcamp.springedumanager_2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.bootcamp.springedumanager_2.exception.RecursoNoEncontradoException;
import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Estudiante;
import cl.bootcamp.springedumanager_2.model.Rol;
import cl.bootcamp.springedumanager_2.model.Usuario;
import cl.bootcamp.springedumanager_2.repository.EstudianteRepository;
import cl.bootcamp.springedumanager_2.repository.UsuarioRepository;

@Service
public class EstudianteService {

	private final EstudianteRepository estudianteRepository;
	private final UsuarioRepository usuarioRepository;
	private final UsuarioService usuarioService;

	public EstudianteService(EstudianteRepository estudianteRepository, UsuarioRepository usuarioRepository,
			UsuarioService usuarioService) {
		this.estudianteRepository = estudianteRepository;
		this.usuarioRepository = usuarioRepository;
		this.usuarioService = usuarioService;
	}

	public List<Estudiante> listar() {
		return estudianteRepository.findAll();
	}

	public Estudiante buscarPorId(int id) {
		return estudianteRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
	}

	@Transactional
	public Estudiante guardar(Estudiante estudiante) {
		if (estudiante.getId() == 0) {
			if (estudianteRepository.existsByEmailIgnoreCase(estudiante.getEmail())
					|| usuarioRepository.existsByCorreoIgnoreCase(estudiante.getEmail())) {
				throw new ReglasNegocioException("El correo ya está registrado");
			}
			Usuario usuario = usuarioService.crear(estudiante.getNombre(), estudiante.getEmail(),
					UsuarioService.CLAVE_INICIAL, Rol.ESTUDIANTE);
			estudiante.setUsuario(usuario);
		} else {
			Estudiante actual = buscarPorId(estudiante.getId());
			if (estudianteRepository.existsByEmailIgnoreCaseAndIdNot(estudiante.getEmail(), estudiante.getId())) {
				throw new ReglasNegocioException("El correo ya está registrado");
			}
			if (actual.getUsuario() != null) {
				usuarioService.actualizarDatos(actual.getUsuario(), estudiante.getNombre(), estudiante.getEmail());
				estudiante.setUsuario(actual.getUsuario());
			} else {
				Usuario usuario = usuarioService.crear(estudiante.getNombre(), estudiante.getEmail(),
						UsuarioService.CLAVE_INICIAL, Rol.ESTUDIANTE);
				estudiante.setUsuario(usuario);
			}
		}
		return estudianteRepository.save(estudiante);
	}

	public void eliminar(int id) {
		Estudiante estudiante = buscarPorId(id);
		estudianteRepository.delete(estudiante);
	}
}
