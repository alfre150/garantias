package com.paquete.gia.android;

import java.util.ArrayList;

import com.paquete.gia.android.Elementos_Lista_Reclamo;
import com.paquete.si_rixio_alfredo.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adaptador_Elementos_Lista_Reclamo extends BaseAdapter {
	protected Activity activity;
	protected ArrayList<Elementos_Lista_Reclamo> items;
	
	
	public Adaptador_Elementos_Lista_Reclamo(Activity activity, ArrayList<Elementos_Lista_Reclamo> items) {
		this.activity = activity;
		this.items = items;
	}


	@Override
	public int getCount() {
		return items.size();
	}


	@Override
	public Object getItem(int position) {
		return items.get(position);
	}
	
	public long getItemId(int position) {
		return items.get(position).getId();
	}


	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		
        if(convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.elementos_reclamo, null);
        }
            
        Elementos_Lista_Reclamo item = items.get(position);
             
        
        TextView tvArtefacto = (TextView) vi.findViewById(R.id.tvArtefacto);
        tvArtefacto.setText(item.gettvArtefacto());
        
        TextView tvSerial = (TextView) vi.findViewById(R.id.tvSerial);
        tvSerial.setText(item.gettvSerial());
        
        TextView tvEstatus = (TextView) vi.findViewById(R.id.tvEstatus);
        tvEstatus.setText(item.gettvEstatus());
        
        TextView tvNroReclamo = (TextView) vi.findViewById(R.id.tvNroReclamo);
        tvNroReclamo.setText(item.gettvNroReclamo());

        return vi;
	}
}