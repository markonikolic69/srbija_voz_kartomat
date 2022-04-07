package test.application.cardpayment;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import test.application.cardpayment.helpers.ProtocolHelper;
import test.application.cardpayment.models.TransactionDataRequest;

import java.io.*;

public class Client implements Runnable{

	public class NetworkBuffer
	{
		public byte[] writeBuffer;
		public byte[] readBuffer;
		public int currentWriteByteCount;
		public int currentReadByteCount;
		public NetworkBuffer() {
			writeBuffer= new byte[writeBufferSize];
			readBuffer = new byte[readBufferSize];
			currentWriteByteCount= 0;
			currentReadByteCount = 0;
		}
	}


	private Socket tcpClient;
	private OutputStream output;
	private InputStream input;
	private NetworkBuffer buffer = new NetworkBuffer();
	private static final int writeBufferSize = 1024;
	private static final int readBufferSize = 1024;

	private boolean started = false;

	public NetworkBuffer getBuffer() {
		return buffer;
	}

	public void connectToServer(String ipAddress, int port) throws IOException
	{


		tcpClient = new Socket();

		SocketAddress socketAddress = new InetSocketAddress(ipAddress, port); 
		tcpClient.connect(socketAddress,100);
		output = tcpClient.getOutputStream();
		input = tcpClient.getInputStream();
		System.out.println("Connected to server, listening for packets");

				Thread t = new Thread(this);
				started = true;
				t.start();
	}



	public void run(){
		System.out.println("Thread running");
		int bytesRead;
		ByteBuffer bf = ByteBuffer.allocate(readBufferSize);
		while (started)
		{
			bytesRead = 0;
			boolean is_ack_received = false;
			try
			{
				//Blocks until a message is received from the server
				//bytesRead = input.read(buffer.readBuffer, 0, readBufferSize);
				byte[] current_byte = new byte[1];
				boolean NOT_end_of_message = true;
				
				//read what is available
				while (NOT_end_of_message/*input.available() != 0*/)
				{
					input.read(current_byte);
					//System.out.println("readed byte: " + Util.toHexString(current_byte));
					bytesRead++;
					bf.put(current_byte);
					if(current_byte[0] == MessageByteConstants.ETX || current_byte[0] == MessageByteConstants.ACK || current_byte[0] == MessageByteConstants.NAK) {
						if(current_byte[0] == MessageByteConstants.ACK) {
							is_ack_received = true;
							System.out.println("Stigao ACK: " + Util.toHexString(current_byte));
							break;
						}
						if(current_byte[0] == MessageByteConstants.NAK) {
							is_ack_received = false;
							System.out.println("Stigao NACK: " + Util.toHexString(current_byte));
							break;
						}
						if(current_byte[0] == MessageByteConstants.ETX) {
							is_ack_received = false;
							//iscitati jor LRC
							System.out.println("Stigao ETX: " + Util.toHexString(current_byte) + ", citam jos LRC kao zadnji bajt");
							input.read(current_byte);
							System.out.println("readed byte: " + Util.toHexString(current_byte));
							bytesRead++;
							bf.put(current_byte);
							
							break;
						}
					}
					//buffer.currentReadByteCount = input.read(buffer.readBuffer);
				    // Process nb bytes
				}
				//bf.get(buffer.readBuffer, 0, bytesRead);
				buffer.readBuffer = new byte[bytesRead];
						
						
				System.arraycopy(bf.array(), 0, buffer.readBuffer, 0, bytesRead);
				buffer.currentReadByteCount = bytesRead;
				if(buffer.readBuffer != null) {
					System.out.println("Bytes received from POS: " + Util.toHexString(buffer.readBuffer));
					System.out.println("Bytes received length: " + buffer.currentReadByteCount);
					if(is_ack_received) {
						byte[] cancel_previous_transaction = new byte[] {0x02, 0x02, 0x32, 0x33, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x03, 0x02};
						flushData(cancel_previous_transaction);
						//sendImmediate(cancel_previous_transaction);
						TransactionDataRequest transactionRequest = new TransactionDataRequest();
						transactionRequest.set_transactionType(TransactionTypesConstants.SALE);
						transactionRequest.set_transactionAmount1("00010000");
						transactionRequest.set_amountCurrency("941");
						transactionRequest.set_printerFlag("0");
						transactionRequest.set_languageId("02");//serbian
						String dataToSend = ProtocolHelper.createSendMessage(transactionRequest.create());
						System.out.println("Send transaction request SALE ");
						//sendImmediate(dataToSend.getBytes(StandardCharsets.US_ASCII));
						flushData(dataToSend.getBytes(StandardCharsets.US_ASCII));
					}else {
					if(bytesRead > 3) {
						//byte[] to_convert_to_string = new byte[bytesRead - 3];
						//System.arraycopy(buffer.readBuffer, 1, to_convert_to_string, 0, to_convert_to_string.length);
						String message = new String(buffer.readBuffer,1, bytesRead - 3,"ASCII");
						System.out.println("String poruka = " + message);
						if(message.startsWith("25") ) {
							System.out.println("Stiglo  Extend HOLD, vratiti ACK");
							//sendImmediate(new byte[] {MessageByteConstants.ACK});
							flushData(new byte[] {MessageByteConstants.ACK});
						}else {
							if(message.startsWith("10")) {
								TransactionDataRequest transactionRequest = new TransactionDataRequest();
								transactionRequest.set_transactionType(TransactionTypesConstants.SALE);
								transactionRequest.set_transactionAmount1("00010000");
								transactionRequest.set_amountCurrency("941");
								transactionRequest.set_printerFlag("0");
								transactionRequest.set_languageId("02");//serbian
								String dataToSend = ProtocolHelper.createSendMessage(transactionRequest.create());
								System.out.println("Send transaction request SALE ");
								//sendImmediate(dataToSend.getBytes(StandardCharsets.US_ASCII));
								flushData(dataToSend.getBytes(StandardCharsets.US_ASCII));
							}else {
								System.out.println("Stiglo  neocekivana poruka, vratiti NACK");
								//sendImmediate(new byte[] {MessageByteConstants.NAK});
								flushData(new byte[] {MessageByteConstants.NAK});
							}
						}
						

						
						
						
						
					}else {
						if(!is_ack_received) {
							System.out.println("Received NACK, nesto nije u redu ");
						}
//						System.out.println("Bytes received length: " + buffer.currentReadByteCount);
//						sendImmediate(MessageStringConstants.ACK.getBytes(StandardCharsets.US_ASCII));
						//sendMessageString(MessageStringConstants.ACK);
					}
					}
//					System.out.println("POSALJI NA OBRADU ");
				}
				bf.clear();
			}
			catch(IOException e )
			{
				//A socket error has occurred
				System.out.println("A socket error has occurred with the client socket " + tcpClient.toString());
				break;
			}

			if (bytesRead == 0)
			{
				//The server has disconnected
				break;
			}

			//            if (OnDataReceived != null)
			//            {
			//                //Send off the data for other classes to handle
			//                OnDataReceived(buffer.readBuffer, bytesRead);
			//            }
			try {
				Thread.sleep(15);
			}catch(Exception e) {

			}
		}

		started = false;
		disconnect();
	}



	/// <summary>
	/// Adds data to the packet to be sent out, but does not send it across the network
	/// </summary>
	/// <param name="data">The data to be sent</param>
	public void addToPacket(byte[] data) throws IOException
	{
		if (buffer.currentWriteByteCount + data.length > buffer.writeBuffer.length)
		{
			flushData();
		}

		System.arraycopy(data, 0, buffer.writeBuffer, buffer.currentWriteByteCount, data.length);//(data, 0, buffer.WriteBuffer, buffer.CurrentWriteByteCount, data.Length);
		buffer.currentWriteByteCount += data.length;
	}

	/// <summary>
	/// Flushes all outgoing data to the server
	/// </summary>
	public void flushData() throws IOException
	{
		System.out.println("Send data to POS: " + Util.toHexString(buffer.writeBuffer));
		System.out.println("Send data length: " + buffer.writeBuffer.length);
		System.out.println("Send data from 0 to " + buffer.currentWriteByteCount);
		output.write(buffer.writeBuffer, 0, buffer.currentWriteByteCount);
		output.flush();
		buffer.currentWriteByteCount = 0;
	}
	
	
	private void flushData(byte[] data) throws IOException
	{
		System.out.println("Send data to POS: " + Util.toHexString(data));
		output.write(data);
		output.flush();
		
	}

	/// <summary>
	/// Sends the byte array data immediately to the server
	/// </summary>
	/// <param name="data"></param>
	public void sendImmediate(byte[] data) throws IOException
	{
		System.out.println("sendImmediate: " + Util.toHexString(data));
		addToPacket(data);
		flushData();
	}

	/// <summary>
	/// Tells whether we're connected to the server
	/// </summary>
	/// <returns></returns>
	public boolean IsConnected()
	{
		return started && tcpClient.isConnected();
	}

	/// <summary>
	/// Disconnect from the server
	/// </summary>
	public void disconnect()
	{
		if (tcpClient == null)
		{
			return;
		}

		System.out.println("Disconnected from server");
		try {
			tcpClient.close();
		}catch(IOException e) {}

		started = false;
	}
	
	


}
