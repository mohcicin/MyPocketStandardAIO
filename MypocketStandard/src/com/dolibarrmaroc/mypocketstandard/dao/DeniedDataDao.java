package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.Compte;

public interface DeniedDataDao {

	public List<String> sendMyErrorData(List<String> in,Compte cp,String tp);
}
