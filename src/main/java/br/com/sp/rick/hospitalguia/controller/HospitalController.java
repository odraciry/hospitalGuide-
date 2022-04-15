package br.com.sp.rick.hospitalguia.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sp.rick.hospitalguia.model.Administrador;
import br.com.sp.rick.hospitalguia.model.Hospital;
import br.com.sp.rick.hospitalguia.repository.HospitalRepository;
import br.com.sp.rick.hospitalguia.repository.TipoHospitalRepository;
import br.com.sp.rick.hospitalguia.util.FirebaseUtil;

@Controller
public class HospitalController {
	@Autowired
	private TipoHospitalRepository repTipo;
	@Autowired
	private HospitalRepository repository;
	@Autowired
	private FirebaseUtil fireUtil;

	@RequestMapping("formHospital")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "formHospital";
	}

	@RequestMapping("salvarHosp")
	public String salvar(Hospital hospital, RedirectAttributes attr,
			@RequestParam("fileFotos") MultipartFile[] fileFotos) {
		// string pra armazenar as URls
		String fotos = hospital.getFotos();
		// percorre cada arquivo no vetor
		for (MultipartFile arquivo : fileFotos) {
			// verifica se o arquivo existe
			if (arquivo.getOriginalFilename().isEmpty()) {
				// vai para o proximo arquivo
				continue;
			}
			try {
				// faz o upload e guarda a URL na string fotos
				fotos += fireUtil.upload(arquivo) + ";";
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		// guarda na variavel hospital as fotos
		hospital.setFotos(fotos);
	
		boolean alteracao = hospital.getId() != null ? true : false;
		try {
			// salva no banco de dados a entidade(admin)
			repository.save(hospital);
			// adiciona uma mensagem de sucesso
			if(!alteracao) {
				attr.addFlashAttribute("mensagemSucesso", "Hospital cadastrado com sucesso. ID:" + hospital.getId());
			}else {
				attr.addFlashAttribute("mensagemSucesso", "Hospital alterado com sucesso. ID:" + hospital.getId());
			}
		} catch (Exception e) {
			//capitura um erro generico e exibe a mensagem
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao salvar " + e.getMessage());
		}

		
		return "redirect:formHospital";
	}

	// request mapping para listar hospitais
	@RequestMapping("listaHosp/{page}")
	public String listaHosp(Model model, @PathVariable("page") int page) {// @PathVariable("page") capitura o valor q
																			// esta sendo passado na request
		// cria um pageable informando os parametros da pagina
		PageRequest pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		// cria um page de hospital atraves dos parametros passados ao repository
		Page<Hospital> pagina = repository.findAll(pageable);
		// adiciona à model, a lista com os hospitais
		model.addAttribute("hospital", pagina.getContent());
		
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
		return "listaHospital";
	}
	
	@RequestMapping("alterarHosp")
	public String alterar(Long id,Model model) {
		Hospital hosp = repository.findById(id).get();
		model.addAttribute("hosp", hosp);
		return "forward:formHospital";
	}
	
	@RequestMapping("excluirHosp")
	public String excluir(Long id){
		Hospital hosp = repository.findById(id).get();
		if(hosp.getFotos().length() > 0) {
			for(String foto : hosp.verFotos()) {
				fireUtil.deletar(foto);
			}
			
		}
		repository.delete(hosp);
		return "redirect:listaHosp/1";
	}
	
	@RequestMapping("excluirFotos")
	public String excluirFotos(Long idHosp, int numFoto, Model model) {
		//busca o hospital 
		Hospital hosp = repository.findById(idHosp).get();
		//busca a URL da fot
		String urlFoto = hosp.verFotos()[numFoto];
		//deleta a foto
		fireUtil.deletar(urlFoto);
		//remover a url do atributo fotos
		hosp.setFotos(hosp.getFotos().replace(urlFoto+";", ""));
		//salva no banco
		repository.save(hosp);
		//coloca o hosp na model
		model.addAttribute("hosp", hosp);
		return "forward:formHospital";
	}
}
