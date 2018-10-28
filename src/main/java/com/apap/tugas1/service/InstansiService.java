package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;

public interface InstansiService {
	void add(InstansiModel instansi);
	void delete(InstansiModel instansi);
	void update(InstansiModel instansi);
	InstansiModel findById(Long id);
	InstansiModel getInstansiByNamaAndProvinsi(String nama, ProvinsiModel provinsi);
	List<InstansiModel> findAll(); 
}