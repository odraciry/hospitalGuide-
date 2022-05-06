package br.com.sp.rick.hospitalguia.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.rick.hospitalguia.model.Avaliacao;

public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long>{

	public List<Avaliacao> findByHospitalId(Long idHosp);
	
}
