package com.dolibarrmaroc.mypocketstandard.logger;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.dolibarrmaroc.mypocketstandard.ConnexionActivity;
import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.DBHandler;
import com.dolibarrmaroc.mypocketstandard.gps.ShowLocationActivity;
import com.dolibarrmaroc.mypocketstandard.gps.TrackingActivity;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyDebug;
import com.dolibarrmaroc.mypocketstandard.models.Services;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.synchronisation.SettingsynchroActivity;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LogerrorsActivity extends Activity {

	private Compte compte;
	private LinearLayout l1;
	private List<CheckBox> ch;
	private List<String> chosed;
	private Button send;

	
	private WakeLock wakelock;
	private ProgressDialog dialog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logerrors);

		l1 = (LinearLayout)findViewById(R.id.homs);

		ch = new ArrayList<>();
		chosed = new ArrayList<>();
		
		send = (Button)findViewById(R.id.sendlog);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chosed.clear();
				for (int i = 0; i < ch.size(); i++) {
					if(ch.get(i).isChecked()){
						chosed.add(ch.get(i).getText().toString());
					}
				}
				if(chosed.size() == 0 || !CheckOutNet.isNetworkConnected(LogerrorsActivity.this)){
					showException();
				}else{
					startUpload(0);
				}
				
			}
		});
		
		
		

		remlireLog();
		
		
	}
	
	private void remlireLog(){
		try {
			LayoutParams lparams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			File file = new File(Environment.getExternalStorageDirectory()+com.dolibarrmaroc.mypocketstandard.utils.URL.path+"/"+com.dolibarrmaroc.mypocketstandard.utils.URL.path_log);
		    ArrayList<Uri> pngUri = new ArrayList<>();
			if(file.exists()){
				 
				for (File fx: file.listFiles()) {
					final CheckBox c = new CheckBox(LogerrorsActivity.this); 
					c.setLayoutParams(lparams); 
					c.setText(fx.getName());

					ch.add(c);
					l1.addView(c);
			    }
			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private String sendFile(String fl,String in1,String in2){
		/*
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE); 
	    emailIntent.setType("text/plain");
	    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] 
	    {"mouhcine.elgarej@gmail.com"}); 
	    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
	    "Test Subject"); 
	    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
	    "go on read the emails"); 
	    */
	    File file = new File(Environment.getExternalStorageDirectory()+com.dolibarrmaroc.mypocketstandard.utils.URL.path+"/"+com.dolibarrmaroc.mypocketstandard.utils.URL.path_log+"/"+fl);
	    return MyDebug.uploadFile(file, in1, in2);
	    /*
	    ArrayList<Uri> pngUri = new ArrayList<>();
		if(file.exists()){
			 
			for (File fx: file.listFiles()) {
				Log.e("fx ",fx.getAbsolutePath());
				 pngUri.add(Uri.fromFile(fx));
				 MyDebug.uploadFile(fx, "", "");
		    }
		}else{
			Log.e("file ","out");
		}
		
		Log.e("file ",pngUri.toString());
         
	    //Log.v(getClass().getSimpleName(), "sPhotoUri=" + Uri.parse("file:/"+ sPhotoFileName));
	    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, pngUri);
	    //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	    */
	}


	class ConnexionTask extends AsyncTask<String, Void, String> {

		List<String> out = new ArrayList<>();
		@Override
		protected String doInBackground(String... params) {

			try {
				
				String x = "ko";
				String in1 = "",in2 = "";
				
				
				Log.e("params ssz",params.length+"");
				String[] ins = params;
				for (int i = 0; i < ins.length; i++) {
					Log.e("params ",ins[i].toString());
					in1 = ins[i].toString().split("//")[0];
					in2 = ins[i].toString().split("//")[1];
				}
				
				for (int i = 0; i < chosed.size(); i++) {

					x = sendFile(chosed.get(i),in1,in2);
					if(!x.equals("ok")){
						out.add(chosed.get(i));
					}
				}
				

			} catch (Exception e) {
				// TODO: handle exception
			}


			/*
				if("technicien".equals(compte.getProfile())){
					service = auth.getService(log, pass);
				}
			 */

			return null;
		}

		protected void onPostExecute(String sResponse) {
			try {
				if (dialog2.isShowing()){
					dialog2.dismiss();

					 if(out.size() > 0){
						 showResponse(out);
					 }
					

				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","error "+e.getMessage());
			}
		}

	}
	
	public void startUpload(int n){
		try {
			Builder dialog = new AlertDialog.Builder(LogerrorsActivity.this);
			dialog.setMessage(getResources().getString(R.string.log3));
			dialog.setTitle(getResources().getString(R.string.log5));
			
			LayoutInflater inflater = this.getLayoutInflater();
			final View dialogView = inflater.inflate(R.layout.inputlogs, null);
	         
	         dialog.setView(dialogView);
	         dialog.setPositiveButton(getResources().getString(R.string.tecv37), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					EditText txt = (EditText)dialogView.findViewById(R.id.inplog1);
					EditText txt2 = (EditText)dialogView.findViewById(R.id.inplog2);
					if(txt != null && txt2 != null){
						if(!txt.equals("") && !txt2.equals("")){
							dialog2 = ProgressDialog.show(LogerrorsActivity.this, getResources().getString(R.string.caus15),
									getResources().getString(R.string.msg_wait_sys), true);
							new ConnexionTask().execute(txt.getText().toString()+"//"+txt2.getText().toString());
						}else{
							Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires", Toast.LENGTH_LONG).show();
						}
					}
				}
			});
	         dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dialog.cancel();
				}
			});
	         dialog.setCancelable(true);
	         dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("erreur",e.getMessage() +" << ");
		}
	}
	
	private void showResponse(List<String> out){
		Builder dialog = new AlertDialog.Builder(LogerrorsActivity.this);
		
		dialog.setTitle(getResources().getString(R.string.cmdtofc10));
		
		String msg = getResources().getString(R.string.log4)+"\n";
		for (int i = 0; i < out.size(); i++) {
			msg += out.get(i)+" \n";
		}
		
		msg += getResources().getString(R.string.log6);
		
		Log.e("msg ",msg);
		dialog.setMessage(msg);
         
         dialog.setPositiveButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 dialog.dismiss();
			}
		});
         dialog.setCancelable(false);
         dialog.show();
	}
	
	private void showException(){
		Builder dialog = new AlertDialog.Builder(LogerrorsActivity.this);
		
		dialog.setTitle(getResources().getString(R.string.cmdtofc10));
		
		String msg = "Veuillez selectionner au moin un fichier log \n";
		msg += "Vérifier votre connexion Internet";
		 
		dialog.setMessage(msg);
         
         dialog.setPositiveButton(getResources().getString(R.string.cmdtofc11), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 dialog.dismiss();
			}
		});
         dialog.setCancelable(false);
         dialog.show();
	}
	
	public void onClickHome(View v){
		Intent intent = new Intent(LogerrorsActivity.this, ConnexionActivity.class);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
}
