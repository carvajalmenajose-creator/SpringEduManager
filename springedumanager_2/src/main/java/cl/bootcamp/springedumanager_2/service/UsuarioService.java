package cl.bootcamp.springedumanager_2.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.bootcamp.springedumanager_2.exception.RecursoNoEncontradoException;
import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Estudiante;
import cl.bootcamp.springedumanager_2.model.Profesor;
import cl.bootcamp.springedumanager_2.model.Rol;
import cl.bootcamp.springedumanager_2.model.Usuario;
import cl.bootcamp.springedumanager_2.repository.EstudianteRepository;
import cl.bootcamp.springedumanager_2.repository.ProfesorRepository;
import cl.bootcamp.springedumanager_2.repository.UsuarioRepository;

@Service
public class UsuarioService {

	public static final String CLAVE_INICIAL = "1234";

	private final UsuarioRepository usuarioRepository;
	private final EstudianteRepository estudianteRepository;
	private final ProfesorRepository profesorRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository,
			ProfesorRepository profesorRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.estudianteRepository = estudianteRepository;
		this.profesorRepository = profesorRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	public Usuario buscarPorId(int id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
	}

	public Usuario crear(String nombre, String correo, String clavePlana, Rol rol) {
		if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
			throw new ReglasNegocioException("El correo ya está registrado como usuario");
		}
		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setCorreo(correo);
		usuario.setPassword(passwordEncoder.encode(clavePlana));
		usuario.setRol(rol);
		usuario.setActivo(true);
		return usuarioRepository.save(usuario);
	}

	public void actualizarDatos(Usuario usuario, String nombre, String correo) {
		if (usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(correo, usuario.getId())) {
			throw new ReglasNegocioException("El correo ya está registrado como usuario");
		}
		usuario.setNombre(nombre);
		usuario.setCorreo(correo);
	}

	@Transactional
	public Usuario guardarDesdeAdmin(int id, String nombre, String correo, String clavePlana, Rol rol,
			boolean activo) {
		if (rol == null) {
			throw new ReglasNegocioException("Debe indicar un rol");
		}

		Usuario usuario;
		if (id == 0) {
			String clave = (clavePlana == null || clavePlana.isBlank()) ? CLAVE_INICIAL : clavePlana;
			usuario = crear(nombre, correo, clave, rol);
			usuario.setActivo(activo);
			usuario = usuarioRepository.save(usuario);
		} else {
			usuario = buscarPorId(id);
			if (usuario.getRol() == Rol.ADMIN && rol != Rol.ADMIN && esUltimoAdmin(usuario)) {
				throw new ReglasNegocioException("Debe permanecer al menos un administrador activo");
			}
			if (usuario.getRol() == Rol.ADMIN && usuario.isActivo() && !activo && esUltimoAdmin(usuario)) {
				throw new ReglasNegocioException("No se puede desactivar al último administrador");
			}
			actualizarDatos(usuario, nombre, correo);
			usuario.setRol(rol);
			usuario.setActivo(activo);
			if (clavePlana != null && !clavePlana.isBlank()) {
				usuario.setPassword(passwordEncoder.encode(clavePlana));
			}
			usuario = usuarioRepository.save(usuario);
		}
		sincronizarPerfilAcademico(usuario);
		return usuario;
	}

	@Transactional
	public void eliminar(int id, int idUsuarioActual) {
		if (id == idUsuarioActual) {
			throw new ReglasNegocioException("No puede eliminar su propio usuario");
		}
		Usuario usuario = buscarPorId(id);
		if (usuario.getRol() == Rol.ADMIN && esUltimoAdmin(usuario)) {
			throw new ReglasNegocioException("Debe permanecer al menos un administrador");
		}

		profesorRepository.findByUsuario_Id(id).ifPresent(profesorRepository::delete);
		estudianteRepository.findByUsuario_Id(id).ifPresent(estudianteRepository::delete);
		if (usuarioRepository.existsById(id)) {
			usuarioRepository.deleteById(id);
		}
	}

	private boolean esUltimoAdmin(Usuario usuario) {
		return usuario.isActivo() && usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN) <= 1;
	}

	private void sincronizarPerfilAcademico(Usuario usuario) {
		if (usuario.getRol() == Rol.PROFESOR) {
			Profesor profesor = profesorRepository.findByUsuario_Id(usuario.getId()).orElse(null);
			if (profesor == null) {
				if (profesorRepository.existsByEmailIgnoreCase(usuario.getCorreo())
						|| estudianteRepository.existsByEmailIgnoreCase(usuario.getCorreo())) {
					throw new ReglasNegocioException("El correo ya está registrado en un perfil académico");
				}
				profesor = new Profesor(0, usuario.getNombre(), usuario.getCorreo(), "");
				profesor.setUsuario(usuario);
			} else {
				if (profesorRepository.existsByEmailIgnoreCaseAndIdNot(usuario.getCorreo(), profesor.getId())) {
					throw new ReglasNegocioException("El correo ya está registrado como profesor");
				}
				profesor.setNombre(usuario.getNombre());
				profesor.setEmail(usuario.getCorreo());
			}
			profesorRepository.save(profesor);
		} else if (usuario.getRol() == Rol.ESTUDIANTE) {
			Estudiante estudiante = estudianteRepository.findByUsuario_Id(usuario.getId()).orElse(null);
			if (estudiante == null) {
				if (estudianteRepository.existsByEmailIgnoreCase(usuario.getCorreo())
						|| profesorRepository.existsByEmailIgnoreCase(usuario.getCorreo())) {
					throw new ReglasNegocioException("El correo ya está registrado en un perfil académico");
				}
				estudiante = new Estudiante(0, usuario.getNombre(), usuario.getCorreo());
				estudiante.setUsuario(usuario);
			} else {
				if (estudianteRepository.existsByEmailIgnoreCaseAndIdNot(usuario.getCorreo(), estudiante.getId())) {
					throw new ReglasNegocioException("El correo ya está registrado como estudiante");
				}
				estudiante.setNombre(usuario.getNombre());
				estudiante.setEmail(usuario.getCorreo());
			}
			estudianteRepository.save(estudiante);
		}
	}
}
