package br.com.sp.rick.hospitalguia.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.rick.hospitalguia.model.Hospital;
import br.com.sp.rick.hospitalguia.repository.HospitalRepository;

@RestController
@RequestMapping("/api/hospital")
public class HospitalRestController {
	@Autowired
	private HospitalRepository repository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Hospital> getHospital() {
		return repository.findAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Hospital> getHospital(@PathVariable("id") Long idHosp) {
		// tenta buscar o hospital no repository
		Optional<Hospital> optional = repository.findById(idHosp);
		// se hospital existir
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/tipo/{id}", method = RequestMethod.GET)
	public Iterable<Hospital> getHospitalTipo(@PathVariable("id") Long idTipo){
		return repository.findByTipoId(idTipo);
	}
}
