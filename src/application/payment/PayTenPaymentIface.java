package application.payment;

import application.payment.impl.models.TransactionDataResponse;

public interface PayTenPaymentIface {
	
	
	public TransactionDataResponse payForTicket(double amount, String order_id, String merchant_id) throws PaymentException;
	
	public void cancelCurrentPayment(String identifier) throws PaymentException;
	
	public boolean close();

}
