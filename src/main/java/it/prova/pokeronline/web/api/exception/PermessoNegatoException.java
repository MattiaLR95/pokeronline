package it.prova.pokeronline.web.api.exception;

public class PermessoNegatoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PermessoNegatoException(String message) {
		super(message);
	}

}
