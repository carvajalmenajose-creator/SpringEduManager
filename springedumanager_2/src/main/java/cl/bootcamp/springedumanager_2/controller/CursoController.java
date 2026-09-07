package cl.bootcamp.springedumanager_2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Curso;
import cl.bootcamp.springedumanager_2.service.CursoService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

	private final CursoService cursoService;

	public CursoController(CursoService cursoService) {
		this.cursoService = cursoService;
	}

	@GetMapping({ "", "/" })
	public String irListar() {
		return "redirect:/cursos/listar";
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("listaCursos", cursoService.listar());
		return "cursos/lista";
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Curso curso, RedirectAttributes redirectAttributes) {
		try {
			cursoService.guardar(curso);
		} catch (ReglasNegocioException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/cursos/listar";
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			cursoService.eliminar(id);
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/cursos/listar";
	}
}
