package com.dolibarrmaroc.mypocketstandard.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.URL;

public class DeniedDataDaoMysql implements DeniedDataDao {

	private String urlData = URL.URL+"writeerror.php";
	private JSONParser parser ;

	@Override
	public List<String> sendMyErrorData(List<String> in, Compte c,String tp) {
		// TODO Auto-generated method stub
		List<String> res = new ArrayList<>();
		
		if(in.size() > 0){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
			nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
			nameValuePairs.add(new BasicNameValuePair("nbr",in.size()+""));
			nameValuePairs.add(new BasicNameValuePair("type",tp));



			for (int i = 0; i < in.size(); i++) {
				nameValuePairs.add(new BasicNameValuePair("input"+i,in.get(i)));
			}

			
			Log.e("denieded data ",nameValuePairs.toString());

			try {
				parser = new JSONParser();
				String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
				
				String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
				
				JSONObject obj = new JSONObject(stfomat);
				
				stfomat = obj.getString("res");
				
				if(!"ok".equals(stfomat)){
					res = in;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				res = in;
				Log.e("DeniedDataDaoMysql json insert eroor data sendMyErrorData",e.getMessage() +" << ");
				MyDebug.WriteLog(this.getClass().getSimpleName(), "sendMyErrorData", nameValuePairs.toString(), e.toString());
			}
		}
		

		return res;
	}

}
