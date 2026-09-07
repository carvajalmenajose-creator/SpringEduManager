package cl.bootcamp.springedumanager_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/css/**", "/js/**").permitAll()
						.requestMatchers("/usuarios/**", "/profesores/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/estudiantes/guardar", "/estudiantes/eliminar/**")
						.hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/cursos/**", "/evaluaciones/**")
						.hasAnyRole("ADMIN", "PROFESOR")
						.requestMatchers("/api/**").authenticated()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("correo")
						.passwordParameter("password")
						.defaultSuccessUrl("/home", true)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout")
						.permitAll())
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
