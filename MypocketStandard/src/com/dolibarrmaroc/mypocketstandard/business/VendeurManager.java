package com.dolibarrmaroc.mypocketstandard.business;

import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.CategorieCustomer;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.Facture;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Promotion;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;

public interface VendeurManager {
	
public int insertFacture(Facture fac);
	
	public List<Produit> selectAllProduct(Compte c);
	public Produit selectProduct(String id,Compte c);
	public Dictionnaire getDictionnaire();
	public List<Client> selectAllClient(Compte c);
	List<Promotion> getPromotions(int idclt,int idprd);
	
	public List<CategorieCustomer> getAllCategorieCustomer(Compte c);
	
	public HashMap<Integer, HashMap<Integer, Promotion>> getPromotionProduits();
	public HashMap<Integer, List<Integer>> getPromotionClients();
	
	public List<Client> selectAllLastClient(Compte c, String in);
	
	public List<Societe> selectSociete();
	
	public List<Tournee> consulterMesTournee(Compte c, String dt);
}
