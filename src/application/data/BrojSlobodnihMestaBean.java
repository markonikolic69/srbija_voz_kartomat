package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class BrojSlobodnihMestaBean {
	
	
	private int brojMesta = 0;
	private String slobodnaMesta = "";

	
	public BrojSlobodnihMestaBean (LinkedTreeMap gson_container) {

		brojMesta = Integer.parseInt(gson_container.getOrDefault("brojMesta", 0).toString());
		slobodnaMesta = gson_container.getOrDefault("slobodnaMesta", 0).toString();

		
	}


	public int getBrojMesta() {
		return brojMesta;
	}


	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}


	public String getSlobodnaMesta() {
		return slobodnaMesta;
	}


	public void setSlobodnaMesta(String slobodnaMesta) {
		this.slobodnaMesta = slobodnaMesta;
	}
	
	

}
