package application.data;

import java.util.ArrayList;
import java.util.List;

public class EtKartaBean {
	
	private VozDataBean vozData = null;
	private List<CenaBean> cenasmer1 = null;
	private List<CenaBean> cenasmer2 = null;
	private SifraBean sifraod = null;
	private SifraBean sifrado = null;
	
	private int smer = 0;
	private double ukupnacena = 0;
	
	private List<PutniciBean> putnici = new ArrayList<PutniciBean>();
	
	private List<String> uneseneleg = new ArrayList<String>();

	public VozDataBean getVozData() {
		return vozData;
	}

	public void setVozData(VozDataBean vozData) {
		this.vozData = vozData;
	}

	public List<CenaBean> getCenasmer1() {
		return cenasmer1;
	}

	public void setCenasmer1(List<CenaBean> cenasmer1) {
		this.cenasmer1 = cenasmer1;
	}

	public List<CenaBean> getCenasmer2() {
		return cenasmer2;
	}

	public void setCenasmer2(List<CenaBean> cenasmer2) {
		this.cenasmer2 = cenasmer2;
	}

	public SifraBean getSifraod() {
		return sifraod;
	}

	public void setSifraod(SifraBean sifraod) {
		this.sifraod = sifraod;
	}

	public SifraBean getSifrado() {
		return sifrado;
	}

	public void setSifrado(SifraBean sifrado) {
		this.sifrado = sifrado;
	}

	public int getSmer() {
		return smer;
	}

	public void setSmer(int smer) {
		this.smer = smer;
	}

	public double getUkupnacena() {
		return ukupnacena;
	}

	public void setUkupnacena(double ukupnacena) {
		this.ukupnacena = ukupnacena;
	}

	public List<PutniciBean> getPutnici() {
		return putnici;
	}

	public void setPutnici(List<PutniciBean> putnici) {
		this.putnici = putnici;
	}

	public List<String> getUneseneleg() {
		return uneseneleg;
	}

	public void setUneseneleg(List<String> uneseneleg) {
		this.uneseneleg = uneseneleg;
	}

	@Override
	public String toString() {
		return "EtKartaBean [vozData=" + vozData + ", cenasmer1=" + cenasmer1 + ", cenasmer2=" + cenasmer2
				+ ", sifraod=" + sifraod + ", sifrado=" + sifrado + ", smer=" + smer + ", ukupnacena=" + ukupnacena
				+ ", putnici=" + putnici + ", uneseneleg=" + uneseneleg + "]";
	}
	
	
	
	
	
}
