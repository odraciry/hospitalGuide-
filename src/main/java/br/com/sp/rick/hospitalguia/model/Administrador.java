package br.com.sp.rick.hospitalguia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.com.sp.rick.hospitalguia.util.HashUtil;
import lombok.Data;

//cria get e set 
@Data
//mapeaia a entidade para o jpa
@Entity
public class Administrador {
	//chave primaria e auto-incremento
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	//define o email como indice unico
	@Column(unique = true)
	@Email
	private String email;
	@NotEmpty
	private String senha;
	
	public void setSenha(String senha) {
		this.senha = HashUtil.hash(senha);
	}
	
	public void setSenhaComHash(String hash) {
		this.senha = hash;
	}
}
