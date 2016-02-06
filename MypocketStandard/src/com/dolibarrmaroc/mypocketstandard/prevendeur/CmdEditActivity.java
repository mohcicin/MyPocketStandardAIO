package com.dolibarrmaroc.mypocketstandard.prevendeur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.menu;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.commercial.FactureActivity;
import com.dolibarrmaroc.mypocketstandard.commercial.NextEtapeActivity;
import com.dolibarrmaroc.mypocketstandard.commercial.VendeurActivity;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDao;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.impression.ImprimerActivity;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.MyProdRemise;
import com.dolibarrmaroc.mypocketstandard.models.Myinvoice;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Promotion;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdDetailActivity.ConnexionTask;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutSysc;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class CmdEditActivity extends Activity implements OnItemClickListener{

	private ioffline myoffline;
	private PowerManager.WakeLock wakelock;
	private Compte compte;


	private ListView myhome;
	private Button addnew,save;


	private HashMap<Integer, Commandeview> mycmd;

	private HashMap<Integer, Produit> panier;
	private List<Produit> stock;
	private double calcul_ca;

	private ProgressDialog dialogload;
	private Dialog dialog;

	private TextView ttc;
	private LinearLayout lc;
	private TextView tc;

	private CommandeManager manager;

	private Commandeview v;

	private SimpleAdapter adapter;
	
	private StockVirtual sv;

	/******************     *****************************/
	private Button facturePop,cancel,addprd,facture;
	private List<HashMap<String, String>> fillMaps;
	private EditText qtep = null,pup = null,totalp =null;
	private View qntlayout;
	private TextView qntview;
	private AutoCompleteTextView spinnere;
	private Produit produit,produitbup;
	private List<String> listprd;

	
	private HashMap<Integer, Produit> backup = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmd_edit);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();





		myoffline = new Offlineimpl(getApplicationContext());

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}


		manager = new CommandeManagerFactory().getManager();


		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
			mycmd = (HashMap<Integer, Commandeview>)getIntent().getSerializableExtra("lscmd");
			v = (Commandeview)getIntent().getSerializableExtra("vc"); 
		}


		myhome = (ListView)findViewById(R.id.cmdupview);
		myhome.setOnItemClickListener(this);

		ttc = (TextView)findViewById(R.id.textView4);

		lc = (LinearLayout)findViewById(R.id.linearLayout1);
		lc.setVisibility(View.GONE);

		addnew = (Button)findViewById(R.id.cmdupup);
		save = (Button)findViewById(R.id.cmdupsave);

		//pdf.setEnabled(false);
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.prodcatlayout);
		dialog.setTitle("Ajouter un autre Produit");

		facturePop = (Button) dialog.findViewById(R.id.itenermoifact);
		facturePop.setText(getResources().getString(R.string.facture2));
		cancel = (Button) dialog.findViewById(R.id.annulershowme);
		addprd = (Button) dialog.findViewById(R.id.factshowme);

		cancel.setVisibility(View.GONE);

		spinnere =  (AutoCompleteTextView) dialog.findViewById(R.id.produitpointer);
		spinnere.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				String selected = (String) parent.getItemAtPosition(position);
				produit = new Produit();
				spinnere.showDropDown();
				final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromInputMethod(parent.getWindowToken(), 0);
				spinnere.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});

				Log.e("Selected Produit Spinner ",selected+"");

				for (Produit i:stock) {
					if (selected.equals(i.getDesig())) {
						produit = new Produit();
						produitbup = new Produit();
						
						produitbup = i;
						produit = i;
						
						/*
						produit.setQtedemander(0);
						produitbup.setQtedemander(0);
						*/
						/*
						if(panier.size() > 0){
							if(panier.containsKey(products.get(i).getRef())){
								produit.setQteDispo(panier.get(products.get(i).getRef()));
							}
						}
						 */

						Log.e("Text selectionner ",produit.toString());
						pup.setText(produit.getPrixUnitaire());
						qtep.setEnabled(true);

						qntlayout.setVisibility(View.VISIBLE);

						int d = produit.getQteDispo() - qnt_disponible(produit.getId());

						qntview.setText(d+"");
						qtep.setEnabled(true);
						//selectionner.setEnabled(true);

						break;
					}
				}
			}
		});

		qntlayout = (LinearLayout)dialog.findViewById(R.id.l1w);
		qntlayout.setVisibility(View.GONE);

		qntview = (TextView)dialog.findViewById(R.id.textView1ww);
		addnew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(v != null){

					//Button facture = (Button) dialog.findViewById(R.id.facturedialog);
					facturePop.setOnClickListener(new View.OnClickListener() {


						@Override
						public void onClick(View v) {
							adapter = getSimple(panier);
							adapter.notifyDataSetChanged();
							myhome.setAdapter(adapter);

							qntlayout.setVisibility(View.GONE);
							qtep.setEnabled(false);
							qtep.setText("");
							qtep.setHint(getResources().getString(R.string.field_qte));
							totalp.setText("0");
							spinnere.setText("");
							pup.setText("0");
							produit = new Produit();

							dialog.dismiss();
						}
					});
					//Button cancel = (Button) dialog.findViewById(R.id.annulerFact);
					cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							adapter = getSimple(panier);
							adapter.notifyDataSetChanged();
							myhome.setAdapter(adapter);


							qntlayout.setVisibility(View.GONE);
							qtep.setEnabled(false);
							qtep.setText("");
							qtep.setHint(getResources().getString(R.string.field_qte));
							totalp.setText("0");
							spinnere.setText("");
							pup.setText("0");
							produit = new Produit();

							dialog.dismiss();
						}
					});

					//Button addprd = (Button) dialog.findViewById(R.id.ajouterproduitdialog);

					addElementToSpinner();

					//////////////**********************************Traitement PoPup************************************************////////////////////
					totalp = (EditText) dialog
							.findViewById(R.id.totaltextdialog);
					totalp.setFocusable(false);
					totalp.setEnabled(false);

					qtep = (EditText) dialog
							.findViewById(R.id.qtedialog);
					qtep.setEnabled(false);
					pup = (EditText) dialog
							.findViewById(R.id.pudialog);

					pup.setFocusable(false);
					pup.setEnabled(false);

					qtep.setText("");
					qtep.setEnabled(false);

					qtep.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							String qteString = getResources().getString(R.string.field_qte);
							if(!s.toString().equals("-") && !s.toString().equals(".")){
								if(!qteString.equals(qtep.getText().toString())){
									String prix = "";
									double pr = 0;
									if("".equals(qtep.getText().toString())){
										totalp.setText("0");
									}
									else {
										int pri = 0;
										prix = qtep.getText().toString();
										if(!"".equals(prix) || prix != null){
											pri = Integer.parseInt(prix);
										}

										pr = Double.parseDouble(produit.getPrixUnitaire())* pri;

										if (produit.getQteDispo() >= Integer.parseInt(prix)) {
											totalp.setText(pr+"");
										}else{
											if(produit.getId() != 0){
												//Toast.makeText(FactureActivity.this, "La quantit� demand� est supperieur � notre stock "+produit.getQteDispo(), Toast.LENGTH_SHORT).show();
												alert(addprd,produit);
												totalp.setText(pr+"");
											}
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

							if(!s.toString().equals("-") && !s.toString().equals(".")){
								if(!qteString.equals(qtep.getText().toString()) && !"".equals(qtep.getText().toString()) ){



								}
							}
						}	
					});

					addprd.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String qteString = getResources().getString(R.string.field_qte);
							if(produit.getId() == 0){

							}else if(qteString.equals(qtep.getText().toString()) || "".equals(qtep.getText().toString())){
								Toast.makeText(CmdEditActivity.this,getResources().getString(R.string.qte_vide), Toast.LENGTH_SHORT).show();
							}else if(Integer.parseInt(qtep.getText().toString()) != 0  && produit.getId() != 0){
								int t = Integer.parseInt(qtep.getText().toString());
								double prt = produit.getPrixttc()*t;

								//Log.e("panier begin ",panier.toString()+" >> "+panier.size()+"");
								
								if(panier.size() == 0){
									
									Log.e("panier zero ","in zerp *********");
									int qt = produit.getQteDispo() - t;

									for (int i = 0; i < stock.size(); i++) {
										if(stock.get(i).getDesig().equals(produit.getDesig())){
											stock.get(i).setQteDispo(qt);
											break;
										}
									}

									

									produit.setQtedemander(t);
									panier.put(produit.getId(), produit);

								}
								else{
									if(panier.containsKey(produit.getId())){
										
										
										int qt = panier.get(produit.getId()).getQtedemander() + t;
										
										//Log.e("qnt in panier ", ">> "+panier.get(produit.getId()).getQtedemander());
										
										//produit.setQtedemander(qt);
										
									//	Log.e("produit in ",produit.toString()+"");
										
										panier.remove(produit.getId());
										
										 
										
										
										panier.put(produit.getId(), new Produit(produit.getRef(), produit.getDesig(), produit.getQteDispo(), produit.getPrixUnitaire(), qt, produit.getPrixttc(), produit.getTva_tx(), produit.getFk_tva(),produit.getId()));

									}else{
										
										int qt = produit.getQtedemander() + t;
										
										
										//produit.setQtedemander(qt);
										
										//Log.e("produit not in  ",produit.toString()+"");
										
										panier.put(produit.getId(), new Produit(produit.getRef(), produit.getDesig(), produit.getQteDispo(), produit.getPrixUnitaire(), t, produit.getPrixttc(), produit.getTva_tx(), produit.getFk_tva(),produit.getId()));
										
									}

								}
								
								
								//Log.e("panier after ",panier.toString()+" >> "+panier.size()+"");
								
								
								adapter = getSimple(panier);
								adapter.notifyDataSetChanged();
								myhome.setAdapter(adapter);
								

								calcul_ca += prt;

								 
								  
								//addBackup(produit,t);
								addBackup(produitbup,t);
								

								qntlayout.setVisibility(View.GONE);
								qtep.setEnabled(false);
								qtep.setText("");
								qtep.setHint(getResources().getString(R.string.field_qte));
								totalp.setText("0");
								spinnere.setText("");
								//pup.setText("0");
								produit = new Produit();
								//facture.setEnabled(true);

								ttc.setText("Total TTC : "+calcul_ca);
							}else{
								AlertDialog.Builder alert = new AlertDialog.Builder(CmdEditActivity.this);
								alert.setTitle(getResources().getString(R.string.qte_zero));
								alert.setMessage(getResources().getString(R.string.qte_size));
								alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										//ajouterproduit.setEnabled(false);
										qtep.setText("");
										qtep.setHint(getResources().getString(R.string.field_qte));
										//pup.setText("0");
										totalp.setText("0");
										return;
									}
								});
								alert.create().show();
							}
						}
					});


					qntlayout.setVisibility(View.GONE);
					qtep.setEnabled(false);
					qtep.setText("");
					qtep.setHint(getResources().getString(R.string.field_qte));
					totalp.setText("0");
					spinnere.setText("");
					pup.setText("0");
					produit = new Produit();

					dialog.show();
				}else{
					showMsgPDF(1);
				}

			}
		});


		//invo.setEnabled(false);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(panier.size() != 0){
					
					AlertDialog.Builder localBuilder = new AlertDialog.Builder(CmdEditActivity.this);
					localBuilder
					.setTitle(getResources().getString(R.string.cmdtofc10))
					.setMessage(getResources().getString(R.string.cmdtofc41))
					.setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.btn_save),
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							paramDialogInterface.dismiss();

							if(CheckOutNet.isNetworkConnected(CmdEditActivity.this)){
								dialogload = ProgressDialog.show(CmdEditActivity.this, getResources().getString(R.string.map_data),
										getResources().getString(R.string.msg_wait), true);
								new UpdateTask().execute();
							}else{
								dialogload = ProgressDialog.show(CmdEditActivity.this, getResources().getString(R.string.map_data),
										getResources().getString(R.string.msg_wait), true);
								new UpdateOfflineTask().execute();
							}
						}
					})
					.setNegativeButton(getResources().getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							paramDialogInterface.dismiss();
							
						}
					});
					localBuilder.setCancelable(false);
					localBuilder.show();
					
					
				}else{
					Toast.makeText(CmdEditActivity.this, getResources().getString(R.string.produit_min2), Toast.LENGTH_LONG).show();
				}

			}	
		});


		mycmd = new HashMap<>();
		produit = new Produit();
		listprd = new ArrayList<>();
		panier=new HashMap<>();
		stock = new ArrayList<>();
		backup = new HashMap<>();
		
		dialogload = ProgressDialog.show(CmdEditActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		new PrepaModeTask().execute();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if(!myoffline.checkFolderexsiste()){
			showmessageOffline();
		}

		super.onStart();
	}





	public void showmessageOffline(){
		try {

			LayoutInflater inflater = this.getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.msgstorage, null);

			AlertDialog.Builder dialog =  new AlertDialog.Builder(CmdEditActivity.this);
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



	public SimpleAdapter getSimple(HashMap<Integer, Produit> in){
		// create the grid item mapping
		SimpleAdapter adap;
		String[] from = new String[] {"desig","qte","pu","rem"};
		int[] to = new int[] { R.id.item12, R.id.item42, R.id.item32 , R.id.item22};

		// prepare the list of all records
		List<HashMap<String, String>>  fillMaps2 = new ArrayList<HashMap<String, String>>();

		Log.e("getsimple ", in.toString()+"");
		List<Produit> data = new ArrayList<>(in.values());
		
		

		if(data.size() == 0){
			HashMap<String, String> map = new HashMap<String, String>();
			//map.put("name", "");
			map.put("desig",getResources().getString(R.string.facture_vide));
			map.put("qte", "");
			map.put("pu", "");
			map.put("rem", "");
			map.put("ref", "0");

			fillMaps2.add(map);
		}else{
			for (int j = 0; j < data.size(); j++) {
				HashMap<String, String> map = new HashMap<String, String>();
				Produit p = data.get(j);
				//map.put("name", p.getRef());
				map.put("ref", p.getId()+"");
				map.put("desig",p.getDesig());
				map.put("qte", p.getQtedemander()+"");
				map.put("pu", p.getPrixttc()+"");
				map.put("rem", "--");
				/*
				if(Double.parseDouble(p.getTva_tx()) == 100){
					map.put("rem", "Offert");
				}else if(Double.parseDouble(p.getTva_tx()) == 0){
					map.put("rem", "--");
				}else{
					map.put("rem", p.getTva_tx());
				}
				 */

				fillMaps2.add(map);
			}
		}

		// fill in the grid_item layout
		adap = new SimpleAdapter(this, fillMaps2, R.layout.grid_item2, from, to);
		return adap;
	}

	public SimpleAdapter getSimple_two(int nmb){
		// create the grid item mapping
		SimpleAdapter adap;
		String[] from = new String[] {"name", "desig","qte","pu"};
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item4 , R.id.item3};

		// prepare the list of all records
		List<HashMap<String, String>>  fillMaps2 = new ArrayList<HashMap<String, String>>();

		Log.e("nmb ",nmb +" >> ");

		if(nmb == 0){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "");
			map.put("name", "");
			map.put("desig",getResources().getString(R.string.facture_vide));
			map.put("qte", "");
			map.put("pu", "");

			fillMaps2.add(map);
			facture.setEnabled(false);
		}else{
			for (int j = 0; j < stock.size(); j++) {
				HashMap<String, String> map = new HashMap<String, String>();
				Produit p = stock.get(j);
				map.put("id", p.getId()+"");
				map.put("name", p.getRef());
				map.put("desig",p.getDesig());
				map.put("qte", p.getQtedemander()+"");
				map.put("pu", p.getPrixUnitaire());

				fillMaps2.add(map);
				facture.setEnabled(true);
			}
		}

		// fill in the grid_item layout
		adap = new SimpleAdapter(this, fillMaps2, R.layout.grid_item, from, to);
		return adap;
	}

	private void showMsgPDF(int n){

		AlertDialog.Builder localBuilder = new AlertDialog.Builder(CmdEditActivity.this);
		localBuilder
		.setTitle(getResources().getString(R.string.cmdtofc10))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.cmdtofc11),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				paramDialogInterface.dismiss();

				Intent intent1 = new Intent(CmdEditActivity.this, CmdViewActivity.class);
				intent1.putExtra("user", compte);
				intent1.putExtra("editcmd", "1");
				intent1.putExtra("cmd", "1");
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent1);
			}
		});

		if(n == 0){
			localBuilder.setMessage(getResources().getString(R.string.produit_min2));
		}else if(n == 3){
			localBuilder.setMessage(getResources().getString(R.string.cmdtofc5));
		}else{
			localBuilder.setMessage(getResources().getString(R.string.cmdtofc7));
		}



		localBuilder.show();
	}



	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}

	class PrepaModeTask extends AsyncTask<Void, Void, String> {


		@Override
		protected String doInBackground(Void... params) {


			listprd = new ArrayList<>();
			listprd.add(getResources().getString(R.string.product_spinner));

			if(v != null){
				panier = new HashMap<>();

				for (int i = 0; i < v.getLsprods().size(); i++) {
					v.getLsprods().get(i).setQtedemander( v.getLsprods().get(i).getQteDispo());
					v.getLsprods().get(i).setQteDispo(0);
					panier.put(v.getLsprods().get(i).getId(), v.getLsprods().get(i));
				}
				Log.e("my vc ",panier.toString() +"  ");
				myoffline = new Offlineimpl(CmdEditActivity.this);
				stock = new ArrayList<>();
				stock = myoffline.LoadProduits("");

				for (int i = 0; i < stock.size(); i++) {
					listprd.add(stock.get(i).getDesig());
				}

				calcul_ca = v.getTtc();
			}


			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialogload.isShowing()){
					dialogload.dismiss();


					lc.setVisibility(View.VISIBLE);
					adapter = getSimple(panier);
					adapter.notifyDataSetChanged();
					myhome.setAdapter(adapter);	

					ttc.setText("Total TTC : "+v.getTtc());

					addnew.setEnabled(true);
					save.setEnabled(true);
				}
				Log.e("end ","cnx");

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}

	public void addElementToSpinner(){
		//Spinner spinnere = (Spinner) dialog.findViewById(R.id.produitspinnerdialog);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listprd);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnere.setAdapter(dataAdapter);

		//spinnere.setOnItemSelectedListener(this);
	}

	public void alert(final Button ajouterproduit,Produit prdouit){
		AlertDialog.Builder alert = new AlertDialog.Builder(CmdEditActivity.this);


		int d = produit.getQteDispo() - qnt_disponible(produit.getId());


		alert.setTitle(getResources().getString(R.string.stock_limit));
		alert.setMessage(
				String.format(
						getResources().getString(R.string.stock_msg),
						d));
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ajouterproduit.setEnabled(false);
				qtep.setText("0");
				//	pup.setText("0");
				totalp.setText("0");


				return;
			}
		});
		alert.create().show();
	}

	private int qnt_disponible(int id){
		int x =0;
		/*
		for (int i = 0; i < stock.size(); i++) {
			if(stock.get(i).getId() == id){
				x += stock.get(i).getQtedemander();
			}
		}
		*/
		
		
		
		if(backup.containsKey(id)){
			x = backup.get(id).getQtedemander();
		}
		return x;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		final HashMap<String, String> map = (HashMap<String, String>) myhome.getItemAtPosition(position);

		Log.e("map select ",map.toString());
		AlertDialog.Builder adb = new AlertDialog.Builder(CmdEditActivity.this);

		//on attribut un titre � notre boite de dialogue
		adb.setTitle(getResources().getString(R.string.facture_action)); 

		//on insére un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
		if(map.size() == 0){
			adb.setMessage(getResources().getString(R.string.facture_vide));
			adb.setNegativeButton(getResources().getString(R.string.btn_cancel),  new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					return;  
				} });  

			//on affiche la boite de dialogue

		}else{
			adb.setMessage(getResources().getString(R.string.facture_choice)+map.get("desig"));


			//on indique que l'on veut le bouton ok à notre boite de dialogue

			adb.setPositiveButton(getResources().getString(R.string.btn_delete), new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) { 
					Log.i(" >> List avant supprission", panier.toString());

					Double prttc = (double) 0;
					Double prht = (double) 0;

					List<Produit> toRemove = new ArrayList<>();

					Produit p_tmp = panier.get(Integer.parseInt(map.get("ref")));

					if(p_tmp != null){
						double r = p_tmp.getPrixttc()*p_tmp.getQtedemander();
						calcul_ca -= r; 

						panier.remove(Integer.parseInt(map.get("ref")));

						for (int i = 0; i < stock.size(); i++) {
							if(stock.get(i).getRef().equals(p_tmp.getRef())){
								stock.get(i).setQteDispo(stock.get(i).getQteDispo() + p_tmp.getQtedemander());
								break;
							}
						}
						
						
						
						backup.remove(Integer.parseInt(map.get("ref")));
					}


					adapter = getSimple(panier);
					adapter.notifyDataSetChanged();
					myhome.setAdapter(adapter);

					ttc.setText("Total TTC : "+calcul_ca);

					return;  
				} 
			});   
			adb.setNegativeButton(getResources().getString(R.string.btn_cancel),  new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					return;  
				} });  
		}


		//on affiche la boite de dialogue

		adb.show();
	}

	 
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				Intent intent = new Intent(this, CmdViewActivity.class);
				intent.putExtra("user", compte);
				intent.putExtra("editcmd", "1");
				intent.putExtra("cmd", "1");
				startActivity (intent);
				this.finish();
			}
			return true;
	}

	class UpdateTask extends AsyncTask<Void, Void, String> {


		String res = "";
		@Override
		protected String doInBackground(Void... params) {


			

			
			VendeurManager vendeurManager = VendeurManagerFactory.getClientManager();
			PayementManager payemn = PayementManagerFactory.getPayementFactory();
			CategorieDao categorie = new CategorieDaoMysql(getApplicationContext());
			CommercialManager managercom = CommercialManagerFactory.getCommercialManager();

			StockVirtual sv  = new StockVirtual(CmdEditActivity.this);

			myoffline = new Offlineimpl(CmdEditActivity.this);

			
			myoffline = new Offlineimpl(getApplicationContext());
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				myoffline.SendOutData(compte);
			}

			
			manager = new CommandeManagerFactory().getManager();

			res = manager.updateCommande(new ArrayList<>(panier.values()), v.getRowid()+"", compte);

			CheckOutSysc.checkInCommandeview(myoffline, CheckOutSysc.checkOutCommandes(manager, compte), compte);

			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialogload.isShowing()){
					dialogload.dismiss();

					String msg ="";
					switch (res) {
					case "0":
						msg = getResources().getString(R.string.cmdtofc40);
						
						
						
						if(backup.size() > 0){
							
							/*
							Myinvoice me = new Myinvoice("0", new ArrayList<>(backup.values()), "0", 0, "", compte, "", "", "", 0, new ArrayList<MyProdRemise>(), null, "", "", "");
							myoffline.updateProduits(me);
							*/
							
							
							List<Produit> lsprd_qnt = new ArrayList<>(backup.values());
							for (int i = 0; i < lsprd_qnt.size(); i++) {
								sv.addPdQtRow(lsprd_qnt.get(i).getId(), lsprd_qnt.get(i).getQtedemander());
							}
						}
						
						break;
					case "1":
						msg = getResources().getString(R.string.cmdtofc36);
						break;
					case "2":
						msg = getResources().getString(R.string.cmdtofc16);
						break;
					case "3":
						msg = getResources().getString(R.string.cmdtofc38);
						break;
					case "4":
						msg = getResources().getString(R.string.cmdtofc39);
						break;
					default:
						msg = getResources().getString(R.string.cmdtofc39);
						break;
					}
					
					new AlertDialog.Builder(CmdEditActivity.this)
					.setTitle(getResources().getString(R.string.cmdtofc10))
					.setMessage(msg)
					.setNegativeButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface d, int arg1) {
							//VendeurActivity.super.onBackPressed();
							d.dismiss();
							Intent intent1 = new Intent(CmdEditActivity.this, CmdViewActivity.class); //CatalogeActivity.class  //VendeurActivity
							intent1.putExtra("user", compte);
							intent1.putExtra("editcmd", "1");
							intent1.putExtra("cmd", "1");
							startActivity(intent1);
							CmdEditActivity.this.finish();
						}

					})
					.setCancelable(false)
					.create().show();

				}
				Log.e("end ","cnx");

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}

	class UpdateOfflineTask extends AsyncTask<Void, Void, String> {

		private long res;

		@Override
		protected String doInBackground(Void... params) {

			
			myoffline = new Offlineimpl(getApplicationContext());
			sv = new StockVirtual(CmdEditActivity.this);
			
			res = myoffline.shynchornizeUpdateCmd(new Commandeview(v.getRowid(), v.getRowid()+"", null, 0, 0, 0, "", new ArrayList<>(panier.values()),0));
		 
			/*
			myoffline = new Offlineimpl(getApplicationContext());
			Myinvoice me = new Myinvoice("0", new ArrayList<>(backup.values()), "0", 0, "", compte, "", "", "", 0,  new ArrayList<MyProdRemise>(), null, "", "", "");
			Log.e("meinvo ",me.toString());
			*/
			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialogload.isShowing()){
					dialogload.dismiss();

					String msg ="";

					if(res == 1){
						msg = getResources().getString(R.string.cmdtofc40);
						if(backup.size() > 0){
							
							/*
							myoffline = new Offlineimpl(getApplicationContext());
							Myinvoice me = new Myinvoice("0", new ArrayList<>(backup.values()), "0", 0, "", compte, "", "", "", 0,  new ArrayList<MyProdRemise>(), null, "", "", "");
							
							myoffline.updateProduits(me);
							*/
							
							List<Produit> lsprd_qnt = new ArrayList<>(backup.values());
							for (int i = 0; i < lsprd_qnt.size(); i++) {
								sv.addPdQtRow(lsprd_qnt.get(i).getId(), lsprd_qnt.get(i).getQtedemander());
							}
						}
					}else{
						msg = getResources().getString(R.string.cmdtofc39);
					}
					
					new AlertDialog.Builder(CmdEditActivity.this)
					.setTitle(getResources().getString(R.string.cmdtofc10))
					.setMessage(msg)
					.setNegativeButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface d, int arg1) {
							//VendeurActivity.super.onBackPressed();
							d.dismiss();
							Intent intent1 = new Intent(CmdEditActivity.this, CmdViewActivity.class); //CatalogeActivity.class  //VendeurActivity
							intent1.putExtra("user", compte);
							intent1.putExtra("editcmd", "1");
							intent1.putExtra("cmd", "1");
							startActivity(intent1);
							CmdEditActivity.this.finish();
						}

					})
					.setCancelable(false)
					.create().show();

					
				}
				Log.e("end ","cnx");

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}
	
	private void addBackup(Produit p,int qt){
		
		p.setQtedemander(qt);
		int id = p.getId();
		//Log.e("start backup ",backup.toString()+"");
		if(backup.size() == 0){
			backup.put(p.getId(), p);
		}else{
			Produit ps = backup.get(id);
			
			if(ps != null){
				ps.setQtedemander(ps.getQtedemander() + p.getQtedemander());
				//Log.e("ps in backup ",ps.toString()+"");
				
				backup.remove(id);
				
				backup.put(id, new Produit(ps.getRef(), ps.getDesig(), ps.getQteDispo(), ps.getPrixUnitaire(), ps.getQtedemander(), ps.getPrixttc(), ps.getTva_tx(), ps.getFk_tva(),ps.getId()));
			}else{
				backup.put(p.getId(), p);
			}
		}
		
		//Log.e("backup ",backup.toString()+"");
	}

}
