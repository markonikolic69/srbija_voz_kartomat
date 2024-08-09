package application.fiskalizacija;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;


public class InvoiceRequestBean {
	
	private String dateAndTimeOfIssue = "";
	private String cashier = "";
	private String buyerId = null;
	private String buyerCostCenterId = null;
	private String invoiceType = "Normal";
	private String transactionType = "Sale";
	private List<PaymentBean> payment = new ArrayList<PaymentBean>();
	private String invoiceNumber = "";
	private String referentDocumentNumber = null;
	private String referentDocumentDT = null;
	private OptionsBean options = new OptionsBean();
	private List<ItemBean> items = new ArrayList<ItemBean>();


	
	public InvoiceRequestBean(List<String> br_vozne_karte_amount, String invoice_number, 
			  PoreskaStopa poreska_stopa) {
        SimpleDateFormat format = new SimpleDateFormat(
        	    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateAndTimeOfIssue = format.format(new java.util.Date());
		
        //cashier = null;
		cashier = "1111";//samo za test
        
        invoiceNumber = invoice_number;//InvoiceUtil.getInvoiceNumber(seq_num, tio_num, "I");
        double ukupno_amount = 0;
        for(String current : br_vozne_karte_amount) {
        	String[] current_vk_amount = current.split(":");
        	ItemBean current_item = new ItemBean(poreska_stopa);
        	current_item.setName("VK:" + current_vk_amount[0]);
        	current_item.setQuantity(1);
            double current_amont = Double.parseDouble(current_vk_amount[1]);
            ukupno_amount = ukupno_amount + current_amont;
        	current_item.setUnitPrice(current_amont);
        	current_item.setTotalAmount(current_amont);
            items.add(current_item);
        }
        
       
        PaymentBean payment_bean = new PaymentBean();
        payment_bean.setAmount(ukupno_amount);
        payment_bean.setPaymentType("Card");
        payment.add(payment_bean);

	}
	
	public String getDateAndTimeOfIssue() {
		return dateAndTimeOfIssue;
	}


	public void setDateAndTimeOfIssue(String dateAndTimeOfIssue) {
		this.dateAndTimeOfIssue = dateAndTimeOfIssue;
	}


	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerCostCenterId() {
		return buyerCostCenterId;
	}

	public void setBuyerCostCenterId(String buyerCostCenterId) {
		this.buyerCostCenterId = buyerCostCenterId;
	}

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

	public List<PaymentBean> getPayment() {
		return payment;
	}

	public void setPayment(List<PaymentBean> payment) {
		this.payment = payment;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public OptionsBean getOptions() {
		return options;
	}

	public void setOptions(OptionsBean options) {
		this.options = options;
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
	}
	
	


	public String getReferentDocumentNumber() {
		return referentDocumentNumber;
	}

	public void setReferentDocumentNumber(String referentDocumentNumber) {
		this.referentDocumentNumber = referentDocumentNumber;
	}

	public String getReferentDocumentDT() {
		return referentDocumentDT;
	}

	public void setReferentDocumentDT(String referentDocumentDT) {
		this.referentDocumentDT = referentDocumentDT;
	}
	

	
	
	

	@Override
	public String toString() {
		return "InvoiceRequestBean [cashier=" + cashier + ", buyerId=" + buyerId + ", buyerCostCenterId="
				+ buyerCostCenterId + ", invoiceType=" + invoiceType + ", transactionType=" + transactionType
				+ ", payment=" + payment + ", invoiceNumber=" + invoiceNumber + ", referentDocumentNumber="
				+ referentDocumentNumber + ", referentDocumentDT=" + referentDocumentDT + ", options=" + options
				+ ", items=" + items +  "]";
	}

	public static void main(String[] args) {
//        //String strDateTime = "2011-08-12T20:17:46.384Z";
//
//        Instant instant = Instant.now();//parse(strDateTime);
//        //dodaj dva sata
//        instant.plusMillis(1000 * 60 * 120);
//        OffsetDateTime odt = OffsetDateTime.now();//parse(strDateTime);
//        ZonedDateTime zdt = ZonedDateTime.now();//parse(strDateTime);
//        
//        System.out.println(instant);
//        System.out.println(odt);
//        System.out.println(zdt);
//        
//        
//        SimpleDateFormat format = new SimpleDateFormat(
//        	    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        	
//        System.out.println(format.format(new java.util.Date()));	

        	
    }
}
