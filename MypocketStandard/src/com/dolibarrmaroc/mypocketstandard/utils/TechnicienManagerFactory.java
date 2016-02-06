package com.dolibarrmaroc.mypocketstandard.utils;

import com.dolibarrmaroc.mypocketstandard.business.DefaultTechnicienManager;
import com.dolibarrmaroc.mypocketstandard.business.TechnicienManager;
import com.dolibarrmaroc.mypocketstandard.dao.TechnicienDao;
import com.dolibarrmaroc.mypocketstandard.dao.TechnicienDaoMysql;




public class TechnicienManagerFactory {

	private static TechnicienManager technicienManager;
	

	
	static{
		TechnicienDao dao = new TechnicienDaoMysql();
		technicienManager = new DefaultTechnicienManager(dao);
	}

	public static TechnicienManager getClientManager() {
		return technicienManager;
	}
}
