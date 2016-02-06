package com.dolibarrmaroc.statistic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.DataSerial;
import com.dolibarrmaroc.mypocketstandard.models.Serial1;
import com.google.gson.Gson;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class VentesActivity extends Activity {

	private WebView myBrowser;
	private Compte compte;
	private Spinner choose;

	private int in_put;
	private String html;
	private String act;
	private String type;
	private HashMap<String, String> months;
	
	private StockVirtual sv;
	
	private MyJavaScriptInterface myJavaScriptInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ventes);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
			in_put = Integer.parseInt(getIntent().getStringExtra("val"));
		}

		sv = new StockVirtual(getApplicationContext());
		
		myBrowser = (WebView)findViewById(R.id.mybrowser);
		
		myJavaScriptInterface = new MyJavaScriptInterface(this);
		myBrowser.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

		myBrowser.getSettings().setJavaScriptEnabled(true); 
		myBrowser.getSettings().setLoadWithOverviewMode(true);
		myBrowser.getSettings().setUseWideViewPort(true);
		myBrowser.setInitialScale(100);
		myBrowser.getSettings().setBuiltInZoomControls(true);
		myBrowser.getSettings().setDisplayZoomControls(false);
		myBrowser.setWebChromeClient(new WebChromeClient());
		
		 DecimalFormat df = new DecimalFormat("00");
		 Calendar celebration = Calendar.getInstance();
		 celebration.setTime(new Date());
		 
		 act = df.format(celebration.get(Calendar.MONTH)+1);
		 
		 months = new HashMap<>();
		 months.put("Jan","01");
		 months.put("Fev","02");
		 months.put("Mar","03");
		 months.put("Avr","04");
		 months.put("Mai","05");
		 months.put("Jui","06");
		 months.put("Jui","07");
		 months.put("Aoû","08");
		 months.put("Sep","09");
		 months.put("Oct","10");
		 months.put("Nov","11");
		 months.put("Dec","12");
				
		
		
		choose = (Spinner) findViewById(R.id.clndrst);
		 
		choose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
				if(choose.getSelectedItemPosition() > 0){
					String sel = choose.getSelectedItem().toString();
					
					act = months.get(sel);
				}
				loadURL();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		 
		loadURL();
	}


	public class MyJavaScriptInterface {
		Context mContext;

		MyJavaScriptInterface(Context c) {
			mContext = c;
		}

		public void showToast(String toast){
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}

		 
		/*
		public String data(){

			Gson json = new Gson();


			List<com.example.model.data> me = new ArrayList<>();
			me.add(new com.example.model.data("Czech Republic","301.90"));
			me.add(new com.example.model.data("Ireland","201.10"));
			me.add(new com.example.model.data("Germany","165.80"));
			me.add(new com.example.model.data("Australia","139.90"));
			me.add(new com.example.model.data("Czech ","15"));
			me.add(new com.example.model.data("yyy","120"));
			me.add(new com.example.model.data("dfb","44"));
			me.add(new com.example.model.data("dfd","12"));

			Log.e(">>> ",json.toJson(me));
			return json.toJson(me);
		}
		*/

		/*
		public String bar(){

			Gson json = new Gson();


			List<com.example.model.data> me = new ArrayList<>();
			me.add(new com.example.model.data("Czech Republic","301.90"));
			me.add(new com.example.model.data("Ireland","201.10"));
			me.add(new com.example.model.data("Germany","165.80"));
			me.add(new com.example.model.data("Australia","139.90"));
			me.add(new com.example.model.data("Czech ","15"));
			me.add(new com.example.model.data("yyy","120"));
			me.add(new com.example.model.data("dfb","44"));
			me.add(new com.example.model.data("dfd","12"));

			Log.e(">>> ",json.toJson(me));
			return json.toJson(me);
		}
		*/
	    @JavascriptInterface
		public String serial(){

			Gson json = new Gson();

			/*
			List<Serial1> s1 = new ArrayList<>();

			List<Double> in1 = new ArrayList<>();
			in1.add(40D);
			in1.add(10D);
			in1.add(10.21);
			in1.add(21.3);

			s1.add(new Serial1("cicin", in1));

			List<String> in2 = new ArrayList<>();
			in2.add("C1");
			in2.add("C2");
			in2.add("C3");
			in2.add("C4");

			DataSerial me = new DataSerial(s1, in2, "Cicin chart","serial chart","MAD");
			*/
			DataSerial me = sv.calculCAGraph(act, type);
			me.setTitle(html);
			me.setYdata(getResources().getString(R.string.stati6));
			me.setXdata("Ventes du mois "+act);
			Log.e(">>> ",json.toJson(me));
			return "["+json.toJson(me)+"]";
		}
	  
	    @JavascriptInterface
		public String donat(){

			Gson json = new Gson();

			
			List<Integer> in2 = new ArrayList<>();

			in2 = sv.calculCAGraphMotifs("MTF");
			
			/*
			in2.add(145);
			in2.add(10);
			in2.add(3);
			in2.add(7);
			in2.add(5);
			in2.add(1);
			in2.add(0);
			*/
			
			Serial1 s1 = new Serial1(getResources().getString(R.string.stati4));
			s1.setSubname(getResources().getString(R.string.stati5));
			s1.setLabel(getResources().getString(R.string.stati7));
			s1.setData2(in2);

			Log.e(">>> ",json.toJson(s1));
			return json.toJson(s1);
		}

	}

	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	 if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			/*
			new AlertDialog.Builder(this)
			.setTitle("Vraiment dï¿½connecter?")
			.setMessage("Vous voulez vraiment dï¿½connecter?")
			.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					//VendeurActivity.super.onBackPressed();
					VendeurActivity.this.finish();
					Intent intent1 = new Intent(VendeurActivity.this, ConnexionActivity.class);
					intent1.putExtra("user", compte);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent1);
					
					Intent intentService = new Intent(VendeurActivity.this, ShowLocationActivity.class);
					stopService(intentService);
					
				}

			}).create().show();
			*/
			onClickHome(LayoutInflater.from(VentesActivity.this).inflate(R.layout.activity_ventes, null));
			
			return true;
		}
		return false;
	}
	
	private void loadURL(){
		if(in_put == 1){
			html = getResources().getString(R.string.stati2);
			type ="FC";
			myBrowser.loadUrl("file:///android_asset/serial.html");
		}else if(in_put == 2){
			html = getResources().getString(R.string.stati1);
			type ="CMD";
			myBrowser.loadUrl("file:///android_asset/serial.html");
		}else if(in_put == 3){
			//html = getResources().getString(R.string.stati1);
			 setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 choose.setVisibility(View.GONE);
			 myBrowser.loadUrl("file:///android_asset/donat.html");
		}
	}
}
