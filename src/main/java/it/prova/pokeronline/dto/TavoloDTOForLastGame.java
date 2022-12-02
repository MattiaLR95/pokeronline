package it.prova.pokeronline.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Tavolo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloDTOForLastGame {
	private Long id;

	private Integer esperienzaMinima;

	private Integer cifraMinima;

	private String denominazione;

	private LocalDate dataCreazione;

	public TavoloDTOForLastGame() {
		super();
	}

	public TavoloDTOForLastGame(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione,
			LocalDate dataCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
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

	public Tavolo buildTavoloModel() {
		Tavolo result = new Tavolo(this.id, this.esperienzaMinima, this.cifraMinima, this.denominazione,
				this.dataCreazione);
		return result;
	}

	public static TavoloDTOForLastGame buildTavoloDTOFromModel(Tavolo tavoloModel) {
		TavoloDTOForLastGame result = new TavoloDTOForLastGame(tavoloModel.getId(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDenominazione(), tavoloModel.getDataCreazione());

		return result;
	}

	public static List<TavoloDTOForLastGame> createTavoloDTOFromModelList(List<Tavolo> modelListInput) {
		return modelListInput.stream().map(tavoloEntity -> {
			return TavoloDTOForLastGame.buildTavoloDTOFromModel(tavoloEntity);
		}).collect(Collectors.toList());
	}

}