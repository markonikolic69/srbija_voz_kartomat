package application.https;

import com.google.gson.internal.LinkedTreeMap;

public class KartaStampaBean {
	
	private int REDNI_BROJ = 0;
	private String DATUM_KUPOVINE = "";
	private String NAZIV_STANICE_ODA = "";
	private String NAZIV_STANICE_DOA = "";
	private String NAZIV_STANICE_ODR = "";
	private String NAZIV_STANICE_DOR = "";
	private String VIA = "";
	private String VIAR = "";
	private int SMER = 0;

	private String BROJ_KARTE = "0";
	public String getBROJ_KARTE() {
		return BROJ_KARTE;
	}

	public void setBROJ_KARTE(String bROJ_KARTE) {
		BROJ_KARTE = bROJ_KARTE;
	}
	private String RAZREDA = "";
	private String RAZREDR = "";
	private String VAZI_OD = "";
	private String VAZI_DO = "";
	private String VOZA = "";
	public String getVOZA() {
		return VOZA;
	}

	public void setVOZA(String vOZA) {
		VOZA = vOZA;
	}
	private String VOZR = "";
	private String VREME_POLASKAA = "";
	private String VREME_DOLASKAA = "";
	
	
	
	
	private String VREME_POLASKAR = "";
	private String VREME_DOLASKAR = "";
	private double CENA = 0.0;
	private String POVLASTICA_NAZIV = "";
	private String BROJ_LEGITIMACIJE = "";
	private String UZRAST = "";
	private String DATUM_RODJENJA = "";
	private String STRANA = "";
	private String BAR_KOD39_URL = "";
	private String BAR_KOD_ASTEC_URL = "";
	private String IME_PUTNIKA = "";
	private String PREZIME_PUTNIKA = "";
	
	private String REZERVACIJA_A = "";
	public String getREZERVACIJA_A() {
		return REZERVACIJA_A;
	}

	public void setREZERVACIJA_A(String rEZERVACIJA_A) {
		REZERVACIJA_A = rEZERVACIJA_A;
	}

	public String getREZERVACIJA_R() {
		return REZERVACIJA_R;
	}

	public void setREZERVACIJA_R(String rEZERVACIJA_R) {
		REZERVACIJA_R = rEZERVACIJA_R;
	}
	private String REZERVACIJA_R = "";
	
	
	public KartaStampaBean (LinkedTreeMap gson_container) {

		REDNI_BROJ = Integer.parseInt(gson_container.getOrDefault("REDNI_BROJ", 0).toString());
		BROJ_KARTE = gson_container.getOrDefault("BROJ_KARTE", 0).toString();
		DATUM_KUPOVINE = "" + gson_container.getOrDefault("DATUM_KUPOVINE", 0).toString();
		NAZIV_STANICE_ODA = "" + gson_container.getOrDefault("NAZIV_STANICE_ODA", 0).toString();
		NAZIV_STANICE_DOA = "" + gson_container.getOrDefault("NAZIV_STANICE_DOA", 0).toString();
		NAZIV_STANICE_ODR = "" + gson_container.getOrDefault("NAZIV_STANICE_ODR", 0).toString();
		NAZIV_STANICE_DOR = "" + gson_container.getOrDefault("NAZIV_STANICE_DOR", 0).toString();
		VIA = "" + gson_container.getOrDefault("VIA", 0).toString();
		VIAR = "" + gson_container.getOrDefault("VIAR", 0).toString();
		
		SMER = Integer.parseInt(gson_container.getOrDefault("SMER", 0).toString());
		
		RAZREDA = "" + gson_container.getOrDefault("RAZREDA", 0).toString();
		RAZREDR = "" + gson_container.getOrDefault("RAZREDR", 0).toString();
		VAZI_OD = "" + gson_container.getOrDefault("VAZI_OD", 0).toString();
		VAZI_DO = "" + gson_container.getOrDefault("VAZI_DO", 0).toString();
		VOZA = "" + gson_container.getOrDefault("VOZA", 0).toString();
		VOZR = "" + gson_container.getOrDefault("VOZR", 0).toString();
		VREME_POLASKAA = "" + gson_container.getOrDefault("VREME_POLASKAA", 0).toString();
		VREME_DOLASKAA = "" + gson_container.getOrDefault("VREME_DOLASKAA", 0).toString();
		
		VREME_POLASKAR = "" + gson_container.getOrDefault("VREME_POLASKAR", 0).toString();
		VREME_DOLASKAR = "" + gson_container.getOrDefault("VREME_DOLASKAR", 0).toString();
		CENA = Double.parseDouble(gson_container.getOrDefault("VREME_DOLASKAA", 0).toString());
		POVLASTICA_NAZIV = "" + gson_container.getOrDefault("POVLASTICA_NAZIV", 0).toString();
		BROJ_LEGITIMACIJE = "" + gson_container.getOrDefault("BROJ_LEGITIMACIJE", 0).toString();
		UZRAST = "" + gson_container.getOrDefault("UZRAST", 0).toString();
		DATUM_RODJENJA = "" + gson_container.getOrDefault("DATUM_RODJENJA", 0).toString();
		STRANA = "" + gson_container.getOrDefault("STRANA", 0).toString();
		BAR_KOD39_URL = "" + gson_container.getOrDefault("BAR_KOD39_URL", 0).toString();
		BAR_KOD_ASTEC_URL = "" + gson_container.getOrDefault("BAR_KOD_ASTEC_URL", 0).toString();
		IME_PUTNIKA = "" + gson_container.getOrDefault("IME_PUTNIKA", 0).toString();
		PREZIME_PUTNIKA = "" + gson_container.getOrDefault("PREZIME_PUTNIKA", 0).toString();
		REZERVACIJA_A = "" + gson_container.getOrDefault("REZERVACIJA_A", 0).toString();
		REZERVACIJA_R = "" + gson_container.getOrDefault("REZERVACIJA_R", 0).toString();
	}
	
	public KartaStampaBean() {
		
	}
	
	public int getREDNI_BROJ() {
		return REDNI_BROJ;
	}
	public void setREDNI_BROJ(int rEDNI_BROJ) {
		REDNI_BROJ = rEDNI_BROJ;
	}
	public String getDATUM_KUPOVINE() {
		return DATUM_KUPOVINE;
	}
	public void setDATUM_KUPOVINE(String dATUM_KUPOVINE) {
		DATUM_KUPOVINE = dATUM_KUPOVINE;
	}
	public String getNAZIV_STANICE_ODA() {
		return NAZIV_STANICE_ODA;
	}
	public void setNAZIV_STANICE_ODA(String nAZIV_STANICE_ODA) {
		NAZIV_STANICE_ODA = nAZIV_STANICE_ODA;
	}
	public String getNAZIV_STANICE_DOA() {
		return NAZIV_STANICE_DOA;
	}
	public void setNAZIV_STANICE_DOA(String nAZIV_STANICE_DOA) {
		NAZIV_STANICE_DOA = nAZIV_STANICE_DOA;
	}
	public String getNAZIV_STANICE_ODR() {
		return NAZIV_STANICE_ODR;
	}
	public void setNAZIV_STANICE_ODR(String nAZIV_STANICE_ODR) {
		NAZIV_STANICE_ODR = nAZIV_STANICE_ODR;
	}
	public String getNAZIV_STANICE_DOR() {
		return NAZIV_STANICE_DOR;
	}
	public void setNAZIV_STANICE_DOR(String nAZIV_STANICE_DOR) {
		NAZIV_STANICE_DOR = nAZIV_STANICE_DOR;
	}
	public String getVIA() {
		return VIA;
	}
	public void setVIA(String vIA) {
		VIA = vIA;
	}
	public String getVIAR() {
		return VIAR;
	}
	public void setVIAR(String vIAR) {
		VIAR = vIAR;
	}
	public int getSMER() {
		return SMER;
	}
	public void setSMER(int sMER) {
		SMER = sMER;
	}
	public String getRAZREDA() {
		return RAZREDA;
	}
	public void setRAZREDA(String rAZREDA) {
		RAZREDA = rAZREDA;
	}
	public String getRAZREDR() {
		return RAZREDR;
	}
	public void setRAZREDR(String rAZREDR) {
		RAZREDR = rAZREDR;
	}
	public String getVAZI_OD() {
		return VAZI_OD;
	}
	public void setVAZI_OD(String vAZI_OD) {
		VAZI_OD = vAZI_OD;
	}
	public String getVAZI_DO() {
		return VAZI_DO;
	}
	public void setVAZI_DO(String vAZI_DO) {
		VAZI_DO = vAZI_DO;
	}
	public String getVOZR() {
		return VOZR;
	}
	public void setVOZR(String vOZR) {
		VOZR = vOZR;
	}
	public String getVREME_POLASKAA() {
		return VREME_POLASKAA;
	}
	public void setVREME_POLASKAA(String vREME_POLASKAA) {
		VREME_POLASKAA = vREME_POLASKAA;
	}
	public String getVREME_DOLASKAA() {
		return VREME_DOLASKAA;
	}
	public void setVREME_DOLASKAA(String vREME_DOLASKAA) {
		VREME_DOLASKAA = vREME_DOLASKAA;
	}
	public String getVREME_POLASKAR() {
		return VREME_POLASKAR;
	}
	public void setVREME_POLASKAR(String vREME_POLASKAR) {
		VREME_POLASKAR = vREME_POLASKAR;
	}
	public String getVREME_DOLASKAR() {
		return VREME_DOLASKAR;
	}
	public void setVREME_DOLASKAR(String vREME_DOLASKAR) {
		VREME_DOLASKAR = vREME_DOLASKAR;
	}
	public double getCENA() {
		return CENA;
	}
	public void setCENA(double cENA) {
		CENA = cENA;
	}
	public String getPOVLASTICA_NAZIV() {
		return POVLASTICA_NAZIV;
	}
	public void setPOVLASTICA_NAZIV(String pOVLASTICA_NAZIV) {
		POVLASTICA_NAZIV = pOVLASTICA_NAZIV;
	}
	public String getBROJ_LEGITIMACIJE() {
		return BROJ_LEGITIMACIJE;
	}
	public void setBROJ_LEGITIMACIJE(String bROJ_LEGITIMACIJE) {
		BROJ_LEGITIMACIJE = bROJ_LEGITIMACIJE;
	}
	public String getUZRAST() {
		return UZRAST;
	}
	public void setUZRAST(String uZRAST) {
		UZRAST = uZRAST;
	}
	public String getDATUM_RODJENJA() {
		return DATUM_RODJENJA;
	}
	public void setDATUM_RODJENJA(String dATUM_RODJENJA) {
		DATUM_RODJENJA = dATUM_RODJENJA;
	}
	public String getSTRANA() {
		return STRANA;
	}
	public void setSTRANA(String sTRANA) {
		STRANA = sTRANA;
	}
	public String getBAR_KOD39_URL() {
		return BAR_KOD39_URL;
	}
	public void setBAR_KOD39_URL(String bAR_KOD39_URL) {
		BAR_KOD39_URL = bAR_KOD39_URL;
	}
	public String getBAR_KOD_ASTEC_URL() {
		return BAR_KOD_ASTEC_URL;
	}
	public void setBAR_KOD_ASTEC_URL(String bAR_KOD_ASTEC_URL) {
		BAR_KOD_ASTEC_URL = bAR_KOD_ASTEC_URL;
	}
	public String getIME_PUTNIKA() {
		return IME_PUTNIKA;
	}
	public void setIME_PUTNIKA(String iME_PUTNIKA) {
		IME_PUTNIKA = iME_PUTNIKA;
	}
	public String getPREZIME_PUTNIKA() {
		return PREZIME_PUTNIKA;
	}
	public void setPREZIME_PUTNIKA(String pREZIME_PUTNIKA) {
		PREZIME_PUTNIKA = pREZIME_PUTNIKA;
	}

	@Override
	public String toString() {
		return "KartaStampaBean [REDNI_BROJ=" + REDNI_BROJ + ", DATUM_KUPOVINE=" + DATUM_KUPOVINE
				+ ", NAZIV_STANICE_ODA=" + NAZIV_STANICE_ODA + ", NAZIV_STANICE_DOA=" + NAZIV_STANICE_DOA
				+ ", NAZIV_STANICE_ODR=" + NAZIV_STANICE_ODR + ", NAZIV_STANICE_DOR=" + NAZIV_STANICE_DOR + ", VIA="
				+ VIA + ", VIAR=" + VIAR + ", SMER=" + SMER + ", BROJ_KARTE=" + BROJ_KARTE + ", RAZREDA=" + RAZREDA
				+ ", RAZREDR=" + RAZREDR + ", VAZI_OD=" + VAZI_OD + ", VAZI_DO=" + VAZI_DO + ", VOZA=" + VOZA
				+ ", VOZR=" + VOZR + ", VREME_POLASKAA=" + VREME_POLASKAA + ", VREME_DOLASKAA=" + VREME_DOLASKAA
				+ ", VREME_POLASKAR=" + VREME_POLASKAR + ", VREME_DOLASKAR=" + VREME_DOLASKAR + ", CENA=" + CENA
				+ ", POVLASTICA_NAZIV=" + POVLASTICA_NAZIV + ", BROJ_LEGITIMACIJE=" + BROJ_LEGITIMACIJE + ", UZRAST="
				+ UZRAST + ", DATUM_RODJENJA=" + DATUM_RODJENJA + ", STRANA=" + STRANA + ", BAR_KOD39_URL="
				+ BAR_KOD39_URL + ", BAR_KOD_ASTEC_URL=" + BAR_KOD_ASTEC_URL + ", IME_PUTNIKA=" + IME_PUTNIKA
				+ ", PREZIME_PUTNIKA=" + PREZIME_PUTNIKA + ", REZERVACIJA_A=" + REZERVACIJA_A + ", REZERVACIJA_R="
				+ REZERVACIJA_R + "]";
	}

	
	

	
	

}
