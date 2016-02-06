package com.dolibarrmaroc.mypocketstandard.utils;

import com.dolibarrmaroc.mypocketstandard.business.DefaultVendeurManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.dao.VendeurDao;
import com.dolibarrmaroc.mypocketstandard.dao.VendeurDaoMysql;


public class VendeurManagerFactory {

	private static VendeurManager vendeurManager;
	

	
	static{
		VendeurDao dao = new VendeurDaoMysql();
		vendeurManager = new DefaultVendeurManager(dao);
	}

	public static VendeurManager getClientManager() {
		return vendeurManager;
	}
}
