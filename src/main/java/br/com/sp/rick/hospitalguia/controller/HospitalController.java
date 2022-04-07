package br.com.sp.rick.hospitalguia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sp.rick.hospitalguia.repository.TipoHospitalRepository;

@Controller
public class HospitalController {
	@Autowired
	private TipoHospitalRepository repTipo;
	@RequestMapping("formHospital")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "formHospital";
	}
}
