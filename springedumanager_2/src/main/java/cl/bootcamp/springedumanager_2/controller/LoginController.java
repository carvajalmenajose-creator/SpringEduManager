package cl.bootcamp.springedumanager_2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/")
	public String irInicio() {
		return "redirect:/home";
	}

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}
}
