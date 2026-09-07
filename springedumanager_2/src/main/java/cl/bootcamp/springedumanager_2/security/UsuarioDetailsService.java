package cl.bootcamp.springedumanager_2.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.bootcamp.springedumanager_2.model.Usuario;
import cl.bootcamp.springedumanager_2.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		return new UsuarioPrincipal(usuario);
	}
}
