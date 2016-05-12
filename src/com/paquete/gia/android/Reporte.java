package com.paquete.gia.android;

import com.paquete.si_rixio_alfredo.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import android.content.ActivityNotFoundException;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Date;

public class Reporte extends ListActivity {
	EditText edtBusqueda;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reporte);
		
		 ArrayList<HashMap<String, String>> laListaElementos = new ArrayList<HashMap<String, String>>();
	     	        
		 Bundle bundle = getIntent().getExtras();
		 String lcCodigoJson = bundle.getString("json");
		 
			try {
				
				JSONObject json = new JSONObject(lcCodigoJson);
	        	JSONArray  loArregloJson = json.getJSONArray("reporte");
	        	
		        for (int i=0;i<loArregloJson.length();i++) {						
					HashMap<String, String> loMapeoElementos = new HashMap<String, String>();	
					JSONObject loElementoArreglo = loArregloJson.getJSONObject(i);
					
					loMapeoElementos.put("id",  String.valueOf(i));
					loMapeoElementos.put("nombre", loElementoArreglo.getString("nombre"));
					laListaElementos.add(loMapeoElementos);			
				}		
	        } catch(JSONException loExcepcionJson) {
	        	 Log.e("log_tag", "Error parsing data "+loExcepcionJson.toString());
	        }
	        
	        ListAdapter adapter = new SimpleAdapter(this, laListaElementos , R.layout.elementos_reporte, 
		                        new String[] { "nombre"}, 
		                        new int[] { R.id.tvReporte});
		    
	        Log.e("log_tag", "Crea el list adapter");
	        
	        setListAdapter(adapter);
	               
	        
	        ListView lv = getListView(); 
	        lv.setTextFilterEnabled(true);	
	        
	        Log.e("log_tag", "Crea el list adapter2");
	      
	        lv.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		//Toast.makeText(Reporte.this, "ID  was clicked.", Toast.LENGTH_LONG).show();
	        		
	        		//codigo de reporte pdf
	        		String lcNombreArchivo = "/storage/sdcard0/Download/Reporte.pdf"; 
	        		
	        		
	        		try{
	        			
	        			OutputStream file = new FileOutputStream(new File(lcNombreArchivo)); 

	                    Document document = new Document(); 
	                    PdfWriter.getInstance(document, file); 
	                    document.open(); 
	                    document.add(new Paragraph("Hola a todos!")); 
	                    document.add(new Paragraph(new Date().toString())); 
	                    document.add(new Paragraph(new Date().toString())); 
	                    document.add(new Paragraph("Hola a todos!")); 

	                    document.close(); 
	                    file.close(); 

	        		}catch(Exception e){
	        			Log.e("Error",e.getMessage());
	        		}
	        		
	        		
	        		File file = new File(lcNombreArchivo);


	                if (file.exists()) {

	                    Uri path = Uri.fromFile(file);
	                    Intent intent = new Intent(Intent.ACTION_VIEW);
	                    intent.setDataAndType(path, "application/pdf");
	                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


	                    try {
	                        startActivity(intent);
	                    } 

	                    catch (ActivityNotFoundException e) {
	                        Toast.makeText(Reporte.this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();

	                    }
	                }
	        		//fin codigo reporte pdf
	        		
				}
			});
		
	}
			
	



	public void cerrar (View view){
		finish();
	}
}
