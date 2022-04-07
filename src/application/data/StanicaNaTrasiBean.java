package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class StanicaNaTrasiBean {
	
	//etTrasaVoza
	
	private int iD_VOZ = 0;
	private int rb = 0;
	private int sifrA_STANICE = 0;
	private String vremE_DOLASKA = "";
	private String vremE_POLASKA = "";
	private String naziV_STANICE = "";
	
	
	public StanicaNaTrasiBean (LinkedTreeMap gson_container) {

		iD_VOZ = (int) Double.parseDouble(gson_container.getOrDefault("iD_VOZ", 0).toString());
		rb = (int) Double.parseDouble(gson_container.getOrDefault("rb", 0).toString());
		sifrA_STANICE = (int) Double.parseDouble(gson_container.getOrDefault("sifrA_STANICE", 0).toString());
		vremE_DOLASKA = gson_container.getOrDefault("vremE_DOLASKA", "").toString();
		vremE_POLASKA = gson_container.getOrDefault("vremE_POLASKA", "").toString();
		naziV_STANICE = gson_container.getOrDefault("naziV_STANICE", "").toString();
	}
	
	public StanicaNaTrasiBean () {
		
	}
	
	
	public int getiD_VOZ() {
		return iD_VOZ;
	}
	public void setiD_VOZ(int iD_VOZ) {
		this.iD_VOZ = iD_VOZ;
	}
	public int getRb() {
		return rb;
	}
	public void setRb(int rb) {
		this.rb = rb;
	}
	public int getSifrA_STANICE() {
		return sifrA_STANICE;
	}
	public void setSifrA_STANICE(int sifrA_STANICE) {
		this.sifrA_STANICE = sifrA_STANICE;
	}
	public String getVremE_DOLASKA() {
		return vremE_DOLASKA;
	}
	public void setVremE_DOLASKA(String vremE_DOLASKA) {
		this.vremE_DOLASKA = vremE_DOLASKA;
	}
	public String getVremE_POLASKA() {
		return vremE_POLASKA;
	}
	public void setVremE_POLASKA(String vremE_POLASKA) {
		this.vremE_POLASKA = vremE_POLASKA;
	}
	public String getNaziV_STANICE() {
		return naziV_STANICE;
	}
	public void setNaziV_STANICE(String naziV_STANICE) {
		this.naziV_STANICE = naziV_STANICE;
	}
	@Override
	public String toString() {
		return "StanicaNaTrasiBean [iD_VOZ=" + iD_VOZ + ", rb=" + rb + ", sifrA_STANICE=" + sifrA_STANICE
				+ ", vremE_DOLASKA=" + vremE_DOLASKA + ", vremE_POLASKA=" + vremE_POLASKA + ", naziV_STANICE="
				+ naziV_STANICE + "]";
	}
	
	

}
