package test.application.cardpayment;

import test.application.cardpayment.models.CancelCurrentTransactionDataResponse;
import test.application.cardpayment.models.CardDataResponse;
import test.application.cardpayment.models.ErrorDataResponse;
import test.application.cardpayment.models.ExtendedErrorDataResponse;
import test.application.cardpayment.models.ExtendedHoldDataResponse;
import test.application.cardpayment.models.HoldDataResponse;
import test.application.cardpayment.models.TransactionDataResponse;

public interface IDevice {
	
	public void receivedTransactionResponseEvent(TransactionDataResponse transactionDataResponse);
	
	public void receivedErrorResponseEvent(ErrorDataResponse errorDataResponse);
	
	public void receivedExtendedErrorResponseEvent(ExtendedErrorDataResponse extendedErrorDataResponse);
	
	public void receivedHoldResponseEvent(HoldDataResponse holdDataResponse);
	
	public void receivedExtendedHoldResponseEvent(ExtendedHoldDataResponse extendedHoldDataResponse);
	
	public void receivedCancelCurrentTransactionResponseEvent(CancelCurrentTransactionDataResponse cancelCurrentTransactionDataResponse);
	
	public void receivedCardResponseEvent(CardDataResponse cardDataResponse);

}
