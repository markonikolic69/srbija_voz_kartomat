package test.application.cardpayment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

import test.application.cardpayment.helpers.ProtocolHelper;
import test.application.cardpayment.models.CancelCurrentTransactionDataRequest;
import test.application.cardpayment.models.CancelCurrentTransactionDataResponse;
import test.application.cardpayment.models.CardDataResponse;
import test.application.cardpayment.models.ErrorDataResponse;
import test.application.cardpayment.models.ExtendedErrorDataResponse;
import test.application.cardpayment.models.ExtendedHoldDataResponse;
import test.application.cardpayment.models.HoldDataResponse;
import test.application.cardpayment.models.TransactionDataRequest;
import test.application.cardpayment.models.TransactionDataResponse;

public class NetworkClient {
	
	
	private static final int MinimalMessageLenght = 4;
    private static Client _client;
    
    private String _ipAddress = "";
    private int _port = 0;

    
    private IDevice _base = null;
    
    
    private Queue<Byte> _byteQueue = new LinkedList<Byte>();
    
    
    public NetworkClient(String ipAddress, int port, IDevice base) {
    	_ipAddress = ipAddress;
    	_port = port;
    	_base = base;
    }

    
    public boolean is_port_open(){
    	return _client == null ? false : _client.IsConnected(); 
    }
    
    

    public boolean open()
    {
        // Connect to a remote device.  
        try
        {

            _client = new Client();

            _client.connectToServer(_ipAddress, _port);



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
    
    public boolean close()
    {
        try
        {
            _client.disconnect();


            return true;
        }
        catch(Exception ioe)
        {
            return false;
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
                                _base.receivedTransactionResponseEvent(transactionDataResponse);
                                break;

                            case MessageIdentifiers.ERROR_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                ErrorDataResponse errorDataResponse = new ErrorDataResponse();
                                errorDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                _base.receivedErrorResponseEvent(errorDataResponse);                                                                    
                                break;

                            case MessageIdentifiers.EXTENDED_ERROR_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                ExtendedErrorDataResponse extendedErrorDataResponse = new ExtendedErrorDataResponse();
                                extendedErrorDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                _base.receivedExtendedErrorResponseEvent(extendedErrorDataResponse);
                                break;
                            
                            case MessageIdentifiers.HOLD_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                HoldDataResponse holdDataResponse = new HoldDataResponse();
                                holdDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                _base.receivedHoldResponseEvent(holdDataResponse);
                                break;
                            
                            case MessageIdentifiers.EXTENDED_HOLD_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                ExtendedHoldDataResponse extendedHoldDataResponse = new ExtendedHoldDataResponse();
                                extendedHoldDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                _base.receivedExtendedHoldResponseEvent(extendedHoldDataResponse);
                                break;
                            
                            case MessageIdentifiers.CARD_DATA_RESPONSE:
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                CardDataResponse cardDataResponse = new CardDataResponse();
                                cardDataResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                _base.receivedCardResponseEvent(cardDataResponse);
                                break;
                            
                            case MessageIdentifiers.CANCEL_CURRENT_TRANSACTION_RESPONSE
                            :
                                decodedMessage = ProtocolHelper.getDecodedMessage(message_byte_array,true);
                                CancelCurrentTransactionDataResponse cancelCurrentTransactionResponse = new CancelCurrentTransactionDataResponse();
                                cancelCurrentTransactionResponse.fillObject(decodedMessage.substring(0, decodedMessage.length()));
                                sendMessageString(MessageStringConstants.ACK);
                                _base.receivedCancelCurrentTransactionResponseEvent(cancelCurrentTransactionResponse);
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
		NetworkClient client = new NetworkClient("192.168.1.104", 3000, new DeviceListener());
		
		TransactionDataRequest transactionRequest = new TransactionDataRequest();
		transactionRequest.set_transactionType(TransactionTypesConstants.INITIALIZATION);
		transactionRequest.set_printerFlag("0");
		transactionRequest.set_languageId("02");//serbian

		System.out.println("try to open connection ...");
		client.open();
		System.out.println("connection opened, try to start initialization ...");
		client.startTransaction(transactionRequest);
		
		System.out.println("initialization done");
		
	}

}
