package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import it.prova.pokeronline.web.api.exception.InsufficientFundException;
import it.prova.pokeronline.web.api.exception.NotEnoughExperienceException;
import it.prova.pokeronline.web.api.exception.NotYourTavoloException;
import it.prova.pokeronline.web.api.exception.PlayerBusyToAnotherTableException;
import it.prova.pokeronline.web.api.exception.TavoloConGiocatoriException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	public List<Tavolo> listAllTavoli(String username) {

		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");
		if (utenteInSessione.isAdmin())
			return (List<Tavolo>) repository.findAll();
		if (utenteInSessione.isSpecialPlayer())
			return repository.findAllTavoloEager(utenteInSessione.getId());

		return null;

	}

	@Override
	public Tavolo caricaSingoloTavolo(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Tavolo caricaSingoloTavoloConUtenti(Long id, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		if (utenteInSessione.isAdmin())
			return repository.findSingleTavoloEagerAdmin(id);

		if (utenteInSessione.isSpecialPlayer())
			return repository.findSingleTavoloEagerSpecialPlayer(utenteInSessione.getId(), id);

		return null;
	}

	@Override
	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		Tavolo tavoloReloaded = repository.findById(tavoloInstance.getId()).orElse(null);
		if (tavoloReloaded == null)
			throw new TavoloNotFoundException("Tavolo non trovato.");

		if (utenteInSessione.isAdmin() && tavoloReloaded.getGiocatori().size() < 1) {
			tavoloReloaded.setUtenteCreazione(utenteInSessione);
			tavoloReloaded.setCifraMinima(tavoloInstance.getCifraMinima());
			tavoloReloaded.setEsperienzaMinima(tavoloInstance.getEsperienzaMinima());
			tavoloReloaded.setDenominazione(tavoloInstance.getDenominazione());
			return repository.save(tavoloReloaded);
		}

		if (utenteInSessione.isSpecialPlayer() && tavoloReloaded.getGiocatori().size() < 1) {
			if (!tavoloReloaded.getUtenteCreazione().getId().equals(utenteInSessione.getId()))
				throw new NotYourTavoloException("Non e' possibile modificare il tavolo creato da un'altro player!");

			tavoloReloaded.setUtenteCreazione(utenteInSessione);
			tavoloReloaded.setCifraMinima(tavoloInstance.getCifraMinima());
			tavoloReloaded.setEsperienzaMinima(tavoloInstance.getEsperienzaMinima());
			tavoloReloaded.setDenominazione(tavoloInstance.getDenominazione());
			return repository.save(tavoloReloaded);
		}
		return null;
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		tavoloInstance.setUtenteCreazione(utenteInSessione);
		tavoloInstance.setDataCreazione(LocalDate.now());
		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		Tavolo tavoloReloaded = repository.findById(idToRemove).orElse(null);
		if (tavoloReloaded == null)
			throw new TavoloNotFoundException("Tavolo non trovato.");

		if (tavoloReloaded.getGiocatori().size() > 0)
			throw new TavoloConGiocatoriException("Impossibile eliminare tavolo, ci stanno giocando dei players.");

		if (utenteInSessione.isAdmin() && tavoloReloaded.getGiocatori().size() < 1)
			repository.deleteById(idToRemove);

		if (utenteInSessione.isSpecialPlayer() && tavoloReloaded.getGiocatori().size() < 1) {
			if (!tavoloReloaded.getUtenteCreazione().getId().equals(utenteInSessione.getId()))
				throw new NotYourTavoloException("Non e' possibile eliminare il tavolo creato da un'altro player!");

			repository.deleteById(idToRemove);
		}

	}

	@Override
	public List<Tavolo> findByExample(Tavolo example, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		if (utenteInSessione.isAdmin())
			return repository.findByExample(example);

		if (utenteInSessione.isSpecialPlayer())
			example.setUtenteCreazione(utenteInSessione);
		return repository.findByExampleEager(example);

	}
	
	@Override
	public List<Tavolo> findAllTavoliPlayable(String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		return repository.findAllTavoliPlayable(utenteInSessione.getEsperienzaAccumulata());
	}

	@Override
	@Transactional
	public void play(Long id, String username) {
		Tavolo tavoloReloaded = repository.findById(id).orElse(null);
		if (tavoloReloaded == null)
			throw new TavoloNotFoundException("Tavolo non trovato.");

		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		if (utenteInSessione.getEsperienzaAccumulata() < tavoloReloaded.getEsperienzaMinima())
			throw new NotEnoughExperienceException("Impossibile giocare al tavolo selezionato, serve piu'esperienza!");

		if (utenteInSessione.getCreditoAccumulato() < tavoloReloaded.getCifraMinima())
			throw new InsufficientFundException("Impossibile giocare al tavolo selezionato, credito insufficiente!");

		List<Tavolo> tavoliDB = (List<Tavolo>) repository.findAll();

		for (Tavolo tavoloItem : tavoliDB) {
			for (Utente giocatoreItem : tavoloItem.getGiocatori()) {
				if (giocatoreItem.getId().equals(utenteInSessione.getId()))
					throw new PlayerBusyToAnotherTableException(
							"Impossibile giocare perche' sei gia' impegnato presso un altro tavolo.");
			}
		}

		double randomNumber = Math.random() * 1000;
		int totDaAggiungereOSottrarre = (int) randomNumber;

		Integer creditoAggiornato = utenteInSessione.getCreditoAccumulato() + totDaAggiungereOSottrarre;

		if (creditoAggiornato < 0) {
			utenteInSessione.setCreditoAccumulato(0);
			utenteRepository.save(utenteInSessione);
			throw new InsufficientFundException("Credito Esaurito.");
		}

		utenteInSessione.setCreditoAccumulato(creditoAggiornato);
		utenteRepository.save(utenteInSessione);
		tavoloReloaded.getGiocatori().add(utenteInSessione);
		repository.save(tavoloReloaded);

	}

	@Override
	@Transactional
	public void leave(Long id, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		Tavolo tavoloReloaded = repository.findById(id).orElse(null);
		if (tavoloReloaded == null)
			throw new TavoloNotFoundException("Tavolo non trovato.");

		for (Utente item : tavoloReloaded.getGiocatori()) {
			if (item.getId().equals(utenteInSessione.getId())) {

				utenteInSessione.setEsperienzaAccumulata(utenteInSessione.getEsperienzaAccumulata() + 1);
				utenteRepository.save(utenteInSessione);

				tavoloReloaded.getGiocatori().remove(utenteInSessione);
				repository.save(tavoloReloaded);
			}

		}

	}

	@Override
	public Tavolo lastGame(String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		List<Tavolo> tavoliDB = (List<Tavolo>) repository.findAll();

		for (Tavolo tavoloItem : tavoliDB) {
			for (Utente giocatoreItem : tavoloItem.getGiocatori()) {
				if (giocatoreItem.getId().equals(utenteInSessione.getId()))
					return tavoloItem;
			}
		}
		return new Tavolo();
	}


}
