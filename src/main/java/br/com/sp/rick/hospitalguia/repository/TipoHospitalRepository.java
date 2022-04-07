package br.com.sp.rick.hospitalguia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.rick.hospitalguia.model.TipoHospital;

public interface TipoHospitalRepository extends PagingAndSortingRepository<TipoHospital, Long>{
	@Query("SELECT c FROM 	TipoHospital c WHERE c.palavrasChave LIKE %:n%")
	public List<TipoHospital> buscarPal(@Param("n") String algo);
	
	@Query("SELECT c FROM 	TipoHospital c WHERE c.descricao LIKE %:n%")
	public List<TipoHospital> buscarDescri(@Param("n") String algo);
	
	@Query("SELECT c FROM 	TipoHospital c WHERE c.nome LIKE %:n%")
	public List<TipoHospital> buscarNome(@Param("n") String algo);
	
	@Query("SELECT c FROM  TipoHospital c WHERE c.nome LIKE %:n% OR c.descricao LIKE %:n% OR c.palavrasChave LIKE %:n%")
	public List<TipoHospital> buscarPorTudo(@Param("n") String algo);
	
	public List<TipoHospital> findAllByOrderByNomeAsc();
	
}
