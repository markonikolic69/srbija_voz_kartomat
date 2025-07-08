package application.transactionreport;


public class FiskalizacijaTransactionDto {
	


	    private String invoiceType;
	    
	    private String transactionType;
	    
	    private String payment;
	    
	    private Double amount;
	    
	    private String request_id;
	    
	    private String PAC;
	    
	    private String requestedBy;
	    
	    private String sdcDateTime;
	    
	    private String invoiceCounter;
	    
	    private String invoiceCounterExtension;
	    
	    private String invoiceNumber;

	    private String verificationUrl;

	    private String verificationQRCode;

	    private String journal;

	    private String tin;

	    private String businessName;

	    private String locationName;

	    private String address;

	    private String district;

	    private String mrc;

	    private int totalAmount;

		public String getInvoiceType() {
			return invoiceType;
		}

		public void setInvoiceType(String invoiceType) {
			this.invoiceType = invoiceType;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}

		public String getPayment() {
			return payment;
		}

		public void setPayment(String payment) {
			this.payment = payment;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public String getRequest_id() {
			return request_id;
		}

		public void setRequest_id(String request_id) {
			this.request_id = request_id;
		}

		public String getPAC() {
			return PAC;
		}

		public void setPAC(String pAC) {
			PAC = pAC;
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

		public String getTin() {
			return tin;
		}

		public void setTin(String tin) {
			this.tin = tin;
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

		public int getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(int totalAmount) {
			this.totalAmount = totalAmount;
		}

		@Override
		public String toString() {
			return "FiskalizacijaTransactionDto [invoiceType=" + invoiceType + ", transactionType=" + transactionType
					+ ", payment=" + payment + ", amount=" + amount + ", request_id=" + request_id + ", PAC=" + PAC
					+ ", requestedBy=" + requestedBy + ", sdcDateTime=" + sdcDateTime + ", invoiceCounter="
					+ invoiceCounter + ", invoiceCounterExtension=" + invoiceCounterExtension + ", invoiceNumber="
					+ invoiceNumber + ", verificationUrl=" + verificationUrl + ", verificationQRCode="
					+ verificationQRCode + ", journal=" + journal + ", tin=" + tin + ", businessName=" + businessName
					+ ", locationName=" + locationName + ", address=" + address + ", district=" + district + ", mrc="
					+ mrc + ", totalAmount=" + totalAmount + "]";
		}
	    
	    

}
