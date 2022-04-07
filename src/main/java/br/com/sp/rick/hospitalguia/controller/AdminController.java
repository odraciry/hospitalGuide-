package br.com.sp.rick.hospitalguia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.Bindable;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sp.rick.hospitalguia.model.Administrador;
import br.com.sp.rick.hospitalguia.repository.AdminRepository;
import br.com.sp.rick.hospitalguia.util.HashUtil;
import br.senai.sp.hellospringboot.model.Cliente;

@Controller
public class AdminController {
	// variavel para persistencia dos dados
	@Autowired
	private AdminRepository repository;

	@RequestMapping("/")
	public void handleRequest() {
		throw new RuntimeException("test exception");
	}

	@RequestMapping("formAdm")
	public String adm() {
		return "formAdm";
	}

	// request mapping para salvar o adm, do tipo POST
	@RequestMapping(value = "salvarAdmin", method = RequestMethod.POST)
	public String salvarAdmin(@Valid Administrador admin, BindingResult result, RedirectAttributes attr) {
		// verifica se houveram erros na validação
		if (result.hasErrors()) {
			// adiciona uma mensagem de erro
			attr.addFlashAttribute("mensagemErro", "verifique os campos...");
			// redireciona para o formulario
			return "redirect:formAdm";
		}
		//variavel para descobrir alteracao ou inseção
		boolean alteracao = admin.getId() != null ? true : false;
		
		//verificar se a senha esta vazia
		if(admin.getSenha().equals(HashUtil.hash(""))) {
			if(!alteracao) {
			// retirar a parte antes do @ no  e-mail
			String parte = admin.getEmail().substring(0, admin.getEmail().indexOf("@"));
			//setar a parte na senha do admin
			admin.setSenha(parte);
			}else {
				// buscar a senha atual no DB
				String hash = repository.findById(admin.getId()).get().getSenha();
				//setar o hash na senha
				admin.setSenhaComHash(hash);
			}
		}
		
		try {
			// salva no banco de dados a entidade(admin)
			repository.save(admin);
			// adiciona uma mensagem de sucesso
			if(!alteracao) {
				attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso. ID:" + admin.getId());
			}else {
				attr.addFlashAttribute("mensagemSucesso", "Administrador alterado com sucesso. ID:" + admin.getId());
			}
		} catch (DataIntegrityViolationException e) {
			//capitura o  erro de dublicidade de email e exibe a mensagem
			System.out.println(e.getMostSpecificCause());
			attr.addFlashAttribute("mensagemErro", "E-mail existente");

		} catch (Exception e) {
			//capitura um erro generico e exibe a mensagem
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao salvar " + e.getMessage());
		}

		return "redirect:formAdm";

	}

	// request mapping para listar adms
	@RequestMapping("listaAdm/{page}")
	public String listaAdmin(Model model, @PathVariable("page") int page) {// @PathVariable("page") capitura o valor q
																			// esta sendo passado na request
		// cria um pageable informando os parametros da pagina
		PageRequest pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		// cria um page de adm atraves dos parametros passados ao repository
		Page<Administrador> pagina = repository.findAll(pageable);
		// adiciona à model, alista com os adms
		model.addAttribute("admins", pagina.getContent());
		// variavel para o total de paginas
		int totalPages = pagina.getTotalPages();
		// cria um list de inteiros para armazenar os numeros das paginas
		List<Integer> numPaginas = new ArrayList<Integer>();
		// preencher o list com as paginas
		for (int i = 1; i <= totalPages; i++) {
			// adicioba a pagina ao list
			numPaginas.add(i);
		}
		// adiciona os valores na model
		model.addAttribute("numPaginas", numPaginas);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pagAtual", page);
		// retorna para o hmtl da lista
		return "listaAdm";
	}
	
	@RequestMapping("alterarAdm")
	public String alterar(Long id,Model model) {
		Administrador adm = repository.findById(id).get();
		model.addAttribute("adm", adm);
		return "forward:formAdm";
	}
	
	@RequestMapping("excluirAdm")
	public String excluir(Long id){
		repository.deleteById(id);
		return "redirect:listaAdm/1";
	}
}
