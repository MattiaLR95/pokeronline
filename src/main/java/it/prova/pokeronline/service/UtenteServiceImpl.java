package it.prova.pokeronline.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import it.prova.pokeronline.web.api.exception.NullException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@Service
@Transactional(readOnly = true)
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	@Override
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);
	}

	@Override
	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new UtenteNotFoundException("Utente non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setEmail(utenteInstance.getEmail());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		return repository.save(utenteReloaded);
	}

	@Override
	@Transactional
	public Utente inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDataRegistrazione(LocalDate.now());
		utenteInstance.setCreditoAccumulato(0);
		utenteInstance.setEsperienzaAccumulata(0);
		return repository.save(utenteInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Utente> findByExample(Utente example) {
		return repository.findByExample(example);
	}

	@Override
	public Utente findByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente eseguiAccesso(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);

	}

	@Override
	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Override
	@Transactional
	public void addCredito(Integer moneyToAdd, String username) {
		Utente utenteInSessione = repository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");

		if (moneyToAdd == null)
			throw new NullException("Il valore da aggiungere al credito non puo' essere null.");

		Integer creditoAumentato = utenteInSessione.getCreditoAccumulato() + moneyToAdd;

		utenteInSessione.setCreditoAccumulato(creditoAumentato);
		repository.save(utenteInSessione);

	}

}