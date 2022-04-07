package application.data;

import java.util.ArrayList;
import java.util.List;

public class VozDataBean {
	private String datum1 = "";
	private int brojputnika = 0;
	private int razred = 0;
	
	private VozBean voz = new VozBean();
	private VozBean vozp = new VozBean();
	
	private String datum2 = "";
	private int razredp = 0;
	private int cenauk = 0;
	public String getDatum1() {
		return datum1;
	}
	public void setDatum1(String datum1) {
		this.datum1 = datum1;
	}
	public int getBrojputnika() {
		return brojputnika;
	}
	public void setBrojputnika(int brojputnika) {
		this.brojputnika = brojputnika;
	}
	public int getRazred() {
		return razred;
	}
	public void setRazred(int razred) {
		this.razred = razred;
	}
	public VozBean getVoz() {
		return voz;
	}
	public void setVoz(VozBean voz) {
		this.voz = voz;
	}
	public VozBean getVozp() {
		return vozp;
	}
	public void setVozp(VozBean vozp) {
		this.vozp = vozp;
	}
	public String getDatum2() {
		return datum2;
	}
	public void setDatum2(String datum2) {
		this.datum2 = datum2;
	}
	public int getRazredp() {
		return razredp;
	}
	public void setRazredp(int razredp) {
		this.razredp = razredp;
	}
	public int getCenauk() {
		return cenauk;
	}
	public void setCenauk(int cenauk) {
		this.cenauk = cenauk;
	}
	@Override
	public String toString() {
		return "VozDataBean [datum1=" + datum1 + ", brojputnika=" + brojputnika + ", razred=" + razred + ", voz=" + voz
				+ ", vozp=" + vozp + ", datum2=" + datum2 + ", razredp=" + razredp + ", cenauk=" + cenauk + "]";
	}
	
	
	
	
}
