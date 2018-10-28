package com.apap.tugas1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.InstansiDb;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService{
	
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Autowired
	private InstansiDb instansiDb;

	@Override
	public void add(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public void delete(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public Optional<PegawaiModel> getPegawaiDetailById(Long id) {
		return pegawaiDb.findById(id);
	}

	@Override
	public PegawaiModel getPegawaiByNip(String nip) {
		return pegawaiDb.findByNip(nip);
		
	}

	@Override
	public List<PegawaiModel> findByTahunMasukAndInstansi(String tahunMasuk, InstansiModel instansi ){
		return pegawaiDb.findByTahunMasukAndInstansi(tahunMasuk, instansi);
	}

	@Override
	public void update(String nip, PegawaiModel newPegawai) {
		PegawaiModel oldPegawai = this.getPegawaiByNip(nip);
		oldPegawai.setInstansi(newPegawai.getInstansi());
		oldPegawai.setNama(newPegawai.getNama());
		oldPegawai.setNip(newPegawai.getNip());
		oldPegawai.setTahunMasuk(newPegawai.getTahunMasuk());
		oldPegawai.setTanggalLahir(newPegawai.getTanggalLahir());
		oldPegawai.setTempatLahir(newPegawai.getTempatLahir());
		oldPegawai.setJabatanList(newPegawai.getJabatanList());
	}


	@Override
	public List<PegawaiModel> findByInstansi(InstansiModel instansi) {
		List<PegawaiModel> listPegawai = pegawaiDb.findAllByInstansi(instansi);
		return listPegawai;
	}
	

	@Override
	public List<PegawaiModel> getFilter(String idInstansi, String idJabatan) {
		List<PegawaiModel> list = new ArrayList<PegawaiModel>();
		InstansiModel instansi = instansiDb.findInstansiById(Long.parseLong(idInstansi));
		List<PegawaiModel> listPegawai = pegawaiDb.findAllByInstansi(instansi);
		for(PegawaiModel pegawai : listPegawai) {
			for(JabatanModel jabatanA : pegawai.getJabatanList()) {
				if(jabatanA.getId() == Long.parseLong(idJabatan)) {
					list.add(pegawai);
				}
			}
		}
		return list;
	}
}