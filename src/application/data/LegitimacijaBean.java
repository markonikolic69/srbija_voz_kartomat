package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class LegitimacijaBean {
	
	private int broJ_LEGITIMACIJE = 0;
	private int iD_LEGITIMACIJE = 0;
	private String ime = "";
	private String prezime = "";
	private String datuM_RODJENJA = "";
	private String vazI_DO = "";
	private int rbr = 1;
	
	public LegitimacijaBean (LinkedTreeMap gson_container) {

		broJ_LEGITIMACIJE = (int) Double.parseDouble(gson_container.getOrDefault("broJ_LEGITIMACIJE", 0).toString());
		iD_LEGITIMACIJE = (int) Double.parseDouble(gson_container.getOrDefault("iD_LEGITIMACIJE", 0).toString());
		ime = gson_container.getOrDefault("ime", "").toString();
		prezime = gson_container.getOrDefault("prezime", "").toString();
		datuM_RODJENJA = gson_container.getOrDefault("datuM_RODJENJA", "").toString();
		vazI_DO = gson_container.getOrDefault("vazI_DO", "").toString();
		rbr = (int) Double.parseDouble(gson_container.getOrDefault("rbr", 0).toString());
	}
	
	
	public int getBroJ_LEGITIMACIJE() {
		return broJ_LEGITIMACIJE;
	}
	public void setBroJ_LEGITIMACIJE(int broJ_LEGITIMACIJE) {
		this.broJ_LEGITIMACIJE = broJ_LEGITIMACIJE;
	}
	public int getiD_LEGITIMACIJE() {
		return iD_LEGITIMACIJE;
	}
	public void setiD_LEGITIMACIJE(int iD_LEGITIMACIJE) {
		this.iD_LEGITIMACIJE = iD_LEGITIMACIJE;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getDatuM_RODJENJA() {
		return datuM_RODJENJA;
	}
	public void setDatuM_RODJENJA(String datuM_RODJENJA) {
		this.datuM_RODJENJA = datuM_RODJENJA;
	}
	public String getVazI_DO() {
		return vazI_DO;
	}
	public void setVazI_DO(String vazI_DO) {
		this.vazI_DO = vazI_DO;
	}
	public int getRbr() {
		return rbr;
	}
	public void setRbr(int rbr) {
		this.rbr = rbr;
	}
	
	@Override
	public String toString() {
		return "LegitimacijaBean [broJ_LEGITIMACIJE=" + broJ_LEGITIMACIJE + ", iD_LEGITIMACIJE=" + iD_LEGITIMACIJE
				+ ", ime=" + ime + ", prezime=" + prezime + ", datuM_RODJENJA=" + datuM_RODJENJA + ", vazI_DO="
				+ vazI_DO + ", rbr=" + rbr + "]";
	}
	
	

}
