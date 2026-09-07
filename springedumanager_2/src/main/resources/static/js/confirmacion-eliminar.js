document.addEventListener('submit', function (event) {
	const form = event.target;
	if (!form.classList.contains('form-eliminar')) {
		return;
	}

	event.preventDefault();

	const nombre = form.dataset.nombre || 'este registro';

	Swal.fire({
		title: '¿Eliminar registro?',
		html: 'Esta acción no se puede deshacer.<br>Se eliminará <strong>' + nombre + '</strong>.',
		icon: 'warning',
		showCancelButton: true,
		focusCancel: true,
		confirmButtonColor: '#FB2C36',
		cancelButtonColor: '#64748b',
		confirmButtonText: 'Sí, eliminar',
		cancelButtonText: 'Cancelar',
		reverseButtons: true
	}).then(function (resultado) {
		if (resultado.isConfirmed) {
			form.submit();
		}
	});
});
