package br.com.sp.rick.hospitalguia.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String cep;
	private String endereco;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String telefone;
	private String horario;
	private String site;
	@ManyToOne
	private TipoHospital tipo;
	private String especialidade;
	@Column(columnDefinition = "TEXT")
	private String fotos;
	@OneToMany(mappedBy = "hospital")
	private List<Avaliacao> avaliacoes;
	
	// retorna as fotos na forma vetor de string
	public String[] verFotos() {
		return fotos.split(";");
	}
}
