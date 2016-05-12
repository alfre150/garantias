package com.paquete.gia.android;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.paquete.si_rixio_alfredo.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
//import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
//import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Agregar_Reclamo extends Activity {

	
	EditText ledtNroGarantia;
    EditText ledtNroFactura;
    Spinner  lspnArtefacto;
    Spinner  lspnModelo;
    EditText ledtSerial;
    Spinner  lspnFalla;
    EditText ledtObservacion;
    
    
    Button btnRegistrarReclamo;
    private ProgressDialog pDialog;
    
    Httppostaux post;
    
    String IP_Server="www.sistemaswebs.com";//IP DE NUESTRO PC
    String URL_connect= "http://"+IP_Server+"/gia/InsertarReclamosMovil.php";//ruta en donde estan nuestros archivos

    String lcCodigosModelo,lcFallasModelo;
    
    	
   
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agregar_reclamo);
		
		post= new Httppostaux();
		
		ledtNroGarantia 		= (EditText) findViewById(R.id.edtNroGarantia_1);
		ledtNroFactura		    = (EditText) findViewById(R.id.edtNroFactura_1);
		lspnArtefacto   		= (Spinner)	 findViewById(R.id.spnArtefacto);
		lspnModelo 				= (Spinner)	 findViewById(R.id.spnModelo);
		ledtSerial          	= (EditText) findViewById(R.id.edtSerial);
		lspnFalla        		= (Spinner)  findViewById(R.id.spnFalla);
		ledtObservacion     	= (EditText) findViewById(R.id.edtObservacion);	
		
		btnRegistrarReclamo      = (Button) findViewById(R.id.btnRegistrarReclamo);
		
		Bundle bundle = getIntent().getExtras();
		String codigoJson = bundle.getString("json");
		
		final Spinner lspnArtefacto = (Spinner) findViewById(R.id.spnArtefacto);
		
			
		try{
			
			JSONObject json1 = new JSONObject(codigoJson);
			JSONArray arrayJson = json1.getJSONArray("Artefactos");
			ArrayList<String> estate = new ArrayList<String>();
			
			for (int i = 0;i<arrayJson.length();i++){
				JSONObject obj = arrayJson.getJSONObject(i);
				estate.add(obj.getString("Artefacto"));
			}
			
			ArrayAdapter <String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estate);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			lspnArtefacto.setAdapter(adapter);			
		}
		catch(JSONException excepcionJson) {
	       	 Log.e("log_tag", "Error parsing data "+excepcionJson.toString());	
	       }

  
		btnRegistrarReclamo.setOnClickListener(new View.OnClickListener(){
            
        	public void onClick(View view){
        		 
        		//Extraemos datos de los EditText
        		String lcNroGarantia	= ledtNroGarantia.getText().toString();
        		String lcNroFactura	 	= ledtNroFactura.getText().toString();
        		String lcArtefacto 	    = lspnArtefacto.getSelectedItem().toString();
        		String lcModelo     	= lspnModelo.getSelectedItem().toString();
        		String lcSerial       	= ledtSerial.getText().toString();
        		String lcFalla    		= lspnFalla.getSelectedItem().toString();
        		String lcObservacion 	= ledtObservacion.getText().toString();
        		
        		
        		//me los traigo solo para verificar vacios
        		String NroGarantia		= ledtNroGarantia.getText().toString();
        		String NroFactura   	= ledtNroFactura.getText().toString();
        		       		
        		//verificamos si estan en blanco
        		if( mChequearDatos(NroGarantia, NroFactura, lcArtefacto, lcModelo, lcSerial, lcFalla, lcObservacion )==true){

        			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
        			new asyncCliente().execute( lcNroGarantia, lcNroFactura, lcArtefacto, lcModelo, lcSerial, lcFalla, lcObservacion );        		               
        			        		
        		}else{
        			//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
        				mMostrarError();
        		}
        	//Esto se utiliza si necesita blanquear los datos luego de chequear	
        	//user.setText("");
        	//pass.setText("");
        		
        	}
        });
        /*Fin de asignacion de evento a boton */
		
		
		//spnArtefacto.setOnItemSelectedListener(oni)(l)		
		lspnArtefacto.setOnItemSelectedListener(new OnItemSelectedListener(){
		
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
			
			Toast.makeText(parentView.getContext(), parentView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
			//toast.ma
			//mObtenerModelo(parentView.getItemAtPosition(position).toString());
			new asyncModelo().execute(parentView.getItemAtPosition(position).toString());
			
			//new asyncFallas().execute(parentView.getItemAtPosition(position).toString());
			
			
		}
		public void onNothingSelected(AdapterView<?> parentView){
	
		}
		});	
/////////////////////////////////////////////////////////////////////////////////////////////	
	}
	
	
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////	
	public void cerrar (View view){
		finish();
	}
	/* Codigo para verificar que esten todos los datos */
	public boolean mChequearDatos( String NroGarantia, String NroFactura, String lcArtefacto, String lcModelo, String lcSerial,
		                        	String lcFalla, String lcObservacion)
    {
    	if 	(NroGarantia.equals("") || NroFactura.equals("") || lcArtefacto.equals("")|| lcModelo.equals("")|| 
    			lcSerial.equals("") || lcFalla.equals("")|| lcObservacion.equals(""))
    	{
    		Log.e("Validacion Fallida", "mChequearDatos: Faltan Parametros");
    		return false;
    	}
    	else
    	{
    	   	return true;
    	}
    	
    	/*if 	(lcClaveCliente.toString() != lcRepitaCliente.toString())
    	{
    		Log.e("Validacion Fallida", "mChequearDatos: Faltan Parametros");
    		return false;
    	}*/
    
    }   
	/* Fin de verificar todos los datos*/
	
	/* Codigo para colocar error cuando este erronea la verificacion*/
	public void mMostrarError()
    {
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error: Faltan Datos, Todos son obligatorios", Toast.LENGTH_LONG);
 	    toast1.show();    	
    }
	/* Fin de verificacion erronea*/

	public boolean mAgregarReclamoWeb(String lcNroGarantia, String lcNroFactura, String lcArtefacto, String lcModelo,
			String lcSerial, String lcFalla, String lcObservacion) {
    	
		int Respuesta =-1;
    	
    	ArrayList<NameValuePair> laParametros= new ArrayList<NameValuePair>();
     		
    	laParametros.add(new BasicNameValuePair("lcNroGarantia",lcNroGarantia));
    	laParametros.add(new BasicNameValuePair("lcNroFactura",lcNroFactura));
    	laParametros.add(new BasicNameValuePair("lcArtefacto",lcArtefacto));
    	laParametros.add(new BasicNameValuePair("lcModelo",lcModelo));
    	laParametros.add(new BasicNameValuePair("lcSerial",lcSerial));
    	laParametros.add(new BasicNameValuePair("lcFalla",lcFalla));
    	laParametros.add(new BasicNameValuePair("lcObservacion",lcObservacion));
    	    	
    	JSONArray jdata=post.getserverdata(laParametros, URL_connect);

      	//si lo que obtuvimos no es null
    	if (jdata!=null && jdata.length() > 0){

    		JSONObject json_data; //creamos un objeto JSON
			try {
				
				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				Respuesta=json_data.getInt("Respuesta");//accedemos al valor 
				 Log.e("Respuesta web","Respuesta= "+Respuesta);//muestro por log que obtuvimos
				 
			} catch (JSONException e) {
				e.printStackTrace();
			}		            
             
			//validamos el valor obtenido
    		 if (Respuesta==0){// [{"logstatus":"0"}] 
    			 Log.e("Respuesta ", "Agregado Satisfactoriamente");
    			 return false;
    		 }
    		 else{// [{"logstatus":"1"}]
    			 Log.e("Respuesta ", "No agrego");
    			 return true;
    		 }
    		 
	  }else{	//json obtenido invalido verificar parte WEB.
    			 Log.e("JSON  ", "ERROR");
	    		return false;
	  }
    	
    }
    /* fin verificacion mAgregarCliente en la bd web*/
	
	
	
	/*Clase asinc task para hacer las conexiones asincronas*/
	 class asyncCliente extends AsyncTask< String, String, String > {
   	 
    	String  lcNroGarantia, lcNroFactura, lcArtefacto, lcModelo, lcSerial, lcFalla, lcObservacion;		
    	
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Agregar_Reclamo.this);
            pDialog.setMessage("Registrando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			//user=params[0];
			//pass=params[1];
			lcNroGarantia =params[0];
			lcNroFactura=params[1];
			lcArtefacto=params[2];
			lcModelo=params[3];
			lcSerial=params[4];
			lcFalla=params[5];
			lcObservacion=params[6];
		    
            
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (mAgregarReclamoWeb(lcNroGarantia, lcNroFactura, lcArtefacto, lcModelo, lcSerial, lcFalla, lcObservacion)==true)
    		{    		    		
    			return "ok"; //login valido
    		}else{    		
    			return "err"; //login invalido     	          	  
    		}
        	
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("ok")){
        	   
        	   Toast toast1 = Toast.makeText(getApplicationContext(),"Su Reclamo sido Registrado", Toast.LENGTH_LONG);
        	    toast1.show(); 

        	   finish();
				
            }else{
            	mMostrarError();
            }
            
                									}
		
        }
	/* Fin Clase asinc task*/
	 
	 public void mLLenarModelo()
	 {
		final Spinner lspnModelo = (Spinner) findViewById(R.id.spnModelo);
			
		try{
			
			JSONObject json1 = new JSONObject(lcCodigosModelo);
			JSONArray arrayJson = json1.getJSONArray("Modelos");
			ArrayList<String> estate = new ArrayList<String>();
			
			for (int i = 0;i<arrayJson.length();i++){
				JSONObject obj = arrayJson.getJSONObject(i);
				estate.add(obj.getString("Modelo"));
			}
			
			ArrayAdapter <String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estate);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			lspnModelo.setAdapter(adapter);			
		}
		catch(JSONException excepcionJson) {
	       	 Log.e("log_tag", "Error parsing data "+excepcionJson.toString());	
	       }
		 
	 }
	 public void mLLenarFallas()
	 {
		final Spinner lspnFallas = (Spinner) findViewById(R.id.spnFalla);
			
		try{
			
			JSONObject json1 = new JSONObject(lcFallasModelo);
			JSONArray arrayJson = json1.getJSONArray("Fallas");
			ArrayList<String> estate = new ArrayList<String>();
			
			for (int i = 0;i<arrayJson.length();i++){
				JSONObject obj = arrayJson.getJSONObject(i);
				estate.add(obj.getString("Falla"));
			}
			
			ArrayAdapter <String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estate);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			lspnFallas.setAdapter(adapter);			
		}
		catch(JSONException excepcionJson) {
	       	 Log.e("log_tag", "Error parsing data "+excepcionJson.toString());	
	       }
		 
	 }


	 /*Clase asinc task para hacer las conexiones asincronas*/
	 class asyncModelo extends AsyncTask< String, String, String > {
   	 
    	String  lcArtefacto;		
    	
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Agregar_Reclamo.this);
            pDialog.setMessage("Actualizando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			
			lcArtefacto=params[0];
						
			lcCodigosModelo = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerModelos.php?lcModelo="+lcArtefacto);
			    		    		    		
			lcFallasModelo = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerFalla.php?Artefacto="+lcArtefacto);
    		
    		return "ok"; //login valido
    		
        	
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("ok")){
        	   
        	   mLLenarModelo();
        	   mLLenarFallas();
       				
            }else{
            	mMostrarError();
            }
            
                									}
		
        }
	/* Fin Clase asinc task*/
	 
	 



}