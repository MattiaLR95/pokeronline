package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository {

	@Query("select t from Tavolo t join fetch t.utenteCreazione u where u.id = ?1")
	List<Tavolo> findAllTavoloEager(Long id);

	@Query("select t from Tavolo t left join fetch t.utenti us join fetch t.utenteCreazione u where u.id = ?1 and t.id = ?2")
	Tavolo findSingleTavoloEagerSpecialPlayer(Long idUtente, Long idTavolo);

	@Query("select t from Tavolo t left join fetch t.utenti us where t.id = ?1")
	Tavolo findSingleTavoloEagerAdmin(Long idTavolo);

	@Query("from Tavolo t where t.esperienzaMinima <= ?1")
	List<Tavolo> findAllTavoliPlayable(Integer esperienzaAccumulata);
}
