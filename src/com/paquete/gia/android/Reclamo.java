package com.paquete.gia.android;

import com.paquete.si_rixio_alfredo.*;


import android.app.ListActivity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

//import android.text.Editable;
//import android.text.TextWatcher;
import android.view.View;
//import android.widget.ArrayAdapter;
import java.util.ArrayList;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class Reclamo extends ListActivity {
	String IP_Server="www.sistemaswebs.com";
	String URL_connect= "http://"+IP_Server+"/gia/ObtenerArtefactos.php";
	
	EditText edtBusqueda;
	Button btnRegistrarReclamo;
	String cadenajson;
	private ProgressDialog pDialog;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reclamo);
		
		 ArrayList<HashMap<String, String>> laListaElementos = new ArrayList<HashMap<String, String>>();
	     	        
		 Bundle bundle = getIntent().getExtras();
		 String lcCodigoJson = bundle.getString("json");
		 
			try {
				
				JSONObject json = new JSONObject(lcCodigoJson);
	        	JSONArray  loArregloJson = json.getJSONArray("reclamos");
	        	
		        for (int i=0;i<loArregloJson.length();i++) {						
					HashMap<String, String> loMapeoElementos = new HashMap<String, String>();	
					JSONObject loElementoArreglo = loArregloJson.getJSONObject(i);
					
					loMapeoElementos.put("id",  String.valueOf(i));
					loMapeoElementos.put("numero", loElementoArreglo.getString("numero"));
					loMapeoElementos.put("estatus", loElementoArreglo.getString("estatus"));
					loMapeoElementos.put("tipoartefacto", loElementoArreglo.getString("tipoartefacto"));
					loMapeoElementos.put("serial", loElementoArreglo.getString("serial"));
					laListaElementos.add(loMapeoElementos);			
				}		
	        } catch(JSONException loExcepcionJson) {
	        	 Log.e("log_tag", "Error parsing data "+loExcepcionJson.toString());
	        }
	        
	        ListAdapter adapter = new SimpleAdapter(this, laListaElementos , R.layout.elementos_reclamo, 
		                        new String[] { "numero","estatus", "tipoartefacto", "serial"}, 
		                        new int[] { R.id.tvNroReclamo, R.id.tvEstatus, R.id.tvArtefacto, R.id.tvSerial });
		    
	        Log.e("log_tag", "Crea el list adapter");
	        
	        setListAdapter(adapter);
	               
	        
	        ListView lv = getListView(); 
	        lv.setTextFilterEnabled(true);	
	      
	        lv.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		Toast.makeText(Reclamo.this, "ID  was clicked.", Toast.LENGTH_LONG).show();
				}
			});
/////////////////////////////////////////////////////////////////////////////////////////////////////////////	        
	        btnRegistrarReclamo= (Button) findViewById(R.id.btnRegistrarReclamo);
	        
	        btnRegistrarReclamo.setOnClickListener(new View.OnClickListener(){
	            
	        	public void onClick(View view)
	        	{
	        		new asyncreclamo().execute("reclamo");        		               
	 
	        	}
	        });
	        
	   	 	   	
		
	        
///////////////////////////////////////////////////////////////////////////////////////////////////////////////	        
	        
	        
	}
			
	class asyncreclamo extends AsyncTask< String,String, String > {
  	   	 
     	String reclamo;
     	   	
         protected void onPreExecute() {
         	//para el progress dialog
             pDialog = new ProgressDialog(Reclamo.this);
             pDialog.setMessage("Procesando....");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
         }
  
 		protected String doInBackground(String... params) {
 			//obtnemos usr y pass
 			reclamo=params[0];
 			
 			cadenajson = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerArtefactos.php");
 			 				      		
 	    	return "ok";
         	
 		}
        
 		/*Una vez terminado doInBackground segun lo que halla ocurrido 
 		pasamos a la sig. activity
 		o mostramos error*/
         protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);
            
            if (result.equals("ok")){

         	  
 	   			
         		   Intent Reclamo=new Intent(Reclamo.this,Agregar_Reclamo.class);
              	   Reclamo.putExtra("json",cadenajson);
              	   Reclamo.putExtra("reclamo",reclamo);
              	   startActivity(Reclamo);
 	   			
 	   				         	  
         	        	   
             }else{
             	
             }
            

         }
 		
         }
	
	



	public void cerrar (View view){
		finish();
	}
	
	public void magregarreclamo (View view){
		Intent i=new Intent(this, Agregar_Reclamo.class);
		startActivity (i);
	}

}
