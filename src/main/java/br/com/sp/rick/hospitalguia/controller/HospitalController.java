package br.com.sp.rick.hospitalguia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sp.rick.hospitalguia.model.Hospital;
import br.com.sp.rick.hospitalguia.repository.HospitalRepository;
import br.com.sp.rick.hospitalguia.repository.TipoHospitalRepository;

@Controller
public class HospitalController {
	@Autowired
	private TipoHospitalRepository repTipo;
	@Autowired
	private HospitalRepository repository;
	@RequestMapping("formHospital")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "formHospital";
	}
	
	@RequestMapping("salvarHosp")
	public String salvar(Hospital hospital, RedirectAttributes attr,@RequestParam("fileFotos") MultipartFile[] fileFotos) {
		//repository.save(hospital);
		System.out.println(fileFotos.length);
		
		return "redirect:formHospital";
	}
}
