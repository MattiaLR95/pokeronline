package it.prova.pokeronline.web.api.exception;

public class PlayerBusyToAnotherTableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlayerBusyToAnotherTableException(String message) {
		super(message);
	}

}
