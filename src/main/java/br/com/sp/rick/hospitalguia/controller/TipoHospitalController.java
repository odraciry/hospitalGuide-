package br.com.sp.rick.hospitalguia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.sp.rick.hospitalguia.model.TipoHospital;
import br.com.sp.rick.hospitalguia.repository.TipoHospitalRepository;

@Controller
public class TipoHospitalController {
	@Autowired
	private TipoHospitalRepository repository;

	@RequestMapping("formTipoHospital")
	public String form() {
		return "formTipoHospital";
	}

	@RequestMapping(value = "salvarHospital", method = RequestMethod.POST)
	public String salvarAdmin(@Valid TipoHospital hosp, BindingResult result, RedirectAttributes attr) {
		// verifica se houveram erros na validação
		if (result.hasErrors()) {
			// adiciona uma mensagem de erro
			attr.addFlashAttribute("mensagemErro", "verifique os campos...");
			// redireciona para o formulario
			return "redirect:formTipoHospital";
		}
		// variavel para descobrir alteracao ou inseção
		boolean alteracao = hosp.getId() != null ? true : false;

		try {
			// salva no banco de dados a entidade(hosp)
			repository.save(hosp);
			// adiciona uma mensagem de sucesso
			if (!alteracao) {
				attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso. ID:" + hosp.getId());
			} else {
				attr.addFlashAttribute("mensagemSucesso", "Administrador alterado com sucesso. ID:" + hosp.getId());
			}
		} catch (Exception e) {
			// capitura um erro generico e exibe a mensagem
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao salvar " + e.getMessage());
		}
		return "redirect:formTipoHospital";
	}

	@RequestMapping("listaTipoHosp/{page}")
	public String listaAdmin(Model model, @PathVariable("page") int page) {// @PathVariable("page") capitura o valor q
																			// esta sendo passado na request
		// cria um pageable informando os parametros da pagina
		PageRequest pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		// cria um page de adm atraves dos parametros passados ao repository
		Page<TipoHospital> pagina = repository.findAll(pageable);
		// adiciona à model, alista com os adms
		model.addAttribute("hosp", pagina.getContent());
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
		return "listaTipoHospital";
	}

	@RequestMapping("alterarTipoHosp")
	public String alterar(Long id, Model model) {
		TipoHospital hosp = repository.findById(id).get();
		model.addAttribute("hosp", hosp);
		return "forward:formHospital";
	}

	@RequestMapping("excluirTipoHosp")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listaTipoHosp/1";
	}
	
	
	@RequestMapping("buscarPor")
	public String buscaPor(String buscarPor,String algo, Model model) {
		if(buscarPor.equals("nome")) {
			model.addAttribute("hosp", repository.buscarNome(algo));
		}else if(buscarPor.equals("descri")) {
			model.addAttribute("hosp", repository.buscarDescri(algo));
		}else if(buscarPor.equals("palChave")){
			model.addAttribute("hosp", repository.buscarPal(algo));
		}else if(buscarPor.equals("")){
			model.addAttribute("hosp", repository.buscarPorTudo(algo));
		}
		return "listaTipoHospital";
	}
}
