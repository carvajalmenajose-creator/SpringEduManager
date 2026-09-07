package cl.bootcamp.springedumanager_2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cl.bootcamp.springedumanager_2.model.Curso;
import cl.bootcamp.springedumanager_2.model.Estudiante;
import cl.bootcamp.springedumanager_2.model.Evaluacion;
import cl.bootcamp.springedumanager_2.model.Profesor;
import cl.bootcamp.springedumanager_2.model.Rol;
import cl.bootcamp.springedumanager_2.model.Usuario;
import cl.bootcamp.springedumanager_2.repository.CursoRepository;
import cl.bootcamp.springedumanager_2.repository.EstudianteRepository;
import cl.bootcamp.springedumanager_2.repository.EvaluacionRepository;
import cl.bootcamp.springedumanager_2.repository.ProfesorRepository;
import cl.bootcamp.springedumanager_2.repository.UsuarioRepository;
import cl.bootcamp.springedumanager_2.service.UsuarioService;

@Component
public class DataInitializer implements CommandLineRunner {

	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	private final EstudianteRepository estudianteRepository;
	private final ProfesorRepository profesorRepository;
	private final CursoRepository cursoRepository;
	private final EvaluacionRepository evaluacionRepository;

	public DataInitializer(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
			EstudianteRepository estudianteRepository, ProfesorRepository profesorRepository,
			CursoRepository cursoRepository, EvaluacionRepository evaluacionRepository) {
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
		this.estudianteRepository = estudianteRepository;
		this.profesorRepository = profesorRepository;
		this.cursoRepository = cursoRepository;
		this.evaluacionRepository = evaluacionRepository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (usuarioRepository.count() == 0) {
			usuarioService.crear("Administrador", "admin@springedumanager.cl", UsuarioService.CLAVE_INICIAL,
					Rol.ADMIN);
		}

		if (profesorRepository.count() == 0) {
			Profesor profesor = new Profesor(0, "Carla Soto", "carla.soto@springedumanager.cl", "Java");
			profesor.setUsuario(usuarioService.crear(profesor.getNombre(), profesor.getEmail(),
					UsuarioService.CLAVE_INICIAL, Rol.PROFESOR));
			profesorRepository.save(profesor);
		}

		if (estudianteRepository.count() == 0) {
			guardarEstudiante("Roberto Gómez", "roberto@correo.cl");
			guardarEstudiante("Juan Pérez", "juan@correo.cl");
			guardarEstudiante("Ana Salazár", "ana@correo.cl");
		} else {
			for (Estudiante estudiante : estudianteRepository.findAll()) {
				if (estudiante.getUsuario() == null) {
					Usuario usuario = usuarioService.crear(estudiante.getNombre(), estudiante.getEmail(),
							UsuarioService.CLAVE_INICIAL, Rol.ESTUDIANTE);
					estudiante.setUsuario(usuario);
					estudianteRepository.save(estudiante);
				}
			}
		}

		if (cursoRepository.count() == 0) {
			cursoRepository.save(new Curso(0, "Java", "Bootcamp Full Stack Java"));
			cursoRepository.save(new Curso(0, "Python", "Bootcamp Full Stack Python"));
			cursoRepository.save(new Curso(0, "PHP", "Bootcamp Full Stack PHP"));
		}

		if (evaluacionRepository.count() == 0) {
			evaluacionRepository.save(new Evaluacion(0, "Prueba Java", "Roberto Gómez", "Java", 6.2));
			evaluacionRepository.save(new Evaluacion(0, "Proyecto Python", "Juan Pérez", "Python", 5.8));
			evaluacionRepository.save(new Evaluacion(0, "Evaluación PHP", "Ana Salazár", "PHP", 6.5));
		}
	}

	private void guardarEstudiante(String nombre, String email) {
		Estudiante estudiante = new Estudiante(0, nombre, email);
		estudiante.setUsuario(
				usuarioService.crear(nombre, email, UsuarioService.CLAVE_INICIAL, Rol.ESTUDIANTE));
		estudianteRepository.save(estudiante);
	}
}
