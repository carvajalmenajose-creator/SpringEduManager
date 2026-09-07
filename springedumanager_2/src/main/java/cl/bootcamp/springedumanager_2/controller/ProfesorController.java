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
import cl.bootcamp.springedumanager_2.model.Profesor;
import cl.bootcamp.springedumanager_2.service.ProfesorService;

@Controller
@RequestMapping("/profesores")
@PreAuthorize("hasRole('ADMIN')")
public class ProfesorController {

	private final ProfesorService profesorService;

	public ProfesorController(ProfesorService profesorService) {
		this.profesorService = profesorService;
	}

	@GetMapping({ "", "/" })
	public String irListar() {
		return "redirect:/profesores/listar";
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("listaProfesores", profesorService.listar());
		return "profesores/lista";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Profesor profesor, RedirectAttributes redirectAttributes) {
		try {
			profesorService.guardar(profesor);
			redirectAttributes.addFlashAttribute("exito",
					"Profesor guardado. Puede iniciar sesión con su correo y la clave 1234.");
		} catch (ReglasNegocioException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/profesores/listar";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			profesorService.eliminar(id);
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/profesores/listar";
	}
}
