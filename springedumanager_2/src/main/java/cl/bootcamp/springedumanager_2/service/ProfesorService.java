package cl.bootcamp.springedumanager_2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.bootcamp.springedumanager_2.exception.RecursoNoEncontradoException;
import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Profesor;
import cl.bootcamp.springedumanager_2.model.Rol;
import cl.bootcamp.springedumanager_2.model.Usuario;
import cl.bootcamp.springedumanager_2.repository.ProfesorRepository;
import cl.bootcamp.springedumanager_2.repository.UsuarioRepository;

@Service
public class ProfesorService {

	private final ProfesorRepository profesorRepository;
	private final UsuarioRepository usuarioRepository;
	private final UsuarioService usuarioService;

	public ProfesorService(ProfesorRepository profesorRepository, UsuarioRepository usuarioRepository,
			UsuarioService usuarioService) {
		this.profesorRepository = profesorRepository;
		this.usuarioRepository = usuarioRepository;
		this.usuarioService = usuarioService;
	}

	public List<Profesor> listar() {
		return profesorRepository.findAll();
	}

	public Profesor buscarPorId(int id) {
		return profesorRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado"));
	}

	@Transactional
	public Profesor guardar(Profesor profesor) {
		if (profesor.getId() == 0) {
			if (profesorRepository.existsByEmailIgnoreCase(profesor.getEmail())
					|| usuarioRepository.existsByCorreoIgnoreCase(profesor.getEmail())) {
				throw new ReglasNegocioException("El correo ya está registrado");
			}
			Usuario usuario = usuarioService.crear(profesor.getNombre(), profesor.getEmail(),
					UsuarioService.CLAVE_INICIAL, Rol.PROFESOR);
			profesor.setUsuario(usuario);
		} else {
			Profesor actual = buscarPorId(profesor.getId());
			if (profesorRepository.existsByEmailIgnoreCaseAndIdNot(profesor.getEmail(), profesor.getId())) {
				throw new ReglasNegocioException("El correo ya está registrado");
			}
			if (actual.getUsuario() != null) {
				usuarioService.actualizarDatos(actual.getUsuario(), profesor.getNombre(), profesor.getEmail());
				profesor.setUsuario(actual.getUsuario());
			} else {
				Usuario usuario = usuarioService.crear(profesor.getNombre(), profesor.getEmail(),
						UsuarioService.CLAVE_INICIAL, Rol.PROFESOR);
				profesor.setUsuario(usuario);
			}
		}
		return profesorRepository.save(profesor);
	}

	public void eliminar(int id) {
		Profesor profesor = buscarPorId(id);
		profesorRepository.delete(profesor);
	}
}
