package it.prova.pokeronline.repository.utente;

import java.util.List;

import it.prova.pokeronline.model.Utente;

public interface CustomUtenteRepository {
	
	List<Utente> findByExample(Utente example);

}
