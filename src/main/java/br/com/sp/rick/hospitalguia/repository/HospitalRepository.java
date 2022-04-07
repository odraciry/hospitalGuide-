package br.com.sp.rick.hospitalguia.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.rick.hospitalguia.model.Hospital;
import br.com.sp.rick.hospitalguia.model.TipoHospital;

public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long>{
 
}
