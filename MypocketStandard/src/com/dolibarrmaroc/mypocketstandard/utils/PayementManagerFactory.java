package com.dolibarrmaroc.mypocketstandard.utils;

import com.dolibarrmaroc.mypocketstandard.business.DefaultFactureManager;
import com.dolibarrmaroc.mypocketstandard.business.DefaultPayementManager;
import com.dolibarrmaroc.mypocketstandard.business.FactureManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.dao.FactureDao;
import com.dolibarrmaroc.mypocketstandard.dao.FactureDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dao.PayementDao;
import com.dolibarrmaroc.mypocketstandard.dao.PayementDaoMysql;



public class PayementManagerFactory {

	private static PayementManager payementFactory;
	

	
	static{
		PayementDao dao = new PayementDaoMysql();
		payementFactory = new DefaultPayementManager(dao);
	}



	public static PayementManager getPayementFactory() {
		return payementFactory;
	}
	
}

