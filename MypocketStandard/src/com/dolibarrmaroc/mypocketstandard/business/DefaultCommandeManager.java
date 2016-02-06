package com.dolibarrmaroc.mypocketstandard.business;

import java.util.List;
import java.util.Map;

import com.dolibarrmaroc.mypocketstandard.dao.CommandeDao;
import com.dolibarrmaroc.mypocketstandard.dao.CommandeDaoMysql;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.FactureGps;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Remises;

public class DefaultCommandeManager implements CommandeManager {

	private CommandeDao dao;
	
	public DefaultCommandeManager(CommandeDao dx){
		this.dao = dx;
	}
	@Override
	public List<Commandeview> charger_commandes(Compte c) {
		// TODO Auto-generated method stub
		return dao.charger_commandes(c);
	}
	@Override
	public String insertCommande(List<Produit> prd, String idclt,
			Compte compte, Map<String, Remises> allremises) {
		// TODO Auto-generated method stub
		return dao.insertCommande(prd, idclt, compte, allremises);
	}
	@Override
	public String CmdToFacture(Commandeview cv, Compte cp) {
		// TODO Auto-generated method stub
		return dao.CmdToFacture(cv,cp);
	}
	@Override
	public String GetNumCommande() {
		// TODO Auto-generated method stub
		return dao.GetNumCommande();
	}
	@Override
	public List<FactureGps> charger_commandes_gps(Compte c, String imei) {
		// TODO Auto-generated method stub
		return dao.charger_commandes_gps(c, imei);
	}
	@Override
	public String updateCommande(List<Produit> prds, String cmd, Compte compte) {
		// TODO Auto-generated method stub
		return dao.updateCommande(prds, cmd, compte);
	}
	@Override
	public List<Commandeview> charger_commandesLast(Compte c, String in) {
		// TODO Auto-generated method stub
		return dao.charger_commandesLast(c, in);
	}
	@Override
	public String CancelCmd(Commandeview cv, Compte c) {
		// TODO Auto-generated method stub
		return dao.CancelCmd(cv, c);
	}

}
