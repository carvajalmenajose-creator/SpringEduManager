package cl.bootcamp.springedumanager_2.exception;

public class ReglasNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReglasNegocioException(String mensaje) {
		super(mensaje);
	}
}
