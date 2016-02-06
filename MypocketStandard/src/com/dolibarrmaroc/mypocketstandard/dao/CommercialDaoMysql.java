package com.dolibarrmaroc.mypocketstandard.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.URL;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;

public class CommercialDaoMysql implements CommercialDao{

	private String urlData = URL.URL+"prospection.php";
	private JSONParser parser ;
	private String url = URL.URL+"allclient.php";
	
	

	public CommercialDaoMysql() {
		// TODO Auto-generated constructor stub
		parser = new JSONParser();
	}

	@Override
	public String insert(Compte c,Prospection p) {
		Log.e("Appel INSERTION", p.toString());
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("create","create"));
		
		if (p.getParticulier() == 1) {
			nameValuePairs.add(new BasicNameValuePair("nom",p.getName()));//p.getFirstname()
		}else{
			nameValuePairs.add(new BasicNameValuePair("nom",p.getName()));
		}
		
        nameValuePairs.add(new BasicNameValuePair("firstname",p.getLastname()));
        nameValuePairs.add(new BasicNameValuePair("particulier",p.getParticulier()+""));
        nameValuePairs.add(new BasicNameValuePair("client",p.getClient()+""));
        nameValuePairs.add(new BasicNameValuePair("address",p.getAddress()));
        nameValuePairs.add(new BasicNameValuePair("zip",p.getZip()));
        nameValuePairs.add(new BasicNameValuePair("town",p.getTown()));
        nameValuePairs.add(new BasicNameValuePair("phone",p.getPhone()));
        nameValuePairs.add(new BasicNameValuePair("fax",p.getFax()));
        nameValuePairs.add(new BasicNameValuePair("email",p.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("capital",p.getCapital()));
        nameValuePairs.add(new BasicNameValuePair("idprof1",p.getIdprof1()));
        nameValuePairs.add(new BasicNameValuePair("idprof2",p.getIdprof2()));
        nameValuePairs.add(new BasicNameValuePair("idprof3",p.getIdprof3()));
        nameValuePairs.add(new BasicNameValuePair("idprof4",p.getIdprof4()));
        nameValuePairs.add(new BasicNameValuePair("typent_id",p.getTypent_id()));
        nameValuePairs.add(new BasicNameValuePair("effectif_id",p.getEffectif_id()));
        nameValuePairs.add(new BasicNameValuePair("assujtva_value",p.getTva_assuj()+""));
        nameValuePairs.add(new BasicNameValuePair("status",p.getStatus()+""));
		nameValuePairs.add(new BasicNameValuePair("commercial_id",c.getIduser()));
        nameValuePairs.add(new BasicNameValuePair("country_id",p.getCountry_id()+""));
        nameValuePairs.add(new BasicNameValuePair("forme_juridique_code",p.getForme_juridique_code()));
		
        nameValuePairs.add(new BasicNameValuePair("latitude",p.getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("longitude",p.getLangitude()+""));
		
        Log.e("Send Message clt simple", nameValuePairs.toString() +"");
		String retour = "-1";
		
		try {
			String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
			String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
			
			Log.e("Insertion Message clt simple", json);
			
			JSONObject obj = new JSONObject(stfomat);
			if(obj.has("message")){
				JSONArray arr = obj.getJSONArray("message");
				int k =arr.length() - 1;
				//retour = arr.getString(k);
			}
			//$message['client']
			retour = obj.getString("client");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retour ="-1";
			Log.e("CommercialDaoMysql json insert prospect  insert",e.getMessage() +" << ");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "insert", nameValuePairs.toString(), e.toString());
		}
		
		Log.e("Retour >> 00",retour);
		return retour;
	}

	@Override
	public ProspectData getInfos(Compte c) {
		
		ProspectData data = new ProspectData();
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("json","json"));
		
		String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
		Log.e("Before parse GetInfo ", json+"");
		
		/*{
			"town":[
			        {"ville":"CASABLANCA"},{"ville":"RABAT"}],
			"formJuridique":[
			                 {"code":"2121","nom":"Soci\u00e9t\u00e9 A R\u00e9sponsabilit\u00e9 Limit\u00e9e"}]
	    }*/
		
		//Log.e("Sample affichage RepondreMoi all client getinfo json", json);
		try {
			String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
			Log.e("RepondreMoi cc", stfomat);
			JSONObject obj = new JSONObject(stfomat);
			JSONArray jarrayTown = obj.getJSONArray("town");
			JSONArray jarrayForm = obj.getJSONArray("formJuridique");
			JSONArray jarrayType = obj.getJSONArray("typent");
			JSONArray jarrayreqfields = obj.getJSONArray("requiredfields");
			
			List<String> list = new ArrayList<>();
			
			List<String> juridique = new ArrayList<>();
			List<String> typent = new ArrayList<>();
			List<String> reqfield = new ArrayList<>();
			
			HashMap<String, String> juridique_code= new HashMap<>();
			HashMap<String,String> typent_code= new HashMap<>();
			HashMap<String,String> typent_id= new HashMap<>();
		
			
			for (int i = 0; i < jarrayTown.length(); i++) {
				if(!"".equals(jarrayTown.getJSONObject(i).getString("ville")) || !"null".equals(jarrayTown.getJSONObject(i).getString("ville") )){
					list.add(jarrayTown.getJSONObject(i).getString("ville"));
				}
			}
			
			for (int i = 0; i < jarrayForm.length(); i++) {
				juridique.add(jarrayForm.getJSONObject(i).getString("nom"));
				juridique_code.put(jarrayForm.getJSONObject(i).getString("nom"), jarrayForm.getJSONObject(i).getString("code"));
			}
			
			for (int i = 0; i < jarrayType.length(); i++) {
				typent.add(jarrayType.getJSONObject(i).getString("labelle"));
				typent_code.put(jarrayType.getJSONObject(i).getString("labelle"), jarrayType.getJSONObject(i).getString("code"));
				typent_id.put(jarrayType.getJSONObject(i).getString("labelle"), jarrayType.getJSONObject(i).getString("id"));
			}
			
			for (int i = 0; i < jarrayreqfields.length(); i++) {
				reqfield.add(jarrayreqfields.getJSONObject(i).getString("libelle"));
			}
			
			data.setJuridique(juridique);
			data.setVilles(list);
			data.setTypent(typent);
			data.setJuridique_code(juridique_code);
			data.setTypent_code(typent_code);
			data.setTypent_id(typent_id);
			data.setLsrequired(reqfield);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("CommercialDaoMysql erpoor getInfos",e.getMessage()+"");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "getInfos", nameValuePairs.toString(), e.toString());
			e.printStackTrace();
			
		}
		
		
		return data;
	}

	@Override
	public String update(Compte c,Prospection p) {
		Log.e("Appel INSERTION", p.toString());
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("create","update"));
		
		if (p.getParticulier() == 1) {
			nameValuePairs.add(new BasicNameValuePair("nom",p.getFirstname()));
		}else{
			nameValuePairs.add(new BasicNameValuePair("nom",p.getName()));
		}
		
        nameValuePairs.add(new BasicNameValuePair("firstname",p.getLastname()));
        nameValuePairs.add(new BasicNameValuePair("rowid",p.getId()+""));
        nameValuePairs.add(new BasicNameValuePair("particulier",p.getParticulier()+""));
        nameValuePairs.add(new BasicNameValuePair("client",p.getClient()+""));
        nameValuePairs.add(new BasicNameValuePair("address",p.getAddress()));
        if(p.getZip() != null)
        nameValuePairs.add(new BasicNameValuePair("zip",p.getZip()));
        
        nameValuePairs.add(new BasicNameValuePair("town",p.getTown()));
        nameValuePairs.add(new BasicNameValuePair("phone",p.getPhone()));
        nameValuePairs.add(new BasicNameValuePair("fax",p.getFax()));
        nameValuePairs.add(new BasicNameValuePair("email",p.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("capital",p.getCapital()));
        nameValuePairs.add(new BasicNameValuePair("idprof1",p.getIdprof1()));
        nameValuePairs.add(new BasicNameValuePair("idprof2",p.getIdprof2()));
        nameValuePairs.add(new BasicNameValuePair("idprof3",p.getIdprof3()));
        nameValuePairs.add(new BasicNameValuePair("idprof4",p.getIdprof4()));
        nameValuePairs.add(new BasicNameValuePair("typent_id",p.getTypent_id()));
        nameValuePairs.add(new BasicNameValuePair("effectif_id",p.getEffectif_id()));
        nameValuePairs.add(new BasicNameValuePair("assujtva_value",p.getTva_assuj()+""));
        nameValuePairs.add(new BasicNameValuePair("status",p.getStatus()+""));
		nameValuePairs.add(new BasicNameValuePair("commercial_id",c.getIduser()));
        nameValuePairs.add(new BasicNameValuePair("country_id",p.getCountry_id()+""));
        nameValuePairs.add(new BasicNameValuePair("forme_juridique_code",p.getForme_juridique_code()));
		
        nameValuePairs.add(new BasicNameValuePair("latitude",p.getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("longitude",p.getLangitude()+""));
        
        
        if(p.getLieux() != null && p.getImage() != null && !"".equals(p.getImage()) && !"".equals(p.getLieux())){
        	  nameValuePairs.add(new BasicNameValuePair("lieux",p.getLieux()));
              
              Log.e("Insertion params", nameValuePairs.toString());
      		nameValuePairs.add(new BasicNameValuePair("images",p.getImage()));
        }
      
        
       
		
		String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
		
		Log.e("Insertion Message", json);
		String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
		String retour = "";
		
		try {
			JSONObject obj = new JSONObject(stfomat);
			JSONArray arr = obj.getJSONArray("message");
			int k =arr.length() - 1;
			retour = arr.getString(k);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("CommercialDaoMysql error update",e.getMessage() +" ");
			retour = "Error Survenue lors d'insertion, réssayer plus tard !!";
			MyDebug.WriteLog(this.getClass().getSimpleName(), "update", nameValuePairs.toString(), e.toString());
		}
		
		Log.e("Retour ",retour);
		return retour;
	}

	@Override
	public List<Societe> getAll(Compte c) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		List<Societe> list = new ArrayList<>();
		
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		
		String json = parser.makeHttpRequest(url, "POST", nameValuePairs);
		
		Log.e("RepondreMoi soc", json);
	
		
		try {
			JSONArray jarray = new JSONArray(json);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject obj = jarray.getJSONObject(i);
				Societe s = new Societe(obj.getInt("rowid"), 
										obj.getString("name"), 
										obj.getString("address"), 
										obj.getString("town"), 
										obj.getString("phone"), 
										obj.getString("fax"), 
										obj.getString("email"), 
										obj.getInt("type"), 
										obj.getInt("company"), 
										obj.getDouble("latitude"), 
										obj.getDouble("longitude"));
				s.setLogo(obj.getString("logo"));
				
				if(obj.getString("imgin").equals("ok") && !"".equals(obj.getString("logo"))){
					
					String imageURL = UrlImage.urlimgclients+obj.getString("logo");
					//Log.e(">>> img",imageURL+"");
					Bitmap bitmap = null;
					try {
						// Download Image from URL
						InputStream input = new java.net.URL(imageURL).openStream();
						// Decode Bitmap
						bitmap = BitmapFactory.decodeStream(input);
						
						 File dir = new File(UrlImage.pathimg+"/client_img");
						 if(!dir.exists())  dir.mkdirs();
						 
						     File file = new File(dir, "/"+obj.getString("logo"));
						     FileOutputStream fOut = new FileOutputStream(file);
						     
						     //Log.e(">>hotos ",produit.getPhoto());
						     
						     if(obj.getString("logo").split("\\.")[1].equals("jpeg") || obj.getString("logo").split("\\.")[1].equals("jpg") || obj.getString("logo").split("\\.")[1].equals("jpe")){
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
				list.add(s);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list = new ArrayList<>();
			Log.e("CommercialDaoMysql get all societe getAll ",e.getMessage()+" >> ");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "getAll", nameValuePairs.toString(), e.toString());
		}
		return list;
	}

	@Override
	public String insertWithImage(Compte c, Prospection p,String ba1, String lieux) {
		//Log.e("Appel INSERTION", p.toString());

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("create","create"));

		if (p.getParticulier() == 1) {
			nameValuePairs.add(new BasicNameValuePair("nom",p.getFirstname()));
		}else{
			nameValuePairs.add(new BasicNameValuePair("nom",p.getName()));
		}

		nameValuePairs.add(new BasicNameValuePair("firstname",p.getLastname()));
		nameValuePairs.add(new BasicNameValuePair("particulier",p.getParticulier()+""));
		nameValuePairs.add(new BasicNameValuePair("client",p.getClient()+""));
		nameValuePairs.add(new BasicNameValuePair("address",p.getAddress()));
		nameValuePairs.add(new BasicNameValuePair("zip",p.getZip()));
		nameValuePairs.add(new BasicNameValuePair("town",p.getTown()));
		nameValuePairs.add(new BasicNameValuePair("phone",p.getPhone()));
		nameValuePairs.add(new BasicNameValuePair("fax",p.getFax()));
		nameValuePairs.add(new BasicNameValuePair("email",p.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("capital",p.getCapital()));
		nameValuePairs.add(new BasicNameValuePair("idprof1",p.getIdprof1()));
		nameValuePairs.add(new BasicNameValuePair("idprof2",p.getIdprof2()));
		nameValuePairs.add(new BasicNameValuePair("idprof3",p.getIdprof3()));
		nameValuePairs.add(new BasicNameValuePair("idprof4",p.getIdprof4()));
		nameValuePairs.add(new BasicNameValuePair("typent_id",p.getTypent_id()));
		nameValuePairs.add(new BasicNameValuePair("effectif_id",p.getEffectif_id()));
		nameValuePairs.add(new BasicNameValuePair("assujtva_value",p.getTva_assuj()+""));
		nameValuePairs.add(new BasicNameValuePair("status",p.getStatus()+""));
		nameValuePairs.add(new BasicNameValuePair("commercial_id",c.getIduser()));
		nameValuePairs.add(new BasicNameValuePair("country_id",p.getCountry_id()+""));
		nameValuePairs.add(new BasicNameValuePair("forme_juridique_code",p.getForme_juridique_code()));

		nameValuePairs.add(new BasicNameValuePair("latitude",p.getLatitude()+""));
		nameValuePairs.add(new BasicNameValuePair("longitude",p.getLangitude()+""));

		nameValuePairs.add(new BasicNameValuePair("lieux",lieux));
		nameValuePairs.add(new BasicNameValuePair("images",ba1));
		Log.e("pssssss >>",nameValuePairs.toString());
		String retour = "-1";

		try {
			String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
			String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);

			Log.e("Insertion Message clt pic", json);

			JSONObject obj = new JSONObject(stfomat);
			if(obj.has("message")){
				JSONArray arr = obj.getJSONArray("message");
				int k =arr.length() - 1;
				//retour = arr.getString(k);
			}
			//$message['client']
			retour = obj.getString("client");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retour ="-1";
			Log.e("CommercialDaoMysql json insert prospect insertWithImage ",e.getMessage() +" << ");
			MyDebug.WriteLog(this.getClass().getSimpleName(), "insertWithImage", nameValuePairs.toString(), e.toString());
		}

		Log.e("Retour >> 00",retour);
		return retour;
	}

	
}
