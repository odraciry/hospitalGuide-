package br.com.sp.rick.hospitalguia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.rick.hospitalguia.model.Hospital;

public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long>{
	
	public List<Hospital> findByTipoId(Long idTipo);
}
