package com.dolibarrmaroc.mypocketstandard.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.models.BordereauGps;
import com.dolibarrmaroc.mypocketstandard.models.BordreauIntervention;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.ImageTechnicien;
import com.dolibarrmaroc.mypocketstandard.models.LabelService;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.models.Services;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.MyLocationListener;
import com.dolibarrmaroc.mypocketstandard.utils.ServiceDao;
import com.dolibarrmaroc.mypocketstandard.utils.URL;


public class TechnicienDaoMysql implements TechnicienDao{

	private JSONParser jsonParser;
	public static final String url = URL.URL+"services.php";
	public static final String urlInsert = URL.URL+"bordereau.php";
	private String urlclt = URL.URL+"listclient.php";
	private static final String URL_BRD = URL.URL+"getBrdDataGps.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID = "id";


	public TechnicienDaoMysql(){
		jsonParser = new JSONParser();
	}

	@Override
	public List<Services> allServices(Compte c) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));

		String jsonString =  jsonParser.makeHttpRequest(
				url , "POST", nameValuePairs);

		List<Services> list = new ArrayList<Services>();

		Log.d("Json retournee >> ", jsonString);
		try{

			JSONArray jArray = new JSONArray(jsonString);
			// check your log for json response
			//Log.d("Login attempt", jArray.toString());


			for(int i=0;i<jArray.length();i++){
				JSONObject json = jArray.getJSONObject(i);
				Services s =new Services();
				Log.d("Produit trouver Successful!", json.toString());

				s.setId(json.getInt(TAG_ID));
				s.setNmb_cmp(json.getInt("nmb"));
				s.setService(json.getString("service"));
				List<LabelService> labels = new ArrayList<>();
				for (int j = 0; j < s.getNmb_cmp(); j++) {
					LabelService lb = new LabelService(json.getString("label"+j));
					labels.add(lb);
				}
				s.setLabels(labels);

				list.add(s);

			}
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return list;
	}



	@Override
	public String insertBordereau(BordreauIntervention bi, Compte c,GpsTracker gps,String imei,String num,String battery) {

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("idclt",bi.getId_clt()));
		nameValuePairs.add(new BasicNameValuePair("duree",bi.getDuree()));
		nameValuePairs.add(new BasicNameValuePair("iduser",c.getIduser()));
		nameValuePairs.add(new BasicNameValuePair("desc",bi.getDescription()));
		nameValuePairs.add(new BasicNameValuePair("fiche",bi.getStatus()));
		nameValuePairs.add(new BasicNameValuePair("objet",bi.getObjet()));
		nameValuePairs.add(new BasicNameValuePair("heur",bi.getHeurD()));
		nameValuePairs.add(new BasicNameValuePair("min",bi.getMinD()));
		nameValuePairs.add(new BasicNameValuePair("month",bi.getMonth()));
		nameValuePairs.add(new BasicNameValuePair("day",bi.getDay()));
		nameValuePairs.add(new BasicNameValuePair("year",bi.getYear()));

		Log.e("Bordereau recupere",bi.toString());

		String jsonString = jsonParser.newmakeHttpRequest(urlInsert, "POST", nameValuePairs);

		try {
			Log.e("Bordereau lien ",jsonString);
			String stfomat = jsonString.substring(jsonString.indexOf("{"),jsonString.lastIndexOf("}")+1);
			JSONObject json = new JSONObject(stfomat);
			ServiceDao daoGps = new ServiceDao();
			int id = json.getInt("feedback");
			if(id != -1)daoGps.insertDataIntrv(gps,imei,num,battery,c,id+"");
			return json.getString("lien");
		} catch (JSONException e) {
			e.printStackTrace();
			MyDebug.WriteLog(this.getClass().getSimpleName(), "insertBordereau", nameValuePairs.toString(), e.toString());
			return "";
		}

	}


	@Override
	public String insertBordereauoff(BordreauIntervention bi, Compte c) {
		String stfomat ="no";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("idclt",bi.getId_clt()));
		nameValuePairs.add(new BasicNameValuePair("duree",bi.getDuree()));
		nameValuePairs.add(new BasicNameValuePair("iduser",c.getIduser()));
		nameValuePairs.add(new BasicNameValuePair("desc",bi.getDescription()));
		nameValuePairs.add(new BasicNameValuePair("fiche",bi.getStatus()));
		nameValuePairs.add(new BasicNameValuePair("objet",bi.getObjet()));
		nameValuePairs.add(new BasicNameValuePair("heur",bi.getHeurD()));
		nameValuePairs.add(new BasicNameValuePair("min",bi.getMinD()));
		nameValuePairs.add(new BasicNameValuePair("month",bi.getMonth()));
		nameValuePairs.add(new BasicNameValuePair("day",bi.getDay()));
		nameValuePairs.add(new BasicNameValuePair("year",bi.getYear()));


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s"); 
		Log.e("sended params",nameValuePairs.toString());

		try {

			String jsonString = jsonParser.newmakeHttpRequest(urlInsert, "POST", nameValuePairs);
			stfomat = jsonString.substring(jsonString.indexOf("{"),jsonString.lastIndexOf("}")+1);

			ServiceDao daoGps = new ServiceDao();

			JSONObject json = new JSONObject(stfomat);
			int id = json.getInt("feedback");

			GpsTracker gps= new GpsTracker();
			gps.setLangitude(bi.getLongitude());
			gps.setLatitude(bi.getLatitude());
			gps.setAltitude(0);
			gps.setDateString(sdf.format(new Date()));
			gps.setDirection(0);
			gps.setSatellite(0+"");
			gps.setSpeed(0);

			if(id != -1)daoGps.insertDataIntrv(gps,bi.getImei(),bi.getNum(),bi.getBattery(),c,id+"");

		} catch (Exception e) {
			e.printStackTrace();
			MyDebug.WriteLog(this.getClass().getSimpleName(), "insertBordereauoff", nameValuePairs.toString(), e.toString());
			return "no";
		}

		Log.e("Bordereau recupere",stfomat);
		return stfomat;
	}


	@Override
	public List<BordereauGps> selectAllBordereau(Compte c) {

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));

		String jsonString =  jsonParser.makeHttpRequest(URL_BRD, "POST", nameValuePairs);

		List<BordereauGps> list = new ArrayList<>();

		Log.e("Facture GPS", jsonString );


		try{
			JSONArray jArray = new JSONArray(jsonString);

			for(int i=0;i<jArray.length();i++){
				JSONObject json = jArray.getJSONObject(i);

				BordereauGps fact = new BordereauGps();

				fact.setId(json.getInt("id"));
				fact.setLat(json.getString("lat"));
				fact.setLng(json.getString("lng"));
				fact.setNumero(json.getString("bordereau"));


				list.add(fact);

			}
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
			MyDebug.WriteLog(this.getClass().getSimpleName(), "selectAllBordereau", nameValuePairs.toString(), e.toString());
		}

		return list;
	}

	@Override
	public void inesrtImage(List<ImageTechnicien> imgs,String lien) {

		Log.d("Lien ",lien);
		String li = URL.URL+"upload_image.php";

		for (int i = 0; i < imgs.size(); i++) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("cmd",imgs.get(i).getName()));
			nameValuePairs.add(new BasicNameValuePair("path",lien));
			nameValuePairs.add(new BasicNameValuePair("image",imgs.get(i).getImageCode()));
			try{
				jsonParser.newmakeHttpRequest(li, "POST", nameValuePairs);
			}catch(Exception e){
				MyDebug.WriteLog(this.getClass().getSimpleName(), "inesrtImage", nameValuePairs.toString(), e.toString());
			}
		}

	}

	@Override
	public List<Client> selectAllClient(Compte c) {

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		//added for new commercial
		nameValuePairs.add(new BasicNameValuePair("techno", "1"));

		String jsonString =  jsonParser.makeHttpRequest(
				urlclt, "POST", nameValuePairs);
		// Parse les donn�es JSON

		List<Client> list = new ArrayList<Client>();

		Log.d("Json retourne >> ", jsonString);
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


				list.add(clt);

			}
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
			MyDebug.WriteLog(this.getClass().getSimpleName(), "selectAllClient", nameValuePairs.toString(), e.toString());
		}
		return list;
	}

	public static String getEncodedString(String str) {
		String ret = null;
		try {
			ret = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
