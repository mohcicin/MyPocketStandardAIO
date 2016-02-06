package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Payement;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;

public interface PayementDao {
	public List<Payement> getFactures(Compte c);
	public List<Payement> getLastFactures(Compte c,String in);
	public String insertPayement(Reglement reg,Compte c);
}
