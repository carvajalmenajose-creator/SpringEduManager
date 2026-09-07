const idEvaluacion = document.getElementById('idEvaluacion');
const nombreEvaluacion = document.getElementById('nombreEvaluacion');
const estudianteEvaluacion = document.getElementById('estudianteEvaluacion');
const cursoEvaluacion = document.getElementById('cursoEvaluacion');
const notaEvaluacion = document.getElementById('notaEvaluacion');
const tituloModalEvaluacion = document.getElementById('tituloModalEvaluacion');

function nuevaEvaluacion() {
	if (!idEvaluacion) {
		return;
	}
	tituloModalEvaluacion.textContent = 'Nueva Evaluación';
	idEvaluacion.value = 0;
	nombreEvaluacion.value = '';
	estudianteEvaluacion.value = '';
	cursoEvaluacion.value = '';
	notaEvaluacion.value = '';
}

function editarEvaluacion(boton) {
	if (!idEvaluacion) {
		return;
	}
	tituloModalEvaluacion.textContent = 'Editar Evaluación';
	idEvaluacion.value = boton.dataset.id;
	nombreEvaluacion.value = boton.dataset.nombre;
	estudianteEvaluacion.value = boton.dataset.estudiante;
	cursoEvaluacion.value = boton.dataset.curso;
	notaEvaluacion.value = boton.dataset.nota;
}
