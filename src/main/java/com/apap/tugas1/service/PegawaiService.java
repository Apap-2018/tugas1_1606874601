package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.PegawaiModel;


public interface PegawaiService {
	void add(PegawaiModel pegawai);
	void delete(PegawaiModel pegawai);
	void update(String nip, PegawaiModel pegawai);
	Optional<PegawaiModel> getPegawaiDetailById(Long id);
	PegawaiModel getPegawaiByNip(String nip);
	List<PegawaiModel> findByTahunMasukAndInstansi(String tahunMasuk, InstansiModel instansi);
	List<PegawaiModel> findByInstansi(InstansiModel instansi);
	List<PegawaiModel> getFilter(String idInstansi, String idJabatan);
	
}