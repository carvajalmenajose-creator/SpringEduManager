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

import cl.bootcamp.springedumanager_2.model.Curso;
import cl.bootcamp.springedumanager_2.service.CursoService;

@RestController
@RequestMapping("/api/cursos")
public class CursoRestController {

	private final CursoService cursoService;

	public CursoRestController(CursoService cursoService) {
		this.cursoService = cursoService;
	}

	@GetMapping
	public List<Curso> listar() {
		return cursoService.listar();
	}

	@GetMapping("/{id}")
	public Curso buscar(@PathVariable int id) {
		return cursoService.buscarPorId(id);
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@PostMapping
	public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
		curso.setId(0);
		return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@PutMapping("/{id}")
	public Curso actualizar(@PathVariable int id, @RequestBody Curso curso) {
		curso.setId(id);
		return cursoService.guardar(curso);
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable int id) {
		cursoService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
