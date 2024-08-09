package application.fiskalizacija;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.internal.LinkedTreeMap;


public class InvoiceResponse {
	
	
	private String _esirNumber = "725/1.0";
	
	
	private String requestedBy = "";
	private String sdcDateTime = "";
	private String invoiceCounter = "";
	private String invoiceCounterExtension = "";
	private String invoiceNumber = "";
	
	private List<Item> taxItems = new ArrayList<Item>();
	
	private String verificationUrl = "";
	
	private String verificationQRCode = "";
	
	private String journal = "";
	
	private String tin = "";
	
	private String businessName = "";
	private String locationName = "";
	private String address = "";
	private String district = "";
	private String mrc = "";
	private String totalAmount = "";

	public InvoiceResponse(LinkedTreeMap gson_container) {
		requestedBy = gson_container.getOrDefault("requestedBy", "").toString();
		sdcDateTime = gson_container.getOrDefault("sdcDateTime", "").toString();
		invoiceCounter = gson_container.getOrDefault("invoiceCounter", "").toString();
		invoiceCounterExtension = gson_container.getOrDefault("invoiceCounterExtension", "").toString();
		invoiceNumber = gson_container.getOrDefault("invoiceNumber", "").toString();
		
		
		List outputList = (List)gson_container.getOrDefault("taxItems", new ArrayList<Item>() );
		
		for(Object current : outputList) {
			taxItems.add(new Item((LinkedTreeMap)current));
		}
		
		verificationUrl = gson_container.getOrDefault("verificationUrl", "").toString();
		
		verificationQRCode = gson_container.getOrDefault("verificationQRCode", "").toString();
		
		journal = gson_container.getOrDefault("journal", "").toString();
		tin = gson_container.getOrDefault("tin", "").toString();
		businessName = gson_container.getOrDefault("businessName", "").toString();
		locationName = gson_container.getOrDefault("locationName", "").toString();
		address = gson_container.getOrDefault("address", "").toString();
		district = gson_container.getOrDefault("district", "").toString();
		mrc = gson_container.getOrDefault("mrc", "").toString();
	}
	
	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}
	
	public InvoiceResponse() {
		
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getSdcDateTime() {
		return sdcDateTime;
	}

	public void setSdcDateTime(String sdcDateTime) {
		this.sdcDateTime = sdcDateTime;
	}

	public String getInvoiceCounter() {
		return invoiceCounter;
	}

	public void setInvoiceCounter(String invoiceCounter) {
		this.invoiceCounter = invoiceCounter;
	}

	public String getInvoiceCounterExtension() {
		return invoiceCounterExtension;
	}

	public void setInvoiceCounterExtension(String invoiceCounterExtension) {
		this.invoiceCounterExtension = invoiceCounterExtension;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public List<Item> getTaxItems() {
		return taxItems;
	}

	public void setTaxItems(List<Item> taxItems) {
		this.taxItems = taxItems;
	}

	public String getVerificationUrl() {
		return verificationUrl;
	}

	public void setVerificationUrl(String verificationUrl) {
		this.verificationUrl = verificationUrl;
	}

	public String getVerificationQRCode() {
		return verificationQRCode;
	}

	public void setVerificationQRCode(String verificationQRCode) {
		this.verificationQRCode = verificationQRCode;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String get_esirNumber() {
		return _esirNumber;
	}

	public void set_esirNumber(String _esirNumber) {
		this._esirNumber = _esirNumber;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getMrc() {
		return mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "InvoiceResponse [_esirNumber=" + _esirNumber + ", requestedBy=" + requestedBy + ", sdcDateTime="
				+ sdcDateTime + ", invoiceCounter=" + invoiceCounter + ", invoiceCounterExtension="
				+ invoiceCounterExtension + ", invoiceNumber=" + invoiceNumber + ", taxItems=" + taxItems
				+ ", verificationUrl=" + verificationUrl + ", verificationQRCode=" + verificationQRCode + ", journal="
				+ journal + ", tin=" + tin + ", businessName=" + businessName + ", locationName=" + locationName
				+ ", address=" + address + ", district=" + district + ", mrc=" + mrc + ", totalAmount=" + totalAmount
				+ "]";
	}

	
	
	
	
	

}
