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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Agregar_Cliente extends Activity {

	
	Spinner  lspnIdentidadCliente;
	EditText ledtRifCliente;
    EditText ledtNombreCliente;
    Spinner  lspnCodigoTelefono;
    EditText ledtTelefonoCliente;
    Spinner  lspnEstadoCliente;
    EditText ledtDireccionCliente;
    EditText ledtCorreoCliente;
    EditText ledtClaveCliente;
    EditText ledtRepitaClaveCliente;
    
    Button btnAgregar;
    private ProgressDialog pDialog;
    
    Httppostaux post;
    String IP_Server="www.sistemaswebs.com";//IP DE NUESTRO PC
    String URL_connect= "http://"+IP_Server+"/gia/InsertarClienteMovil.php";//ruta en donde estan nuestros archivos

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agregar_cliente);
		
		post= new Httppostaux();
		lspnIdentidadCliente	= (Spinner)  findViewById(R.id.spnIdentificacion);
		ledtRifCliente 			= (EditText) findViewById(R.id.edtRifCi);
		ledtNombreCliente		= (EditText) findViewById(R.id.edtNombre);
		lspnCodigoTelefono		= (Spinner)	 findViewById(R.id.spnCodCelular);
		ledtTelefonoCliente 	= (EditText) findViewById(R.id.edtTelefono);
		lspnEstadoCliente 		= (Spinner) findViewById(R.id.spnEstado);
		ledtDireccionCliente 	= (EditText) findViewById(R.id.edtDireccion);
		ledtCorreoCliente 		= (EditText) findViewById(R.id.edtCorreo);
		ledtClaveCliente 		= (EditText) findViewById(R.id.edtClave);
		ledtRepitaClaveCliente  = (EditText) findViewById(R.id.edtRepiteClave);
		
		btnAgregar= (Button) findViewById(R.id.btnAgregarCliente);
    /*
     * Codigo para asignarle el evento al boton
     */
		btnAgregar.setOnClickListener(new View.OnClickListener(){
            
        	public void onClick(View view){
        		 
        		//Extraemos datos de los EditText
        		String lcRifCliente		 	= lspnIdentidadCliente.getSelectedItem().toString() + ledtRifCliente.getText().toString();
        		String lcNombreCliente	 	= ledtNombreCliente.getText().toString();
        		String lcTelefonoCliente 	= lspnCodigoTelefono.getSelectedItem().toString() + ledtTelefonoCliente.getText().toString();
        		String lcEstadoCliente	 	= lspnEstadoCliente.getSelectedItem().toString();
        		String lcDireccionCliente 	= ledtDireccionCliente.getText().toString();
        		String lcCorreoCliente 		= ledtCorreoCliente.getText().toString();
        		String lcClaveCliente 		= ledtClaveCliente.getText().toString();
        		String lcRepitaClaveCliente = ledtRepitaClaveCliente.getText().toString();
        		
        		//me los traigo solo para verificar vacios
        		String RifCliente		= ledtRifCliente.getText().toString();
        		String TelefonoCliente 	= ledtTelefonoCliente.getText().toString();
        		       		
        		//verificamos si estan en blanco
        		if( mChequearDatos( RifCliente ,lcNombreCliente,TelefonoCliente,lcEstadoCliente,
        							lcDireccionCliente,lcCorreoCliente,lcClaveCliente,lcRepitaClaveCliente)==true){

        			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
        			new asyncCliente().execute( lcRifCliente ,lcNombreCliente,lcTelefonoCliente,lcEstadoCliente,
							lcDireccionCliente,lcCorreoCliente,lcClaveCliente,lcRepitaClaveCliente);        		               
        			        		
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
	}
	public void cerrar (View view){
		finish();
	}
	/* Codigo para verificar que esten todos los datos */
	public boolean mChequearDatos(String RifCliente ,String lcNombreCliente,String TelefonoCliente,String lcEstadoCliente,
								  String lcDireccionCliente ,String lcCorreoCliente ,String lcClaveCliente ,String lcRepitaCliente )
    {
    	if 	(RifCliente.equals("") || lcNombreCliente.equals("") || TelefonoCliente.equals("")|| lcEstadoCliente.equals("")|| 
    			lcDireccionCliente.equals("") || lcCorreoCliente.equals("")|| lcClaveCliente.equals("")|| lcRepitaCliente.equals(""))
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

	public boolean mAgregarClienteWeb(String lcRifCliente ,String lcNombreCliente,String lcTelefonoCliente,String lcEstadoCliente,
			  String lcDireccionCliente ,String lcCorreoCliente ,String lcClaveCliente) {
    	
		int Respuesta =-1;
    	
    	ArrayList<NameValuePair> laParametros= new ArrayList<NameValuePair>();
     		
    	laParametros.add(new BasicNameValuePair("lcRifCliente",lcRifCliente));
    	laParametros.add(new BasicNameValuePair("lcNombreCliente",lcNombreCliente));
    	laParametros.add(new BasicNameValuePair("lcTelefonoCliente",lcTelefonoCliente));
    	laParametros.add(new BasicNameValuePair("lcEstadoCliente",lcEstadoCliente));
    	laParametros.add(new BasicNameValuePair("lcDireccionCliente",lcDireccionCliente));
    	laParametros.add(new BasicNameValuePair("lcCorreoCliente",lcCorreoCliente));
    	laParametros.add(new BasicNameValuePair("lcClaveCliente",lcClaveCliente));
    	    	
    	JSONArray jdata=post.getserverdata(laParametros, URL_connect);

      	//si lo que obtuvimos no es null
    	if (jdata!=null && jdata.length() > 0){

    		JSONObject json_data; //creamos un objeto JSON
			try {
				
				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				Respuesta=json_data.getInt("Respuesta");//accedemos al valor 
				 Log.e("Respuesta web","Respuesta= "+Respuesta);//muestro por log que obtuvimos
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
   	 
    	String lcRifCliente,lcNombreCliente,lcTelefonoCliente,lcEstadoCliente;
    	String lcDireccionCliente,lcCorreoCliente,lcClaveCliente,lcRepitaClaveCliente;
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Agregar_Cliente.this);
            pDialog.setMessage("Registrando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			//user=params[0];
			//pass=params[1];
			lcRifCliente =params[0];
		    lcNombreCliente=params[1];
		    lcTelefonoCliente=params[2];
		    lcEstadoCliente=params[3];
		    lcDireccionCliente=params[4];
		    lcCorreoCliente=params[5];
		    lcClaveCliente=params[6];
		    lcRepitaClaveCliente=params[7];
		    
            
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (mAgregarClienteWeb(lcRifCliente ,lcNombreCliente,lcTelefonoCliente,lcEstadoCliente,
					lcDireccionCliente,lcCorreoCliente,lcClaveCliente)==true)
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

				Intent i=new Intent(Agregar_Cliente.this, Inicio.class);
				//i.putExtra("user",user);
				startActivity(i); 
				
            }else{
            	mMostrarError();
            }
            
                									}
		
        }
	/* Fin Clase asinc task*/
}
