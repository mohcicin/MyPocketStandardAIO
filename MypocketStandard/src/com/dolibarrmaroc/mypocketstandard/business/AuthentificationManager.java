package com.dolibarrmaroc.mypocketstandard.business;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.ConfigGps;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketWitouhtProduct;
import com.dolibarrmaroc.mypocketstandard.models.Services;

public interface AuthentificationManager {

	public Compte login(String login,String password);
	public ConfigGps getGpsConfig();
	public Services getService(String login, String password,int srv);
	public MyTicketWitouhtProduct lodSociete(String st);
}
