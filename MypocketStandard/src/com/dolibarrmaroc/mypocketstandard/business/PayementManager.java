package com.dolibarrmaroc.mypocketstandard.business;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Payement;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;

public interface PayementManager {
	public List<Payement> getFactures(Compte c);
	public String insertPayement(Reglement reg,Compte c);
	public List<Payement> getLastFactures(Compte c,String in);
}
