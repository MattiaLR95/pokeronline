package it.prova.pokeronline.repository.utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.pokeronline.model.Utente;

public class CustomUtenteRepositoryImpl implements CustomUtenteRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Utente> findByExample(Utente example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select distinct u from Utente u where u.id = u.id ");

		if (StringUtils.isNotEmpty(example.getUsername())) {
			whereClauses.add(" u.username like :username ");
			paramaterMap.put("username", "%" + example.getUsername() + "%");
		}
		if (StringUtils.isNotEmpty(example.getNome())) {
			whereClauses.add(" u.nome like :nome ");
			paramaterMap.put("nome", "%" + example.getNome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCognome())) {
			whereClauses.add(" u.cognome like :cognome ");
			paramaterMap.put("cognome", "%" + example.getCognome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getEmail())) {
			whereClauses.add(" u.email like :email ");
			paramaterMap.put("email", "%" + example.getEmail() + "%");
		}
		if (example.getDataRegistrazione() != null) {
			whereClauses.add(" u.dataRegistrazione >= :dataRegistrazione ");
			paramaterMap.put("dataRegistrazione", example.getDataRegistrazione());
		}
		if (example.getCreditoAccumulato() != null) {
			whereClauses.add(" u.creditoAccumulato >= :creditoAccumulato ");
			paramaterMap.put("creditoAccumulato", example.getCreditoAccumulato());
		}
		if (example.getEsperienzaAccumulata() != null) {
			whereClauses.add(" u.esperienzaAccumulata >= :esperienzaAccumulata ");
			paramaterMap.put("esperienzaAccumulata", example.getEsperienzaAccumulata());
		}
		if (example.getStato() != null) {
			whereClauses.add("u.stato = :stato ");
			paramaterMap.put("stato", example.getStato());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Utente> typedQuery = entityManager.createQuery(queryBuilder.toString(), Utente.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}
		return typedQuery.getResultList();
	}

}