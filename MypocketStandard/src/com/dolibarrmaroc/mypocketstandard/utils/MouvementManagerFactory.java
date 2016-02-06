package com.dolibarrmaroc.mypocketstandard.utils;

import javax.net.ssl.ManagerFactoryParameters;

import com.dolibarrmaroc.mypocketstandard.business.DefaultMouvementManager;
import com.dolibarrmaroc.mypocketstandard.business.MouvementManager;
import com.dolibarrmaroc.mypocketstandard.dao.MouvementDao;
import com.dolibarrmaroc.mypocketstandard.dao.MouvementDaoMysql;

public class MouvementManagerFactory {

	private static MouvementManager manager;
	
	static{
		MouvementDao dao = new MouvementDaoMysql();
		manager = new DefaultMouvementManager(dao);
	}

	public static MouvementManager getManager() {
		return manager;
	}

	public static void setManager(MouvementManager manager) {
		MouvementManagerFactory.manager = manager;
	}
}
