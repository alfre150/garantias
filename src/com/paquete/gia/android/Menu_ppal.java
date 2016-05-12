package com.paquete.gia.android;


import org.json.JSONObject;
//import org.json.JSONException;
//import org.json.JSONObject;

//import com.paquete.gia.android.Inicio.asynclogin;
import com.paquete.si_rixio_alfredo.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
//import android.util.Log;
import android.util.Log;
import android.view.*;
//import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
/**import android.widget.Toast;*/
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;

public class Menu_ppal extends Activity {

	
	String IP_Server="www.sistemaswebs.com";
	String URL_connect= "http://"+IP_Server+"/gia/ObtenerDatosMovil.php";
	ImageButton btnClientes;
	ImageButton btnReclamos;
	ImageButton btnReportes;
	String Ventana;
	private ProgressDialog pDialog;
	JSONObject json;
	String cadenajson;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_ppal);
        
        btnClientes= (ImageButton) findViewById(R.id.btnClientes);
        
        btnClientes.setOnClickListener(new View.OnClickListener(){
            
        	public void onClick(View view)
        	{
        		new asyncclientes().execute("clientes");        		               
 
        	}
        });
        
        btnReclamos= (ImageButton) findViewById(R.id.btnReclamos);
        
        btnReclamos.setOnClickListener(new View.OnClickListener(){
            
        	public void onClick(View view)
        	{
        		new asyncclientes().execute("reclamos");        		               
 
        	}
        });
        
        btnReportes= (ImageButton) findViewById(R.id.btnReportes);
        
        btnReportes.setOnClickListener(new View.OnClickListener(){
            
        	public void onClick(View view)
        	{
        		new asyncclientes().execute("reportes");        		               
 
        	}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void mabrirclientes (View view){
    	
    	Intent cliente=new Intent (this, Cliente.class);
    	//cliente.
    	startActivity(cliente);
    }
    
    public void mabrirreclamo (View view){
    	
    	Intent reclamo=new Intent (this, Reclamo.class);
    	startActivity(reclamo);
    }
    
    public void mabrirreporte (View view){
    	
    	Intent reporte=new Intent (this, Reporte.class);
    	startActivity(reporte);
    }
    
    public void cerrar (View view){
    	finish();
    }
    
    /*Clase asinc task para hacer las conexiones asincronas*/
	 class asyncclientes extends AsyncTask< String,String, String > {
   	 
    	String Ventana;
    	   	
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Menu_ppal.this);
            pDialog.setMessage("Procesando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			Ventana=params[0];
			
			if (Ventana == "clientes")
			{
				cadenajson = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerClientesMovil.php");
			}
			if (Ventana == "reclamos")
			{
				cadenajson = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerReclamosMovil.php");
			}
			if (Ventana == "reportes")
			{
				cadenajson = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerReportesMovil.php");
			}
			//json = RespuestaJson.getJSONfromURL(URL_connect);
			
			//cadenajson = RespuestaJsonString.getJSONfromURL(URL_connect);
     		
	    	return "ok";
        	
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("ok")){

        	   if (Ventana == "clientes")
	   			{
        		   Intent Cliente=new Intent(Menu_ppal.this,Cliente.class);
             	   Cliente.putExtra("json",cadenajson);
             	   startActivity(Cliente);
	   			}
	   			if (Ventana == "reclamos")
	   			{
	   				Intent Reclamos =new Intent(Menu_ppal.this,Reclamo.class);
	   				Reclamos.putExtra("json",cadenajson);
             	   startActivity(Reclamos);
	   			}
	   			if (Ventana == "reportes")
	   			{
	   				Intent Reportes =new Intent(Menu_ppal.this,Reporte.class);
	   				Reportes.putExtra("json",cadenajson);
             	   startActivity(Reportes);
	   			}
        	  
        	        	   
            }else{
            	err_login();
            }
           

        }
		
        }
	/* Fin Clase asinc task*/
	 
	 
	 /* Codigo para colocar error cuando este erronea la verificacion*/
		public void err_login()
	    {
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:No se encontro lista", Toast.LENGTH_SHORT);
	 	    toast1.show();    	
	    }
		/* Fin de verificacion erronea*/
}
