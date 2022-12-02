package it.prova.pokeronline.web.api;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
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
	
}
