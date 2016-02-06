package com.dolibarrmaroc.mypocketstandard.utils;

import com.dolibarrmaroc.mypocketstandard.business.AuthentificationManager;
import com.dolibarrmaroc.mypocketstandard.business.DefaultAuthentificationManager;
import com.dolibarrmaroc.mypocketstandard.business.DefaultTechnicienManager;
import com.dolibarrmaroc.mypocketstandard.business.TechnicienManager;
import com.dolibarrmaroc.mypocketstandard.dao.ConnexionDao;
import com.dolibarrmaroc.mypocketstandard.dao.ConnexionDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dao.TechnicienDao;
import com.dolibarrmaroc.mypocketstandard.dao.TechnicienDaoMysql;




public class ConnexionManagerFactory {

	private  static AuthentificationManager auth;
	private  static ConnexionDao connect;

	
	static{
		connect = new ConnexionDaoMysql();
		auth = new DefaultAuthentificationManager(connect);
	}

	public static AuthentificationManager getCConnexionManager() {
		return auth;
	}
}
