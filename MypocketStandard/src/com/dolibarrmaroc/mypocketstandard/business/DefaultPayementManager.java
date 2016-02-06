package com.dolibarrmaroc.mypocketstandard.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.dao.PayementDao;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Payement;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.URL;

public class DefaultPayementManager implements PayementManager{
	
	private PayementDao dao;
	
	public DefaultPayementManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public DefaultPayementManager(PayementDao dao) {
		super();
		this.dao = dao;
	}


	public void setDao(PayementDao dao) {
		this.dao = dao;
	}


	@Override
	public List<Payement> getFactures(Compte c) {
		// TODO Auto-generated method stub
		return dao.getFactures(c);
	}

	@Override
	public String insertPayement(Reglement reg, Compte c) {
		// TODO Auto-generated method stub
		return dao.insertPayement(reg, c);
	}


	@Override
	public List<Payement> getLastFactures(Compte c, String in) {
		// TODO Auto-generated method stub
		return dao.getLastFactures(c, in);
	}

	
	
}
