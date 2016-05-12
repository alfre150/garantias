package com.paquete.gia.android;


public class Elementos_Lista_Reclamo  {
	protected long id;
	protected String tvArtefacto;
	protected String tvSerial;
	protected String tvEstatus;
	protected String tvNroReclamo;
	
	
	public long getId() {
		return id;
	}
	
	
	public Elementos_Lista_Reclamo() {
		this.tvArtefacto = "";
		this.tvSerial = "";
		this.tvEstatus = "";
		this.tvNroReclamo="";
	}
	
	public Elementos_Lista_Reclamo(String tvArtefacto, String tvSerial, String tvEstatus, String tvNroReclamo) {
		this.tvArtefacto = tvArtefacto;
		this.tvSerial = tvSerial;
		this.tvEstatus = tvEstatus;
		this.tvNroReclamo= tvNroReclamo;
	}
	
	public String gettvArtefacto() {
		return tvArtefacto;
	}
	
	public void settvArtefacto(String tvArtefacto) {
		this.tvArtefacto = tvArtefacto;
	}
	
	public String gettvSerial() {
		return tvSerial;
	}
	
	public void settvSerial(String tvSerial) {
		this.tvSerial = tvSerial;
	}
	
	public String gettvEstatus() {
		return tvEstatus;
	}
	
	public void settvEstatus(String tvEstatus) {
		this.tvEstatus = tvEstatus;
	}
	
	public String gettvNroReclamo() {
		return tvNroReclamo;
	}
	
	public void settvNroReclamo(String tvNroReclamo) {
		this.tvNroReclamo = tvNroReclamo;
	}
}


	