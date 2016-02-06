package com.dolibarrmaroc.mypocketstandard.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.ConfigGps;
import com.dolibarrmaroc.mypocketstandard.models.FactureGps;
import com.dolibarrmaroc.mypocketstandard.models.FileData;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;
import com.dolibarrmaroc.mypocketstandard.models.Remises;


public interface FactureManager {
	
	public FileData insert(List<Produit> prd,String idclt,int nmb,String commentaire,Compte compte,String reglement,String amount,String numChek ,int typeImpriment, Map<String, Remises> allremises,int type_invoice);
	public FileData insertoff(Prospection pros,List<Produit> prd,String idclt,int nmb,String commentaire,Compte compte,String reglement , String amount,String numChek ,int typeImpriment, Map<String, Remises> allremises,HashMap<Integer, Reglement> hstmp);
	public List<FactureGps> listFacture(Compte c);
	
	public String insertoffline(Prospection ps,List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement, String amount,
			String numChek, int typeImpriment, Map<String, Remises> allremises,
			HashMap<Integer, Reglement> reg);
	
	public String insertcicin(List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement , String amount , String numChek ,int typeImpriment,Map<String, Remises> allremises,HashMap<Integer, Reglement> lsreg,String rs,int type_invoice);
}
