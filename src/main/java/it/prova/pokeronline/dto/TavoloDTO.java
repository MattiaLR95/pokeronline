package it.prova.pokeronline.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloDTO {

	private Long id;

	@NotNull(message = "{esperienzaAccumulata.notnull}")
	private Integer esperienzaMinima;

	@NotNull(message = "{creditoAccumulato.notnull}")
	private Integer cifraMinima;

	@NotBlank(message = "{denominazione.notblank}")
	private String denominazione;

	private LocalDate dataCreazione;

	private Set<Utente> utenti = new HashSet<Utente>(0);

	private UtenteDTO utenteCreazione;

	public TavoloDTO() {
		super();
	}

	public TavoloDTO(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione,
			LocalDate dataCreazione, Set<Utente> utenti, UtenteDTO utenteCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.utenti = utenti;
		this.utenteCreazione = utenteCreazione;
	}

	public TavoloDTO(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione,
			LocalDate localDate) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = localDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEsperienzaMinima() {
		return esperienzaMinima;
	}

	public void setEsperienzaMinima(Integer esperienzaMinima) {
		this.esperienzaMinima = esperienzaMinima;
	}

	public Integer getCifraMinima() {
		return cifraMinima;
	}

	public void setCifraMinima(Integer cifraMinima) {
		this.cifraMinima = cifraMinima;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<Utente> getUtenti() {
		return utenti;
	}

	public void setUtenti(Set<Utente> utenti) {
		this.utenti = utenti;
	}

	public UtenteDTO getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(UtenteDTO utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo buildTavoloModel() {
		Tavolo result = new Tavolo(this.id, this.esperienzaMinima, this.cifraMinima, this.denominazione,
				this.dataCreazione);
		if (this.utenti.size() > 0)
			result.setGiocatori(utenti);
		return result;
	}

	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel, boolean includeUtente) {
		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDenominazione(), tavoloModel.getDataCreazione());

		if (includeUtente)
			result.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(tavoloModel.getUtenteCreazione()));

		if (tavoloModel.getGiocatori().size() > 0)
			result.setUtenti(tavoloModel.getGiocatori());

		return result;
	}

	public static List<TavoloDTO> createTavoloDTOFromModelList(List<Tavolo> modelListInput, boolean includeUtente) {
		return modelListInput.stream().map(tavoloEntity -> {
			return TavoloDTO.buildTavoloDTOFromModel(tavoloEntity, includeUtente);
		}).collect(Collectors.toList());
	}

}