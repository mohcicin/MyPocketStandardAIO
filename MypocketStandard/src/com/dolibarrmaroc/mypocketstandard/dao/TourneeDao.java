package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Motifs;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;

public interface TourneeDao {

	public List<Tournee> consulterMesTournee(Compte cp, String dt);
	
	public HashMap<Integer, List<Integer>> getPromotionClients();
	
	public List<Societe> selectSociete();
	
	public String sendMotifs(Motifs mt,Compte cp,String tp);
}
