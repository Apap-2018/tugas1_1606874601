package com.apap.tugas1.controller;

import java.util.List;

import com.apap.tugas1.model.*;
import com.apap.tugas1.service.JabatanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String find(@RequestParam("idJabatan") Long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id);
		if (jabatan!=null ) {
			model.addAttribute("jabatan", jabatan);
			return "view-jabatan";
		}
		return "not-found";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "tambah-jabatan";
	}

	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.add(jabatan);
		model.addAttribute("nama", jabatan.getNama());
		return "tambah-jabatan-success";
	}
	
	@RequestMapping(value = "/jabatan/ubah/{id_jabatan}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "id_jabatan") String id_jabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(id_jabatan));
		model.addAttribute("jabatan", jabatan);
		model.addAttribute("newJabatan", new JabatanModel());
		return "update-jabatan";
	}

	@RequestMapping(value = "/jabatan/ubah/{id_jabatan}", method = RequestMethod.POST)
	private String update(@ModelAttribute JabatanModel newJabatan, 
			@PathVariable(value = "id_jabatan") String id_jabatan, Model model) {
		jabatanService.update(Long.parseLong(id_jabatan), newJabatan);
		model.addAttribute("id_jabatan", id_jabatan);
		model.addAttribute("nama", newJabatan.getNama());
		return "update-jabatan-success";
	}
	
	@RequestMapping(value = "/jabatan/hapus/{id_jabatan}", method = RequestMethod.GET)
    private String delete(@PathVariable(value = "id_jabatan") String id_jabatan, Model model) {
        JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(id_jabatan));
        List<PegawaiModel> listPegawai = jabatan.getPegawaiList();
        if(listPegawai.isEmpty()) {
        	jabatanService.delete(jabatan);
        	model.addAttribute("nama", jabatan.getNama());
        	return "delete";
        }
        model.addAttribute("nama", jabatan.getNama());
        return "delete-fail";
    }
	
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
    private String view(Model model) {
		List<JabatanModel> listJabatan = jabatanService.findAll();
        model.addAttribute("listJabatan", listJabatan);
        return "view-all-jabatan";
    }
}
