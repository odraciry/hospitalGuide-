package br.com.sp.rick.hospitalguia.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.rick.hospitalguia.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{
	public Usuario findByEmailAndSenha(String email, String senha);
}
