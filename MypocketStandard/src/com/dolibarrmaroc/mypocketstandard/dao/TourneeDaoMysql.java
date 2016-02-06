package com.dolibarrmaroc.mypocketstandard.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Motifs;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.URL;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;

public class TourneeDaoMysql implements TourneeDao{

	private static final String urlData = URL.URL+"gettour.php";
	private static final String urlmot = URL.URL+"createmotifs.php";
	private JSONParser parser ;
	
	private HashMap<Integer, List<Integer>> listPromoByClient;
	private List<Societe> lssociete;
	
	public TourneeDaoMysql(){
		super();
		parser = new JSONParser();
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
			String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
			
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
			Log.e("TourneeDaoMysql load tour error consulterMesTournee ",e.getMessage()+ "  <<");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "consulterMesTournee", nameValuePairs.toString(), e.toString());
		}

		return tour;
	}
	@Override
	public HashMap<Integer, List<Integer>> getPromotionClients() {
		// TODO Auto-generated method stub
		return listPromoByClient;
	}
	@Override
	public List<Societe> selectSociete() {
		// TODO Auto-generated method stub
		return lssociete;
	}
	@Override
	public String sendMotifs(Motifs mt, Compte c, String tp) {
		// TODO Auto-generated method stub
		String msg = "ko";
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		nameValuePairs.add(new BasicNameValuePair("user",c.getIduser()+""));//c.getLogin()
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair(tp,tp+""));
		nameValuePairs.add(new BasicNameValuePair("tour",mt.getTour().getRowid()+""));
		nameValuePairs.add(new BasicNameValuePair("soc",mt.getClt().getId()+""));
		
		try {
			nameValuePairs.add(new BasicNameValuePair("dt",sdf.format(mt.getDt())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		nameValuePairs.add(new BasicNameValuePair("note",mt.getComment()));
		nameValuePairs.add(new BasicNameValuePair("motf",mt.getMt()));
		nameValuePairs.add(new BasicNameValuePair("data", c.getIduser()+"#"+c.getPassword()+"#"+tp+"#"+mt.getTour().getRowid()+"#"+mt.getClt().getId()+"#"+sdf.format(mt.getDt())+"#"+mt.getComment()+"#"+mt.getMt()+""));
		
		

			Log.e("send durl mes motifs ",nameValuePairs.toString());
			 
			
		try {
			String json = parser.makeHttpRequest(urlmot, "POST", nameValuePairs);
			
			Log.e("motifs json ", json);
			
			//[{"rowid":"1","label":"new task","dates":"2015-09-02","datee":"2015-09-19","color":"#0080ff","secteur":"CA","idsecteur":"18","clients":[],"grp":"vendeur","idgrp":"7","usr":"","recur":["1","3","6"]}]
			String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
			
			JSONObject ob = new JSONObject(stfomat);
			
			msg = ob.getString("msg");
			
		}catch(Exception e){
			msg = "ko";
			Log.e("TourneeDaoMysql sendmoif",e.getMessage()+"");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "sendMotifs", nameValuePairs.toString(), e.toString());
			return msg;
		}
		
		return msg;
	}

}
