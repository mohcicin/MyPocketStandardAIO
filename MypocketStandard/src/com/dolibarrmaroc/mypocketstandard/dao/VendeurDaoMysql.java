package com.dolibarrmaroc.mypocketstandard.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.models.CategorieCustomer;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.Facture;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Promotion;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.URL;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;

public class VendeurDaoMysql implements VendeurDao {

	private JSONParser jsonParser;
	private String urlprd = URL.URL+"produit.php";
	private String urlclt = URL.URL+"listclient.php";
	private String urlcatclt = URL.URL+"seecategorieclients.php";
	private static final String urlData = URL.URL+"gettour.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID = "id";

	private String jsonprd,jsonclt;
	private Dictionnaire dicot = new Dictionnaire();

	private List<Societe> lssociete;
	
	/*
	 * Integer => id Produit
	 * Integer => id Promotion
	 */
	private HashMap<Integer, HashMap<Integer, Promotion>> listPromoByProduits;

	/*
	 * Integer => id Client
	 * Integer => id Promot
	 */
	private HashMap<Integer, List<Integer>> listPromoByClient;

	public VendeurDaoMysql() {
		jsonParser = new JSONParser();
		listPromoByProduits = new HashMap<>();
		listPromoByClient = new HashMap<>();
		lssociete = new ArrayList<>();
	}

	@Override
	public int insertFacture(Facture fac) {
		return 0;
	}

	public HashMap<Integer, HashMap<Integer, Promotion>> getListPromoByProduits() {
		return listPromoByProduits;
	}

	public void setListPromoByProduits(
			HashMap<Integer, HashMap<Integer, Promotion>> listPromoByProduits) {
		this.listPromoByProduits = listPromoByProduits;
	}

	public HashMap<Integer, List<Integer>> getListPromoByClient() {
		return listPromoByClient;
	}

	public void setListPromoByClient(
			HashMap<Integer, List<Integer>> listPromoByClient) {
		this.listPromoByClient = listPromoByClient;
	}

	@Override
	public List<Produit> selectAllProduct(Compte c) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));

		String jsonString =  jsonParser.makeHttpRequest(
				urlprd , "POST", nameValuePairs);
		// Parse les donn�es JSON

		List<Produit> list = new ArrayList<Produit>();

		ArrayList<HashMap<String, String>> mapsss = new ArrayList<>();

		listPromoByProduits = new HashMap<>();

		Log.e("Json retourne >> ", jsonString);
		try{

			JSONArray jArray = new JSONArray(jsonString);
			// check your log for json response
			//Log.d("Dictionnaire", jArray.);

			//[{"id":"1","ref":"c00001","desig":"Produit1","stock":"100","pu":"100,00"},
			for(int i=0;i<jArray.length();i++){
				if(i == 0){
					JSONArray dico = jArray.getJSONArray(i);
					for (int j = 0; j < dico.length(); j++) {
						JSONObject jsone = dico.getJSONObject(j);
						HashMap<String, String> dic = new HashMap<>();
						dic.put(jsone.getString("code"), jsone.getString("libelle"));
						mapsss.add(dic);
					}
				}else{
					JSONObject json = jArray.getJSONObject(i);
					Produit produit = new Produit();
					//Log.e(">>> Produit trouver Successful!", json.toString());

					/*
					String pu = json.getString("pu");
					String[] parts = pu.split(".");

					String mni = parts[1].substring(0, 1);

					String part = parts[1]+"."+ mni;
					for (int j = 0; j < parts.length; j++) {
						part += Integer.parseInt(parts[j]);
					}
					 */

					produit.setId(json.getInt("id"));
					produit.setDesig(json.getString("desig"));
					produit.setPrixUnitaire(json.getString("pu"));
					produit.setQteDispo(json.getInt("stock"));
					produit.setRef(json.getString("ref"));
					produit.setPrixttc(json.getDouble("price_ttc"));
					produit.setFk_tva(json.getString("fk_tva"));
					produit.setTva_tx(json.getString("tva_tx"));
					list.add(produit);

					int nombre_promos = json.getInt("nombre_promotion");
					HashMap<Integer, Promotion> map = new HashMap<>();

					if(nombre_promos>0){
						for (int j = 0; j < nombre_promos; j++) {

							Promotion p = new Promotion(json.getInt("id_promos"+j), 
									Integer.parseInt(json.getString("type_promos"+j)), 
									Integer.parseInt(json.getString("promos"+j)), 
									Integer.parseInt(json.getString("qte_promos"+j)));
							map.put(p.getId(), p);
						}
					}
					else{
						Promotion p = new Promotion(0, 
								-1, 
								0, 
								0);
						map.put(p.getId(), p);

					}
					listPromoByProduits.put(json.getInt("id"), map);
				}

			}
		}catch(Exception e){
			Log.e("VendeurDaoMysql log_tag", "Error parsing data selectAllProduct " + e.toString());
			MyDebug.WriteLog(this.getClass().getSimpleName(), "selectAllProduct", nameValuePairs.toString(), e.toString());
		}

		dicot.setDico(mapsss);
		//Log.i("Dictionnaire >> ",dicot.toString());

		return list;
	}

	public Produit selectProduct(String id,Compte c) {
		List<Produit> list = selectAllProduct(c);

		int k = Integer.parseInt(id);

		Produit produit = new Produit();

		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getId() == k){
				produit = list.get(i);
			}else{
				produit = null;
			}
		}
		return produit;
	}

	@Override
	public List<Client> selectAllClient(Compte c) {

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));

		String jsonString =  jsonParser.makeHttpRequest(
				urlclt, "POST", nameValuePairs);
		// Parse les donn�es JSON

		List<Client> list = new ArrayList<Client>();
		
		lssociete = new ArrayList<>();

		Log.e("Json retourne >> ", jsonString);
		try{

			JSONArray jArray = new JSONArray(jsonString);
			// check your log for json response
			//Log.d("Login attempt", jArray.toString());


			for(int i=0;i<jArray.length();i++){
				JSONObject json = jArray.getJSONObject(i);
				Client clt = new Client();

				//"rowid":"3","name":"karouani","client":"1","zip":"54020","town":null,
				//"stcomm":"Jamais contact\u00e9","prefix_comm":null,"code_client":"14589"

				clt.setId(json.getInt("rowid"));
				clt.setName(json.getString("name"));
				clt.setZip(json.getString("zip"));
				clt.setTown(json.getString("town"));
				clt.setLatitude(Double.parseDouble(json.getString("latitude")));
				clt.setLongitude(Double.parseDouble(json.getString("longitude")));
				if(!c.getProfile().toLowerCase().equals("technicien")){
					clt.setEmail(json.getString("email"));
				}


				int nombre_promos = json.getInt("nombre_promotion");
				List<Integer> listP = new ArrayList<>();

				if(nombre_promos>0){
					for (int j = 0; j < nombre_promos; j++) {
						int idp = Integer.parseInt(json.getString("id_promos"+j));
						listP.add(idp);
					}
				}

				listPromoByClient.put(json.getInt("rowid"), listP);
				list.add(clt);
				
				/******************************* Load societe data **********************************/
				Societe s = new Societe(json.getInt("rowid"), 
						json.getString("name"), 
						json.getString("address"), 
						json.getString("town"), 
						json.getString("phone"), 
						json.getString("fax"), 
						json.getString("email"), 
						json.getInt("type"), 
						json.getInt("company"), 
						json.getDouble("latitude"), 
						json.getDouble("longitude"));
						s.setLogo(json.getString("logo"));
						
						if(json.getString("imgin").equals("ok") && !"".equals(json.getString("logo"))){
							
							String imageURL = UrlImage.urlimgclients+json.getString("logo");
							//Log.e(">>> img",imageURL+"");
							Bitmap bitmap = null;
							try {
								// Download Image from URL
								InputStream input = new java.net.URL(imageURL).openStream();
								// Decode Bitmap
								bitmap = BitmapFactory.decodeStream(input);
								
								 File dir = new File(UrlImage.pathimg+"/client_img");
								 if(!dir.exists())  dir.mkdirs();
								 
								     File file = new File(dir, "/"+json.getString("logo"));
								     FileOutputStream fOut = new FileOutputStream(file);
								     
								     //Log.e(">>hotos ",produit.getPhoto());
								     
								     if(json.getString("logo").split("\\.")[1].equals("jpeg") || json.getString("logo").split("\\.")[1].equals("jpg") || json.getString("logo").split("\\.")[1].equals("jpe")){
								    	  bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
								     }else{
								    	  bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
								     }
								   
								     fOut.flush();
								     fOut.close();
							} catch (Exception e) {
								e.printStackTrace();
								Log.e(">> ","pic out clt "+e.getMessage());
							}
							
						}
						lssociete.add(s);

			}
		}catch(Exception e){
			Log.e("VendeurDaoMysql log_tag", "Error parsing data selectAllClient " + e.toString());
			MyDebug.WriteLog(this.getClass().getSimpleName(), "selectAllClient", nameValuePairs.toString(), e.toString());
		}
		return list;
	}

	@Override
	public Dictionnaire getDictionnaire() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("all","okk"));

		String jsonString =  jsonParser.makeHttpRequest(
				urlprd , "POST", nameValuePairs);
		// Parse les donn�es JSON


		ArrayList<HashMap<String, String>> mapsss = new ArrayList<>();


		try{

			JSONArray jArray = new JSONArray(jsonString);
			// check your log for json response
			//Log.d("Dictionnaire", jArray.);

			//[{"id":"1","ref":"c00001","desig":"Produit1","stock":"100","pu":"100,00"},
			for(int i=0;i<jArray.length();i++){
					JSONArray dico = jArray.getJSONArray(i);
					for (int j = 0; j < dico.length(); j++) {
						JSONObject jsone = dico.getJSONObject(j);
						HashMap<String, String> dic = new HashMap<>();
						dic.put(jsone.getString("code"), jsone.getString("libelle"));
						mapsss.add(dic);
					}

			}
		}catch(Exception e){
			Log.e("VendeurDaoMysql log_tag", "Error parsing data getDictionnaire " + e.toString());
			MyDebug.WriteLog(this.getClass().getSimpleName(), "getDictionnaire", nameValuePairs.toString(), e.toString());
		}

		dicot.setDico(mapsss);
		//Log.i("Dictionnaire >> ",dicot.toString());

		return dicot;
	}

	@Override
	public List<Promotion> getPromotions(int idclt, int idprd) {
		List<Integer> list = this.getListPromoByClient().get(idclt);
		List<Promotion> lista = new ArrayList<>();
		HashMap<Integer, Promotion> map = this.getListPromoByProduits().get(idprd);

		for (int i = 0; i < list.size(); i++) {
			if(map.containsKey(list.get(i))){
				lista.add(map.get(list.get(i)));
			}
		}
		if(lista.size() == 0) lista.add(new Promotion(0, -1, 1, 0));

		return lista;
	}

	@Override
	public HashMap<Integer, HashMap<Integer, Promotion>> getPromotionProduits() {
		// TODO Auto-generated method stub
		return this.listPromoByProduits;
	}

	@Override
	public HashMap<Integer, List<Integer>> getPromotionClients() {
		// TODO Auto-generated method stub
		return this.listPromoByClient;
	}

	@Override
	public List<CategorieCustomer> getAllCategorieCustomer(Compte c) {
		// TODO Auto-generated method stub
		List<CategorieCustomer> res = new ArrayList<>();

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));

		try {
			String jsonString =  jsonParser.makeHttpRequest(
					urlcatclt , "POST", nameValuePairs);

			JSONObject obj = new JSONObject(jsonString);

			JSONArray lsfree = obj.getJSONArray("lsfree");
			JSONArray lscat = obj.getJSONArray("lscategories");
			
			
			List<Integer> tmp = new ArrayList<>();
			for (int i = 0; i < lsfree.length(); i++) {
				tmp.add(lsfree.getInt(i));
			}
			
			res.add(new CategorieCustomer(0, "Categorie par defaut",  "Categorie par defaut", tmp));

			for (int i = 0; i < lscat.length(); i++) {
				JSONObject jb = lscat.getJSONObject(i);
				
				tmp = new ArrayList<>();
				for (int j = 0; j < jb.getJSONArray("clts").length(); j++) {
					tmp.add(jb.getJSONArray("clts").getInt(j));
				}
				res.add(new CategorieCustomer(jb.getInt("id"), jb.getString("label"), jb.getString("description"), tmp));
			}


			Log.e("secto  "," "+res.size());
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("VendeurDaoMysql error parse secteur getAllCategorieCustomer ",e.getMessage()+"  ");
			res = new ArrayList<>();
			MyDebug.WriteLog(this.getClass().getSimpleName(), "getAllCategorieCustomer", nameValuePairs.toString(), e.toString());
		}


		return res;
	}

	@Override
	public List<Client> selectAllLastClient(Compte c, String in) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("rowid",in));

		String jsonString =  jsonParser.makeHttpRequest(
				urlclt, "POST", nameValuePairs);
		// Parse les donn�es JSON

		List<Client> list = new ArrayList<Client>();

		Log.e("Json retourne >> ", jsonString);
		lssociete = new ArrayList<>();
		try{

			JSONArray jArray = new JSONArray(jsonString);
			// check your log for json response
			//Log.d("Login attempt", jArray.toString());


			for(int i=0;i<jArray.length();i++){
				JSONObject json = jArray.getJSONObject(i);
				Client clt = new Client();

				//"rowid":"3","name":"karouani","client":"1","zip":"54020","town":null,
				//"stcomm":"Jamais contact\u00e9","prefix_comm":null,"code_client":"14589"

				clt.setId(json.getInt("rowid"));
				clt.setName(json.getString("name"));
				clt.setZip(json.getString("zip"));
				clt.setTown(json.getString("town"));
				clt.setLatitude(Double.parseDouble(json.getString("latitude")));
				clt.setLongitude(Double.parseDouble(json.getString("longitude")));
				if(!c.getProfile().toLowerCase().equals("technicien")){
					clt.setEmail(json.getString("email"));
				}


				int nombre_promos = json.getInt("nombre_promotion");
				List<Integer> listP = new ArrayList<>();

				if(nombre_promos>0){
					for (int j = 0; j < nombre_promos; j++) {
						int idp = Integer.parseInt(json.getString("id_promos"+j));
						listP.add(idp);
					}
				}

				listPromoByClient.put(json.getInt("rowid"), listP);
				list.add(clt);
				
				/******************************* Load societe data **********************************/
				Societe s = new Societe(json.getInt("rowid"), 
						json.getString("name"), 
						json.getString("address"), 
						json.getString("town"), 
						json.getString("phone"), 
						json.getString("fax"), 
						json.getString("email"), 
						json.getInt("type"), 
						json.getInt("company"), 
						json.getDouble("latitude"), 
						json.getDouble("longitude"));
						s.setLogo(json.getString("logo"));
						
						if(json.getString("imgin").equals("ok") && !"".equals(json.getString("logo"))){
							
							String imageURL = UrlImage.urlimgclients+json.getString("logo");
							//Log.e(">>> img",imageURL+"");
							Bitmap bitmap = null;
							try {
								// Download Image from URL
								InputStream input = new java.net.URL(imageURL).openStream();
								// Decode Bitmap
								bitmap = BitmapFactory.decodeStream(input);
								
								 File dir = new File(UrlImage.pathimg+"/client_img");
								 if(!dir.exists())  dir.mkdirs();
								 
								     File file = new File(dir, "/"+json.getString("logo"));
								     FileOutputStream fOut = new FileOutputStream(file);
								     
								     //Log.e(">>hotos ",produit.getPhoto());
								     
								     if(json.getString("logo").split("\\.")[1].equals("jpeg") || json.getString("logo").split("\\.")[1].equals("jpg") || json.getString("logo").split("\\.")[1].equals("jpe")){
								    	  bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
								     }else{
								    	  bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
								     }
								   
								     fOut.flush();
								     fOut.close();
							} catch (Exception e) {
								e.printStackTrace();
								Log.e(">> ","pic out clt "+e.getMessage());
							}
							
						}
						lssociete.add(s);

			}
		}catch(Exception e){
			Log.e("VendeurDaoMysql log_tag", "Error parsing data selectAllLastClient " + e.toString());
			MyDebug.WriteLog(this.getClass().getSimpleName(), "selectAllLastClient", nameValuePairs.toString(), e.toString());
		}
		return list;
	}

	@Override
	public List<Societe> selectSociete() {
		// TODO Auto-generated method stub
		return lssociete;
	}


	@Override
	public List<Tournee> consulterMesTournee(Compte c, String dt) {
		// TODO Auto-generated method stub
		
		List<Tournee> tour = new ArrayList<>();
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("user",c.getIduser()+""));//c.getLogin()
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("action","android"));
		nameValuePairs.add(new BasicNameValuePair("dt",dt));

			Log.e("send durl mes tournee ",nameValuePairs.toString());
			
			listPromoByClient = new HashMap<>();
			lssociete = new ArrayList<>();
			
		try {
			String json = jsonParser.makeHttpRequest(urlData, "POST", nameValuePairs);
			
			Log.e("start", json);
			
			//[{"rowid":"1","label":"new task","dates":"2015-09-02","datee":"2015-09-19","color":"#0080ff","secteur":"CA","idsecteur":"18","clients":[],"grp":"vendeur","idgrp":"7","usr":"","recur":["1","3","6"]}]
			String stfomat = json.substring(json.indexOf("["),json.lastIndexOf("]")+1);


			
			//Log.e("json",stfomat);
			JSONArray jsr = new JSONArray(stfomat);

			for (int i = 0; i < jsr.length(); i++) {
				JSONObject obj = jsr.getJSONObject(i);

				Tournee tr = new Tournee();
				
				tr.setRowid(obj.getLong("rowid"));
				tr.setLabel(obj.getString("label"));
				tr.setDebut(obj.getString("dates"));
				tr.setFin(obj.getString("datee"));
				tr.setColor(obj.getString("color"));
				
				tr.setSecteur(obj.getString("secteur"));
				tr.setIdsecteur(obj.getLong("idsecteur"));
				
				
				
				tr.setGrp(obj.getString("grp"));
				tr.setIdgrp(obj.getLong("idgrp"));
				
				JSONArray cl = obj.getJSONArray("clients");
				List<Client> lsclt = new ArrayList<>();
				for (int j = 0; j < cl.length(); j++) {
					JSONObject o = cl.getJSONObject(j);
					Client ct = new Client(o.getInt("rowid"), o.getString("name"), "", o.getString("town"), o.getString("email"), o.getString("phone"), o.getString("address"));
					
					lsclt.add(ct);
					
					/******************************* Load societe data **********************************/
					Societe s = new Societe(o.getInt("rowid"), 
							o.getString("name"), 
							o.getString("address"), 
							o.getString("town"), 
							o.getString("phone"), 
							o.getString("fax"), 
							o.getString("email"), 
							o.getInt("type"), 
							o.getInt("company"), 
							o.getDouble("latitude"), 
							o.getDouble("longitude"));
							s.setLogo(o.getString("logo"));
							
							if(o.getString("imgin").equals("ok") && !"".equals(o.getString("logo"))){
								
								String imageURL = UrlImage.urlimgclients+o.getString("logo");
								//Log.e(">>> img",imageURL+"");
								Bitmap bitmap = null;
								try {
									// Download Image from URL
									InputStream input = new java.net.URL(imageURL).openStream();
									// Decode Bitmap
									bitmap = BitmapFactory.decodeStream(input);
									
									 File dir = new File(UrlImage.pathimg+"/client_img");
									 if(!dir.exists())  dir.mkdirs();
									 
									     File file = new File(dir, "/"+o.getString("logo"));
									     FileOutputStream fOut = new FileOutputStream(file);
									     
									     //Log.e(">>hotos ",produit.getPhoto());
									     
									     if(o.getString("logo").split("\\.")[1].equals("jpeg") || o.getString("logo").split("\\.")[1].equals("jpg") || o.getString("logo").split("\\.")[1].equals("jpe")){
									    	  bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
									     }else{
									    	  bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
									     }
									   
									     fOut.flush();
									     fOut.close();
								} catch (Exception e) {
									e.printStackTrace();
									Log.e(">> ","pic out clt "+e.getMessage());
								}
								
							}
							lssociete.add(s);
							
				  /*************************************** end ************************************************/
					
					int nombre_promos = o.getInt("nombre_promotion");
					List<Integer> listP = new ArrayList<>();

					if(nombre_promos>0){
						for (int f = 0; f < nombre_promos; f++) {
							int idp = Integer.parseInt(o.getString("id_promos"+f));
							listP.add(idp);
						}
					}

					listPromoByClient.put(o.getInt("rowid"), listP);
				}
				
				tr.setLsclt(lsclt);
				
				JSONArray rc = obj.getJSONArray("recur");
				List<Integer> lsrc = new ArrayList<>();
				for (int j = 0; j < rc.length(); j++) {
					
					lsrc.add(rc.getInt(j));
				}
				
				tr.setRecur(lsrc);
				
				
				
				
				tour.add(tr);
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			tour = new ArrayList<>();
			Log.e("VendeurDaoMysql load tour error consulterMesTournee ",e.getMessage()+ "  <<");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "consulterMesTournee", nameValuePairs.toString(), e.toString());
		}

		return tour;
	}
}
