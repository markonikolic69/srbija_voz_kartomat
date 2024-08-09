package application;

public interface IPaymentCallbackInfo {
	
	
	public void setPaymentSessionMessage(boolean is_uspesna_kupovina, String message, int broj_putnika,
			String fiskal_journal, String fiskal_qrcode);

}
