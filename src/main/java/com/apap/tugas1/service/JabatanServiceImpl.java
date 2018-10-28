package com.apap.tugas1.service;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

@Service
@Transactional
public class JabatanServiceImpl implements JabatanService {

	@Autowired
	private JabatanDb jabatanDb;
	
	@Override
	public void add(JabatanModel jabatan) {
		jabatanDb.save(jabatan);
		
	}

	@Override
	public void delete(JabatanModel jabatan) {
		jabatanDb.delete(jabatan);
		
	}

	@Override
	public JabatanModel getJabatanDetailById(Long id) {
		return jabatanDb.findJabatanById(id);
	}

	@Override
	public List<JabatanModel> findAll() {
		return jabatanDb.findAll();
	}

	@Override
	public void update(Long id_jabatan, JabatanModel jabatan) {
		JabatanModel oldJabatan = this.getJabatanDetailById(id_jabatan);
		oldJabatan.setDeskripsi(jabatan.getDeskripsi());
		oldJabatan.setNama(jabatan.getNama());
		oldJabatan.setGajiPokok(jabatan.getGajiPokok());
	}

}