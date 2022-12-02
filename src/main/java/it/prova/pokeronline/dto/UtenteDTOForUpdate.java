package it.prova.pokeronline.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Utente;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtenteDTOForUpdate {

	private Long id;

	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	@NotBlank(message = "{email.notblank}")
	private String email;

	private Long[] ruoliIds;

	public UtenteDTOForUpdate() {
	}

	public UtenteDTOForUpdate(Long id, String username, String nome, String cognome) {
		super();
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
	}

	public UtenteDTOForUpdate(Long id, String username, String nome, String cognome, String email) {
		super();
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long[] getRuoliIds() {
		return ruoliIds;
	}

	public void setRuoli(Long[] ruoliIds) {
		this.ruoliIds = ruoliIds;
	}

	public Utente buildUtenteModel(boolean includeRoles) {
		Utente result = new Utente(this.id, this.username, this.nome, this.cognome, this.email);
		if (includeRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> new Ruolo(id)).collect(Collectors.toSet()));

		return result;
	}

	public static UtenteDTOForUpdate buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTOForUpdate result = new UtenteDTOForUpdate(utenteModel.getId(), utenteModel.getUsername(),
				utenteModel.getNome(), utenteModel.getCognome(), utenteModel.getEmail());

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

	public static List<UtenteDTOForUpdate> createUtenteDTOListFromModelList(List<Utente> modelListInput) {
		return modelListInput.stream().map(utenteEntity -> {
			return UtenteDTOForUpdate.buildUtenteDTOFromModel(utenteEntity);
		}).collect(Collectors.toList());
	}

}