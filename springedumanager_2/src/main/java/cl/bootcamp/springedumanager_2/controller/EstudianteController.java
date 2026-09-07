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
import cl.bootcamp.springedumanager_2.model.Estudiante;
import cl.bootcamp.springedumanager_2.service.EstudianteService;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

	private final EstudianteService estudianteService;

	public EstudianteController(EstudianteService estudianteService) {
		this.estudianteService = estudianteService;
	}

	@GetMapping({ "", "/" })
	public String irListar() {
		return "redirect:/estudiantes/listar";
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("listaEstudiantes", estudianteService.listar());
		return "estudiantes/lista";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Estudiante estudiante, RedirectAttributes redirectAttributes) {
		try {
			estudianteService.guardar(estudiante);
			redirectAttributes.addFlashAttribute("exito",
					"Estudiante guardado. Puede iniciar sesión con su correo y la clave 1234.");
		} catch (ReglasNegocioException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/estudiantes/listar";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			estudianteService.eliminar(id);
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/estudiantes/listar";
	}
}
