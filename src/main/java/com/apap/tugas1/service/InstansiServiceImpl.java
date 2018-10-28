package com.apap.tugas1.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.InstansiDb;

@Service
@Transactional
public class InstansiServiceImpl implements InstansiService{
	
	@Autowired
	private InstansiDb instansiDb;

	@Override
	public void add(InstansiModel instansi) {
		instansiDb.save(instansi);
		
	}

	@Override
	public void delete(InstansiModel instansi) {
		instansiDb.delete(instansi);
		
	}

	@Override
	public void update(InstansiModel instansi) {
		instansiDb.save(instansi);
		
	}

	@Override
	public InstansiModel getInstansiByNamaAndProvinsi(String nama, ProvinsiModel provinsi) {
		return instansiDb.findByNamaAndProvinsi(nama, provinsi);
	}
	
	@Override
	public List<InstansiModel> findAll() {
		return instansiDb.findAll();
	}

	@Override
	public InstansiModel findById(Long id) {
		return instansiDb.findInstansiById(id);
	}
}
