package com.dolibarrmaroc.mypocketstandard.models;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.utils.URL;
import com.google.gson.Gson;

public class MyDebug implements Serializable{

	private static String path = Environment.getExternalStorageDirectory()+URL.path+"/"+URL.path_log;//path
	private static File file;
	private static Gson gson;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");

	private String classname;
	private String methodname;
	private String inputparams;
	private String message;
	public MyDebug() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyDebug(String classname, String methodname, String inputparams,
			String message) {
		super();
		this.classname = classname;
		this.methodname = methodname;
		this.inputparams = inputparams;
		this.message = message;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getInputparams() {
		return inputparams;
	}
	public void setInputparams(String inputparams) {
		this.inputparams = inputparams;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MyDebug [classname=" + classname + ", methodname=" + methodname
				+ ", inputparams=" + inputparams + ", message=" + message + "]";
	}

	public static void WriteLog(String classname, String methodname, String inputparams,
			String message){
		try {
			file = new File(path);

			file.setWritable(true);
			file.setReadable(true);
			if(!file.exists()){
				Log.e("Loger root folder","mkdirs "+file.mkdirs());
			}


			file = new File(path, "/logger_"+sdf.format(new Date())+".log");

			if(!file.exists()){
				file.createNewFile();
				file.mkdir();
			}

			if(file.exists()){
				FileWriter fw = new FileWriter(file, true);
				PrintWriter pout = new PrintWriter(fw);
				pout.println("************** Begin Error In "+new Date()+" ******************");
				pout.println("Class : "+classname);
				pout.println("Methode : "+methodname);
				pout.println("Parameters : "+inputparams);
				pout.println("Messages : "+message);
				pout.println("************* End Error In "+new Date()+" *********************");
				pout.close();
			}



		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void CleanLog(String in){
		try {
			file = new File(path+"/"+in);

			if(file.exists()){

				file.delete();
				/*
				for (File fx: file.listFiles()) {
					fx.delete();
				}
				*/
			}




		} catch (Exception e) {
			// TODO: handle exception
			Log.e("eroor in delete log ",e.getMessage()+"");
		}
	}

	public static String uploadFile(File fl,String in1, String in2) {

		/*
		  InputStream inputStream;
		  try {
		    inputStream = new FileInputStream(fl);//new File(filePath)
		    byte[] data;
		    try {
		      data = IOUtils.toByteArray(inputStream);

		      HttpClient httpClient = new DefaultHttpClient();
		      HttpPost httpPost = new HttpPost("http://41.142.241.192:89/dislach_new/doliDroid/upload_cicin.php");


		      InputStreamBody inputStreamBody = new InputStreamBody(new ByteArrayInputStream(data), fileName);
		      MultipartEntity multipartEntity = new MultipartEntity();
		      multipartEntity.addPart("file", inputStreamBody);
		      httpPost.setEntity(multipartEntity);

		      HttpResponse httpResponse = httpClient.execute(httpPost);

		      // Handle response back from script.
		      if(httpResponse != null) {

		      } else { // Error, no response.

		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  } catch (Exception e1) {
		    e1.printStackTrace();
		  }
		 */
		String res = "ok";
		try {

			// the URL where the file will be posted
			String postReceiverUrl = URL.URL_Log;//"http://41.142.241.192:89/dislach_new/doliDroid/upload_logger.php";
			// new HttpClient
			HttpClient httpClient = new DefaultHttpClient();

			// post header
			HttpPost httpPost = new HttpPost(postReceiverUrl);

			FileBody fileBody = new FileBody(fl);

			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("file", fileBody);
			
			reqEntity.addPart("username", new StringBody(in1));
			reqEntity.addPart("password", new StringBody(in2));
			httpPost.setEntity(reqEntity);
			
			
			

			// execute HTTP post request
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {

				res = EntityUtils.toString(resEntity).trim();

				// you can add an if statement here and do other actions based on the response
			}
		} catch (Exception e) {
			// TODO: handle exception
			res = "ko";
		}
		
		if(res.equals("ok")){
			MyDebug.CleanLog(fl.getName());
		}

		return res;
	}
	
	public static String uploadFileImei(File fl,String in1) {

		String res = "ok";
		try {

			// the URL where the file will be posted
			String postReceiverUrl = URL.URL_Log;//"http://41.142.241.192:89/dislach_new/doliDroid/upload_logger.php";
			// new HttpClient
			HttpClient httpClient = new DefaultHttpClient();

			// post header
			HttpPost httpPost = new HttpPost(postReceiverUrl);

			FileBody fileBody = new FileBody(fl);

			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("file", fileBody);
			
			reqEntity.addPart("username", new StringBody(in1));
			reqEntity.addPart("password", new StringBody(in1));
			httpPost.setEntity(reqEntity);
			
			
			

			// execute HTTP post request
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {

				res = EntityUtils.toString(resEntity).trim();

				// you can add an if statement here and do other actions based on the response
			}
			
			if(res.equals("ok")){
				MyDebug.CleanLog(fl.getName());
			}

		} catch (Exception e) {
			// TODO: handle exception
			res = "ko";
		}

		return res;
	}


}
