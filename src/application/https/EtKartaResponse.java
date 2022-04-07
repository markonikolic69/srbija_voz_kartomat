package application.https;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

import application.data.StanicaNaTrasiBean;

public class EtKartaResponse {
	
	private int result = -1;
	private String error = "";
	private List<KartaStampaBean> karte = null;
	
	
	public EtKartaResponse (LinkedTreeMap gson_container){
		result = Integer.parseInt(gson_container.getOrDefault("result", 0).toString());
		error = "" + gson_container.getOrDefault("error", 0).toString();
		
		karte = new ArrayList<KartaStampaBean>();
		List outputList = (List)gson_container.getOrDefault("karte", new ArrayList<KartaStampaBean>() );
		
		for(Object current : outputList) {
			karte.add(new KartaStampaBean((LinkedTreeMap)current));
		}
	}


	public int getResult() {
		return result;
	}


	public void setResult(int result) {
		this.result = result;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	public List<KartaStampaBean> getKarte() {
		return karte;
	}


	public void setKarte(List<KartaStampaBean> karte) {
		this.karte = karte;
	}


	@Override
	public String toString() {
		return "EtKartaResponse [result=" + result + ", error=" + error + ", karte=" + karte + "]";
	}
	
	
	
	
	

}
