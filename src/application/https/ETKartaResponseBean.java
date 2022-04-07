package application.https;

import com.google.gson.internal.LinkedTreeMap;

public class ETKartaResponseBean {
	

	
	private int currency = -1;
	private String trantype = "";
	private String okUrl = "";
	private String failUrl = "";
	private String amount = "";
	private String oid = "";
	private String clientid = "";
	private String storetype = "";
	private String lang = "";
	private String hashAlgorithm = "";
	private String rnd = "";
	private String encoding = "";
	private String hash = "";

	//{"currency":941,"trantype":"Auth","okUrl":"https://testekarta.srbvoz.rs/etest21/SV/Index","failUrl":"https://testekarta.srbvoz.rs/etest21/SV/Index",
		//"amount":"300.00","oid":"SVKOID0000031","clientid":"13IN060595","storetype":"3d_pay_hosting","lang":"sr","hashAlgorithm":"ver2",
	//"rnd":"N2855INH5EM5T7VYL9LA","encoding":"utf-8","hash":"XmAZAy6X7ywMMA0lCHIxl/0UPVzrCEdu2WzZTUtwQEcI6KMJPzF4vTBIp3TcfLnkSI/QXFpMmU1RT5WFadzgaQ=="}
	
	
	public ETKartaResponseBean() {
		
	}
	
	public ETKartaResponseBean (LinkedTreeMap gson_container) {

		currency = Integer.parseInt(gson_container.getOrDefault("currency", 0).toString());
		trantype = gson_container.getOrDefault("trantype", 0).toString();
		okUrl = gson_container.getOrDefault("okUrl", 0).toString();
		failUrl = gson_container.getOrDefault("failUrl", 0).toString();
		amount = gson_container.getOrDefault("amount", 0).toString();
		oid = gson_container.getOrDefault("oid", 0).toString();
		clientid = gson_container.getOrDefault("clientid", 0).toString();
		storetype = gson_container.getOrDefault("storetype", 0).toString();
		lang = gson_container.getOrDefault("lang", 0).toString();
		hashAlgorithm = gson_container.getOrDefault("hashAlgorithm", 0).toString();
		rnd = gson_container.getOrDefault("rnd", 0).toString();
		encoding = gson_container.getOrDefault("encoding", 0).toString();
		hash = gson_container.getOrDefault("hash", 0).toString();

		
	}
	
	
	
	public int getCurrency() {
		return currency;
	}



	public void setCurrency(int currency) {
		this.currency = currency;
	}



	public String getTrantype() {
		return trantype;
	}



	public void setTrantype(String trantype) {
		this.trantype = trantype;
	}



	public String getOkUrl() {
		return okUrl;
	}



	public void setOkUrl(String okUrl) {
		this.okUrl = okUrl;
	}



	public String getFailUrl() {
		return failUrl;
	}



	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}



	public String getAmount() {
		String[] amt_splt = amount.split("\\.");
		System.out.println("amt_splt = " + amt_splt);
		System.out.println("amt_splt lwngth = " + amt_splt.length);
		if(amt_splt.length == 1) {
			return amt_splt[0];
		}else {
			return amt_splt[0] + "." + amt_splt[1];
		}
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	public String getOid() {
		return oid;
	}



	public void setOid(String oid) {
		this.oid = oid;
	}



	public String getClientid() {
		return clientid;
	}



	public void setClientid(String clientid) {
		this.clientid = clientid;
	}



	public String getStoretype() {
		return storetype;
	}



	public void setStoretype(String storetype) {
		this.storetype = storetype;
	}



	public String getLang() {
		return lang;
	}



	public void setLang(String lang) {
		this.lang = lang;
	}



	public String getHashAlgorithm() {
		return hashAlgorithm;
	}



	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}



	public String getRnd() {
		return rnd;
	}



	public void setRnd(String rnd) {
		this.rnd = rnd;
	}



	public String getEncoding() {
		return encoding;
	}



	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}



	public String getHash() {
		return hash;
	}



	public void setHash(String hash) {
		this.hash = hash;
	}



	public String get_order_id() {
		return oid;
	}

	public String get_merchant_id() {
		return clientid;
	}



	@Override
	public String toString() {
		return "ETKartaResponseBean [currency=" + currency + ", trantype=" + trantype + ", okUrl=" + okUrl
				+ ", failUrl=" + failUrl + ", amount=" + amount + ", oid=" + oid + ", clientid=" + clientid
				+ ", storetype=" + storetype + ", lang=" + lang + ", hashAlgorithm=" + hashAlgorithm + ", rnd=" + rnd
				+ ", encoding=" + encoding + ", hash=" + hash + "]";
	}


	
	

}
