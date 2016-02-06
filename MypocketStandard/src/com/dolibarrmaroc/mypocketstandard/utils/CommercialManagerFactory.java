package com.dolibarrmaroc.mypocketstandard.utils;

import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.DefaultCommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.DefaultVendeurManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.dao.CommercialDao;
import com.dolibarrmaroc.mypocketstandard.dao.CommercialDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dao.VendeurDao;
import com.dolibarrmaroc.mypocketstandard.dao.VendeurDaoMysql;


public class CommercialManagerFactory {

	private static CommercialManager commercial;
	

	
	static{
		CommercialDao dao = new CommercialDaoMysql();
		commercial = new DefaultCommercialManager(dao);
	}

	public static CommercialManager getCommercialManager() {
		return commercial;
	}
}
