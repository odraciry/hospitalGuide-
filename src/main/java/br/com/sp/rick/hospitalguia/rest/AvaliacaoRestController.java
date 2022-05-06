package br.com.sp.rick.hospitalguia.rest;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.rick.hospitalguia.annotation.Privado;
import br.com.sp.rick.hospitalguia.annotation.Publico;
import br.com.sp.rick.hospitalguia.model.Avaliacao;
import br.com.sp.rick.hospitalguia.repository.AvaliacaoRepository;

@RestController
@RequestMapping("/api/avaliacao")
public class AvaliacaoRestController {
	@Autowired
	private AvaliacaoRepository repository;
	
	@Privado
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody Avaliacao avaliacao){
		repository.save(avaliacao);
		return ResponseEntity.created(URI.create("/api/avalicao"+avaliacao.getId())).body(avaliacao);
	}
	
	@Publico
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Avaliacao getById(@PathVariable("id") Long idAvaliacao) {
		return repository.findById(idAvaliacao).get();
	}
	
	@Publico
	@RequestMapping(value="/hospital/{id}", method = RequestMethod.GET)
	public List<Avaliacao> getByHospital(@PathVariable("id") Long idHospital) {
		return repository.findByHospitalId(idHospital);
	}
	
}
