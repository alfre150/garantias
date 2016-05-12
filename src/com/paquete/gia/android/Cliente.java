package com.paquete.gia.android;

//import java.util.ArrayList;
//import java.util.List;

import com.paquete.si_rixio_alfredo.*;

import android.app.ListActivity;
import android.content.Intent;
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

import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class Cliente extends ListActivity {
	EditText edtBusqueda;
	
	 	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente);
		
		 ArrayList<HashMap<String, String>> laListaElementos = new ArrayList<HashMap<String, String>>();
	     	        
		 Bundle bundle = getIntent().getExtras();
		 String lcCodigoJson = bundle.getString("json");
		 
			try {
				
				JSONObject json = new JSONObject(lcCodigoJson);
	        	JSONArray  loArregloJson = json.getJSONArray("clientes");
	        	
		        for (int i=0;i<loArregloJson.length();i++) {						
					HashMap<String, String> loMapeoElementos = new HashMap<String, String>();	
					JSONObject loElementoArreglo = loArregloJson.getJSONObject(i);
					
					loMapeoElementos.put("id",  String.valueOf(i));
					loMapeoElementos.put("nombre", loElementoArreglo.getString("nombre"));
					loMapeoElementos.put("correo", loElementoArreglo.getString("correo"));
					laListaElementos.add(loMapeoElementos);			
				}		
	        } catch(JSONException loExcepcionJson) {
	        	 Log.e("log_tag", "Error parsing data "+loExcepcionJson.toString());
	        }
	        
	        ListAdapter adapter = new SimpleAdapter(this, laListaElementos , R.layout.elementos_cliente, 
		                        new String[] { "nombre","correo"}, 
		                        new int[] { R.id.lblNombreCliente,R.id.lblCorreoCliente });
		    
	        Log.e("log_tag", "Crea el list adapter");
	        
	        setListAdapter(adapter);
	               
	        
	        ListView lv = getListView(); 
	        lv.setTextFilterEnabled(true);	
	      
	        lv.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		Toast.makeText(Cliente.this, "ID  was clicked.", Toast.LENGTH_LONG).show();
				}
			});
		
	}
	
		
	public void cerrar (View view){
		finish();
	}
	
	
	public void magregarclientes (View view){
		Intent i=new Intent(this, Agregar_Cliente.class);
		startActivity (i);
	}
}
