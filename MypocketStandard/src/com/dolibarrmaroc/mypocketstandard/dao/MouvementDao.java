package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.LoadStock;
import com.dolibarrmaroc.mypocketstandard.models.Mouvement;
import com.dolibarrmaroc.mypocketstandard.models.Produit;

public interface MouvementDao {

	public LoadStock laodStock(Compte cp);
	public String makemouvement(List<Mouvement> mvs,Compte cp,String label);
	public String makeechange(List<Mouvement> mvs,Compte cp,String label,String clt,int tpmv);
}
