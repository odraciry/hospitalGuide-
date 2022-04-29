package br.com.sp.rick.hospitalguia.rest;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.rick.hospitalguia.annotation.Publico;
import br.com.sp.rick.hospitalguia.model.Erro;
import br.com.sp.rick.hospitalguia.model.Hospital;
import br.com.sp.rick.hospitalguia.model.Usuario;
import br.com.sp.rick.hospitalguia.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {
	@Autowired
	private UsuarioRepository repository;

	@Publico
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario) {
		try {
			// insere o usuario no banco de dados
			repository.save(usuario);
			// retorna um codigo HTTP 201, informa como acessar o recurso inserido
			// e acrescenta no corpo da resposta o bjt inserido
			return ResponseEntity.created(URI.create("/api/usuario/" + usuario.getId())).body(usuario);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Registtro Duplicado", e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> getUsuario(@PathVariable("id") Long idUser) {
		// tenta buscar o usuario no repository
		Optional<Usuario> optional = repository.findById(idUser);
		// se usuario existir
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> atualizarUsuario(@RequestBody Usuario usuario, @PathVariable("id") Long id){
		//validação do ID
		if(id != usuario.getId()) {
			throw new RuntimeException("ID inválido");
		}
		repository.save(usuario);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirUsuario(@PathVariable("id") Long idUsuario){
		repository.deleteById(idUsuario);
		return ResponseEntity.noContent().build();
	}
}
