package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void add(JabatanModel jabatan);
	void delete(JabatanModel jabatan);
	void update(Long id_jabatan, JabatanModel jabatan);
	JabatanModel getJabatanDetailById(Long id);
	List<JabatanModel> findAll();

}