package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class FrekventneStaniceBean {
	
	
	private int iD_TERMINALA = 0;
	private int rednI_BROJ = 0;
	private int sifrA_STANICE = 0;
	private int sifrA_UPUTNE_STANICE = 0;
	private String naziV_UPUTNE_STANICE = "";
	private String naziV_UPUTNE_STANICE_latin = "";

	private String naziV_UPUTNE_STANICE_cir = "";
	
	public FrekventneStaniceBean (LinkedTreeMap gson_container) {
		iD_TERMINALA = (int) Double.parseDouble(gson_container.getOrDefault("iD_TERMINALA", 0).toString());
		rednI_BROJ = (int) Double.parseDouble(gson_container.getOrDefault("rednI_BROJ", 0).toString());
		sifrA_STANICE = (int) Double.parseDouble(gson_container.getOrDefault("sifrA_STANICE", 0).toString());
		naziV_UPUTNE_STANICE = gson_container.getOrDefault("naziV_UPUTNE_STANICE", "").toString();
		sifrA_UPUTNE_STANICE = (int) Double.parseDouble(gson_container.getOrDefault("sifrA_UPUTNE_STANICE", 0).toString());
	}
	
	public int getiD_TERMINALA() {
		return iD_TERMINALA;
	}
	public void setiD_TERMINALA(int iD_TERMINALA) {
		this.iD_TERMINALA = iD_TERMINALA;
	}
	public int getRednI_BROJ() {
		return rednI_BROJ;
	}
	public void setRednI_BROJ(int rednI_BROJ) {
		this.rednI_BROJ = rednI_BROJ;
	}
	public int getSifrA_STANICE() {
		return sifrA_STANICE;
	}
	public void setSifrA_STANICE(int sifrA_STANICE) {
		this.sifrA_STANICE = sifrA_STANICE;
	}
	public int getSifrA_UPUTNE_STANICE() {
		return sifrA_UPUTNE_STANICE;
	}
	public void setSifrA_UPUTNE_STANICE(int sifrA_UPUTNE_STANICE) {
		this.sifrA_UPUTNE_STANICE = sifrA_UPUTNE_STANICE;
	}
	public String getNaziV_UPUTNE_STANICE() {
		return naziV_UPUTNE_STANICE;
	}
	public void setNaziV_UPUTNE_STANICE(String naziV_UPUTNE_STANICE) {
		this.naziV_UPUTNE_STANICE = naziV_UPUTNE_STANICE;
	}
	public String getNaziV_UPUTNE_STANICE_latin() {
		return naziV_UPUTNE_STANICE_latin;
	}

	public void setNaziV_UPUTNE_STANICE_latin(String naziV_UPUTNE_STANICE_latin) {
		this.naziV_UPUTNE_STANICE_latin = naziV_UPUTNE_STANICE_latin;
	}

	public String getNaziV_UPUTNE_STANICE_cir() {
		return naziV_UPUTNE_STANICE_cir;
	}

	public void setNaziV_UPUTNE_STANICE_cir(String naziV_UPUTNE_STANICE_cir) {
		this.naziV_UPUTNE_STANICE_cir = naziV_UPUTNE_STANICE_cir;
	}

	@Override
	public String toString() {
		return "FrekventneStaniceBean [iD_TERMINALA=" + iD_TERMINALA + ", rednI_BROJ=" + rednI_BROJ + ", sifrA_STANICE="
				+ sifrA_STANICE + ", sifrA_UPUTNE_STANICE=" + sifrA_UPUTNE_STANICE + ", naziV_UPUTNE_STANICE="
				+ naziV_UPUTNE_STANICE + ", naziV_UPUTNE_STANICE_latin=" + naziV_UPUTNE_STANICE_latin
				+ ", naziV_UPUTNE_STANICE_cir=" + naziV_UPUTNE_STANICE_cir + "]";
	}
	
	

	
	
	
	
	

}
