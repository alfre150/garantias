package com.paquete.gia.android;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.gia.serviciosgia.MainActivity;
//import com.gia.serviciosgia.mostrario;
//import com.gia.serviciosgia.MainActivity.asynclogin;
//import com.paquete.gia.android.Httppostaux;
//import com.gia.serviciosgia.R;
import com.paquete.si_rixio_alfredo.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**import android.widget.Toast;*/
import android.content.*;
public class Inicio extends Activity {

	EditText user;
    EditText pass;
    Button blogin;
    private ProgressDialog pDialog;
    
    Httppostaux post;
    String IP_Server="www.sistemaswebs.com";//IP DE NUESTRO PC
    String URL_connect= "http://"+IP_Server+"/gia/index_csMovil.php";//ruta en donde estan nuestros archivos
    String rol,estatus;
    String cadenajson;
    
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);
		
		
		/*
		 * Codigo para obtener los datos de cajas 
		 */
			post= new Httppostaux();
	        user= (EditText) findViewById(R.id.edtUsuario);
	        pass= (EditText) findViewById(R.id.edtCorreo);
	        
	        blogin= (Button) findViewById(R.id.btnAceptar);
        /*
         * Codigo para asignarle el evento al boton
         */
	        blogin.setOnClickListener(new View.OnClickListener(){
	            
	        	public void onClick(View view){
	        		 
	        		//Extraemos datos de los EditText
	        		String usuario=user.getText().toString();
	        		String passw=pass.getText().toString();
	        		
	        		//verificamos si estan en blanco
	        		if( checklogindata( usuario , passw )==true){

	        			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
	        			new asynclogin().execute(usuario,passw);        		               
	        			        		
	        		}else{
	        			//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
	        			err_login();
	        		}
	        		
	        	user.setText("");
	        	pass.setText("");
	        		
	        	}
	        });
	        /*Fin de asignacion de evento a boton */
	        
	    
	}
		
	  
	/* Codigo para verificar que esten todos los datos */
	public boolean checklogindata(String username ,String password )
    {
    	if 	(username.equals("") || password.equals(""))
    	{
    		Log.e("Login ui", "checklogindata user or pass error");
    		return false;
    	}
    	else
    	{
    	   	return true;
    	}
    
    }   
	/* Fin de verificar todos los datos*/
	
	/* Codigo para colocar error cuando este erronea la verificacion*/
	public void err_login()
    {
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
	/* Fin de verificacion erronea*/
	
	/*Verificacion de loginestatus*/
	public boolean loginstatus(String username ,String password ) {
    	int logstatus=-1;
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
		    		postparameters2send.add(new BasicNameValuePair("usuario",username));
		    		postparameters2send.add(new BasicNameValuePair("password",password));

		   //realizamos una peticion y como respuesta obtenes un array JSON
      		JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

      		/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
      		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
      		 * observar el progressdialog
      		 * la podemos eliminar si queremos
      		 */
		    //SystemClock.sleep(950);
		    		
		    //si lo que obtuvimos no es null
		    	if (jdata!=null && jdata.length() > 0){

		    		JSONObject json_data; //creamos un objeto JSON
					try {
						json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
						 logstatus=json_data.getInt("logstatus");//accedemos al valor 
						 Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
						 
						 rol=json_data.getString("rol");//accedemos al valor 
						 Log.e("rol","rol= "+rol);//muestro por log que obtuvimos
						 
						 estatus=json_data.getString("estatus");//accedemos al valor 
						 Log.e("estatus","estatus= "+estatus);//muestro por log que obtuvimos
						 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
		             
					//validamos el valor obtenido
		    		 if (logstatus==0){// [{"logstatus":"0"}] 
		    			 Log.e("loginstatus ", "invalido");
		    			 return false;
		    		 }
		    		 else{// [{"logstatus":"1"}]
		    			 Log.e("loginstatus ", "valido");
		    			 return true;
		    		 }
		    		 
			  }else{	//json obtenido invalido verificar parte WEB.
		    			 Log.e("JSON  ", "ERROR");
			    		return false;
			  }
    	
    }
    /* fin verificacion loginstatus*/
	
	/*Clase asinc task para hacer las conexiones asincronas*/
	 class asynclogin extends AsyncTask< String, String, String > {
    	 
     	String user,pass;
         protected void onPreExecute() {
         	//para el progress dialog
             pDialog = new ProgressDialog(Inicio.this);
             pDialog.setMessage("Autenticando....");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(false);
             pDialog.show();
         }
  
 		protected String doInBackground(String... params) {
 			//obtnemos usr y pass
 			user=params[0];
 			pass=params[1];
             
 			//enviamos y recibimos y analizamos los datos en segundo plano.
     		if (loginstatus(user,pass)==true){    		    		
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
            
            if (result.equals("ok"))
            {

 				if (estatus.equals("Activo"))
 				{
 					
 					Log.e("if_activo=","entro en el if activo");	
 					
 					if (rol.equals("Administrador"))
 					{
 						Log.e("if_administrador=","entro en el if administrador");	
 						Intent i=new Intent(Inicio.this, Menu_ppal.class);
 		 				i.putExtra("user",user);
 		 				startActivity(i); 
 					}
 					if (rol.equals("Clientes"))
 					{
 						/*Log.e("if_clientes=","entro en el if clientes");	
 						Intent i=new Intent(Inicio.this, Reclamo.class);
 		 				i.putExtra("rol",rol);
 		 				startActivity(i); */
 						
 						new asyncreclamos().execute("clientes");  
 					}
 					if (rol.equals("Gerentes"))
 					{
 						/*Log.e("if_clientes=","entro en el if clientes");	
 						Intent i=new Intent(Inicio.this, Reclamo.class);
 		 				i.putExtra("rol",rol);
 		 				startActivity(i); */
 						
 						new asyncreportes().execute("gerentes");  
 					}

 					
 				}
 				else
 				{
 					Toast lcMensaje = Toast.makeText(getApplicationContext(),"Usuario inactivo"+ estatus, Toast.LENGTH_LONG);
 					lcMensaje.show();
 				}
 				
 				
             }else{
             	err_login();
             }
             
                 									}
 		
         }
	/* Fin Clase asinc task*/
	 
	public void magregarclientes (View view){
			Intent i=new Intent(this, Agregar_Cliente.class);
			startActivity (i);
		} 
	
	public void cerrar (View view){
    	System.exit(0);
    }
	
	/*Clase asinc task para hacer las conexiones asincronas*/
	 class asyncreclamos extends AsyncTask< String, String, String > {
   	 
    	String Ventana;
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Inicio.this);
            pDialog.setMessage("Procesando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			Ventana=params[0];
			
			cadenajson = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerReclamosMovilPersonal.php?lcCliente=V4458391");
			
			return "ok";        	
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("ok"))
           {
				Log.e("if_clientes=","entro en el if clientes");	
				Intent i=new Intent(Inicio.this, Reclamo.class);
		 		i.putExtra("rol",rol);
		 		i.putExtra("json",cadenajson);
		 		startActivity(i); 
			}
			else
			{
            	err_login();
            }
          }
	}
	/* Fin Clase asinc task*/
	 
	 /*Clase asinc task para hacer las conexiones asincronas*/
	 class asyncreportes extends AsyncTask< String, String, String > {
   	 
    	String Ventana;
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Inicio.this);
            pDialog.setMessage("Procesando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			Ventana=params[0];
			
			cadenajson = RespuestaJsonString.getJSONfromURL("http://"+IP_Server+"/gia/ObtenerReportesMovil.php");
			
			return "ok";        	
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("ok"))
           {
				Log.e("if_clientes=","entro en el if clientes");	
				Intent i=new Intent(Inicio.this, Reporte.class);
		 		i.putExtra("rol",rol);
		 		i.putExtra("json",cadenajson);
		 		startActivity(i); 
			}
			else
			{
            	err_login();
            }
          }
	}
	/* Fin Clase asinc task*/
}


