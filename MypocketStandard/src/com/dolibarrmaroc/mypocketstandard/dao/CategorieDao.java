package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.List;
import java.util.Map;

import com.dolibarrmaroc.mypocketstandard.models.Categorie;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Remises;

public interface CategorieDao {

	public List<Categorie> LoadCategories(Compte cp);
	
}
