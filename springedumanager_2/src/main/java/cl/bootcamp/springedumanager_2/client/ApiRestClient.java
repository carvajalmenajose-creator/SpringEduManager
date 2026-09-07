package cl.bootcamp.springedumanager_2.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cl.bootcamp.springedumanager_2.model.Curso;
import cl.bootcamp.springedumanager_2.model.Estudiante;

@Component
public class ApiRestClient {

	private final RestTemplate restTemplate;

	public ApiRestClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Estudiante[] listarEstudiantes(String urlBase, String usuario, String clave) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(usuario, clave);
		HttpEntity<Void> entidad = new HttpEntity<>(headers);
		ResponseEntity<Estudiante[]> respuesta = restTemplate.exchange(urlBase + "/api/estudiantes", HttpMethod.GET,
				entidad, Estudiante[].class);
		return respuesta.getBody();
	}

	public Curso[] listarCursos(String urlBase, String usuario, String clave) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(usuario, clave);
		HttpEntity<Void> entidad = new HttpEntity<>(headers);
		ResponseEntity<Curso[]> respuesta = restTemplate.exchange(urlBase + "/api/cursos", HttpMethod.GET, entidad,
				Curso[].class);
		return respuesta.getBody();
	}
}
