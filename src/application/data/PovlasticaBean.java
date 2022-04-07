package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class PovlasticaBean {
	
	private int iD_POVLASTICE = 0;
	private String naziv = "";
	private int razred = 0;
	private int procenaT_RAZ_1 = 0;
	private int procenaT_RAZ_2 = 0;
	private int sifrA_POVLASTICE = 0;
	
	public PovlasticaBean (LinkedTreeMap gson_container) {
		naziv = gson_container.getOrDefault("naziv", "").toString();
		iD_POVLASTICE = (int) Double.parseDouble(gson_container.getOrDefault("iD_POVLASTICE", 0).toString());
		razred = (int) Double.parseDouble(gson_container.getOrDefault("razred", 0).toString());
		procenaT_RAZ_1 = (int) Double.parseDouble(gson_container.getOrDefault("procenaT_RAZ_1", 0).toString());
		procenaT_RAZ_2 = (int) Double.parseDouble(gson_container.getOrDefault("procenaT_RAZ_2", 0).toString());
		sifrA_POVLASTICE = (int) Double.parseDouble(gson_container.getOrDefault("sifrA_POVLASTICE", 0).toString());
	}
	
	
	public int getiD_POVLASTICE() {
		return iD_POVLASTICE;
	}
	public void setiD_POVLASTICE(int iD_POVLASTICE) {
		this.iD_POVLASTICE = iD_POVLASTICE;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getRazred() {
		return razred;
	}
	public void setRazred(int razred) {
		this.razred = razred;
	}
	public int getProcenaT_RAZ_1() {
		return procenaT_RAZ_1;
	}
	public void setProcenaT_RAZ_1(int procenaT_RAZ_1) {
		this.procenaT_RAZ_1 = procenaT_RAZ_1;
	}
	public int getProcenaT_RAZ_2() {
		return procenaT_RAZ_2;
	}
	public void setProcenaT_RAZ_2(int procenaT_RAZ_2) {
		this.procenaT_RAZ_2 = procenaT_RAZ_2;
	}
	public int getSifrA_POVLASTICE() {
		return sifrA_POVLASTICE;
	}
	public void setSifrA_POVLASTICE(int sifrA_POVLASTICE) {
		this.sifrA_POVLASTICE = sifrA_POVLASTICE;
	}
	
	
	@Override
	public String toString() {
		return "PovlasticaBean [iD_POVLASTICE=" + iD_POVLASTICE + ", naziv=" + naziv + ", razred=" + razred
				+ ", procenaT_RAZ_1=" + procenaT_RAZ_1 + ", procenaT_RAZ_2=" + procenaT_RAZ_2 + ", sifrA_POVLASTICE="
				+ sifrA_POVLASTICE + "]";
	}
	
	
	
	

}
