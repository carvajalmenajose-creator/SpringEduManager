package cl.bootcamp.springedumanager_2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.bootcamp.springedumanager_2.exception.ReglasNegocioException;
import cl.bootcamp.springedumanager_2.model.Rol;
import cl.bootcamp.springedumanager_2.security.UsuarioPrincipal;
import cl.bootcamp.springedumanager_2.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping({ "", "/" })
	public String irListar() {
		return "redirect:/usuarios/listar";
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("listaUsuarios", usuarioService.listar());
		model.addAttribute("roles", Rol.values());
		return "usuarios/lista";
	}

	@PostMapping("/guardar")
	public String guardar(@RequestParam int id, @RequestParam String nombre, @RequestParam String correo,
			@RequestParam(required = false) String password, @RequestParam Rol rol,
			@RequestParam(defaultValue = "false") boolean activo, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.guardarDesdeAdmin(id, nombre, correo, password, rol, activo);
			redirectAttributes.addFlashAttribute("exito",
					id == 0
							? "Usuario guardado. Si no indicó clave, puede iniciar sesión con 1234."
							: "Usuario actualizado.");
		} catch (ReglasNegocioException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/usuarios/listar";
	}

	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable int id,
			@AuthenticationPrincipal UsuarioPrincipal actual, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.eliminar(id, actual.getId());
			redirectAttributes.addFlashAttribute("exito", "Usuario eliminado.");
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/usuarios/listar";
	}
}
