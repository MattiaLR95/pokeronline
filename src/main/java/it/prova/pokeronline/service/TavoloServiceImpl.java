package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository tavoloRepository;

	@Override
	public List<Tavolo> listAllTavoli() {
		return (List<Tavolo>) tavoloRepository.findAll();
	}

	@Override
	public Tavolo caricaSingoloTavolo(Long id) {
		return tavoloRepository.findById(id).orElse(null);
	}

	@Override
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	public void rimuovi(Long idToRemove) throws TavoloNotFoundException {
		Tavolo tavoloDaEliminare = tavoloRepository.findById(idToRemove).orElseThrow(
				() -> new TavoloNotFoundException("Tavolo not found! Impossibile completare l'operazione"));

		tavoloRepository.delete(tavoloDaEliminare);
	}

	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		return (List<Tavolo>) tavoloRepository.findByExample(example);
	}

}
