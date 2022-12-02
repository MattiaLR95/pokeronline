package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;

public interface CustomTavoloRepository {

	List<Tavolo> findByExample (Tavolo example);
	
	List<Tavolo> findByExampleEager(Tavolo example);

}
