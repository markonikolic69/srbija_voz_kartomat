package application.data;


import com.google.gson.internal.LinkedTreeMap;

public class KartomatBean {
	private int iD_TERMINALA = 0;
	private int logovan = 0;
	private int iD_USER = 0;
	private String useR_NAME = "";
	private String password = "";
	private int sifrA_STANICE = 0;
	private String naziV_STANICE = "";
	private int broJ_BLAGAJNE = 0;
	private String seriaL_NUMBER = "";
	private String maC_ADDRESS = "";
	private String apP_VERSION = "";
	private int versioN_NUMBER = 0;
	private String lasT_UPDATE = "";
	private long trenutnI_BROJ_KARTE = 0;
	
	public KartomatBean (LinkedTreeMap gson_container) {
		iD_TERMINALA = (int) Double.parseDouble(gson_container.getOrDefault("iD_TERMINALA", 0).toString());
		logovan = (int) Double.parseDouble(gson_container.getOrDefault("logovan", 0).toString());
		iD_USER = (int) Double.parseDouble(gson_container.getOrDefault("iD_USER", 0).toString());
		useR_NAME = gson_container.getOrDefault("useR_NAME", "").toString();
		password = gson_container.getOrDefault("password", "").toString();
		sifrA_STANICE = (int) Double.parseDouble(gson_container.getOrDefault("sifrA_STANICE", 0).toString());
		naziV_STANICE = gson_container.getOrDefault("naziV_STANICE", "").toString();
		broJ_BLAGAJNE = (int) Double.parseDouble(gson_container.getOrDefault("broJ_BLAGAJNE", 0).toString());
		seriaL_NUMBER = gson_container.getOrDefault("seriaL_NUMBER", "").toString();
		maC_ADDRESS = gson_container.getOrDefault("maC_ADDRESS", "").toString();
		apP_VERSION = gson_container.getOrDefault("apP_VERSION", "").toString();
		versioN_NUMBER = (int) Double.parseDouble(gson_container.getOrDefault("versioN_NUMBER", 0).toString());
		lasT_UPDATE = gson_container.getOrDefault("lasT_UPDATE", "") == null ? "" : gson_container.getOrDefault("lasT_UPDATE", "").toString();
		trenutnI_BROJ_KARTE = (int) Double.parseDouble(gson_container.getOrDefault("trenutnI_BROJ_KARTE", 0).toString());
	}
	
	
	
	
	public int getiD_TERMINALA() {
		return iD_TERMINALA;
	}
	public void setiD_TERMINALA(int iD_TERMINALA) {
		this.iD_TERMINALA = iD_TERMINALA;
	}
	public int getLogovan() {
		return logovan;
	}
	public void setLogovan(int logovan) {
		this.logovan = logovan;
	}
	public int getiD_USER() {
		return iD_USER;
	}
	public void setiD_USER(int iD_USER) {
		this.iD_USER = iD_USER;
	}
	public String getUseR_NAME() {
		return useR_NAME;
	}
	public void setUseR_NAME(String useR_NAME) {
		this.useR_NAME = useR_NAME;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSifrA_STANICE() {
		return sifrA_STANICE;
	}
	public void setSifrA_STANICE(int sifrA_STANICE) {
		this.sifrA_STANICE = sifrA_STANICE;
	}
	public String getNaziV_STANICE() {
		return naziV_STANICE;
	}
	public void setNaziV_STANICE(String naziV_STANICE) {
		this.naziV_STANICE = naziV_STANICE;
	}
	public int getBroJ_BLAGAJNE() {
		return broJ_BLAGAJNE;
	}
	public void setBroJ_BLAGAJNE(int broJ_BLAGAJNE) {
		this.broJ_BLAGAJNE = broJ_BLAGAJNE;
	}
	public String getSeriaL_NUMBER() {
		return seriaL_NUMBER;
	}
	public void setSeriaL_NUMBER(String seriaL_NUMBER) {
		this.seriaL_NUMBER = seriaL_NUMBER;
	}
	public String getMaC_ADDRESS() {
		return maC_ADDRESS;
	}
	public void setMaC_ADDRESS(String maC_ADDRESS) {
		this.maC_ADDRESS = maC_ADDRESS;
	}
	public String getApP_VERSION() {
		return apP_VERSION;
	}
	public void setApP_VERSION(String apP_VERSION) {
		this.apP_VERSION = apP_VERSION;
	}
	public int getVersioN_NUMBER() {
		return versioN_NUMBER;
	}
	public void setVersioN_NUMBER(int versioN_NUMBER) {
		this.versioN_NUMBER = versioN_NUMBER;
	}
	public String getLasT_UPDATE() {
		return lasT_UPDATE;
	}
	public void setLasT_UPDATE(String lasT_UPDATE) {
		this.lasT_UPDATE = lasT_UPDATE;
	}
	public long getTrenutnI_BROJ_KARTE() {
		return trenutnI_BROJ_KARTE;
	}
	public void setTrenutnI_BROJ_KARTE(long trenutnI_BROJ_KARTE) {
		this.trenutnI_BROJ_KARTE = trenutnI_BROJ_KARTE;
	}
	@Override
	public String toString() {
		return "KartomatBean [iD_TERMINALA=" + iD_TERMINALA + ", logovan=" + logovan + ", iD_USER=" + iD_USER
				+ ", useR_NAME=" + useR_NAME + ", password=" + password + ", sifrA_STANICE=" + sifrA_STANICE
				+ ", naziV_STANICE=" + naziV_STANICE + ", broJ_BLAGAJNE=" + broJ_BLAGAJNE + ", seriaL_NUMBER="
				+ seriaL_NUMBER + ", maC_ADDRESS=" + maC_ADDRESS + ", apP_VERSION=" + apP_VERSION + ", versioN_NUMBER="
				+ versioN_NUMBER + ", lasT_UPDATE=" + lasT_UPDATE + ", trenutnI_BROJ_KARTE=" + trenutnI_BROJ_KARTE
				+ "]";
	}
	
	
	
	
}
