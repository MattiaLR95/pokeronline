package it.prova.pokeronline.web.api.exception;

public class NotEnoughExperienceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotEnoughExperienceException(String message) {
		super(message);
	}

}
