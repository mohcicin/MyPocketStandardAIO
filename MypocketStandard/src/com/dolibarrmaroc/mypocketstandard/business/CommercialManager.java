package com.dolibarrmaroc.mypocketstandard.business;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Societe;

public interface CommercialManager {
	public String insert(Compte c,Prospection prospect);
	public ProspectData getInfos(Compte c);
	public List<Societe> getAll(Compte c);
	public String update(Compte c,Prospection p); 
	public String insertWithImage(Compte compte, Prospection client,
			String ba1, String lieux);
}
