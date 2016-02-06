package com.dolibarrmaroc.mypocketstandard.business;

import com.dolibarrmaroc.mypocketstandard.dao.ConnexionDao;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.ConfigGps;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketWitouhtProduct;
import com.dolibarrmaroc.mypocketstandard.models.Services;

public class DefaultAuthentificationManager implements AuthentificationManager {

	private ConnexionDao dao;
	
	public DefaultAuthentificationManager() {
	}
	
	public DefaultAuthentificationManager(ConnexionDao dao) {
		super();
		this.dao = dao;
	}

	public ConnexionDao getDao() {
		return dao;
	}

	public void setDao(ConnexionDao dao) {
		this.dao = dao;
	}

	@Override
	public Compte login(String login, String password) {
		return dao.login(login, password);
	}

	@Override
	public ConfigGps getGpsConfig() {
		return dao.getGpsConfig();
	}

	@Override
	public Services getService(String login, String password,int srv) {
		return dao.getService(login, password,srv);
	}

	@Override
	public MyTicketWitouhtProduct lodSociete(String st) {
		// TODO Auto-generated method stub
		return dao.lodSociete(st);
	}


}
