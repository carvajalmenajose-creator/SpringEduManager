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
import cl.bootcamp.springedumanager_2.model.Evaluacion;
import cl.bootcamp.springedumanager_2.service.CursoService;
import cl.bootcamp.springedumanager_2.service.EstudianteService;
import cl.bootcamp.springedumanager_2.service.EvaluacionService;

@Controller
@RequestMapping("/evaluaciones")
public class EvaluacionController {

	private final EvaluacionService evaluacionService;
	private final EstudianteService estudianteService;
	private final CursoService cursoService;

	public EvaluacionController(EvaluacionService evaluacionService, EstudianteService estudianteService,
			CursoService cursoService) {
		this.evaluacionService = evaluacionService;
		this.estudianteService = estudianteService;
		this.cursoService = cursoService;
	}

	@GetMapping({ "", "/" })
	public String irListar() {
		return "redirect:/evaluaciones/listar";
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("listaEvaluaciones", evaluacionService.listar());
		model.addAttribute("listaEstudiantes", estudianteService.listar());
		model.addAttribute("listaCursos", cursoService.listar());
		return "evaluaciones/lista";
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Evaluacion evaluacion, RedirectAttributes redirectAttributes) {
		try {
			evaluacionService.guardar(evaluacion);
		} catch (ReglasNegocioException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/evaluaciones/listar";
	}

	@PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			evaluacionService.eliminar(id);
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/evaluaciones/listar";
	}
}
