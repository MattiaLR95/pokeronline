package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.TavoloConGiocatoriException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
@Transactional(readOnly = true)
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
	@Transactional
	public Tavolo aggiornaAdmin(Tavolo tavoloInstance) {
		if (!tavoloInstance.getGiocatori().isEmpty())
			throw new TavoloConGiocatoriException(
					"Giocatori ancora presenti nel tavolo! Impossibile completare l'operazione");
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		if (tavoloInstance.getId().equals(null))
			throw new IdNotNullForInsertException("Vietato inserire l'id del tavolo! Operazione annullata");
		return tavoloRepository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) throws TavoloNotFoundException {
		Tavolo tavoloDaEliminare = tavoloRepository.findById(idToRemove).orElseThrow(
				() -> new TavoloNotFoundException("Tavolo not found! Impossibile completare l'operazione"));
		
		if(!tavoloDaEliminare.getGiocatori().isEmpty())
			throw new TavoloConGiocatoriException ("Giocatori ancora presenti nel tavolo! Impossibile completare l'operazione");

		tavoloRepository.delete(tavoloDaEliminare);
	}

	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		return (List<Tavolo>) tavoloRepository.findByExample(example);
	}

}
