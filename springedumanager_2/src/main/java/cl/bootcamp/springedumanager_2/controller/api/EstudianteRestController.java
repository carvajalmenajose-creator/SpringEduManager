package cl.bootcamp.springedumanager_2.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bootcamp.springedumanager_2.model.Estudiante;
import cl.bootcamp.springedumanager_2.service.EstudianteService;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteRestController {

	private final EstudianteService estudianteService;

	public EstudianteRestController(EstudianteService estudianteService) {
		this.estudianteService = estudianteService;
	}

	@GetMapping
	public List<Estudiante> listar() {
		return estudianteService.listar();
	}

	@GetMapping("/{id}")
	public Estudiante buscar(@PathVariable int id) {
		return estudianteService.buscarPorId(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
		estudiante.setId(0);
		return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.guardar(estudiante));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public Estudiante actualizar(@PathVariable int id, @RequestBody Estudiante estudiante) {
		estudiante.setId(id);
		return estudianteService.guardar(estudiante);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable int id) {
		estudianteService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
