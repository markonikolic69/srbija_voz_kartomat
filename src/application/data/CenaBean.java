package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class CenaBean implements Cloneable {
	
	
	private double cenau = 0.0;
	private double cenao = 0.0;
	private double cenad = 0.0;
	private double cenab = 0.0;
	private double cenar = 0.0;
	private int tiprez = 0;
	
	public CenaBean (LinkedTreeMap gson_container) {

		cenau = Double.parseDouble(gson_container.getOrDefault("cenau", 0).toString());
		cenao = Double.parseDouble(gson_container.getOrDefault("cenao", 0).toString());
		cenad = Double.parseDouble(gson_container.getOrDefault("cenad", 0).toString());
		cenab = Double.parseDouble(gson_container.getOrDefault("cenab", 0).toString());
		cenar = Double.parseDouble(gson_container.getOrDefault("cenar", 0).toString());
		tiprez = (int) Double.parseDouble(gson_container.getOrDefault("tiprez", 0).toString());
		
	}
	
	public CenaBean () {
		
	}
	
	public double getCenau() {
		return cenau;
	}
	public void setCenau(double cenau) {
		this.cenau = cenau;
	}
	public double getCenao() {
		return cenao;
	}
	public void setCenao(double cenao) {
		this.cenao = cenao;
	}
	public double getCenad() {
		return cenad;
	}
	public void setCenad(double cenad) {
		this.cenad = cenad;
	}
	public double getCenab() {
		return cenab;
	}
	public void setCenab(double cenab) {
		this.cenab = cenab;
	}
	public double getCenar() {
		return cenar;
	}
	public void setCenar(double cenar) {
		this.cenar = cenar;
	}
	public int getTiprez() {
		return tiprez;
	}
	public void setTiprez(int tiprez) {
		this.tiprez = tiprez;
	}
	@Override
	public String toString() {
		return "CenaBean [cenau=" + cenau + ", cenao=" + cenao + ", cenad=" + cenad + ", cenab=" + cenab + ", cenar="
				+ cenar + ", tiprez=" + tiprez + "]";
	}
	
	
	public Object clone() 
    {
		try {
        return super.clone();
		}catch(Exception e) {
			return this;
		}
    }

}
