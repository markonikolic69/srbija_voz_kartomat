package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class KartaBean {
	
	
	private int ID_USER = 0;
    private String HASH = "";
    private String DATUM_KUPOVINE = "";
    private int ID_RELACIJE = 0;
    private int OD = 0;
    private int DO = 0;
    private String SMER = "";
    private int VIA = 0;
    private String VAZI_OD = "";
    private String VAZI_DO = "";
    private int VOZA = 0;
    private String RAZREDA = "";
    private String VREME_POLASKAA = "";
    private String VREME_DOLASKAA = "";
    private String RAZREDR = "";
    private int BROJ_PUTNIKA = 0;
    private double KURS_DINARA_ZA_EVRO = 0;
    private int UKUPNA_CENA = 0;
    private int PLACENA = 0;
    private String ETKARTADATA = "";
    
    
    
    private String _gde_kupljena = "";
    
	private String _id_karte = "";
    private String _povratna_via = "";
    private String _vreme_kupovine = "";
    private String _povratna_voz_id = "";
    private String _rang = "";
    private String _povratna_rang = "";
    private String _broj_putnika = "";
    private String _povlastice = "";
    private String _povlastice_osnov = "";
    private String _naziv_stanice_od = "";
    private String _ukupna_cena = "";
    
    public KartaBean() {
    	
    }
    
    public KartaBean(LinkedTreeMap gson_container) {
    	ID_USER = Integer.parseInt(gson_container.getOrDefault("ID_USER", 0).toString());
    	DATUM_KUPOVINE = gson_container.getOrDefault("DATUM_KUPOVINE", 0).toString();
    	ID_RELACIJE = Integer.parseInt(gson_container.getOrDefault("ID_RELACIJE", 0).toString());
    	OD = Integer.parseInt(gson_container.getOrDefault("OD", 0).toString());
    	DO = Integer.parseInt(gson_container.getOrDefault("DO", 0).toString());
    	SMER = gson_container.getOrDefault("SMER", 0).toString();
    	VIA = Integer.parseInt(gson_container.getOrDefault("VIA", 0).toString());
    	VAZI_OD = gson_container.getOrDefault("VAZI_OD", 0).toString();
    	VAZI_DO = gson_container.getOrDefault("VAZI_DO", 0).toString();
    	VOZA = Integer.parseInt(gson_container.getOrDefault("VOZA", 0).toString());
    	RAZREDA = gson_container.getOrDefault("RAZREDA", 0).toString();
    	VREME_POLASKAA = gson_container.getOrDefault("VREME_POLASKAA", 0).toString();
    	VREME_DOLASKAA = gson_container.getOrDefault("VREME_DOLASKAA", 0).toString();
    	RAZREDR = gson_container.getOrDefault("RAZREDR", 0).toString();
    	BROJ_PUTNIKA = Integer.parseInt(gson_container.getOrDefault("BROJ_PUTNIKA", 0).toString());
    	_broj_putnika = "" + BROJ_PUTNIKA;
    	UKUPNA_CENA = Integer.parseInt(gson_container.getOrDefault("UKUPNA_CENA", 0).toString());
    	_ukupna_cena = "" + UKUPNA_CENA;
    	_gde_kupljena = gson_container.getOrDefault("KUPLJENA_KOD", 0).toString();
    	_id_karte = gson_container.getOrDefault("KARTA_ID", 0).toString();
    	_povratna_via = gson_container.getOrDefault("VIAR", 0).toString();
    	_vreme_kupovine = gson_container.getOrDefault("VREME_KUPOVINE", 0).toString();
    	_povratna_voz_id = gson_container.getOrDefault("VOZR", 0).toString();
    	_rang = gson_container.getOrDefault("RANGA", 0).toString();
    	_povratna_rang = gson_container.getOrDefault("RANGR", 0).toString();
    	_povlastice = gson_container.getOrDefault("POVLASTICE", 0).toString();
    	_povlastice_osnov = gson_container.getOrDefault("POVLASTICE_OSNOV", 0).toString();
    	_naziv_stanice_od = gson_container.getOrDefault("STANICA_OD", 0).toString();
    	
    }
    
    
    private boolean is_povratna() {
    	return _povlastice_osnov.equals("POVRATNA");
    }
    public String get_gde_kupljena() {
		return _gde_kupljena;
	}
	public void set_gde_kupljena(String _gde_kupljena) {
		this._gde_kupljena = _gde_kupljena;
	}
	public String get_id_karte() {
		return _id_karte;
	}
	public void set_id_karte(String _id_karte) {
		this._id_karte = _id_karte;
	}
	public String get_povratna_via() {
		return _povratna_via;
	}
	public void set_povratna_via(String _povratna_via) {
		this._povratna_via = _povratna_via;
	}
	public String get_vreme_kupovine() {
		return _vreme_kupovine;
	}
	public void set_vreme_kupovine(String _vreme_kupovine) {
		this._vreme_kupovine = _vreme_kupovine;
	}
	public String get_povratna_voz_id() {
		return _povratna_voz_id;
	}
	public void set_povratna_voz_id(String _povratna_voz_id) {
		this._povratna_voz_id = _povratna_voz_id;
	}
	public String get_rang() {
		return _rang;
	}
	public void set_rang(String _rang) {
		this._rang = _rang;
	}
	public String get_povratna_rang() {
		return _povratna_rang;
	}
	public void set_povratna_rang(String _povratna_rang) {
		this._povratna_rang = _povratna_rang;
	}
	public String get_broj_putnika() {
		return _broj_putnika;
	}
	public void set_broj_putnika(String _broj_putnika) {
		this._broj_putnika = _broj_putnika;
	}
	public String get_povlastice() {
		return _povlastice;
	}
	public void set_povlastice(String _povlastice) {
		this._povlastice = _povlastice;
	}
	public String get_povlastice_osnov() {
		return _povlastice_osnov;
	}
	public void set_povlastice_osnov(String _povlastice_osnov) {
		this._povlastice_osnov = _povlastice_osnov;
	}

    public String get_ukupna_cena() {
		return _ukupna_cena;
	}
	public void set_ukupna_cena(String _ukupna_cena) {
		this._ukupna_cena = _ukupna_cena;
	}
	public String get_naziv_stanice_od() {
		return _naziv_stanice_od;
	}
	public void set_naziv_stanice_od(String _naziv_stanice_od) {
		this._naziv_stanice_od = _naziv_stanice_od;
	}
	public String get_naziv_stanice_do() {
		return _naziv_stanice_do;
	}
	public void set_naziv_stanice_do(String _naziv_stanice_do) {
		this._naziv_stanice_do = _naziv_stanice_do;
	}
	private String _naziv_stanice_do = "";
    
    
    
	public int getID_USER() {
		return ID_USER;
	}
	public void setID_USER(int iD_USER) {
		ID_USER = iD_USER;
	}
	public String getHASH() {
		return HASH;
	}
	public void setHASH(String hASH) {
		HASH = hASH;
	}
	public String getDATUM_KUPOVINE() {
		return DATUM_KUPOVINE;
	}
	public void setDATUM_KUPOVINE(String dATUM_KUPOVINE) {
		DATUM_KUPOVINE = dATUM_KUPOVINE;
	}
	public int getID_RELACIJE() {
		return ID_RELACIJE;
	}
	public void setID_RELACIJE(int iD_RELACIJE) {
		ID_RELACIJE = iD_RELACIJE;
	}
	public int getOD() {
		return OD;
	}
	public void setOD(int oD) {
		OD = oD;
	}
	public int getDO() {
		return DO;
	}
	public void setDO(int dO) {
		DO = dO;
	}
	public String getSMER() {
		return SMER;
	}
	public void setSMER(String sMER) {
		SMER = sMER;
	}
	public int getVIA() {
		return VIA;
	}
	public void setVIA(int vIA) {
		VIA = vIA;
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
	public int getVOZA() {
		return VOZA;
	}
	public void setVOZA(int vOZA) {
		VOZA = vOZA;
	}
	public String getRAZREDA() {
		return RAZREDA;
	}
	public void setRAZREDA(String rAZREDA) {
		RAZREDA = rAZREDA;
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
	public String getRAZREDR() {
		return RAZREDR;
	}
	public void setRAZREDR(String rAZREDR) {
		RAZREDR = rAZREDR;
	}
	public int getBROJ_PUTNIKA() {
		return BROJ_PUTNIKA;
	}
	public void setBROJ_PUTNIKA(int bROJ_PUTNIKA) {
		BROJ_PUTNIKA = bROJ_PUTNIKA;
	}
	public double getKURS_DINARA_ZA_EVRO() {
		return KURS_DINARA_ZA_EVRO;
	}
	public void setKURS_DINARA_ZA_EVRO(double kURS_DINARA_ZA_EVRO) {
		KURS_DINARA_ZA_EVRO = kURS_DINARA_ZA_EVRO;
	}
	public int getUKUPNA_CENA() {
		return UKUPNA_CENA;
	}
	public void setUKUPNA_CENA(int uKUPNA_CENA) {
		UKUPNA_CENA = uKUPNA_CENA;
	}
	public int getPLACENA() {
		return PLACENA;
	}
	public void setPLACENA(int pLACENA) {
		PLACENA = pLACENA;
	}
	public String getETKARTADATA() {
		return ETKARTADATA;
	}
	public void setETKARTADATA(String eTKARTADATA) {
		ETKARTADATA = eTKARTADATA;
	}
	@Override
	public String toString() {
		return "KartaBean [ID_USER=" + ID_USER + ", HASH=" + HASH + ", DATUM_KUPOVINE=" + DATUM_KUPOVINE
				+ ", ID_RELACIJE=" + ID_RELACIJE + ", OD=" + OD + ", DO=" + DO + ", SMER=" + SMER + ", VIA=" + VIA
				+ ", VAZI_OD=" + VAZI_OD + ", VAZI_DO=" + VAZI_DO + ", VOZA=" + VOZA + ", RAZREDA=" + RAZREDA
				+ ", VREME_POLASKAA=" + VREME_POLASKAA + ", VREME_DOLASKAA=" + VREME_DOLASKAA + ", RAZREDR=" + RAZREDR
				+ ", BROJ_PUTNIKA=" + BROJ_PUTNIKA + ", KURS_DINARA_ZA_EVRO=" + KURS_DINARA_ZA_EVRO + ", UKUPNA_CENA="
				+ UKUPNA_CENA + ", PLACENA=" + PLACENA + ", ETKARTADATA=" + ETKARTADATA + ", _gde_kupljena="
				+ _gde_kupljena + ", _id_karte=" + _id_karte + ", _povratna_via=" + _povratna_via + ", _vreme_kupovine="
				+ _vreme_kupovine + ", _povratna_voz_id=" + _povratna_voz_id + ", _rang=" + _rang + ", _povratna_rang="
				+ _povratna_rang + ", _broj_putnika=" + _broj_putnika + ", _povlastice=" + _povlastice
				+ ", _povlastice_osnov=" + _povlastice_osnov + "]";
	}
	
	public String for_printing() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("          VOZNA KARTA\n");
		buffer.append("  " + _gde_kupljena +  "    "+ _id_karte + "   " + DATUM_KUPOVINE + "   " + _vreme_kupovine +  "\n");
		buffer.append("A  VOZ  " + VOZA +  "  /"+ "         " + "R  VOZ  " + _povratna_voz_id + "   /" +   "\n");
		buffer.append("OD  " + _naziv_stanice_od +  "          "+ "DO  " +  _naziv_stanice_do +    "\n");
		if(is_povratna()) {
			buffer.append("DO  " + _naziv_stanice_do +  "          "+ "DO  " +  _naziv_stanice_od +   "\n");
		}
		buffer.append("VIA  " + VIA +  "            " +  "VIA " + _povratna_via  +   "\n");
		buffer.append("RANG      RAZRED    VAZI OD  " + VAZI_OD +   "RANG      RAZRED"   +   "\n");
		buffer.append(_rang + "  " + RAZREDA +   "VAZI DO  "  + VAZI_DO +  "    " + _povratna_rang + "   " + RAZREDR + "\n");
		buffer.append("BR. PUTNIKA    POVLASTICA    OSNOV    RSD:" + "\n");
		buffer.append(_broj_putnika + "    " + _povlastice + "       " + _povlastice + "    " + _povlastice_osnov + "  " + _ukupna_cena + "\n");
		buffer.append("    KB-2B  " +   "\n\n");
		buffer.append("\n\n\n\n\n\n\n");
		
		return buffer.toString();
	}
	
	
	
    
    


}
