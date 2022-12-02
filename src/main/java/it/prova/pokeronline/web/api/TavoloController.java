package it.prova.pokeronline.web.api;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("api/tavolo")
public class TavoloController {

	@Autowired
	private TavoloService tavoloService;

	@GetMapping
	public List<TavoloDTO> listAll(Principal principal) {
		return TavoloDTO.createTavoloDTOFromModelList(tavoloService.listAllTavoli(principal.getName()), false);
	}

	@GetMapping("/{id}")
	public TavoloDTO findById(@PathVariable(value = "id", required = true) long id, Principal principal) {
		Tavolo tavolo = tavoloService.caricaSingoloTavoloConUtenti(id, principal.getName());

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		return TavoloDTO.buildTavoloDTOFromModel(tavolo, false);
	}
	
	@PostMapping
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTO tavoloInput, Principal principal) {
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (tavoloInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

		Tavolo tavoloInserito = tavoloService.inserisciNuovo(tavoloInput.buildTavoloModel(), principal.getName());
		return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito, false);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id, Principal principal) {
		tavoloService.rimuovi(id, principal.getName());
	}
	
	@PutMapping("/{id}")
	public TavoloDTO update(@Valid @RequestBody TavoloDTO tavoloInput, @PathVariable(required = true) Long id,
			Principal principal) {
		Tavolo tavolo = tavoloService.caricaSingoloTavolo(id);

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		tavoloInput.setId(id);
		Tavolo tavoloAggiornato = tavoloService.aggiorna(tavoloInput.buildTavoloModel(), principal.getName());
		return TavoloDTO.buildTavoloDTOFromModel(tavoloAggiornato, false);
	}
	
	@PostMapping("/search")
	public List<TavoloDTO> search(@RequestBody TavoloDTO example, Principal principal) {
		return TavoloDTO.createTavoloDTOFromModelList(
				tavoloService.findByExample(example.buildTavoloModel(), principal.getName()), false);
	}
}
