package application.payment.impl;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
//import java.util.Arrays;

import org.apache.log4j.Logger;

//import application.IPaymentCallbackInfo;
//import application.payment.MessageStringConstants;
import application.payment.PaymentException;
import application.payment.impl.helpers.MessageByteConstants;
import application.payment.impl.helpers.MessageIdentifiers;
import application.payment.impl.helpers.TransactionTypesConstants;
import application.payment.impl.helpers.ProtocolHelper;
import application.payment.impl.models.*;
import application.util.HexUtil;


import java.io.*;

public class Client implements Runnable{


	private static final Logger logger = Logger.getLogger("Client");

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

	//ovo je vise za staru implementaciju, kada se startuje thread
	public Client() {

	}


	//	public Client(IPaymentCallbackInfo callback) {
	//		_callback = callback;
	//	}


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



	public void connectToServerNoThread(String ipAddress, int port) throws IOException
	{

		System.out.println("--> connectToServerNoThread(), ipAddress = " + ipAddress + ", port = " + port);
		logger.info("--> connectToServerNoThread(), ipAddress = " + ipAddress + ", port = " + port);
		tcpClient = new Socket();

		SocketAddress socketAddress = new InetSocketAddress(ipAddress, port); 
		tcpClient.connect(socketAddress,3000);
		output = tcpClient.getOutputStream();
		input = tcpClient.getInputStream();
		logger.info("Connected to server NO THREAD, listening for packets");


		started = true;

	}

	public void connectToServer(String ipAddress, int port) throws IOException
	{


		tcpClient = new Socket();

		SocketAddress socketAddress = new InetSocketAddress(ipAddress, port); 
		tcpClient.connect(socketAddress,100);
		output = tcpClient.getOutputStream();
		input = tcpClient.getInputStream();
		logger.info("Connected to server, listening for packets");

		Thread t = new Thread(this);
		started = true;
		t.start();
	}



	public String sendTransaction(String transaction_data_request, boolean send_initialization) throws PaymentException{
		System.out.println("Thread is NOT running");
		int bytesRead;
		ByteBuffer bf = ByteBuffer.allocate(readBufferSize);

		bytesRead = 0;
		//nova implementacija protokola, sad se ceka void a ne ack
		//boolean is_ack_received = false;
		//boolean is_void_received = false;

		String to_return = "";


		try {
			String dataToSend = transaction_data_request;
			logger.info("dataToSend = " + dataToSend);
			if(send_initialization) {
				TransactionDataRequest init_message = new TransactionDataRequest();
				init_message.set_transactionType(TransactionTypesConstants.INITIALIZATION);
				init_message.set_printerFlag("0");
				init_message.set_languageId("02");//serbian
				dataToSend = ProtocolHelper.createSendMessage(init_message.create());
			}
			sendImmediate(dataToSend.getBytes(StandardCharsets.US_ASCII));
			if(send_initialization) {
				logger.info("initialization done");
			}else {
				logger.info("NO initialization");
			}
		}catch(Exception e) {
			logger.error("ERROR, exception when try to send request for intialization, details: " + e.getMessage() +
					", caouse: " + e.getCause(), e);
			throw new PaymentException("Banka nedostupna, pokusajte kasnije");
		}



		try
		{

			boolean NOT_END_OF_SESSION= true;
//			boolean is_first_time = true;

			//			boolean is_init_terminal_done = false;


			while(NOT_END_OF_SESSION) {

				//Blocks until a message is received from the server
				//bytesRead = input.read(buffer.readBuffer, 0, readBufferSize);
				byte[] current_byte = new byte[1];
				//boolean NOT_end_of_message = true;


				//read what is available
				while (true /*NOT_end_of_message*/)
				{
					input.read(current_byte);
					//System.out.println("readed byte: " + Util.toHexString(current_byte));
					bytesRead++;
					bf.put(current_byte);
					if(current_byte[0] == MessageByteConstants.ETX || current_byte[0] == MessageByteConstants.ACK || 
							current_byte[0] == MessageByteConstants.NAK ) {
						if(current_byte[0] == MessageByteConstants.ACK ) {
							//resetuj citanje
							bytesRead = 0;
							bf.clear();
							logger.info("Stigao ACK: " + HexUtil.toHexString(current_byte) + ", ostajem u petlji da sacekam VOID poruku");
						}else {
							if(current_byte[0] == MessageByteConstants.NAK) {
								//								is_ack_received = false;
								//is_void_received = false;
								logger.info("Stigao NACK: " + HexUtil.toHexString(current_byte));
								break;
							}else {
								if(current_byte[0] == MessageByteConstants.ETX) {
									flushData(new byte[] {MessageByteConstants.ACK});
									//is_void_received = true;
									//iscitati jor LRC
									logger.info("Stigao ETX: " + HexUtil.toHexString(current_byte) + ", citam jos LRC kao zadnji bajt");
									input.read(current_byte);
									logger.info("readed byte: " + HexUtil.toHexString(current_byte));
									bytesRead++;
									bf.put(current_byte);

									break;
								}
							}
						}
					}
				}//end while

				//po preporuci, ako sam izasao iz petlje i ako je inicijalizacija bila uspesna (is_ack_received = true)
				//onda sacekam pola sekunde i nastavljam sa radom
				//06.03.2025 - po preporuci payten-a, ne salje se vise terminal initialization zahtev tako da cekanje
				//od pola sekunde izbacujemo
//				if(is_void_received && is_first_time) {
//					is_first_time = false;
//					try {
//						logger.info("start sleep 500 milisec");
//						Thread.sleep(500);
//						logger.info("end of sleep");
//					}catch(Throwable e) {
//
//					}
//
//				}

				buffer.readBuffer = new byte[bytesRead];

				//stigla poruka os POS terminala, obrada poruke ....

				System.arraycopy(bf.array(), 0, buffer.readBuffer, 0, bytesRead);
				buffer.currentReadByteCount = bytesRead;
				if(buffer.readBuffer != null) {
					logger.info("Bytes received from POS: " + HexUtil.toHexString(buffer.readBuffer));
					logger.info("Bytes received length: " + buffer.currentReadByteCount);


					if(bytesRead > 3) {

						//							String message = new String(buffer.readBuffer,1, bytesRead - 3,"ASCII");
						//							System.out.println("String poruka = " + message);
						//							String message_ident = message.substring(0,2);                        
						String message_ident = ProtocolHelper.getIdentifier(buffer.readBuffer);
						String message = ProtocolHelper.getDecodedMessage(buffer.readBuffer,true);
						logger.info("String poruka, message_ident = " + message_ident);
						logger.info("String poruka = " + message);
						if(message_ident.equals(MessageIdentifiers.EXTENDED_HOLD_RESPONSE) ) {
							logger.info("Stiglo  Extend HOLD, vratiti ACK");
							ExtendedHoldDataResponse extendedHoldDataResponse = new ExtendedHoldDataResponse();
							extendedHoldDataResponse.fillObject(message.substring(0, message.length()));           
							flushData(new byte[] {MessageByteConstants.ACK});
							//_callback.setPaymentSessionMessage(extendedHoldDataResponse.get_displayMessage().trim());
						}else {
							//dobili smo poruku da se ocekuje da posaljemo zahtev za transakcijom
							if(message_ident.equals(MessageIdentifiers.TRANSACTION_REQUEST)) {
								logger.info("Send transaction request SALE ");
								flushData(transaction_data_request.getBytes(StandardCharsets.US_ASCII));
							}else {
								if(message_ident.equals(MessageIdentifiers.HOLD_RESPONSE)) {
									logger.info("Stiglo HOLD, vratiti ACK");
									HoldDataResponse holdDataResponse = new HoldDataResponse();
									holdDataResponse.fillObject(message.substring(0, message.length()));           
									flushData(new byte[] {MessageByteConstants.ACK});
									//_callback.setPaymentSessionMessage(holdDataResponse.get_displayMessage().trim());
								}else {
									if(message_ident.equals(MessageIdentifiers.TRANSACTION_RESPONSE) || message_ident.equals(MessageIdentifiers.SETTLEMENT_END)) {
										
										if(message_ident.equals(MessageIdentifiers.TRANSACTION_RESPONSE)) {
											logger.info("Stiglo TRANSACTION RESPONSE, vratiti ACK i pozitivan izlaz iz metode");
										}else {
											logger.info("Stiglo SETTLEMENT END - end of the day RESPONSE, vratiti ACK i pozitivan izlaz iz metode");
										}
										TransactionDataResponse transactionDataResponse = new TransactionDataResponse();
										transactionDataResponse.fillObject(message.substring(0, message.length()));       
										String tran_type = transactionDataResponse.get_transactionType();
										String tran_flag = transactionDataResponse.get_transactionFlag();
										logger.info("Stiglo TRANSACTION TYPE = " + tran_type);
										//return transactionDataResponse;
										if(tran_type.equals("00")) {
											flushData(transaction_data_request.getBytes(StandardCharsets.US_ASCII));
										}else {
											logger.info("Stiglo TRANSACTION FLAG = " + tran_flag);
											NOT_END_OF_SESSION = false;
											to_return =  message;
											flushData(new byte[] {MessageByteConstants.ACK});

											break;

										}

									} else {
										if(message_ident.equals(MessageIdentifiers.CANCEL_CURRENT_TRANSACTION_REQUEST)) {
											logger.info("Stiglo CANCEL_CURRENT_TRANSACTION_REQUEST, prosledi cancel request");
											byte[] cancel_previous_transaction = new byte[] {0x02, 0x02, 0x32, 0x33, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x03, 0x02};
											flushData(cancel_previous_transaction);
											//return transactionDataResponse;

										} else {
											if(message_ident.equals(MessageIdentifiers.CANCEL_CURRENT_TRANSACTION_RESPONSE)) {
												logger.info("Stiglo CANCEL_CURRENT_TRANSACTION_RESPONSE, prosledi ack i vrati rezultat");

												flushData(new byte[] {MessageByteConstants.ACK});
												NOT_END_OF_SESSION = false;
												to_return =  message;
												break;

											} else {
												if(message_ident.equals(MessageIdentifiers.EXTENDED_ERROR_RESPONSE)) {
													logger.info("Stiglo EXTENDED_ERROR_RESPONSE, prosledi ack, ispitati da li treba settlement, ako treba, poslati, ako ne, vratiti gresku");
													flushData(new byte[] {MessageByteConstants.ACK});
													ExtendedErrorDataResponse extendedErrorResponse = new ExtendedErrorDataResponse();
													extendedErrorResponse.fillObject(message.substring(0, message.length()));
													logger.info("Stiglo EXTENDED_ERROR_RESPONSE, extendedErrorResponse = " + extendedErrorResponse);
													if(extendedErrorResponse.get_identifier().equals("02")) {
														TransactionDataRequest settlement_message = new TransactionDataRequest();
														settlement_message.set_transactionType(TransactionTypesConstants.SETTLEMENT);
														settlement_message.set_printerFlag("0");
														settlement_message.set_languageId("02");//serbian
														flushData ( ProtocolHelper.createSendMessage(settlement_message.create()).getBytes(StandardCharsets.US_ASCII) );
													}else {
														logger.info("Stigloerror poruka koja nije vezana za settlement vec neki drugi razlog, prekidamo flow");										

														NOT_END_OF_SESSION = false;
														throw new PaymentException( extendedErrorResponse.get_displayMessage() );
													}
												}else {
													logger.info("Stiglo neocekivana poruka , poruka o gresci ili slicno, poslati ack i prekinuti flow");										
													flushData(new byte[] {MessageByteConstants.ACK});
													NOT_END_OF_SESSION = false;
													throw new PaymentException( message );
												}
											}
										}
									}
									//										System.out.println("Stiglo  neocekivana poruka, vratiti NACK");
									//										flushData(new byte[] {MessageByteConstants.NAK});
								}
							}
						}

					}else {
						//received NACK
						logger.info("Stiglo NACK , neocekivano se desila greska, pokusati ponovo");	
						NOT_END_OF_SESSION = false;
						//							if(!is_ack_received) {
						//								System.out.println("Received NACK, nesto nije u redu ");
						//							}
						throw new PaymentException( "Greska, pokusajte ponovo" );
					}

				}
				bf.clear();
			}
		}//end while endless
		catch(IOException e )
		{
			//A socket error has occurred
			e.printStackTrace();
			System.out.println("A socket error has occurred with the client socket, details:  " + e.getMessage());

			throw new PaymentException("Izgubljena veza sa serverom, pokusajte ponovo");
		}
		catch(Throwable e )
		{
			//A socket error has occurred
			e.printStackTrace();
			logger.error("Unknown has occurred with the client socket, details: " + e.getMessage(), e);

			throw new PaymentException("Izgubljena veza sa serverom, pokusajte ponovo");
		}
		finally {
			bf.clear();
		}

		return to_return;
	}

























	public void run(){
		logger.info("Thread running");
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
							logger.info("Stigao ACK: " + HexUtil.toHexString(current_byte));
							break;
						}
						if(current_byte[0] == MessageByteConstants.NAK) {
							is_ack_received = false;
							logger.info("Stigao NACK: " + HexUtil.toHexString(current_byte));
							break;
						}
						if(current_byte[0] == MessageByteConstants.ETX) {
							is_ack_received = false;
							//iscitati jor LRC
							logger.info("Stigao ETX: " + HexUtil.toHexString(current_byte) + ", citam jos LRC kao zadnji bajt");
							input.read(current_byte);
							logger.info("readed byte: " + HexUtil.toHexString(current_byte));
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
					logger.info("Bytes received from POS: " + HexUtil.toHexString(buffer.readBuffer));
					logger.info("Bytes received length: " + buffer.currentReadByteCount);
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
						logger.info("Send transaction request SALE ");
						//sendImmediate(dataToSend.getBytes(StandardCharsets.US_ASCII));
						flushData(dataToSend.getBytes(StandardCharsets.US_ASCII));
					}else {
						if(bytesRead > 3) {
							//byte[] to_convert_to_string = new byte[bytesRead - 3];
							//System.arraycopy(buffer.readBuffer, 1, to_convert_to_string, 0, to_convert_to_string.length);
							String message = new String(buffer.readBuffer,1, bytesRead - 3,"ASCII");
							System.out.println("String poruka = " + message);
							if(message.startsWith("25") ) {
								logger.info("Stiglo  Extend HOLD, vratiti ACK");
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
									logger.info("Send transaction request SALE ");
									//sendImmediate(dataToSend.getBytes(StandardCharsets.US_ASCII));
									flushData(dataToSend.getBytes(StandardCharsets.US_ASCII));
								}else {
									logger.info("Stiglo  neocekivana poruka, vratiti NACK");
									//sendImmediate(new byte[] {MessageByteConstants.NAK});
									flushData(new byte[] {MessageByteConstants.NAK});
								}
							}






						}else {
							if(!is_ack_received) {
								logger.info("Received NACK, nesto nije u redu ");
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
				logger.error("A socket error has occurred with the client socket " + tcpClient.toString(), e);
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
		logger.info("Send data to POS: " + HexUtil.toHexString(buffer.writeBuffer));
		logger.info("Send data length: " + buffer.writeBuffer.length);
		logger.info("Send data from 0 to " + buffer.currentWriteByteCount);
		output.write(buffer.writeBuffer, 0, buffer.currentWriteByteCount);
		output.flush();
		buffer.currentWriteByteCount = 0;
	}


	private void flushData(byte[] data) throws IOException
	{
		logger.info("Send data to POS: " + HexUtil.toHexString(data));
		output.write(data);
		output.flush();

	}

	/// <summary>
	/// Sends the byte array data immediately to the server
	/// </summary>
	/// <param name="data"></param>
	public void sendImmediate(byte[] data) throws IOException
	{
		logger.info("sendImmediate: " + HexUtil.toHexString(data));
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

		logger.info("Disconnected from server");
		try {
			tcpClient.close();
		}catch(IOException e) {}

		started = false;
	}


}
