package com.dolibarrmaroc.mypocketstandard.commercial;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.karouani.cicin.widget.AutocompleteCustomArrayAdapter;
import com.karouani.cicin.widget.CustomAutoCompleteTextChangedListener;
import com.karouani.cicin.widget.CustomAutoCompleteView;
 
import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.dao.VendeurDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.maps.MainActivity;
import com.dolibarrmaroc.mypocketstandard.models.CategorieCustomer;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Promotion;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.TotauxTicket;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdDetailActivity;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdViewActivity;
import com.dolibarrmaroc.mypocketstandard.utils.CaisseDolibarr;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.Functions;
import com.dolibarrmaroc.mypocketstandard.utils.JSONParser;
import com.dolibarrmaroc.mypocketstandard.utils.MyLocationListener;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.TinyDB;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;


public class VendeurActivity extends android.support.v4.app.FragmentActivity implements OnClickListener,OnItemSelectedListener{

	
	public CustomAutoCompleteView clientspinner;
	// adapter for auto-complete
	public ArrayAdapter<String> myAdapter;
	public String[] values,value2 ;
	
	public CustomAutoCompleteView categoriepinner;
	
	private List<String> list1 = new ArrayList<String>(),list2 = new ArrayList<String>(),list3 = new ArrayList<String>();
	
	private WakeLock wakelock;
	private TinyDB db;

	//Declaration Objet
	private VendeurManager vendeurManager;
	private Compte compte;
	private Produit produit;
	private Client client;
	private JSONParser json;
	private GpsTracker gps;
	private Dictionnaire dico;
	private double totalTTC = 0;
	private double totalHT = 0;

	//Spinner Remplissage
	private List<String> listclt;
	private List<String> listprd;
	private List<String> listcat;

	//Asynchrone avec connexion 
	private ProgressDialog dialog;
	private ProgressDialog dialog2;

	
	//synchro offline
	private Offlineimpl myoffline;
	private long sysnbr;

	//Declaration Interface
	private Button ajouterproduit;
	private Button fact;
	private EditText idfacture,qte,pu,total;

	//private Spinner clientspinner,proSpinner;
	private CustomAutoCompleteView proSpinner;

	private int firstexecution;
	private HashMap<String, Integer> panier = new HashMap<>();
	
	private int cmdTrue=0;

	//Autre Variable
	private List<Produit> products,produitsFacture;
	private List<Client> clients;
	private List<CategorieCustomer> lscat;
	private int firstinstance;
	private String prix;
	
	private LinearLayout lsqnt;
	private TextView qntdispoview;

	private TextView mtntotal;
	
	private RadioButton btn1,btn2,btn3;
	private int type_invoice;
	private RadioGroup grp_btn;
	
	private StockVirtual sv;
	
	public VendeurActivity() {
		produit = new Produit();
		firstexecution = 0;
		vendeurManager = VendeurManagerFactory.getClientManager();
		dico = new Dictionnaire();
		listclt = new ArrayList<String>();
		listprd = new ArrayList<String>();

		produitsFacture = new ArrayList<Produit>();

		products = new ArrayList<Produit>();
		clients = new ArrayList<Client>();
		gps = new GpsTracker();
		firstinstance = 0;

		json = new JSONParser();
		

	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		clientspinner =  (CustomAutoCompleteView) findViewById(R.id.clientspinner);
		if(clients.size() > 0 && listclt.size() > 0){
			myoffline = new Offlineimpl(getApplicationContext());
			List<Prospection> pros = new ArrayList<>();
			pros = myoffline.LoadProspection("");
			if(!CheckOutNet.isNetworkConnected(getApplicationContext())){
				if(pros.size() > 0) {
					for (int i = 0; i < pros.size(); i++) {
						if(pros.get(i).getClient() == 1){
							if(!listclt.contains(pros.get(i).getName())){
								Client c = new Client(pros.get(i).getIdpros(), pros.get(i).getName(), pros.get(i).getZip(), pros.get(i).getTown(), pros.get(i).getEmail(), pros.get(i).getPhone(), pros.get(i).getAddress());
								clients.add(c);
								listclt.add(pros.get(i).getName());
								addItemsOnSpinnerCustom(clientspinner,1);
							}
						}
					}
				}
				
			}
		}
		/*
		if(CheckOutNet.isNetworkConnected(getApplicationContext())){
			clients = vendeurManager.selectAllClient(compte);
			listclt = new ArrayList<>();
			for (int i = 0; i < clients.size(); i++) {
				listclt.add(clients.get(i).getName());
			}
		}
		Log.e("in system resume ","hopa pas");
		*/
		
		super.onResume();
	}
	@Override
	protected void onStart() {
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();

		Log.e("firstexecute",firstexecution+"");
		if(firstexecution == 0){
			//dialog = ProgressDialog.show(VendeurActivity.this, getResources().getString(R.string.map_data),
				//	getResources().getString(R.string.msg_wait), true);
			
			
			/*****************************  new update  
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				
				myoffline = new Offlineimpl(getApplicationContext());
				if(myoffline.checkAvailableofflinestorage() > 0){
					dialog2 = ProgressDialog.show(VendeurActivity.this, getResources().getString(R.string.caus15),
							getResources().getString(R.string.msg_wait_sys), true);
					new ServerSideTask().execute();
				}else{
					dialog = ProgressDialog.show(VendeurActivity.this, getResources().getString(R.string.map_data),
							getResources().getString(R.string.msg_wait), true);
						new ConnexionTask().execute();	
				}
				 
				//new ConnexionTask().execute();
			}else{
				dialog = ProgressDialog.show(VendeurActivity.this, getResources().getString(R.string.map_data),
						getResources().getString(R.string.msg_wait), true);
				new OfflineTask().execute();
			}
			*/
			
			dialog = ProgressDialog.show(VendeurActivity.this, getResources().getString(R.string.map_data),
					getResources().getString(R.string.msg_wait), true);
			new OfflineTask().execute();
		}

		/******
		 * 
		 * 
		 */
		proSpinner =  (CustomAutoCompleteView) findViewById(R.id.produitspinner);
		proSpinner.setEnabled(false);
		
		qte = (EditText) findViewById(R.id.qte);
		qte.setEnabled(false);
		
		ajouterproduit = (Button) findViewById(R.id.ajouterproduit);
		ajouterproduit.setEnabled(false);
		
		fact = (Button) findViewById(R.id.facture);
		fact.setEnabled(false);
		
		super.onStart();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendeur);

		try {
			
			db = new TinyDB(this);

			myoffline = new Offlineimpl(getBaseContext());
			
			gps = getGpsApplication();

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			Bundle objetbunble  = this.getIntent().getExtras();

			if (objetbunble != null) {
				compte = (Compte) getIntent().getSerializableExtra("user");
				cmdTrue = Integer.parseInt(getIntent().getStringExtra("cmd"));
			}

			// put sample data to database
			//insertSampleData();
			clientspinner =  (CustomAutoCompleteView) findViewById(R.id.clientspinner); //(AutoCompleteTextView) findViewById(R.id.clientspinner);
			clientspinner.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					//final TextView txt = (TextView) findViewById(R.id.cicin);
					
					Log.e("eheloo","click");
						String selected = clientspinner.getSelected(parent, view, position, id);
						//txt.setText(selected);
						
						client = new Client();
						Log.e(">>>> client ",selected+ "");
						for (int i = 0; i < clients.size(); i++) {
							client = clients.get(i);

							if (client.getName().equals(selected)) {
								client = clients.get(i);
								clientspinner.setEnabled(false);
								Log.e("Text selectionner ",client.toString());
								
								proSpinner.setEnabled(true);
								proSpinner.setFocusable(true);
								break;
							}

						}

					}
			});
			clientspinner.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					CustomAutoCompleteTextChangedListener txt = new CustomAutoCompleteTextChangedListener(VendeurActivity.this,R.layout.list_view_row,list1);

					myAdapter = txt.onTextChanged(s, start, before, count);
					myAdapter.notifyDataSetChanged();
					clientspinner.setAdapter(myAdapter);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			// set the custom ArrayAdapter
			
			
			
			proSpinner =  (CustomAutoCompleteView) findViewById(R.id.produitspinner);
			proSpinner.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					// TODO Auto-generated method stub
					String selected = proSpinner.getSelected(parent, view, position, id);
					//txt.setText(selected);
					produit = new Produit();
					Log.e(">>>> produit ",selected+ "");
					for (int i = 0; i < products.size(); i++) {
						if (selected.equals(products.get(i).getDesig())) {
							produit = products.get(i);
							if(panier.size() > 0){
								if(panier.containsKey(products.get(i).getRef())){
									produit.setQteDispo(panier.get(products.get(i).getRef()));
								}
							}

							pu.setText(produit.getPrixUnitaire());
							total.setText(produit.getPrixUnitaire()+"");

							qte.setText("");

							Log.e("Text selectionner ",produit.toString());

							ajouterproduit.setEnabled(true);
							qte.setEnabled(true);
							ajouterproduit.setEnabled(true);
							
							lsqnt.setVisibility(View.VISIBLE);
							qntdispoview.setText(""+produit.getQteDispo());

							break;
						}
					}
				}
			});
			proSpinner.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					CustomAutoCompleteTextChangedListener txt = new CustomAutoCompleteTextChangedListener(VendeurActivity.this,R.layout.list_view_row,list2);

					myAdapter = txt.onTextChanged(s, start, before, count);
					myAdapter.notifyDataSetChanged();
					proSpinner.setAdapter(myAdapter);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			
			categoriepinner =  (CustomAutoCompleteView) findViewById(R.id.categoriespinner);
			categoriepinner.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					// TODO Auto-generated method stub
					String selected = categoriepinner.getSelected(parent, view, position, id);
					Log.e("categorie sel ",selected);
					
					CategorieCustomer cc = null;
					
					for (int i = 0; i < lscat.size(); i++) {
						if(selected.equals(lscat.get(i).getLibelle())){
							cc = lscat.get(i);
						}
					}
					
					if(cc != null){
						listclt = new ArrayList<>();
						Log.e(">cats ",cc.getLsclt().toString());
						for (int j = 0; j < cc.getLsclt().size(); j++) {
							for (int i = 0; i < clients.size(); i++) {
								if(cc.getLsclt().get(j) == clients.get(i).getId()){
									listclt.add(clients.get(i).getName());
								}
							}
						}
						
						if(listclt.size() == 0){
							for (int i = 0; i < clients.size(); i++) {
								listclt.add(clients.get(i).getName());
							}
						}
						addItemsOnSpinnerCustom(clientspinner, 1);
					}
					
				}
			});
			categoriepinner.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					CustomAutoCompleteTextChangedListener txt = new CustomAutoCompleteTextChangedListener(VendeurActivity.this,R.layout.list_view_row,list3);

					myAdapter = txt.onTextChanged(s, start, before, count);
					myAdapter.notifyDataSetChanged();
					categoriepinner.setAdapter(myAdapter);
				//	Log.e("chnaged ",s.toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					//Log.e("before ",s.toString());
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					//Log.e("editable ",s.toString());
					String selected = s.toString();
					Log.e("categorie after sel ",selected);
					
					CategorieCustomer cc = null;
					
					for (int i = 0; i < lscat.size(); i++) {
						if(selected.equals(lscat.get(i).getLibelle())){
							cc = lscat.get(i);
						}
					}
					
					if(cc != null){
						listclt = new ArrayList<>();
						for (int j = 0; j < cc.getLsclt().size(); j++) {
							for (int i = 0; i < clients.size(); i++) {
								if(cc.getLsclt().get(j) == clients.get(i).getId()){
									listclt.add(clients.get(i).getName());
								}
							}
						}
						
						if(listclt.size() == 0){
							for (int i = 0; i < clients.size(); i++) {
								listclt.add(clients.get(i).getName());
							}
						}
						addItemsOnSpinnerCustom(clientspinner, 1);
					}else{
						listclt = new ArrayList<>();
						
							for (int i = 0; i < clients.size(); i++) {
								listclt.add(clients.get(i).getName());
							}
						addItemsOnSpinnerCustom(clientspinner, 1);
					}
				
				}
			});

			qte = (EditText) findViewById(R.id.qte);
			qte.setText("");

			qte.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String qteString = getResources().getString(R.string.field_qte);
					
					Log.e(">on>s ",s.toString()+" << "+qte.getText().toString());
					if(!s.toString().equals("-") && !s.toString().equals(".")){
						if(!qteString.equals(qte.getText().toString())){
							String prix = "";
							double pr = 0;
							if("".equals(qte.getText().toString())){
								total.setText("0");
							}else {
								prix = qte.getText().toString();
								pr = Double.parseDouble(produit.getPrixUnitaire())* Integer.parseInt(prix);

								if (produit.getQteDispo() >= Integer.parseInt(prix)) {
									total.setText(pr+"");
									ajouterproduit.setEnabled(true);
								}else{
									if(produit.getId() != 0){
										alert();
									}

									total.setText(pr+"");
								}
							}
						}
					}
					

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					String qteString = getResources().getString(R.string.field_qte);
					Log.e(">after>s ",s.toString()+" << "+qte.getText().toString());
					if(!s.toString().equals("-") && !s.toString().equals(".")){
						if(!qteString.equals(qte.getText().toString()) && !"".equals(qte.getText().toString()) ){
							List<Promotion> l = chargerMyPromo(client.getId(), produit.getId()); //vendeurManager.getPromotions(client.getId(), produit.getId());
							//for (int i = 0; i < l.size(); i++) {
							Promotion promos = l.get(0);

							if(promos.getType() == 1){
								int p = promos.getPromos();
								int q = promos.getQuantite();
								int n = 0;
								int qd = 0;
								
								if(produitsFacture.contains(produit)){
									for (Produit pr:produitsFacture) {
										if(pr.getRef() == produit.getRef()){
											n = pr.getQtedemander();
											//n = n + Integer.parseInt(qte.getText().toString());
											qd = produit.getQteDispo() + n;
										}
									}
								}else{
									qd = produit.getQteDispo();
								}
								//int qd = produit.getQteDispo() + produit.getQtedemander();
								
								
								int d = Integer.parseInt(qte.getText().toString());
								 Log.e("Affichage",n+ ">> "+qd);
								 
								if(!checkPromo(qd,n,d,q,p)){
									alertPromos();
								}
							}
						}
					}
					
				}
			});

			pu = (EditText) findViewById(R.id.pu);
			pu.setFocusable(false);
			pu.setEnabled(false);

			total = (EditText) findViewById(R.id.totaltext);
			total.setFocusable(false);
			total.setEnabled(false);

			fact = (Button) findViewById(R.id.facture);

			ajouterproduit = (Button) findViewById(R.id.ajouterproduit);
			
			lsqnt = (LinearLayout) findViewById(R.id.linearLayout4);
			lsqnt.setVisibility(View.GONE);
			qntdispoview = (TextView)findViewById(R.id.labelqntdispo4);
			
			mtntotal = (TextView) findViewById(R.id.labelqntdispo44);
			mtntotal.setText("0DH");

			fact.setOnClickListener(this);
			ajouterproduit.setOnClickListener(this);
			//proSpinner.setOnItemSelectedListener(this);
			
			/*
			proSpinner.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					String selected = (String) parent.getItemAtPosition(position);
					produit = new Produit();
					proSpinner.showDropDown();
					final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromInputMethod(parent.getWindowToken(), 0);
					proSpinner.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});

					Log.e("Selected Produit Spinner ",selected);

					for (int i = 0; i < products.size(); i++) {
						if (selected.equals(products.get(i).getDesig())) {
							produit = products.get(i);
							if(panier.size() > 0){
								if(panier.containsKey(products.get(i).getRef())){
									produit.setQteDispo(panier.get(products.get(i).getRef()));
								}
							}

							pu.setText(produit.getPrixUnitaire());
							total.setText(produit.getPrixUnitaire()+"");

							qte.setText("");

							Log.e("Text selectionner ",produit.toString());

							ajouterproduit.setEnabled(true);
							qte.setEnabled(true);
							ajouterproduit.setEnabled(true);
							
							lsqnt.setVisibility(View.VISIBLE);
							qntdispoview.setText(""+produit.getQteDispo());

							break;
						}
					}

				}
			});
			
			*/
			//clientspinner.setOnItemSelectedListener(this);
			/*
			clientspinner.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,
						long arg3) {
					String selected = (String) parent.getItemAtPosition(position);
					client = new Client();
					clientspinner.showDropDown();

					final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromInputMethod(parent.getWindowToken(), 0);

					clientspinner.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});

					Log.e("Selected Client Spinner ",selected);

					for (int i = 0; i < clients.size(); i++) {
						client = clients.get(i);

						if (client.getName().equals(selected)) {
							client = clients.get(i);
							clientspinner.setEnabled(false);
							Log.e("Text selectionner ",client.toString());
							
							proSpinner.setEnabled(true);
							proSpinner.setFocusable(true);
							break;
						}

					}
				}
			});
			*/
			
			btn1 = (RadioButton)findViewById(R.id.radioButton1);
			btn2 = (RadioButton)findViewById(R.id.radioButton2);
			btn3 = (RadioButton)findViewById(R.id.radioButton3);
			
			
			btn1.setChecked(true);
			
			btn1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					type_invoice = 0;
				}
			});
			
			btn2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					type_invoice = 2;
				}
			});

			btn3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					type_invoice = 3;
				}
			});
			
			sv  = new StockVirtual(VendeurActivity.this);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (clientspinner.hasFocus()) {
				// sends focus to another field (user pressed "Next")
				proSpinner.requestFocus();
				return true;
			}
			else if (proSpinner.hasFocus()) {
				// sends focus to another field (user pressed "Next")
				qte.requestFocus();
				return true;
			}
		}else if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			/*
			new AlertDialog.Builder(this)
			.setTitle("Vraiment d�connecter?")
			.setMessage("Vous voulez vraiment d�connecter?")
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
			onClickHome(LayoutInflater.from(VendeurActivity.this).inflate(R.layout.activity_vendeur, null));
			
			return true;
		}
		return false;
	}

	/*
	public void addItemsOnSpinner(AutoCompleteTextView s,int type) {

		if(type == 1){
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listclt);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s.setAdapter(dataAdapter);
			
			list = listclt;
			values= new String[listclt.size()];
			for (int i = 0; i < listclt.size(); i++) {
				values[i] = listclt.get(i);
			}
			
			myAdapter = new AutocompleteCustomArrayAdapter(VendeurActivity.this, R.layout.list_view_row, values);
			clientspinner.setAdapter(myAdapter);

		}else{		
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listprd);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s.setAdapter(dataAdapter);
		}

	}
	*/
	
	public void addItemsOnSpinnerCustom(CustomAutoCompleteView s,int type) {

		if(type == 1){
			
			/*
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listclt);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s.setAdapter(dataAdapter);
			*/
			list1 = listclt;
			values= new String[listclt.size()];
			for (int i = 0; i < listclt.size(); i++) {
				values[i] = listclt.get(i);
			}
			
			
			myAdapter = new AutocompleteCustomArrayAdapter(VendeurActivity.this, R.layout.list_view_row, values);
			s.setAdapter(myAdapter);

		}else if(type == -1){
			
			/*
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listclt);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s.setAdapter(dataAdapter);
			*/
			list3 = listcat;
			values= new String[listcat.size()];
			for (int i = 0; i < listcat.size(); i++) {
				values[i] = listcat.get(i);
			}
			
			
			myAdapter = new AutocompleteCustomArrayAdapter(VendeurActivity.this, R.layout.list_view_row, values);
			s.setAdapter(myAdapter);

		}else{		
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listprd);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s.setAdapter(dataAdapter);
			
			list2 = listprd;
			value2= new String[listprd.size()];
			for (int i = 0; i < listprd.size(); i++) {
				value2[i] = listprd.get(i);
			}
			
			
			myAdapter = new AutocompleteCustomArrayAdapter(VendeurActivity.this, R.layout.list_view_row, value2);
			s.setAdapter(myAdapter);
		}

	}

	class ConnexionTask extends AsyncTask<Void, Void, String> {


		int nclt;
		int nprod;
		
		@Override
		protected String doInBackground(Void... params) {

			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			//Log.i("Produit ",vendeurManager.selectAllProduct(compte).toString());

			
			products = vendeurManager.selectAllProduct(compte);
			sv  = new StockVirtual(VendeurActivity.this);
			
			nprod = products.size();
		
			
			dico = vendeurManager.getDictionnaire();
			
			//Log.e(">> DICO << ",dico.toString());

			for (int i = 0; i < products.size(); i++) {
				Produit p = new Produit();

				p = products.get(i);
				listprd.add(p.getDesig());

				//Log.d("Produit "+i,p.toString());qq
				for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
					if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
						Log.e("is me ",sv.getAllProduits(-1).get(j).getRef()+" ## "+products.get(i).getRef());
						products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
					}
				}
				
				for (int j = 0; j < sv.getAllProduitsVentes(-1).size(); j++) {
					if(sv.getAllProduitsVentes(-1).get(j).getId() == products.get(i).getId()){
						products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduitsVentes(-1).get(j).getQteDispo());
					}
				}
			}

			clients = vendeurManager.selectAllClient(compte);
			nclt = clients.size();
			
			for (int i = 0; i < clients.size(); i++) {
				listclt.add(clients.get(i).getName());
			}
			

			
			/*********************** offline ****************************************/
			Log.e("begin offline from network",">>start load");
		
			
			
			//myoffline.CleanService();
			
			
			
			if(!myoffline.checkFolderexsiste()){
				showmessageOffline();
			}else{
				if(products.size() > 0){
					myoffline.CleanProduits();
					myoffline.CleanPromotionProduit();
					sysnbr += myoffline.shynchronizeProduits(products);
					sysnbr += myoffline.shynchronizePromotion(vendeurManager.getPromotionProduits());
				}
				
				
				if(dico.getDico().size() > 0){
					myoffline.CleanDico();
					sysnbr += myoffline.shynchronizeDico(dico);
				}
				
				if(clients.size() > 0){
					myoffline.CleanClients();
					myoffline.CleanPromotionClient();
					sysnbr += myoffline.shynchronizeClients(clients);
					sysnbr += myoffline.shynchronizePromotionClient(vendeurManager.getPromotionClients());
				}
				
				CommercialManager manager = CommercialManagerFactory.getCommercialManager();
				myoffline.CleanProspectData();
				sysnbr += myoffline.shynchronizeProspect(manager.getInfos(compte));
				
				myoffline.CleanPayement();
				PayementManager payemn = PayementManagerFactory.getPayementFactory();
				sysnbr += myoffline.shynchronizePayement(payemn.getFactures(compte));
			}
			
			Log.e("start ","start cnx task");
			
			lscat = new ArrayList<>();
			listcat = new ArrayList<>();
			lscat = vendeurManager.getAllCategorieCustomer(compte);
			if(lscat.size() > 0){
				myoffline.CleanCategorieClients();
				for (int i = 0; i < lscat.size(); i++) {
					myoffline.shnchronizeCategorieClients(lscat.get(i), compte);
				}
			}
		//	Log.e(">cat ",lscat.toString());
			for (int i = 0; i < lscat.size(); i++) {
				listcat.add(lscat.get(i).getLibelle()+"");
			}
			
			
			
			
			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing()){
					dialog.dismiss();
					addItemsOnSpinnerCustom(proSpinner,2);
					addItemsOnSpinnerCustom(clientspinner,1);
					addItemsOnSpinnerCustom(categoriepinner,-1);
					
					
					firstexecution = 1989;
					
					/*
					if(!myoffline.checkFolderexsiste() || (sysnbr == -7)){
						showmessageOffline();
					}
					*/
					String msg ="";
					
					if(nprod == 0){
						msg += getResources().getString(R.string.caus26)+"\n";
					}
					
					if(nclt == 0){
						msg += getResources().getString(R.string.caus27)+"\n";
					}
					
					int k =0;
					if(nclt == 0 || nprod == 0 ){
						alertPrdClt(msg);
						k=-1;
					}
					
					if(k == 0) {
						if(myoffline.LoadClients("").size() != nclt || myoffline.LoadProduits("").size() != nprod){
							showmessageOffline();
						}
					}
					
					
					Log.e("end ","end cnx task");
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}
	
	
	class ServerSideTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				myoffline = new Offlineimpl(getApplicationContext());
				if(CheckOutNet.isNetworkConnected(getApplicationContext())){
					myoffline.SendOutData(compte);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erreu synchro",e.getMessage() +" << ");
			}
			
			Log.e("start ","start cnx service");
			
			return null;
		}

		protected void onPostExecute(String sResponse) {
			try {
				if (dialog2.isShowing()){
					dialog2.dismiss();
					
					dialog = ProgressDialog.show(VendeurActivity.this, getResources().getString(R.string.map_data),
							getResources().getString(R.string.msg_wait), true);
					
					if(CheckOutNet.isNetworkConnected(getApplicationContext())){
						new ConnexionTask().execute();	
					}
					
					Log.e("end ","start cnx service");
					/*
					Intent intent2 = new Intent(ConnexionActivity.this, SettingsynchroActivity.class);
					intent2.putExtra("user", compte);
					startActivity(intent2);
					*/
				}
				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","");
			}
		}

	}

	class OfflineTask extends AsyncTask<Void, Void, String> {

		
		private int is_tr;

		@Override
		protected String doInBackground(Void... params) {

			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


			//listclt = new ArrayList<>();
			//listprd = new ArrayList<>();
			myoffline = new Offlineimpl(getBaseContext());
			products = myoffline.LoadProduits("");
			
			
			sv = new StockVirtual(VendeurActivity.this);
		
			
			dico = myoffline.LoadDeco("");
			
			Log.e(">> R/E << ",sv.getAllProduits(-1).toString());
			
			//Log.e("Produit 1 ",products.toString());

			for (int i = 0; i < products.size(); i++) {
				Produit p = new Produit();

				p = products.get(i);
				listprd.add(p.getDesig());

				//reduce qnt from rendu et echange 
				for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
					if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
						products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
					}
				}
				
				/*
				for (int j = 0; j < sv.getAllProduitsVentes(-1).size(); j++) {
					if(sv.getAllProduitsVentes(-1).get(j).getId() == products.get(i).getId()){
						products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduitsVentes(-1).get(j).getQteDispo());
					}
				}
				*/
				
			}
			
			//Log.e("Produit s3 ",products.toString());
			
			Log.e("Produit in cache ",sv.getAllProduitsVentes(-1).toString());
			
			for (int i = 0; i < products.size(); i++) {

				for (int j = 0; j < sv.getAllProduitsVentes(-1).size(); j++) {
					if(sv.getAllProduitsVentes(-1).get(j).getId() == products.get(i).getId()){
						products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduitsVentes(-1).get(j).getQteDispo());
					}
				}
			}
			
			//Log.e("Produit 3 ",products.toString());

			//Log.e("begin offline from offline",">>start load "+products.toString());
			
			if(compte.getIstour() == 1){//com.dolibarrmaroc.com.utils.URL.is_tour
				List<Tournee> trs = Functions.prepaTourneeData(myoffline.LoadTourneeList("")).get(Functions.getNumberOfDay(new Date()));
				int n = trs.size();
				
				Log.e(">>>tourne in  "," in in "+n);
				is_tr = n;
				if(n > 0){
					clients = new ArrayList<>();
					for (int i = 0; i < trs.size(); i++) {
						clients.addAll(trs.get(i).getLsclt());
					}
					
				}
				/*
				else{
					clients = myoffline.LoadClients("");
				}
				*/
			}else{
				clients = myoffline.LoadClients("");
			}
			
			
			
			
			
			Log.e("star client 1pros ",clients.toString()+"");
			Log.e("star client clt ",clients.size()+"");
			
			for (int i = 0; i < clients.size(); i++) {
				listclt.add(clients.get(i).getName());
			}

				List<Prospection> pros = myoffline.LoadProspection("");
					for (int i = 0; i < pros.size(); i++) {
						if(pros.get(i).getClient() == 1){
							Client c = new Client(pros.get(i).getIdpros(), pros.get(i).getName(), pros.get(i).getZip(), pros.get(i).getTown(), pros.get(i).getEmail(), pros.get(i).getPhone(), pros.get(i).getAddress());
							clients.add(c);
							listclt.add(pros.get(i).getName());
						}
					}
			
					lscat = new ArrayList<>();
					listcat = new ArrayList<>();
					lscat = myoffline.LoadCategorieClients("");
					
					for (int i = 0; i < lscat.size(); i++) {
						listcat.add(lscat.get(i).getLibelle()+"");
					}
			
			
			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing()){
					dialog.dismiss();
					addItemsOnSpinnerCustom(proSpinner,2);
					addItemsOnSpinnerCustom(clientspinner,1);
					addItemsOnSpinnerCustom(categoriepinner	,-1);
					firstexecution = 1989;
					
					if(is_tr == 0 && (compte.getIstour() == 1)){//com.dolibarrmaroc.com.utils.URL.is_tour){
						new AlertDialog.Builder(VendeurActivity.this)
					    .setTitle(getResources().getString(R.string.cmdtofc12))
					    .setMessage(getResources().getString(R.string.task6))
					    .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialogc, int which) { 
					        	dialogc.dismiss();
					        }
					     })
					     .setCancelable(true)
					     .show();
					}
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {


	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}


	@Override
	public void onClick(View v) {
		String qteString = getResources().getString(R.string.field_qte);
		if (v.getId() == R.id.ajouterproduit) {
			if(qteString.equals(qte.getText().toString()) || "".equals(qte.getText().toString())){
				Toast.makeText(VendeurActivity.this,getResources().getString(R.string.qte_vide), Toast.LENGTH_SHORT).show();
			}else{
				int t = Integer.parseInt(qte.getText().toString());

				int tr = 0;
				for (int i = 0; i < produitsFacture.size(); i++) {
					if(produitsFacture.get(i).getDesig().equals(produit.getDesig())){
						int k = produitsFacture.get(i).getQtedemander() + t;
						produitsFacture.get(i).setQtedemander(k);
						double prt = produit.getPrixttc()*t;
						totalTTC += prt; 

						tr = 1;
						break;
					}else{
						tr = 0;
					}
				}
				if (tr == 0) {
					produit.setQtedemander(t);
					produitsFacture.add(produit);
					double prt = produit.getPrixttc()*t;
					totalTTC += prt; 
				}
				totalHT += Double.parseDouble(total.getText().toString());

				Toast.makeText(VendeurActivity.this,getResources().getString(R.string.msg_add_product), Toast.LENGTH_SHORT).show();
				Log.i("Produits List ", produitsFacture.toString());
				fact.setEnabled(true);

				Log.e(">>>>>> --------",">>> Firstly "+produit.getQteDispo());

				if(panier.size() == 0){
					int qt = produit.getQteDispo() - Integer.parseInt(qte.getText().toString());
					panier.put(produit.getRef(), qt);
				}
				else{
					if(panier.containsKey(produit.getRef())){
						int qt = panier.get(produit.getRef()) - Integer.parseInt(qte.getText().toString());
						panier.put(produit.getRef(), qt);

					}else{
						int qt = produit.getQteDispo() - Integer.parseInt(qte.getText().toString());
						panier.put(produit.getRef(), qt);
					}

				}

				proSpinner.setText("");
				produit = new Produit();
				pu.setText(getResources().getString(R.string.field_pu));
				total.setText(getResources().getString(R.string.total_ht));
				qte.setText(getResources().getString(R.string.field_qte));
				qte.setEnabled(false);
				
				mtntotal.setText(totalTTC+" DH");
				lsqnt.setVisibility(View.GONE);
				
				Log.e(">>>>>> --------",">>> Secondly "+produit.getQteDispo());
				Log.e(">>>>>> --------",">>> Finally "+panier.get(produit.getRef()));
			}

		}else if(v.getId() == R.id.facture){
			
			Log.e(">> sel ",type_invoice+"");
			
			if(check_radio()){
				if(products.size() > 0 || products != null){
					Intent intent = new Intent(VendeurActivity.this, FactureActivity.class);
					//intent.putExtra"products", produitsFacture);

					Map<String, Promotion> prom = new HashMap<>();
					for (int i = 0; i < products.size(); i++) {
						Promotion pr =chargerMyPromo(client.getId(), products.get(i).getId()).get(0);//vendeurManager.getPromotions(client.getId(), products.get(i).getId()).get(0);
						prom.put( products.get(i).getRef(), pr);
					}

					db.saveMapPromotion("allpromotion", prom);

					for (int i = 0; i < products.size(); i++) {
						Produit p = products.get(i);
						intent.putExtra("products"+i, p);
						Log.d("Product Spinner >> "+i, p.toString());
					}

					for (int i = 0; i < produitsFacture.size(); i++) {
						Produit pm = produitsFacture.get(i);
						if(panier.size() > 0){
							if(panier.containsKey(pm.getRef())){
								pm.setQteDispo(panier.get(pm.getRef()));
							}
						}
						intent.putExtra("produit"+i, pm);
					}
					intent.putExtra("nmbproduct", produitsFacture.size()+"");
					intent.putExtra("nmbproducts", products.size()+"");
					intent.putExtra("total", totalTTC+"");
					intent.putExtra("idclt", client.getId()+"");
					intent.putExtra("gps", gps);
					intent.putExtra("compte", compte);

					intent.putExtra("dico", dico.getDico());

					intent.putExtra("totalht", totalHT+"");
					
					intent.putExtra("cmd", cmdTrue+"");
					
					/*
					int xbtn = grp_btn.getCheckedRadioButtonId();
					btn1 = (RadioButton)findViewById(xbtn);
					*/
					intent.putExtra("typeinvoice", type_invoice+"");

					for (int i = 0; i < produitsFacture.size(); i++) {
						Log.d("Products Envoie", produitsFacture.toString());
						Log.d("Nombre Envoie", produitsFacture.size()+"");
					}
					startActivity(intent);
					produitsFacture = new ArrayList<Produit>();
					VendeurActivity.this.finish();
				}else{
					//Toast.makeText(VendeurActivity.this,getResources().getString(R.string.produit_min), Toast.LENGTH_SHORT).show();
					alerttype_invo(getResources().getString(R.string.produit_min));
				}
			}else{
				alerttype_invo(getResources().getString(R.string.cmdtofc25));
			}
			

		}
	}

	public GpsTracker getGpsApplication(){

		LocationManager mlocManager=null;
		MyLocationListener mlocListener;
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);

		if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			if(MyLocationListener.latitude>0)
			{
				gps.setLangitude(""+MyLocationListener.longitude);
				gps.setLatitude(""+MyLocationListener.latitude);
				gps.setAltitude(MyLocationListener.altitude);
				gps.setDateString(MyLocationListener.dateString);
				gps.setDirection(MyLocationListener.direction);
				gps.setSatellite(MyLocationListener.satellite);
				gps.setSpeed(MyLocationListener.speed);
			}
			else
			{
				gps.setLangitude(""+0);
				gps.setLatitude(""+0);
			}
		}
		return gps;
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.vendeur_tab, menu);
		if(compte != null){
			if(compte.getPermission() == 0){
				menu.removeItem(R.id.createUserMenu);
				menu.removeItem(R.id.updateUserMenu);
			}
			
			if(compte.getPermissionbl() == 0){
				menu.removeItem(R.id.viewcmd);
				menu.removeItem(R.id.addcmd);
			}
			
			if(!CheckOutNet.isNetworkConnected(VendeurActivity.this)){
				menu.removeItem(R.id.updateUserMenu);
			}
		}
		
		return super.onCreateOptionsMenu(menu);
	}
*/
/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/************************MENU Carte *****************************************
		case R.id.pointerClt:
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				Intent photosIntent = new Intent(this, MainActivity.class);
				photosIntent.putExtra("user", compte);
				startActivity(photosIntent);
			}else{
				alertmaps();
			}
			
			break;

		case R.id.createUserMenu:
			Intent intent3 = new Intent(VendeurActivity.this, CommercialActivity.class);
			intent3.putExtra("user", compte);
			startActivity(intent3);
			break;
		case R.id.updateFactureUser:
			Intent intent4 = new Intent(VendeurActivity.this, PayementActivity.class);
			intent4.putExtra("user", compte);
			intent4.putExtra("dico", dico.getDico());
			startActivity(intent4);
			break;
		case R.id.createofflineticket:
			Intent intent5 = new Intent(VendeurActivity.this, OfflineActivity.class);
			intent5.putExtra("user", compte);
			startActivity(intent5);
			break;
		case R.id.setingsynchrooff:
			Intent intent6 = new Intent(VendeurActivity.this, SettingsynchroActivity.class);
			intent6.putExtra("user", compte);
			startActivity(intent6);
			break;
		case R.id.viewcmd:
			Intent intent7 = new Intent(VendeurActivity.this, CmdViewActivity.class);//ViewcommandeActivity
			intent7.putExtra("user", compte);
			startActivity(intent7);
			break;
		case R.id.addcmd:
			Intent intent8 = new Intent(VendeurActivity.this, CatalogeActivity.class);
			intent8.putExtra("user", compte);
			intent8.putExtra("cmd", "1");
			startActivity(intent8);
			break;
		case R.id.clsv:
			Intent intent9 = new Intent(VendeurActivity.this, TransfertvirtualstockActivity.class);
			intent9.putExtra("user", compte);
			intent9.putExtra("cmd", "0");
			startActivity(intent9);
			break;
		case R.id.updateUserMenu:
			Intent intent10 = new Intent(VendeurActivity.this, UpdateClientActivity.class);
			intent10.putExtra("user", compte);
			startActivity(intent10);
			break;
		}
		
		return true;

	}
*/
	public void alert(){
		AlertDialog.Builder alert = new AlertDialog.Builder(VendeurActivity.this);
		alert.setTitle(getResources().getString(R.string.stock_limit));
		alert.setMessage(
				String.format(
						getResources().getString(R.string.stock_msg),
						produit.getQteDispo()));
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ajouterproduit.setEnabled(false);
				return;
			}
		});
		alert.create().show();
	}
	
	public void alertmaps(){
		AlertDialog.Builder alert = new AlertDialog.Builder(VendeurActivity.this);
		alert.setTitle(getResources().getString(R.string.caus17));
		alert.setMessage(getResources().getString(R.string.caus18));
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}
	
	public void alertPrdClt(String msg){
		AlertDialog.Builder alert = new AlertDialog.Builder(VendeurActivity.this);
		alert.setTitle(getResources().getString(R.string.cmdtofc10));
		alert.setMessage(msg);
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}

	public boolean checkPromo(int qd,int n,int d,int q,int p){
		int x = qd - (n+d);
		int gratuite = ((n+d)/p)*q;
		int  res = x - gratuite;
		if(res >= 0){
			Log.e("Voila Promos","Produits "+produit+" gratuite "+gratuite);
			return true;
		}
		else{
			Log.e("Stock Limite","Stock Limité "+x);
			return false;
		}
	}

	public void alertPromos(){
		List<Promotion> l = chargerMyPromo(client.getId(), produit.getId());//vendeurManager.getPromotions(client.getId(), produit.getId());
		//for (int i = 0; i < l.size(); i++) {
		Promotion p = l.get(0);

		AlertDialog.Builder alert = new AlertDialog.Builder(VendeurActivity.this);
		alert.setTitle(getResources().getString(R.string.promo_alert));
		alert.setMessage(
				String.format(
						getResources().getString(R.string.promo_msg)+" car vous avez "+p.getQuantite()+" Produit Gratuit",
						panier.get(produit.getRef())));
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ajouterproduit.setEnabled(false);
				return;
			}
		});
		alert.create().show();
	}

	
	 
	
	public List<Promotion> chargerMyPromo(int cl,int pr){
		/*
		if(CheckOutNet.isNetworkConnected(getApplicationContext()) && (myoffline.checkClient_is_Prospect(cl) == -1)){
			return vendeurManager.getPromotions(cl,pr);
		}
		*/
		return myoffline.getPromotions(cl,pr);
	}
	
	public void showmessageOffline(){
		try {
			 
	         LayoutInflater inflater = this.getLayoutInflater();
	         View dialogView = inflater.inflate(R.layout.msgstorage, null);
	         
	         AlertDialog.Builder dialog =  new AlertDialog.Builder(VendeurActivity.this);
	         dialog.setView(dialogView);
 	 	     dialog.setTitle(R.string.caus1);
 	 	     dialog.setPositiveButton(R.string.caus8, new DialogInterface.OnClickListener() {
 	 	        public void onClick(DialogInterface dialog, int which) { 
 	 	        	 dialog.cancel();
 	 	        }
 	 	     });
 	 	     dialog.setCancelable(true);
 	 	     dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



	/*********************************************************************************************
	 * 							AutoComplate
	*********************************************************************************************/
	
/*
	@Override
	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		//final TextView txt = (TextView) findViewById(R.id.cicin);
		
		Log.e("eheloo","click");
		if(view.getId() == R.id.clientspinner){
			String selected = clientspinner.getSelected(parent, view, position, id);
			//txt.setText(selected);
			
			client = new Client();
			Log.e(">>>> client ",selected+ "");
			for (int i = 0; i < clients.size(); i++) {
				client = clients.get(i);

				if (client.getName().equals(selected)) {
					client = clients.get(i);
					clientspinner.setEnabled(false);
					Log.e("Text selectionner ",client.toString());
					
					proSpinner.setEnabled(true);
					proSpinner.setFocusable(true);
					break;
				}

			}

		}else if(view.getId() == R.id.produitspinner){
			String selected = proSpinner.getSelected(parent, view, position, id);
			//txt.setText(selected);
			produit = new Produit();
			Log.e(">>>> produit ",selected+ "");
			for (int i = 0; i < products.size(); i++) {
				if (selected.equals(products.get(i).getDesig())) {
					produit = products.get(i);
					if(panier.size() > 0){
						if(panier.containsKey(products.get(i).getRef())){
							produit.setQteDispo(panier.get(products.get(i).getRef()));
						}
					}

					pu.setText(produit.getPrixUnitaire());
					total.setText(produit.getPrixUnitaire()+"");

					qte.setText("");

					Log.e("Text selectionner ",produit.toString());

					ajouterproduit.setEnabled(true);
					qte.setEnabled(true);
					ajouterproduit.setEnabled(true);
					
					lsqnt.setVisibility(View.VISIBLE);
					qntdispoview.setText(""+produit.getQteDispo());

					break;
				}
			}
		}
		
	}
	*/
	/*********************************************************************************************
	 * 									AutoComplate
	*********************************************************************************************/
	
	private boolean check_radio(){
		if(btn1.isChecked() || btn2.isChecked() || btn3.isChecked() ){
			Log.e("checkedos ",btn1.isChecked() +"#"+ btn2.isChecked() +"#"+ btn3.isChecked());
			return true;
		}
		return false;
	}
	
	public void alerttype_invo(String st){
		AlertDialog.Builder alert = new AlertDialog.Builder(VendeurActivity.this);
		alert.setTitle(getResources().getString(R.string.cmdtofc10));
		alert.setMessage(st);
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}
	
	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	
	
	
}