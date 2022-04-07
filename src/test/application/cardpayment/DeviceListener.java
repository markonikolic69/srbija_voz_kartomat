package test.application.cardpayment;

import test.application.cardpayment.models.CancelCurrentTransactionDataResponse;
import test.application.cardpayment.models.CardDataResponse;
import test.application.cardpayment.models.ErrorDataResponse;
import test.application.cardpayment.models.ExtendedErrorDataResponse;
import test.application.cardpayment.models.ExtendedHoldDataResponse;
import test.application.cardpayment.models.HoldDataResponse;
import test.application.cardpayment.models.TransactionDataResponse;

public class DeviceListener implements IDevice {

	@Override
	public void receivedTransactionResponseEvent(TransactionDataResponse transactionDataResponse) {
		System.out.println("received transactionDataResponse = " + transactionDataResponse);

	}

	@Override
	public void receivedErrorResponseEvent(ErrorDataResponse errorDataResponse) {
		System.out.println("received errorDataResponse = " + errorDataResponse);

	}

	@Override
	public void receivedExtendedErrorResponseEvent(ExtendedErrorDataResponse extendedErrorDataResponse) {
		System.out.println("received extendedErrorDataResponse = " + extendedErrorDataResponse);

	}

	@Override
	public void receivedHoldResponseEvent(HoldDataResponse holdDataResponse) {
		System.out.println("received holdDataResponse = " + holdDataResponse);

	}

	@Override
	public void receivedExtendedHoldResponseEvent(ExtendedHoldDataResponse extendedHoldDataResponse) {
		System.out.println("received extendedHoldDataResponse = " + extendedHoldDataResponse);

	}

	@Override
	public void receivedCancelCurrentTransactionResponseEvent(
			CancelCurrentTransactionDataResponse cancelCurrentTransactionDataResponse) {
		System.out.println("received cancelCurrentTransactionDataResponse = " + cancelCurrentTransactionDataResponse);// TODO Auto-generated method stub

	}

	@Override
	public void receivedCardResponseEvent(CardDataResponse cardDataResponse) {
		System.out.println("received cardDataResponse = " + cardDataResponse);

	}

}
