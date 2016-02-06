package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.List;
import java.util.Map;

import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.FactureGps;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Remises;

public interface CommandeDao {

	public List<Commandeview> charger_commandes(Compte c);
	public List<Commandeview> charger_commandesLast(Compte c,String in);
	public String insertCommande(List<Produit> prd, String idclt,  Compte compte, Map<String, Remises> allremises);
	public String CmdToFacture(Commandeview cv,Compte cp);
	public String GetNumCommande();
	public List<FactureGps> charger_commandes_gps(Compte c,String imei);
	public String updateCommande(List<Produit> prd,String cmd,Compte compte);
	public String CancelCmd(Commandeview cv, Compte c);
}
