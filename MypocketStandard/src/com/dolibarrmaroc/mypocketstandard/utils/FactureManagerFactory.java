package com.dolibarrmaroc.mypocketstandard.utils;

import com.dolibarrmaroc.mypocketstandard.business.DefaultFactureManager;
import com.dolibarrmaroc.mypocketstandard.business.FactureManager;
import com.dolibarrmaroc.mypocketstandard.dao.FactureDao;
import com.dolibarrmaroc.mypocketstandard.dao.FactureDaoMysql;



public class FactureManagerFactory {

	private static FactureManager factureManager;
	

	
	static{
		FactureDao dao = new FactureDaoMysql();
		factureManager = new DefaultFactureManager(dao);
	}

	public static FactureManager getFactureManager() {
		return factureManager;
	}
}

