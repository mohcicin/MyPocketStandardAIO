package com.dolibarrmaroc.mypocketstandard.business;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.dao.CommercialDao;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Societe;

public class DefaultCommercialManager implements CommercialManager {

	private CommercialDao dao;

	public DefaultCommercialManager(CommercialDao dao) {
		super();
		this.dao = dao;
	}
	
	@Override
	public String insert(Compte c,Prospection prospect) {
		// TODO Auto-generated method stub
		return dao.insert(c,prospect);
	}
	


	@Override
	public ProspectData getInfos(Compte c) {
		// TODO Auto-generated method stub
		return dao.getInfos(c);
	}

	@Override
	public List<Societe> getAll(Compte c) {
		// TODO Auto-generated method stub
		return dao.getAll(c);
	}

	@Override
	public String update(Compte c, Prospection p) {
		// TODO Auto-generated method stub
		return dao.update(c, p);
	}

	@Override
	public String insertWithImage(Compte compte, Prospection client,
			String ba1, String lieux) {
		// TODO Auto-generated method stub
		return dao.insertWithImage(compte, client, ba1, lieux);
	}

}
