package application.data;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.internal.LinkedTreeMap;


public class VozBean {

	private int odsifra = 0;
	private int dosifra = 0;
	private int idvoz = 0;
	private int brvoz = 0;
	private String nazivvoz = "";
	private int rang = 0;
	private String vremep = "";
	private String vremed = "";
	private String nazivod = "";
	private String nazivdo = "";
	private int idrel = 0;
	private int relkm = 0;
	private String ponuda = "";
	private double cenau = 0.0;
	private String trajanje_putovanja = "";
	private String datum_dolaska = "";
	private int bsM_R2 = 0;
	
	private int bsM_R1 = 0;
	private List<StanicaNaTrasiBean> etTrasaVoza = null;
	
	private String _datum_polaska = "";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	private String _trajanje_datum = "";
	public String get_trajanje_datum() {
		return _trajanje_datum;
	}

	public void set_trajanje_datum(String datum_polaska, String trajanje_datum) {
		_trajanje_datum = convertDateFormat(trajanje_datum);
		_trajanje_broj_dana = getNumDayFromToday(datum_polaska, _trajanje_datum) + 1;
	}
	
	public Calendar get_trajanje_datum_Calendar() {
		
		Calendar cal = Calendar.getInstance();
		try {
		Date datum_vazenja = sdf.parse(_trajanje_datum);
		cal.setTime(datum_vazenja);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return cal;
	}

	public int get_trajanje_broj_dana() {
		return _trajanje_broj_dana;
	}

	private int _trajanje_broj_dana = 0;
	
	private String _datum_vreme_polaska = "";
	
	private String _datum_vreme_povratka = "";
	
	private boolean _is_selected = false;
	
	private int br_slobodnih_mesta = 0;

	public int getBr_slobodnih_mesta() {
		return br_slobodnih_mesta;
	}

	public void setBr_slobodnih_mesta(int br_slobodnih_mesta) {
		this.br_slobodnih_mesta = br_slobodnih_mesta;
	}

	public boolean isRezervacioni_sistem() {
		return rezervacioni_sistem;
	}

	public void setRezervacioni_sistem(boolean rezervacioni_sistem) {
		this.rezervacioni_sistem = rezervacioni_sistem;
	}

	public boolean isSoko() {
		return soko;
	}

	public void setSoko(boolean soko) {
		this.soko = soko;
	}

	private boolean rezervacioni_sistem = false;

	private boolean soko = false;


	public VozBean (LinkedTreeMap gson_container, String datum_polaska) {

		odsifra = (int) Double.parseDouble(gson_container.getOrDefault("odsifra", 0).toString());
		dosifra = (int) Double.parseDouble(gson_container.getOrDefault("dosifra", 0).toString());
		idvoz = (int) Double.parseDouble(gson_container.getOrDefault("idvoz", 0).toString());
		brvoz = (int) Double.parseDouble(gson_container.getOrDefault("brvoz", 0).toString());
		nazivvoz = gson_container.getOrDefault("nazivvoz", "").toString();
		rang = (int) Double.parseDouble(gson_container.getOrDefault("rang", 0).toString());
		vremep = gson_container.getOrDefault("vremep", "").toString();
		vremed = gson_container.getOrDefault("vremed", "").toString();
		nazivod = gson_container.getOrDefault("nazivod", "").toString();
		nazivdo = gson_container.getOrDefault("nazivdo", "").toString();
		idrel = (int) Double.parseDouble(gson_container.getOrDefault("idrel", 0).toString());
		relkm = (int) Double.parseDouble(gson_container.getOrDefault("relkm", 0).toString());
		ponuda = gson_container.getOrDefault("ponuda", "").toString();
		cenau = Double.parseDouble(gson_container.getOrDefault("cenau", 0).toString());
		trajanje_putovanja = gson_container.getOrDefault("trajanje_putovanja", "").toString();
		datum_dolaska = gson_container.getOrDefault("datum_dolaska", "").toString().substring(0,10);
		br_slobodnih_mesta = (int) Double.parseDouble(gson_container.getOrDefault("br_slobodnih_mesta", 0).toString());
		rezervacioni_sistem = Boolean.parseBoolean(gson_container.getOrDefault("rezervacioni_sistem", "false").toString());
		soko = Boolean.parseBoolean(gson_container.getOrDefault("soko", "false").toString());
		bsM_R2 = (int) Double.parseDouble(gson_container.getOrDefault("bsM_R2", 0).toString());
		bsM_R1 = (int) Double.parseDouble(gson_container.getOrDefault("bsM_R1", 0).toString());
		String[] splt = datum_dolaska.split("-");
		datum_dolaska = splt[2] + "." + splt[1] + "." + splt[0];
		etTrasaVoza = new ArrayList<StanicaNaTrasiBean>();
		
		
		List outputList = (List)gson_container.getOrDefault("etTrasaVoza", new ArrayList<StanicaNaTrasiBean>() );
		
		for(Object current : outputList) {
			etTrasaVoza.add(new StanicaNaTrasiBean((LinkedTreeMap)current));
		}
		
		_datum_vreme_polaska = datum_polaska/*datum_dolaska.substring(0,10)*/ + " " + vremep;
		_datum_polaska = datum_polaska;
		_datum_vreme_povratka = datum_dolaska.substring(0,10) + " " + vremed;

	}
	
	public VozBean() {
		
	}
	
	public String getDatumPolaska() {
		//return datum_dolaska;
		return _datum_polaska;
	}
	
	public String getDatumPolaskaForApi() {
		String to_return  = datum_dolaska.replace(".", "-");
		return to_return;
	}
	
	public int getOdsifra() {
		return odsifra;
	}
	public void setOdsifra(int odsifra) {
		this.odsifra = odsifra;
	}
	public int getDosifra() {
		return dosifra;
	}
	public void setDosifra(int dosifra) {
		this.dosifra = dosifra;
	}
	public int getIdvoz() {
		return idvoz;
	}
	public void setIdvoz(int idvoz) {
		this.idvoz = idvoz;
	}
	public int getBrvoz() {
		return brvoz;
	}
	public void setBrvoz(int brvoz) {
		this.brvoz = brvoz;
	}
	public String getNazivvoz() {
		return nazivvoz;
	}
	public void setNazivvoz(String nazivvoz) {
		this.nazivvoz = nazivvoz;
	}
	public int getRang() {
		return rang;
	}
	public String getRangNaziv() {
//		switch(rang) {
//		case 1 : return "BRZI";
//		case 6 : return "REGIO";
//		default: return "" + rang;
//		}
		return "" + rang;
	}
	
	public String getRangOpis() {

		switch(rang) {
		case 0 : return "Pt";
		case 1 : return "B";
		case 2 : return "IC";
		case 4 : return "Re";
		case 6 : return "Re";
		case 7 : return "REx";
		default: return "" + rang;
		}

	}
	
	
	public void setRang(int rang) {
		this.rang = rang;
	}
	public String getVremep() {
		return vremep;
	}
	public void setVremep(String vremep) {
		this.vremep = vremep;
	}
	public String getVremed() {
		return vremed.trim();
	}
	public void setVremed(String vremed) {
		this.vremed = vremed.trim();
	}
	public String getNazivod() {
		return nazivod;
	}
	public void setNazivod(String nazivod) {
		this.nazivod = nazivod;
	}
	public String getNazivdo() {
		return nazivdo;
	}
	public void setNazivdo(String nazivdo) {
		this.nazivdo = nazivdo;
	}
	public int getIdrel() {
		return idrel;
	}
	public void setIdrel(int idrel) {
		this.idrel = idrel;
	}
	public int getRelkm() {
		return relkm;
	}
	public void setRelkm(int relkm) {
		this.relkm = relkm;
	}
	public String getPonuda() {
		return ponuda;
	}
	public void setPonuda(String ponuda) {
		this.ponuda = ponuda;
	}
	public double getCenau() {
		return cenau;
	}
	public void setCenau(double cenau) {
		this.cenau = cenau;
	}
	public String getTrajanje_putovanja() {
		return trajanje_putovanja;
	}
	public void setTrajanje_putovanja(String trajanje_putovanja) {
		this.trajanje_putovanja = trajanje_putovanja;
	}
	public String getDatum_dolaska() {
		return datum_dolaska;
	}
	public void setDatum_dolaska(String datum_dolaska) {
		this.datum_dolaska = datum_dolaska;
	}
	public List<StanicaNaTrasiBean> getEtTrasaVoza() {
		return etTrasaVoza;
	}
	public void setEtTrasaVoza(List<StanicaNaTrasiBean> etTrasaVoza) {
		this.etTrasaVoza = etTrasaVoza;
	}
	
	

	public String get_datum_vreme_polaska() {
		return _datum_vreme_polaska;
	}

	public void set_datum_vreme_polaska(String _datum_vreme_polaska) {
		this._datum_vreme_polaska = _datum_vreme_polaska;
	}

	public String get_datum_vreme_povratka() {
		return _datum_vreme_povratka;
	}

	public void set_datum_vreme_povratka(String _datum_vreme_povratka) {
		this._datum_vreme_povratka = _datum_vreme_povratka;
	}

	public boolean is_is_selected() {
		return _is_selected;
	}

	public void set_is_selected(boolean _is_selected) {
		this._is_selected = _is_selected;
	}
	
	public int getBsM_R2() {
		return bsM_R2;
	}

	public void setBsM_R2(int bsM_R2) {
		this.bsM_R2 = bsM_R2;
	}

	public int getBsM_R1() {
		return bsM_R1;
	}

	public void setBsM_R1(int bsM_R1) {
		this.bsM_R1 = bsM_R1;
	}


	@Override
	public String toString() {
		return "VozBean [odsifra=" + odsifra + ", dosifra=" + dosifra + ", idvoz=" + idvoz + ", brvoz=" + brvoz
				+ ", nazivvoz=" + nazivvoz + ", rang=" + rang + ", vremep=" + vremep + ", vremed=" + vremed
				+ ", nazivod=" + nazivod + ", nazivdo=" + nazivdo + ", idrel=" + idrel + ", relkm=" + relkm
				+ ", ponuda=" + ponuda + ", cenau=" + cenau + ", trajanje_putovanja=" + trajanje_putovanja
				+ ", datum_dolaska=" + datum_dolaska + ", etTrasaVoza=" + etTrasaVoza + ", _datum_vreme_polaska="
				+ _datum_vreme_polaska + ", _datum_vreme_povratka=" + _datum_vreme_povratka + ", _is_selected="
				+ _is_selected + ", br_slobodnih_mesta=" + br_slobodnih_mesta + ", rezervacioni_sistem="
				+ rezervacioni_sistem + ", soko=" + soko + ", bsM_R2=" + bsM_R2 + ", bsM_R1=" + bsM_R1 +"]";
	}
	
	private static String convertDateFormat(String old_format) {
		String[] parsed = old_format.split("/");
		int dan = Integer.parseInt(parsed[1]);
		int mesec = Integer.parseInt(parsed[0]);
		String dan_str = dan < 10 ? "0" + dan : "" + dan;
		String mesec_str = mesec < 10 ? "0" + mesec : "" + mesec;
		return dan_str + "." + mesec_str + "." + parsed[2];
	}
	 
	
	private static int getNumDayFromToday(String vazi_od, String vazi_do) {
		try {
	    
	    java.util.Date today = sdf.parse(vazi_od);
	    java.util.Date secondDate = sdf.parse(vazi_do);

	    long diffInMillies = Math.abs(secondDate.getTime() - today.getTime());
	    return (int)TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("getNumDayFromToday error, details: " + e.getMessage());
			return 0;
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println("getNumDayFromToday = " + getNumDayFromToday("03.11.2025", "03.11.2025"));
		
		System.out.println("getNumDayFromToday = " + getNumDayFromToday(convertDateFormat("11/3/2025"), convertDateFormat("11/17/2025")));
	}
	
	

}
