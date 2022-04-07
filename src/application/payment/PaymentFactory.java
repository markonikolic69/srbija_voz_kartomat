package application.payment;

import application.payment.impl.PayTenNetworkClient;


public class PaymentFactory {
	
	public static PayTenPaymentIface getPaymentInterface(String ipAddress, int port) {
		return new PayTenNetworkClient(ipAddress, port);
	}

}
