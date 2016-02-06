package com.dolibarrmaroc.mypocketstandard.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.dao.FactureDao;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.ConfigGps;
import com.dolibarrmaroc.mypocketstandard.models.FactureGps;
import com.dolibarrmaroc.mypocketstandard.models.FileData;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;
import com.dolibarrmaroc.mypocketstandard.models.Remises;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;



public class DefaultFactureManager implements FactureManager {

	private FactureDao dao;
	
	
	public DefaultFactureManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FactureDao getDao() {
		return dao;
	}

	public void setDao(FactureDao dao) {
		this.dao = dao;
	}

	public DefaultFactureManager(FactureDao dao) {
		super();
		this.dao = dao;
	}

	@Override
	public FileData insert(List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement , String amount ,String numChek ,int typeImpriment,Map<String, Remises> allremises,int type_invoice) {
		return dao.insert(prd, idclt, nmb, commentaire, compte, reglement , amount , numChek , typeImpriment,allremises,type_invoice);
	}

	@Override
	public List<FactureGps> listFacture(Compte c) {
		return dao.listFacture(c);
	}

	@Override
	public FileData insertoff(Prospection pros,List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement, String amount,
			String numChek, int typeImpriment, Map<String, Remises> allremises,
			HashMap<Integer, Reglement> hstmp) {
		// TODO Auto-generated method stub
		return dao.insertoff(pros,prd, idclt, nmb, commentaire, compte, reglement, amount, numChek, typeImpriment, allremises, hstmp);
	}

	@Override
	public String insertoffline(Prospection ps, List<Produit> prd,
			String idclt, int nmb, String commentaire, Compte compte,
			String reglement, String amount, String numChek, int typeImpriment,
			Map<String, Remises> allremises, HashMap<Integer, Reglement> reg) {
		// TODO Auto-generated method stub
		return dao.insertoffline(ps, prd, idclt, nmb, commentaire, compte, reglement, amount, numChek, typeImpriment, allremises, reg);
	}
	
	@Override
	public String insertcicin(List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement , String amount , String numChek ,int typeImpriment,Map<String, Remises> allremises,HashMap<Integer, Reglement> lsreg,String rs,int type_invoice){
		return dao.insertcicin(prd, idclt, nmb, commentaire, compte, reglement, amount, numChek, typeImpriment, allremises, lsreg,rs,type_invoice);
	}
	
}
