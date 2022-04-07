package test.application.cardpayment.models;

import test.application.cardpayment.MessageIdentifiers;
import test.application.cardpayment.MessageStringConstants;

public class CancelCurrentTransactionDataRequest implements IDataRequest {
	
	
	private String _identifier = MessageIdentifiers.CANCEL_CURRENT_TRANSACTION_REQUEST;

	@Override
	public String getIdentifier() {
		
		return _identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		// TODO Auto-generated method stub
		_identifier = identifier;
	}
	
    public String Create()
    {
    	StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MessageStringConstants.STX);
        stringBuilder.append(MessageStringConstants.STX);
        stringBuilder.append(_identifier);
        stringBuilder.append(MessageStringConstants.DEFAULT_VALUE);
        stringBuilder.append(MessageStringConstants.DEFAULT_VALUE);
        stringBuilder.append(MessageStringConstants.DEFAULT_VALUE);
        stringBuilder.append(MessageStringConstants.ETX);

        return stringBuilder.toString();
    }

}
