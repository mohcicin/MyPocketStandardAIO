package com.dolibarrmaroc.mypocketstandard.utils;

import javax.net.ssl.ManagerFactoryParameters;

import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.DefaultCommandeManager;
import com.dolibarrmaroc.mypocketstandard.dao.CommandeDao;
import com.dolibarrmaroc.mypocketstandard.dao.CommandeDaoMysql;

public class CommandeManagerFactory {

	private static CommandeManager manager;
	
	static{
		CommandeDao dao = new CommandeDaoMysql();
		manager = new DefaultCommandeManager(dao);
	}

	public static CommandeManager getManager() {
		return manager;
	}

	
}
