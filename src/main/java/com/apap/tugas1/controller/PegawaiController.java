package com.apap.tugas1.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.apap.tugas1.model.*;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;

	@Autowired
	private ProvinsiService provinsiService;

	@Autowired
	private JabatanService jabatanService;

	@Autowired
	private InstansiService instansiService;

	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> listJabatan = jabatanService.findAll();
		List<InstansiModel> listInstansi = instansiService.findAll();
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("listInstansi", listInstansi);
		return "home";
	}

	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String find(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		if (pegawai != null) {
			model.addAttribute("pegawai", pegawai);
			return "view-pegawai";
		}
		return "not-found";
	}

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		List<InstansiModel> listInstansi = instansiService.findAll();
		List<JabatanModel> listJabatan = jabatanService.findAll();
		List<ProvinsiModel> listProvinsi = provinsiService.findAll();
		List<Long> jabatanIdList = new ArrayList<Long>();

		model.addAttribute("pegawai", new PegawaiModel());
		model.addAttribute("listInstansi", listInstansi);
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("listProvinsi", listProvinsi);
		model.addAttribute("jabatanIdList", jabatanIdList);
		
		return "tambah-pegawai";
	}

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addSubmit(@RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi,
			@RequestParam("jabatan") List<String> jabatanIdList, @ModelAttribute PegawaiModel pegawai, BindingResult bindingResult, Model model) {
		
		// instansi
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		pegawai.setInstansi(instansi);
		
		// jabatan
		List<JabatanModel> jabatanList = new ArrayList<JabatanModel>();
		for(String id : jabatanIdList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(id));
			jabatanList.add(jabatan);
		}
		pegawai.setJabatanList(jabatanList);
	
		// generate NIP
		Long kodeInstansi = pegawai.getInstansi().getId();
		String tahunMasuk = pegawai.getTahunMasuk();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		String tanggalLahir = dateFormat.format(pegawai.getTanggalLahir());
		String nip = kodeInstansi + "" + tanggalLahir + "" + tahunMasuk;
		
		List<PegawaiModel> pegawaiSama = pegawaiService.findByTahunMasukAndInstansi(tahunMasuk, instansi);
		int count = 1;

		if (pegawaiSama.isEmpty()) nip = nip + "01";
		else {
			String tanggalLahirPegawai = dateFormat.format(pegawai.getTanggalLahir());
			for (PegawaiModel pegawaiLain : pegawaiSama) {
				String tanggalLahirPegawaiLain = dateFormat.format(pegawaiLain.getTanggalLahir());
				if (tanggalLahirPegawai.equals(tanggalLahirPegawaiLain)) count++;
			}
			if (count < 10) nip = nip + "0" + count;
			else nip = nip + count;
		}

		pegawai.setNip(nip);
		pegawaiService.add(pegawai);
		model.addAttribute("nip", nip);
		return "tambah-pegawai-success";
	}

	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("newPegawai", new PegawaiModel());

		List<ProvinsiModel> listProvinsi = provinsiService.findAll();
		List<JabatanModel> listJabatan = jabatanService.findAll();
		List<InstansiModel> listInstansi = instansiService.findAll();

		model.addAttribute("listProvinsi", listProvinsi);
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("listInstansi", listInstansi);

		return "update-pegawai";
	}

	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.POST)
	private String update(@ModelAttribute PegawaiModel newPegawai, @RequestParam("instansi") String idInstansi,
			@RequestParam("provinsi") String idProvinsi, @RequestParam("jabatan") List<String> jabatanIdList,
			@PathVariable(value = "nip") String nip, BindingResult bindingResult, Model model) {
		
		// instansi
		InstansiModel newInstansi = instansiService.findById(Long.parseLong(idInstansi));
		newPegawai.setInstansi(newInstansi);
		
//		List<JabatanModel> newJabatanList = new ArrayList<JabatanModel>();
//		for (String str : newIdJabatanList) {
//			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
//			newJabatanList.add(jabatan);
//		}

		// jabatan
				List<JabatanModel> jabatanList = new ArrayList<JabatanModel>();
				for(String id : jabatanIdList) {
					JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(id));
					jabatanList.add(jabatan);
				}
				newPegawai.setJabatanList(jabatanList);
		

		// Generate NIP
		Long kodeInstansi = newPegawai.getInstansi().getId();
		String tahunMasuk = newPegawai.getTahunMasuk();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		String tanggalLahir = dateFormat.format(newPegawai.getTanggalLahir());

		String nipBaru = kodeInstansi + "" + tanggalLahir + "" + tahunMasuk;

		InstansiModel instansi = instansiService.findById(kodeInstansi);
		List<PegawaiModel> pegawaiSama = pegawaiService.findByTahunMasukAndInstansi(tahunMasuk, instansi);
		int count = 1;

		if (pegawaiSama.isEmpty()) nipBaru = nipBaru + "01";
		else {
			String tanggalLahirPegawai = dateFormat.format(newPegawai.getTanggalLahir());

			for (PegawaiModel pegawaiLain : pegawaiSama) {
				String tanggalLahirPegawaiLain = dateFormat.format(pegawaiLain.getTanggalLahir());
				if (tanggalLahirPegawai.equals(tanggalLahirPegawaiLain)) count++;
			}
			if (count < 10) nipBaru = nipBaru + "0" + count;
			else nipBaru = nipBaru + count;
		}

		newPegawai.setNip(nipBaru);
		pegawaiService.update(nip, newPegawai);
		model.addAttribute("nip", nipBaru);
		return "update-pegawai-success";
	}


	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	private String find(Model model) {
		List<ProvinsiModel> listProvinsi = provinsiService.findAll();
		List<JabatanModel> listJabatan = jabatanService.findAll();
		List<InstansiModel> listInstansi = instansiService.findAll();

		model.addAttribute("listProvinsi", listProvinsi);
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("listInstansi", listInstansi);
		return "cari-pegawai";
	}

	@RequestMapping(value = "/pegawai/cari", params = { "cari" }, method = RequestMethod.POST)
	private String findGo(@ModelAttribute PegawaiModel newPegawai, @RequestParam("instansi") String idInstansi,
			@RequestParam("provinsi") String idProvinsi, @RequestParam("jabatan") String idJabatan, Model model) {
		
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(idJabatan));
		List<PegawaiModel> listPegawai = pegawaiService.getFilter(idInstansi, idJabatan);
		List<InstansiModel> listInstansi = instansiService.findAll();
		List<JabatanModel> listJabatan = jabatanService.findAll();
		List<ProvinsiModel> listProvinsi = provinsiService.findAll();
		
		model.addAttribute("namaInstansi", instansi.getNama());
		model.addAttribute("namaJabatan", jabatan.getNama());
		model.addAttribute("listPegawai", listPegawai);
		model.addAttribute("listInstansi", listInstansi);
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("listProvinsi", listProvinsi);

		return "cari-pegawai";
	}

	@RequestMapping(value = "/pegawai/termuda-tertua", method = RequestMethod.GET)
	private String tuamuda(@RequestParam("idInstansi") Long idInstansi, @ModelAttribute PegawaiModel newPegawai,
			Model model) {

		InstansiModel instansi = instansiService.findById(idInstansi);
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		listPegawai.sort(new Comparator<PegawaiModel>() { // urut dari tertua
			@Override
			public int compare(PegawaiModel pegawai1, PegawaiModel pegawai2) {
				return pegawai2.getTanggalLahir().compareTo(pegawai1.getTanggalLahir());
			}
		});

		PegawaiModel pegawaiTua = listPegawai.get(0);
		String instansiTua = pegawaiTua.getInstansi().getNama();
		String provinsiTua = pegawaiTua.getInstansi().getProvinsi().getNama();
		List<JabatanModel> listJabatanTua = pegawaiTua.getJabatanList();

		PegawaiModel pegawaiMuda = listPegawai.get(listPegawai.size() - 1);
		String instansiMuda = pegawaiMuda.getInstansi().getNama();
		String provinsiMuda = pegawaiMuda.getInstansi().getProvinsi().getNama();
		List<JabatanModel> listJabatanMuda = pegawaiMuda.getJabatanList();

		model.addAttribute("pegawaiTua", pegawaiTua);
		model.addAttribute("instansiTua", instansiTua);
		model.addAttribute("provinsiTua", provinsiTua);
		model.addAttribute("listJabatanTua", listJabatanTua);
		model.addAttribute("pegawaiMuda", pegawaiMuda);
		model.addAttribute("instansiMuda", instansiMuda);
		model.addAttribute("provinsiMuda", provinsiMuda);
		model.addAttribute("listJabatanMuda", listJabatanMuda);

		return "pegawai-tua-muda";
	}

}
