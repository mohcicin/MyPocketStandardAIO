
package com.dolibarrmaroc.mypocketstandard.dashboard;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dolibarrmaroc.mypocketstandard.AboutActivity;
import com.dolibarrmaroc.mypocketstandard.ConnexionActivity;
import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.business.AuthentificationManager;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.commercial.OfflineActivity;
import com.dolibarrmaroc.mypocketstandard.commercial.PayementActivity;
import com.dolibarrmaroc.mypocketstandard.commercial.ReglementOfflineActivity;
import com.dolibarrmaroc.mypocketstandard.commercial.VendeurActivity;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDao;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dao.DeniedDataDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDao;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDaoMysql;
import com.dolibarrmaroc.mypocketstandard.database.DBHandler;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.gps.ShowLocationActivity;
import com.dolibarrmaroc.mypocketstandard.intervention.InterventionhistoActivity;
import com.dolibarrmaroc.mypocketstandard.intervention.TechnicienActivity;
import com.dolibarrmaroc.mypocketstandard.maps.MainActivity;
import com.dolibarrmaroc.mypocketstandard.models.Categorie;
import com.dolibarrmaroc.mypocketstandard.models.CategorieCustomer;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Services;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CatalogeActivity;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdCacheActivity;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdEditActivity;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdViewActivity;
import com.dolibarrmaroc.mypocketstandard.stocks.TransfertstockActivity;
import com.dolibarrmaroc.mypocketstandard.stocks.TransfertvirtualstockActivity;
import com.dolibarrmaroc.mypocketstandard.synchronisation.SynchronisationHomeActivity;
import com.dolibarrmaroc.mypocketstandard.tiers.CommercialActivity;
import com.dolibarrmaroc.mypocketstandard.tiers.PersonnePhysiqueActivity;
import com.dolibarrmaroc.mypocketstandard.tiers.UpdateClientActivity;
import com.dolibarrmaroc.mypocketstandard.tour.MotifToureeActivity;
import com.dolibarrmaroc.mypocketstandard.tour.TourneeViewerActivity;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutSysc;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.ConnexionManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.Functions;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.URL;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;
import com.dolibarrmaroc.statistic.HomeStatisticActivity;
import com.dolibarrmaroc.statistic.StatistiqueActivity;
import com.dolibarrmaroc.statistic.VentesActivity;
import com.karouani.cicin.widget.alert.AlertDialogList;



/**
 * This is a simple activity that demonstrates the dashboard user interface pattern.
 *
 */

public class HomeActivity extends Activity 
{
	/* buttons events */
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;
	private Button btn9;
	private TextView username;

	/**
	 * onCreate - called when the activity is first created.
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 *
	 */  
	//Declaration Objet
	private VendeurManager vendeurManager;
	private StockVirtual sv;
	private List<Produit> products;
	private Compte compte;
	private ProgressDialog dialogSynchronisation;
	//synchro offline
	private Offlineimpl myoffline;
	private Dictionnaire dico;
	//Spinner Remplissage
	private List<String> listclt;
	private List<String> listprd;
	private List<Client> clients;

	private DBHandler mydb ;
	private WakeLock wakelock;
	private ProgressDialog dialog2;
	
	public HomeActivity() {
		// TODO Auto-generated constructor stub
		vendeurManager = VendeurManagerFactory.getClientManager();
		products = new ArrayList<Produit>();
		dico = new Dictionnaire();

		listclt = new ArrayList<String>();
		listprd = new ArrayList<String>();
		clients = new ArrayList<Client>();
	}

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


		 
		setContentView(R.layout.activity_home);
		username = (TextView)findViewById(R.id.usernameview);

		mydb = new DBHandler(this);

		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
			
			username.setText(compte.getLogin()+"");
		}

		Log.e("Compte User ",compte.toString());
		vendeurManager = VendeurManagerFactory.getClientManager();
		myoffline = new Offlineimpl(getApplicationContext());
		
		
		Log.e(">> cls ",myoffline.LoadClsCmdList("").toString());

		if(CheckOutNet.isNetworkConnected(getApplicationContext())){
			/*
			if(myoffline.checkAvailableofflinestorage() > 0){
				dialog2 = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.caus15),
						getResources().getString(R.string.msg_wait_sys), true);
				new ServerSideTask().execute();
			}
			 */
			synchronisation();

			//new ConnexionTask().execute();
		}
		
		/* init btn */
		btn1 = (Button)findViewById(R.id.home_btn_livraison);
		btn2 = (Button)findViewById(R.id.home_btn_tiers);
		btn3 = (Button)findViewById(R.id.home_btn_prise_cmd);
		btn4 = (Button)findViewById(R.id.home_btn_maps);
		btn5 = (Button)findViewById(R.id.home_btn_intervention);
		btn6 = (Button)findViewById(R.id.home_btn_stock);
		btn7 = (Button)findViewById(R.id.home_btn_statistque);
		btn8 = (Button)findViewById(R.id.home_btn_synchronisation);
		btn9 = (Button)findViewById(R.id.home_btn_logout);
		
		
		
		ShowMyHome();
		
		/*
		 * Check tournee
		 */
		
		sv  = new StockVirtual(HomeActivity.this);
		//sv.calculCAGraphMotifs("MTF");
		
		/*
		sv.addOperation_virtual("MTF", 1, 0);
		sv.addOperation_virtual("MTF", 4, 1);
		sv.addOperation_virtual("MTF", 3, 3);
		sv.addOperation_virtual("MTF", 1, 8);
		sv.addOperation_virtual("MTF", 2, 10);
		sv.addOperation_virtual("MTF", 5, 6);
		sv.addOperation_virtual("MTF", 2, 1);
		
		sv.addOperation_virtual("CMD", 145, 0);
		sv.addOperation_virtual("CMD", 45, 1);
		sv.addOperation_virtual("CMD", 345, 3);
		sv.addOperation_virtual("CMD", 17, 8);
		sv.addOperation_virtual("CMD", 28, 10);
		sv.addOperation_virtual("CMD", 54, 6);
		sv.addOperation_virtual("CMD", 72, 1);
		
		sv.addOperation_virtual("FC", 145, 0);
		sv.addOperation_virtual("FC", 45, 1);
		sv.addOperation_virtual("FC", 345, 3);
		sv.addOperation_virtual("FC", 17, 8);
		sv.addOperation_virtual("FC", 28, 10);
		sv.addOperation_virtual("FC", 54, 6);
		sv.addOperation_virtual("FC", 72, 1);
		*/
		
	}

	/**
	 * onDestroy
	 * The final call you receive before your activity is destroyed. 
	 * This can happen either because the activity is finishing (someone called finish() on it, 
	 * or because the system is temporarily destroying this instance of the activity to save space. 
	 * You can distinguish between these two scenarios with the isFinishing() method.
	 *
	 */

	protected void onDestroy ()
	{
		super.onDestroy ();
	}

	/**
	 * onPause
	 * Called when the system is about to start resuming a previous activity. 
	 * This is typically used to commit unsaved changes to persistent data, stop animations 
	 * and other things that may be consuming CPU, etc. 
	 * Implementations of this method must be very quick because the next activity will not be resumed 
	 * until this method returns.
	 * Followed by either onResume() if the activity returns back to the front, 
	 * or onStop() if it becomes invisible to the user.
	 *
	 */

	protected void onPause ()
	{
		super.onPause ();
	}

	/**
	 * onRestart
	 * Called after your activity has been stopped, prior to it being started again.
	 * Always followed by onStart().
	 *
	 */

	protected void onRestart ()
	{
		super.onRestart ();
	}

	/**
	 * onResume
	 * Called when the activity will start interacting with the user. 
	 * At this point your activity is at the top of the activity stack, with user input going to it.
	 * Always followed by onPause().
	 *
	 */

	protected void onResume ()
	{
		super.onResume ();
	}

	/**
	 * onStart
	 * Called when the activity is becoming visible to the user.
	 * Followed by onResume() if the activity comes to the foreground, or onStop() if it becomes hidden.
	 *
	 */

	protected void onStart ()
	{
		super.onStart ();
		ShowMyHome();
	}

	/**
	 * onStop
	 * Called when the activity is no longer visible to the user
	 * because another activity has been resumed and is covering this one. 
	 * This may happen either because a new activity is being started, an existing one 
	 * is being brought in front of this one, or this one is being destroyed.
	 *
	 * Followed by either onRestart() if this activity is coming back to interact with the user, 
	 * or onDestroy() if this activity is going away.
	 */

	protected void onStop ()
	{
		super.onStop ();
	}

	/**
	 */
	public void onClickFeature(View v) {
		// TODO Auto-generated method stub

		
		myoffline = new Offlineimpl(HomeActivity.this);

		int id = v.getId ();
		switch (id) {
		case R.id.home_btn_logout :
			deconnecter(this).create().show();
			break;
		case R.id.home_btn_synchronisation :
			//startActivity (new Intent(getApplicationContext(), F1Activity.class));
			//PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			//wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
			//wakelock.acquire();

			Intent intentsys = new Intent(HomeActivity.this, SynchronisationHomeActivity.class);
			intentsys.putExtra("user", compte);
			startActivity(intentsys);

			break;
		case R.id.home_btn_statistque :
			//startActivity (new Intent(getApplicationContext(), F3Activity.class));StatistiqueActivity
			int st =0;
				List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alertfc2st = new ArrayList<>();
				Intent st1 = new Intent(getApplicationContext(), StatistiqueActivity.class); //CatalogeActivity.class  //CmdViewActivity
				st1.putExtra("user", compte);
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog createfc1st = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(st1, getString(R.string.title_activity_statistique), "invoice_see");
				alertfc2st.add(createfc1st);
				
				
				
				if("vendeur".equals(compte.getProfile().toLowerCase()) || compte.getFacture() != 0){
					Intent intentfc3 = new Intent(getApplicationContext(), VentesActivity.class);
					intentfc3.putExtra("user", compte);
					intentfc3.putExtra("val", "1");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc3 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc3, getString(R.string.stati9), "chartf");
					alertfc2st.add(updatefc3);
					st++;
				}
				
				//if(("PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase()) && compte.getPermissionbl() != 0) || (compte.getPermissionbl() != 0 && "vendeur".equals(compte.getProfile().toLowerCase()))){
				if("PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase()) || compte.getPermissionbl() != 0){
					
					
					
					Intent intentfc2 = new Intent(getApplicationContext(), VentesActivity.class);
					intentfc2.putExtra("user", compte);
					intentfc2.putExtra("val", "2");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc2 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc2, getString(R.string.stati8), "chartf");
					
					alertfc2st.add(updatefc2);
					st++;
					
				}
				
				if(compte.getIstour() != 0){
					Intent intentfc4 = new Intent(getApplicationContext(), VentesActivity.class);
					intentfc4.putExtra("user", compte);
					intentfc4.putExtra("val", "3");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc4 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc4, getString(R.string.stati3), "charts");
					alertfc2st.add(updatefc4);
					st++;
				}
				
				if(st == 0){
					alertPrdClt(getString(R.string.syscl9));
				}else{
					new AlertDialogList(HomeActivity.this, alertfc2st).show();
				}
			
			break;
		case R.id.home_btn_livraison :
			//startActivity (new Intent(getApplicationContext(), VendeurActivity.class));)
			if(compte.getFacture() != 0){
				//if(compte.getPermissionbl() == 0 || "vendeur".equals(compte.getProfile().toLowerCase())){
				if(compte.getPermissionbl() == 0 || "vendeur".equals(compte.getProfile().toLowerCase()) || compte.getFacture() == 1){
					List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alertfc2 = new ArrayList<>();
					Intent intentfc1 = new Intent(getApplicationContext(), VendeurActivity.class); //CatalogeActivity.class  //CmdViewActivity
					intentfc1.putExtra("user", compte);
					intentfc1.putExtra("cmd", "0");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog createfc1 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc1, getString(R.string.title_activity_vendeur), "invoice_see");


					Intent intentfc2 = new Intent(getApplicationContext(), PayementActivity.class);
					intentfc2.putExtra("user", compte);
					intentfc2.putExtra("dico", myoffline.LoadDeco("").getDico());
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc2 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc2, getString(R.string.title_activity_payement), "invoice");


					Intent intentfc3 = new Intent(getApplicationContext(), OfflineActivity.class);
					intentfc3.putExtra("user", compte);
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc3 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc3, getString(R.string.title_activity_offline), "invoice_lock");

					Intent intentfc4 = new Intent(getApplicationContext(), ReglementOfflineActivity.class);
					intentfc4.putExtra("user", compte);
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc4 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc4, getString(R.string.title_activity_reglement_offline), "invoice_pay");
					
					

					alertfc2.add(createfc1);
					alertfc2.add(updatefc2);
					alertfc2.add(updatefc3);
					alertfc2.add(updatefc4);
					new AlertDialogList(HomeActivity.this, alertfc2).show();

				}else{
					alertPrdClt(getString(R.string.syscl10));
				}
			}else{
				AlertMsg();
			}
			

			break;
		case R.id.home_btn_tiers :
			/*
			myoffline = new Offlineimpl(getApplicationContext());
			CommercialManager manager = CommercialManagerFactory.getCommercialManager();
			List<Societe> lsosc = new ArrayList<>();
			lsosc = CheckOutSysc.checkOutAllSociete(manager, compte);
			if(lsosc.size() > 0){
				CheckOutSysc.checkInSocietes(myoffline, lsosc, compte);
			}
			 */

			//startActivity (new Intent(getApplicationContext(), F5Activity.class));


			if(compte.getPermission() != 0 || "vendeur".equals(compte.getProfile().toLowerCase())){
				List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alerts = new ArrayList<>();
				Intent intentX = new Intent(getApplicationContext(), CommercialActivity.class); //CatalogeActivity.class  //CmdViewActivity
				intentX.putExtra("user", compte);
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog create = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentX, getString(R.string.comm_new_head), "user_yellow_add");

				Intent intentY = new Intent(getApplicationContext(), PersonnePhysiqueActivity.class);
				intentY.putExtra("user", compte);
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog update = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentY,  getString(R.string.comm_title_head), "user_yellow_edit");

				alerts.add(create);
				alerts.add(update);
				new AlertDialogList(HomeActivity.this, alerts).show();
			}else{
				alertPrdClt(getString(R.string.syscl7));
			}

			break;
		case R.id.home_btn_stock :
			int stock =0;

				
				List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alerts2 = new ArrayList<>();
				if("Administrateur magasinier".toLowerCase().equals(compte.getProfile().toLowerCase()) || compte.getExpedition() != 0){

					Intent intents1 = new Intent(getApplicationContext(), TransfertstockActivity.class); //CatalogeActivity.class  //CmdViewActivity
					intents1.putExtra("user", compte);
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog creates1 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intents1, getString(R.string.title_activity_transfertstock), "warehouse_worker");
					alerts2.add(creates1);
					stock++;
				}

				
				if(compte.getFacture() != 0 || "vendeur".equals(compte.getProfile().toLowerCase())){
					Intent intents2 = new Intent(getApplicationContext(), TransfertvirtualstockActivity.class);
					intents2.putExtra("user", compte);
					intents2.putExtra("cmd", "0");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updates2 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intents2, getString(R.string.title_activity_transfertvirtualstock), "warehouse_put");
					alerts2.add(updates2);
					stock++;
				}
				
				if(stock == 0){
					alertPrdClt(getString(R.string.mvm13));
				}else{
					new AlertDialogList(HomeActivity.this, alerts2).show();
				}

			break;
		case R.id.home_btn_prise_cmd : 

			List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alertc2 = new ArrayList<>();
			int n = 0;
			//if(("PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase()) && compte.getPermissionbl() != 0) || (compte.getPermissionbl() != 0 && "vendeur".equals(compte.getProfile().toLowerCase())) ){
			if(compte.getPermissionbl() != 0 || "PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase())){
				
				Intent intentc1 = new Intent(getApplicationContext(), CatalogeActivity.class); //CatalogeActivity.class  //CmdViewActivity
				intentc1.putExtra("user", compte);
				intentc1.putExtra("cmd", "1");
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog createc1 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentc1, getString(R.string.title_activity_cataloge), "buy");

				Intent intentc2 = new Intent(getApplicationContext(), CmdViewActivity.class);
				intentc2.putExtra("user", compte);
				intentc2.putExtra("cmd", "1");
				intentc2.putExtra("editcmd", "0");
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatec2 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentc2, getString(R.string.title_activity_cmd_view), "catalog");
				
				Intent intentc3 = new Intent(getApplicationContext(), CmdCacheActivity.class);
				intentc3.putExtra("user", compte);
				intentc3.putExtra("cmd", "1");
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatec3 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentc3, getString(R.string.title_activity_cmd_cache), "catalog");
				
				Intent intentc4 = new Intent(getApplicationContext(), CmdViewActivity.class);
				intentc4.putExtra("user", compte);
				intentc4.putExtra("cmd", "1");
				intentc4.putExtra("editcmd", "1");
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatec4 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentc4, getString(R.string.edcmd3), "update_cmd");
				
				
				
				

				alertc2.add(createc1);
				alertc2.add(updatec2);
				alertc2.add(updatec3);
				alertc2.add(updatec4);
				n++;
				
			}
			
			if(compte.getIstour() != 0){

				Intent intentfc5 = new Intent(getApplicationContext(), TourneeViewerActivity.class);
				intentfc5.putExtra("user", compte);
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc5 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc5, getString(R.string.title_activity_tournee_viewer), "tour1");
				
				Intent intentfc6 = new Intent(getApplicationContext(), MotifToureeActivity.class);
				intentfc6.putExtra("user", compte);
				com.dolibarrmaroc.mypocketstandard.models.AlertDialog updatefc6 = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentfc6, getString(R.string.title_activity_motif_touree), "tour2");
				
				alertc2.add(updatefc5);
				alertc2.add(updatefc6);
				n++;
			}
			
			if(n == 0){
				alertPrdClt(getString(R.string.syscl8));
			}else{
				new AlertDialogList(HomeActivity.this, alertc2).show();
			}
			
			break;
		case R.id.home_btn_maps : 

			if(CheckOutNet.isNetworkConnected(HomeActivity.this)){
				
				List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alerts = new ArrayList<>();
				
				int map = 0;
				
				
				//if(("PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase()) && compte.getPermissionbl() != 0) || (compte.getPermissionbl() != 0 && "vendeur".equals(compte.getProfile().toLowerCase()))){
				if("PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase()) || compte.getPermissionbl() != 0){
					Intent intentZ = new Intent(getApplicationContext(), MainActivity.class);
					intentZ.putExtra("user", compte);
					intentZ.putExtra("type", "3");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog updateZ = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentZ,  getString(R.string.mapstitle3), "invoice_see");
					alerts.add(updateZ);
					map++;
				}
				
				
				if("technicien".equals(compte.getProfile().toLowerCase()) || compte.getIntervention() != 0){
					Intent intentw = new Intent(getApplicationContext(), MainActivity.class);
					intentw.putExtra("user", compte);
					intentw.putExtra("type", "4");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog bordereau = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentw,  "Bordereau", "invoice_see");
					alerts.add(bordereau);
					map++;
				}

				if("vendeur".equals(compte.getProfile().toLowerCase()) || compte.getFacture() != 0){

					Intent intentY = new Intent(getApplicationContext(), MainActivity.class);
					intentY.putExtra("user", compte);
					intentY.putExtra("type", "2");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog update = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentY,  getString(R.string.mapstitle1), "invoice_see");
					
					alerts.add(update);
					map++;
				}
				
				if("vendeur".equals(compte.getProfile().toLowerCase()) || compte.getFacture() != 0 || "PRE-VENDEURS".toLowerCase().equals(compte.getProfile().toLowerCase()) || compte.getPermissionbl() != 0){
					Intent intentX = new Intent(getApplicationContext(), MainActivity.class); //CatalogeActivity.class  //CmdViewActivity
					intentX.putExtra("user", compte);
					intentX.putExtra("type", "1");
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog create = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentX, getString(R.string.mapstitle2), "customers");

					alerts.add(create);
					map++;
				}
			
				
				if(map == 0){
					alertPrdClt(getString(R.string.syscl9));
				}else{
					new AlertDialogList(HomeActivity.this, alerts).show();
				}
				
				
				
				
			}else{
				alertmaps();
			}

			break;
		default: 
			if("technicien".equals(compte.getProfile().toLowerCase()) || compte.getIntervention() != 0){
				int e =0;
				String msg = "";
				
				Services service = myoffline.LoadServices("");
				if(myoffline.LoadClients("").size() == 0){
					//alertPrdClt(getString(R.string.caus27));
					msg += getString(R.string.caus27)+"\n";
					e++;
				}

				if(compte.getId_service() == -1){
					//alertPrdClt(getString(R.string.tecv53));
					msg += getString(R.string.tecv53)+"\n";
					e++;
				}

				if(service.getService() == null || "".equals(service.getService())){
					//alertPrdClt(getString(R.string.tecv60));
					msg += getString(R.string.tecv60)+"\n";
					e++;
				}

				if(e == 0){
					
					
					List<com.dolibarrmaroc.mypocketstandard.models.AlertDialog> alerts = new ArrayList<>();
					Intent intentX = new Intent(getApplicationContext(), TechnicienActivity.class); //CatalogeActivity.class  //CmdViewActivity
					intentX.putExtra("user", compte);
					intentX.putExtra("service", service.getService());
					intentX.putExtra("nmbService", service.getNmb_cmp()+"");
					for (int i = 0; i < service.getLabels().size(); i++) {
						intentX.putExtra("labels"+i, service.getLabels().get(i).getLabel());
					}
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog create = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentX, getString(R.string.tecv54), "tour1");

					Intent intentY = new Intent(getApplicationContext(), InterventionhistoActivity.class);
					intentY.putExtra("user", compte);
					com.dolibarrmaroc.mypocketstandard.models.AlertDialog update = new com.dolibarrmaroc.mypocketstandard.models.AlertDialog(intentY,  getString(R.string.tecv55), "catalog");
					
					alerts.add(create);
					alerts.add(update);
					
					new AlertDialogList(HomeActivity.this, alerts).show();
				}else{
					alertPrdClt(msg);
				}
			}else{
				alertPrdClt(getString(R.string.syscl12));
			}
			
			break;
		}
	}

	/**
	 */
	// More Methods
	/****************************************************
	 * Methode de synchronisation
	 */

	public void synchronisation() {
		if(CheckOutNet.isNetworkConnected(getApplicationContext())){

			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			myoffline = new Offlineimpl(getApplicationContext());
			if(myoffline.checkAvailableofflinestorage() > 0){
				dialogSynchronisation = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.caus15),
						getResources().getString(R.string.msg_wait_sys), true);
				new ServerSideTask().execute();
			}else{
				/*	
				sv  = new StockVirtual(HomeActivity.this);
					if(sv.getSyc() == 1){

						dialogSynchronisation = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.map_data),
								getResources().getString(R.string.msg_wait), true);
						new ConnexionTask().execute();	

					}
				 */
				dialogSynchronisation = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.map_data),
						getResources().getString(R.string.msg_wait), true);
				new ConnexionTask().execute();	
			}

			//new ConnexionTask().execute();
		}else{
			//Alert No network
		}
	}

	class ServerSideTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			try {
				myoffline = new Offlineimpl(getApplicationContext());
				if(CheckOutNet.isNetworkConnected(getApplicationContext())){
					
					if(compte.getProfile().toLowerCase().equals("technicien")){
						myoffline.sendOutIntervention(compte);
					}
					
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
				if (dialogSynchronisation.isShowing()){
					dialogSynchronisation.dismiss();

					dialogSynchronisation = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.map_data),
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

	class ConnexionTask extends AsyncTask<Void, Void, String> {


		int nclt;
		int nprod;

		@Override
		protected String doInBackground(Void... params){

			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			

			Log.e("Compte >> User ",compte.toString());
			sv  = new StockVirtual(HomeActivity.this);
			vendeurManager = VendeurManagerFactory.getClientManager();
			myoffline = new Offlineimpl(HomeActivity.this);
			CommandeManager managercmd =  new CommandeManagerFactory().getManager();
			CommercialManager manager = CommercialManagerFactory.getCommercialManager();
			PayementManager payemn = PayementManagerFactory.getPayementFactory();
			CategorieDao categorie = new CategorieDaoMysql(getApplicationContext());
			AuthentificationManager auth = ConnexionManagerFactory.getCConnexionManager(); 

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
		
			
			try {
				
				if(!myoffline.checkFolderexsiste()){
					showmessageOffline();
				}else{

					int vsv = sv.getSyc();
					Log.e("is alreadey sysc ",vsv+" ## "+compte.getProfile());
					if(vsv == 1){
						if(CheckOutNet.isNetworkConnected(HomeActivity.this)){
							HashMap<String, Integer> res = new HashMap<>();
							
							
							
							
							if(compte.getProfile().toLowerCase().equals("vendeur") || compte.getFacture() != 0){
								res = CheckOutSysc.ReloadProdClt(HomeActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 0,manager);

								CheckOutSysc.RelaodClientSectInfoCommDicto(HomeActivity.this, myoffline, compte, vendeurManager, manager, 2);  //, 0);

								/*
								TourneeDao tour = new TourneeDaoMysql();
								List<Tournee> lstr = tour.consulterMesTournee(compte, sdf.format(new Date()));//"2015-09-11"
								myoffline.shynchornizeTournee(lstr);
								*/
								sv.deletePdQt("");
								
								
								nprod = res.get("prod");
								nclt = res.get("clt");
							}

							if(compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase()) || compte.getPermissionbl() != 0){
								res = CheckOutSysc.ReloadProdClt(HomeActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 3,manager);
								nclt = res.get("clt");
								
								res = CheckOutSysc.ReloadProdClt(HomeActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 4,manager);
								nprod = res.get("prod");
								
								CheckOutSysc.RelaodClientSectInfoCommDicto(HomeActivity.this, myoffline, compte, vendeurManager, manager, 1);
								
								sv.deletePdQt("");
								
							}

							

							if(compte.getProfile().toLowerCase().equals("technicien".toLowerCase()) || compte.getIntervention() != 0){
								nprod = 1;
								if(compte.getId_service() != -1){
									CheckOutSysc.checkInServices(myoffline, CheckOutSysc.checkOutServices(auth, compte), compte);
									CheckOutSysc.ReloadProdClt(HomeActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 3,manager);
								}
							}
							
							nclt = myoffline.LoadClients("").size() +  myoffline.LoadClients("").size();
							nprod = myoffline.LoadCategorieList("").size() + myoffline.LoadProduits("").size();
							
							if((compte.getProfile().toLowerCase().equals("Administrateur magasinier".toLowerCase()) || compte.getExpedition() != 0) && (!compte.getProfile().toLowerCase().equals("technicien".toLowerCase()) && compte.getIntervention() == 0) && (!compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase()) && compte.getPermissionbl() == 0) && (!compte.getProfile().toLowerCase().equals("vendeur") && compte.getFacture() == 0)){
								nclt = 1;
								nprod = 1;
							} else if((compte.getProfile().toLowerCase().equals("technicien".toLowerCase()) || compte.getIntervention() != 0) && (!compte.getProfile().toLowerCase().equals("Administrateur magasinier".toLowerCase()) || compte.getExpedition() == 0) && (!compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase()) && compte.getPermissionbl() == 0) && (!compte.getProfile().toLowerCase().equals("vendeur") && compte.getFacture() == 0)){
								nprod = 1;
							}
						
						}
					}else{
						
						if(compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase()) || compte.getPermissionbl() != 0){
							nclt = myoffline.LoadClients("").size();
							nprod = myoffline.LoadCategorieList("").size();
							Log.e("in prof ","pre_vendeur");
						}

						if(compte.getProfile().toLowerCase().equals("vendeur") || compte.getFacture() != 0){
							nclt = myoffline.LoadClients("").size();
							nprod = myoffline.LoadProduits("").size();
							Log.e("in prof ","vendeur");
						}

						if(compte.getProfile().toLowerCase().equals("Administrateur magasinier".toLowerCase()) || compte.getExpedition() != 0){
							nclt = 1;
							nprod = 1;
						}

						if(compte.getProfile().toLowerCase().equals("technicien".toLowerCase()) || compte.getIntervention() != 0){
							nclt = 1;
							nprod = 1;
						}
						
						nclt = myoffline.LoadClients("").size() +  myoffline.LoadClients("").size();
						nprod = myoffline.LoadCategorieList("").size() + myoffline.LoadProduits("").size();
						
						if((compte.getProfile().toLowerCase().equals("Administrateur magasinier".toLowerCase()) || compte.getExpedition() != 0) && (!compte.getProfile().toLowerCase().equals("technicien".toLowerCase()) && compte.getIntervention() == 0) && (!compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase()) && compte.getPermissionbl() == 0) && (!compte.getProfile().toLowerCase().equals("vendeur") && compte.getFacture() == 0)){
							nclt = 1;
							nprod = 1;
						} else if((compte.getProfile().toLowerCase().equals("technicien".toLowerCase()) || compte.getIntervention() != 0) && (!compte.getProfile().toLowerCase().equals("Administrateur magasinier".toLowerCase()) || compte.getExpedition() == 0) && (!compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase()) && compte.getPermissionbl() == 0) && (!compte.getProfile().toLowerCase().equals("vendeur") && compte.getFacture() == 0)){
							nprod = 1;
						}
					}
					
					if(CheckOutNet.isNetworkConnected(HomeActivity.this)){
						List<String> inres = new DeniedDataDaoMysql().sendMyErrorData(myoffline.LoadDenided("0"), compte,"facture");
						List<String> inres2 = new DeniedDataDaoMysql().sendMyErrorData(myoffline.LoadDenided("1"), compte,"commande");
						myoffline.CleanAllDeniededData();
						
						if(inres != null){
							for (int i = 0; i < inres.size(); i++) {
								myoffline.PutDeniededDataFw(inres.get(i), 0);
							}
						}
						if(inres2 != null){
							for (int i = 0; i < inres2.size(); i++) {
								myoffline.PutDeniededDataFw(inres2.get(i), 1);
							}
						}
					}
					
					if(CheckOutNet.isNetworkConnected(HomeActivity.this)){
						List<String> inres = new DeniedDataDaoMysql().sendMyErrorData(myoffline.LoadDeniededInterv(), compte,"intervention");
						myoffline.CleanDeniededInterv();
						
						if(inres != null){
							for (int i = 0; i < inres.size(); i++) {
								myoffline.PutDeniededInterv(null, inres.get(i));
							}
						}
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}

			

			Log.e("start ","start cnx task "+nclt+" c###p "+nprod);




			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialogSynchronisation.isShowing()){
					dialogSynchronisation.dismiss();
					String msg ="";
					//wakelock.release();

					if(nprod == 0){
						msg += getResources().getString(R.string.caus26)+"\n \n";
					}

					if(nclt == 0){
						msg += getResources().getString(R.string.caus27)+"\n \n";
					}

					msg += getResources().getString(R.string.caus28)+"\n";
					
					int k =0;
					if(nclt == 0 || nprod == 0 ){
						alertPrdClt(msg);
						k=-1;
					}

					if(k == 0) {
						if(nclt == 0 || nprod == 0 ){
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

	public void showmessageOffline(){
		try {

			LayoutInflater inflater = this.getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.msgstorage, null);

			AlertDialog.Builder dialog =  new AlertDialog.Builder(HomeActivity.this);
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

	public void alertPrdClt(String msg){
		AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
		alert.setTitle(getResources().getString(R.string.cmdtofc10));
		alert.setMessage(msg);
		alert.setNegativeButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			deconnecter(this).create().show();
			return true;
		}
		return false;
	}

	public void alertmaps(){
		AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
		alert.setTitle(getResources().getString(R.string.caus17));
		alert.setMessage(getResources().getString(R.string.caus18));
		alert.setNegativeButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}

	public void goHome(Context context) 
	{
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity (intent);
	}

	/**
	 * Use the activity label to set the text in the activity's title text view.
	 * The argument gives the name of the view.
	 *
	 * <p> This method is needed because we have a custom title bar rather than the default Android title bar.
	 * See the theme definitons in styles.xml.
	 * 
	 * @param textViewId int
	 * @return void
	 */

	public void setTitleFromActivityLabel (int textViewId)
	{
		TextView tv = (TextView) findViewById (textViewId);
		if (tv != null) tv.setText (getTitle ());
	} // end setTitleText

	/**
	 * Show a string on the screen via Toast.
	 * 
	 * @param msg String
	 * @return void
	 */

	public void toast (String msg)
	{
		Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	} // end toast

	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) 
	{
		Log.d("Demo", msg);
		toast (msg);
	}

	public AlertDialog.Builder deconnecter(final Activity context){
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(context.getResources().getString(R.string.btn_decon));
		alert.setMessage(context.getResources().getString(R.string.tecv47));
		alert.setNegativeButton(context.getResources().getString(R.string.description_logout), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//startActivity (new Intent(getApplicationContext(), F2Activity.class));
				TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String imei = tManager.getDeviceId();
				if(mydb.numberOfRows() > 0){
					Log.e(">>"+imei," >> "+mydb.numberOfRows());
					mydb.deleteUser(imei);
					//Log.e("All Compte",mydb.getAll().toString());
				}

				Intent intentService = new Intent(HomeActivity.this, ShowLocationActivity.class);
				stopService(intentService);


				Intent intent = new Intent(context,ConnexionActivity.class);
				startActivity(intent);
				context.finish();
				return;
			}
		});

		alert.setPositiveButton(context.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});
		alert.setCancelable(true);
		return alert;
	}

	private void ShowMyHome(){
		if(compte.getProfile().toLowerCase().equals("PRE-VENDEURS".toLowerCase())){
			btn1.setEnabled(false);
			btn2.setEnabled(false);
			btn4.setEnabled(false);
			btn5.setEnabled(false);
			btn6.setEnabled(false);
		}else if(compte.getProfile().toLowerCase().equals("Administrateur magasinier".toLowerCase())){
			btn1.setEnabled(false);
			btn2.setEnabled(false);
			btn3.setEnabled(false);
			btn4.setEnabled(false);
			btn5.setEnabled(false);
			btn8.setEnabled(false);
		}
	}
	
	public void onClickAbout (View v){
		Intent intent = new Intent(this, AboutActivity.class);
		intent.putExtra("user", compte);
		startActivity (intent);
	}
	
	public void AlertMsg(){
		AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
		alert.setTitle(getResources().getString(R.string.vendcat2));
		alert.setMessage(getResources().getString(R.string.vendcat3));
		alert.setNegativeButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				return;
			}
		});
		alert.setCancelable(false);
		alert.create().show();
	}
	
	
} // end class
