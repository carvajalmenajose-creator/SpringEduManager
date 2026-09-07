package cl.bootcamp.springedumanager_2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(nullable = false, length = 150)
	private String nombre;

	@NotBlank
	@Column(nullable = false, length = 150)
	private String estudiante;

	@NotBlank
	@Column(nullable = false, length = 150)
	private String curso;

	@Column(nullable = false)
	private double nota;

	public Evaluacion() {
	}

	public Evaluacion(int id, String nombre, String estudiante, String curso, double nota) {
		this.id = id;
		this.nombre = nombre;
		this.estudiante = estudiante;
		this.curso = curso;
		this.nota = nota;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(String estudiante) {
		this.estudiante = estudiante;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}
}
