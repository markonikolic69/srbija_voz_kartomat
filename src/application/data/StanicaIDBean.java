package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class StanicaIDBean {
	
	private String naziv = "";
	private int sifra = 0;
	private String naziv_latin = "";
	private String naziv_cirilic = "";
	


	public StanicaIDBean (LinkedTreeMap gson_container) {

		sifra = (int) Double.parseDouble(gson_container.getOrDefault("sifra", 0).toString());
		naziv = gson_container.getOrDefault("naziv", "").toString();

	}
	
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getSifra() {
		return sifra;
	}
	public void setSifra(int sifra) {
		this.sifra = sifra;
	}
	public String getNaziv_latin() {
		return naziv_latin;
	}


	public void setNaziv_latin(String naziv_latin) {
		this.naziv_latin = naziv_latin;
	}


	public String getNaziv_cirilic() {
		return naziv_cirilic;
	}


	public void setNaziv_cirilic(String naziv_cirilic) {
		this.naziv_cirilic = naziv_cirilic;
	}


	@Override
	public String toString() {
		return "StanicaIDBean [naziv=" + naziv + ", sifra=" + sifra + ", naziv_latin=" + naziv_latin
				+ ", naziv_cirilic=" + naziv_cirilic + "]";
	}

	

	
	
	

}
