package it.prova.pokeronline.web.api;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.TavoloDTOForLastGame;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

@RestController
@RequestMapping("api/play")
public class GameController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private TavoloService tavoloService;

	@GetMapping("/compraCredito/{money}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addCredito(@PathVariable(required = true) Integer money, Principal principal) {
		utenteService.addCredito(money, principal.getName());
	}

	@PostMapping("/search")
	public List<TavoloDTO> search(Principal principal) {
		return TavoloDTO.createTavoloDTOFromModelList(tavoloService.findAllTavoliPlayable(principal.getName()), false);
	}

	@GetMapping("/{id}")
	public void play(@PathVariable(value = "id", required = true) long idTavolo, Principal principal) {
		tavoloService.play(idTavolo, principal.getName());
	}

	@GetMapping("/leave/{id}")
	public void leave(@PathVariable(value = "id", required = true) long idTavolo, Principal principal) {
		tavoloService.leave(idTavolo, principal.getName());
	}

	@GetMapping("/lastGame")
	public TavoloDTOForLastGame lastGame(Principal principal) {
		return TavoloDTOForLastGame.buildTavoloDTOFromModel(tavoloService.lastGame(principal.getName()));
	}

}