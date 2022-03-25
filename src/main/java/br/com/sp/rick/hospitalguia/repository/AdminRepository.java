package br.com.sp.rick.hospitalguia.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.rick.hospitalguia.model.Administrador;

public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long>{

}
