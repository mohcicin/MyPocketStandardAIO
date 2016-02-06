package com.dolibarrmaroc.mypocketstandard.business;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.dao.TechnicienDao;
import com.dolibarrmaroc.mypocketstandard.models.BordereauGps;
import com.dolibarrmaroc.mypocketstandard.models.BordreauIntervention;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.ImageTechnicien;
import com.dolibarrmaroc.mypocketstandard.models.Services;


public class DefaultTechnicienManager implements TechnicienManager {

	private TechnicienDao dao;
	
	public DefaultTechnicienManager() {
	}

	
	public DefaultTechnicienManager(TechnicienDao dao) {
		super();
		this.dao = dao;
	}

	public TechnicienDao getDao() {
		return dao;
	}


	public void setDao(TechnicienDao dao) {
		this.dao = dao;
	}


	@Override
	public String insertBordereau(BordreauIntervention bi, Compte c,GpsTracker gps,String imei,String num,String battery) {
		return dao.insertBordereau(bi, c, gps, imei, num, battery);
	}


	@Override
	public List<BordereauGps> selectAllBordereau(Compte c) {
		return dao.selectAllBordereau(c);
	}


	@Override
	public List<Services> allServices(Compte c) {
		return dao.allServices(c);
	}


	@Override
	public void inesrtImage(List<ImageTechnicien> imgs,String lien) {
		dao.inesrtImage(imgs,lien);
	}


	@Override
	public List<Client> selectAllClient(Compte c) {
		// TODO Auto-generated method stub
		return dao.selectAllClient(c);
	}


	@Override
	public String insertBordereauoff(BordreauIntervention bi, Compte c) {
		// TODO Auto-generated method stub
		return dao.insertBordereauoff(bi, c);
	}



}
