package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {

	public List<Tavolo> listAllTavoli();

	public Tavolo caricaSingoloTavolo(Long id);

	public Tavolo aggiornaAdmin(Tavolo tavoloInstance);

	public Tavolo inserisciNuovo(Tavolo tavoloInstance);

	public void rimuovi(Long idToRemove);
	
	public List<Tavolo> findByExample(Tavolo example);

	Tavolo caricaSingoloTavoloConUtenti(Long id, String username);
	
}
