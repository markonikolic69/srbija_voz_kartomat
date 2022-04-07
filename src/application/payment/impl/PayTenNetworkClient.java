package application.payment.impl;



import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

import application.IPaymentCallbackInfo;
import application.payment.MessageStringConstants;
import application.payment.PayTenPaymentIface;
import application.payment.PaymentException;
import application.payment.impl.helpers.MessageByteConstants;
import application.payment.impl.helpers.MessageIdentifiers;
import application.payment.impl.helpers.ProtocolHelper;
import application.payment.impl.helpers.TransactionTypesConstants;
import application.payment.impl.models.*;



public class PayTenNetworkClient implements PayTenPaymentIface{
	
	
	private static final int MinimalMessageLenght = 4;
    private static Client _client;
    
    private String _ipAddress = "";
    private int _port = 0;

    
    
    private Queue<Byte> _byteQueue = new LinkedList<Byte>();
    
    
    public PayTenNetworkClient(String ipAddress, int port) {
    	_ipAddress = ipAddress;
    	_port = port;
    }
    
////////////////////////////////////////PayTenPaymentIface implementation/////////////////////////////////////////////////
    
	public TransactionDataResponse payForTicket(double amount, String order_id, String merchant_id) throws PaymentException{
		System.out.println("--> payForTicket, amount = " + amount + ", order_id = " + order_id + ", merchant_id = " + merchant_id);
		TransactionDataRequest transactionRequest = new TransactionDataRequest();
		transactionRequest.set_transactionType(TransactionTypesConstants.SALE);
		transactionRequest.set_transactionAmount1(String.format("%012d", (int)amount*100));
		transactionRequest.set_amountCurrency("941");
		transactionRequest.set_printerFlag("0");
		transactionRequest.set_languageId("02");//serbian
		String dataToSend = ProtocolHelper.createSendMessage(transactionRequest.create());

		System.out.println("try to open connection ...");
		open();
		System.out.println("connection opened, try to start initialization ...");
		String transaction_response = _client.sendTransaction(dataToSend, true);
		System.out.println("Kraj, dobio je objekat transaction_response = " + transaction_response);
		TransactionDataResponse to_return = new TransactionDataResponse();
		to_return.fillObject(transaction_response);
		return to_return;
		

	}
	
	public void cancelCurrentPayment(String identifier) throws PaymentException{
		byte[] cancel_previous_transaction = new byte[] {0x02, 0x02, 0x32, 0x33, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x03, 0x02};
		if(!is_port_open()) {
			if( !open() ) {
				throw new PaymentException("Problem u komunikaciji, prijavite gresku u kupovini preko kartomata");
			}
		}
		_client.sendTransaction(new String(cancel_previous_transaction, StandardCharsets.US_ASCII), false);
	}
	
    public boolean close()
    {
        try
        {
            _client.disconnect();


            return true;
        }
        catch(Throwable e)
        {
            return false;
        }
    }

////////////////////////////////////end of PayTenPaymentIface impmementation////////////////////////////////////////////    
    private boolean is_port_open(){
    	return _client == null ? false : _client.IsConnected(); 
    }
    
    

    private boolean open()
    {
        // Connect to a remote device.  
        try
        {

            _client = new Client();

            _client.connectToServerNoThread(_ipAddress, _port);



            return true;
        }
        catch (Exception e)
        {
            System.out.println("unable to connect to payment terminal, details : " + e.getMessage());
            return false;
        }
    }
    
    
    private boolean send(String data)
    {
        try
        {
            if (_client!=null && !_client.IsConnected())
            {
                _client.connectToServer(_ipAddress, _port);
            }

            _client.sendImmediate(data.getBytes(StandardCharsets.US_ASCII));
            
            
            while(_client.getBuffer().currentReadByteCount == 0) {
            	Thread.sleep(1000);
            }
            _client_OnDataReceived(_client.getBuffer().readBuffer, _client.getBuffer().currentReadByteCount);
            parse(_byteQueue);
            return true;
        }
        catch (IOException ioe)
        {
        	System.out.println("IOException when try to send data, details: " + ioe.getMessage());
        }
        catch (Exception e)
        {
        	System.out.println("Unknown when try to send data, details: " + e.getMessage());
        }
        return false;
    }
    
    private void _client_OnDataReceived(byte[] data, int bytesRead)
    {
        try
        {
            for (int i = 0; i < bytesRead; i++)
            {
                _byteQueue.offer(Byte.valueOf(data[i]));
            }
        }
        catch(Exception e)
        {
            // ignored
        }
    }
    

    
    public boolean startTransaction(TransactionDataRequest transactionData)
    {
    	String dataToSend = ProtocolHelper.createSendMessage(transactionData.create());


    	return send(dataToSend);


    }
    
    public boolean cancelTransaction(CancelCurrentTransactionDataRequest cancelCurrentTransactionDataRequest)
    {
    	if (cancelCurrentTransactionDataRequest == null) return false;

    	String dataToSend = ProtocolHelper.createSendMessage(cancelCurrentTransactionDataRequest.Create());


    	return send(dataToSend);

    }
	
    
    public boolean sendMessageString(String message)
    {
    	String dataToSend = ProtocolHelper.createSendMessage(message);


    	return send(dataToSend);

    }
    
    private byte[] toPrimitive(Byte[] array) {
    	int j=0;
    	byte[] bytes = new byte[array.length];
    	for(Byte b: array)
    	    bytes[j++] = b.byteValue();
    	
    	return bytes;
    }
    
    
    private void parse( Queue<Byte> byteQueue)
    {
        if (byteQueue.size() < MinimalMessageLenght)
        {
            return;
        }
        
        if (byteQueue.peek() == Byte.valueOf(MessageByteConstants.STX) && byteQueue.contains(Byte.valueOf(MessageByteConstants.ETX)))
        {
            List<Byte> message = new LinkedList<Byte>();

            while (byteQueue.size() > 0)
            {
                if (byteQueue.peek().byteValue() == MessageByteConstants.ETX && byteQueue.size() > 1)
                {
                    message.add(byteQueue.poll());

                    byte messageLrc = ProtocolHelper.calculateLRCByte(toPrimitive((Byte[])message.toArray()), 1, message.size() - 1);

                    if (messageLrc == byteQueue.peek())
                    {
                    	byte[] message_byte_array = toPrimitive((Byte[])message.toArray());
                        String identifier = ProtocolHelper.getIdentifier(message_byte_array);
                        String decodedMessage;

                        switch (identifier)
                        {
                            case MessageIdentifiers.TRANSACTION_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                TransactionDataResponse transactionDataResponse = new TransactionDataResponse();
                                transactionDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                               
                                break;

                            case MessageIdentifiers.ERROR_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                ErrorDataResponse errorDataResponse = new ErrorDataResponse();
                                errorDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                //_base.setPaymentSessionMessage("ODBIJENO");                                                                    
                                break;

                            case MessageIdentifiers.EXTENDED_ERROR_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                ExtendedErrorDataResponse extendedErrorDataResponse = new ExtendedErrorDataResponse();
                                extendedErrorDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                //_base.setPaymentSessionMessage("ODBIJENO"); 
                                break;
                            
                            case MessageIdentifiers.HOLD_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                HoldDataResponse holdDataResponse = new HoldDataResponse();
                                holdDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                //_base.setPaymentSessionMessage(holdDataResponse.get_displayMessage().trim()); 
                                break;
                            
                            case MessageIdentifiers.EXTENDED_HOLD_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                ExtendedHoldDataResponse extendedHoldDataResponse = new ExtendedHoldDataResponse();
                                extendedHoldDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                //_base.setPaymentSessionMessage(extendedHoldDataResponse.get_displayMessage().trim()); 
                                break;
                            
                            case MessageIdentifiers.CARD_DATA_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                CardDataResponse cardDataResponse = new CardDataResponse();
                                cardDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                
                                break;
                            
                            case MessageIdentifiers.CANCEL_CURRENT_TRANSACTION_RESPONSE
                            :
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                CancelCurrentTransactionDataResponse cancelCurrentTransactionResponse = new CancelCurrentTransactionDataResponse();
                                cancelCurrentTransactionResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                
                                break;
                            
                            default:
                                System.out.println("Unknown identifier.");
                                sendMessageString(MessageStringConstants.NAK);
                                break;
                        }
                        
                        message.clear();
                        break;
                    }
                }
                else
                {
                    message.add(byteQueue.poll());
                }
            }
        }
        else
        {
            do
            {
                Byte dataByte = byteQueue.peek();
                if (dataByte.byteValue() != MessageByteConstants.STX)
                {
                    byteQueue.poll();
                }
                else
                {
                    break;
                }
            }
            while (byteQueue.size() > 0);
        }
    }
    

    
	public static void main(String[] args) throws Exception{
		
		double amount = 123.3;
		String formated_double = String.format("%012d", (int)amount*100);
		System.out.println("formated_double = " + formated_double);
		//IPaymentCallbackInfo base = new DummyCallback();
		PayTenNetworkClient ptn_client = new PayTenNetworkClient("192.168.1.104", 3000);
		
		System.out.println("Kraj, dobio je objekat TransactionDataResponse = " + ptn_client.payForTicket(123, "SVC00003452", "J3REST123"));
//		
//		TransactionDataRequest transactionRequest = new TransactionDataRequest();
//		transactionRequest.set_transactionType(TransactionTypesConstants.INITIALIZATION);
//		transactionRequest.set_printerFlag("0");
//		transactionRequest.set_languageId("02");//serbian
//
//		System.out.println("try to open connection ...");
//		client.open();
//		System.out.println("connection opened, try to start initialization ...");
//		client.startTransaction(transactionRequest);
//		
//		System.out.println("initialization done");
		
	}
}
