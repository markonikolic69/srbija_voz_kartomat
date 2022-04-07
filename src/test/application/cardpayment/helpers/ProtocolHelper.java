package test.application.cardpayment.helpers;

import java.nio.charset.StandardCharsets;

import test.application.cardpayment.MagicNumbers;
import test.application.cardpayment.MessageStringConstants;

public class ProtocolHelper {
	
	
	public static String createSendMessage(String message)
    {
        message = message.replace(MessageStringConstants.STX, "\u0002");
        message = message.replace(MessageStringConstants.FS, "\u001C");
        message = message.replace(MessageStringConstants.ETX, "\u0003");
        message = message.replace(MessageStringConstants.RS, "\u001E");
        message = message.replace(MessageStringConstants.US, "\u001F");
        message = message.replace(MessageStringConstants.ACK, "\u0006");
        message = message.replace(MessageStringConstants.NAK, "\u0015");
        String forCalculate = message.replace("\u0002", "");
        String LRCValue = LCR.calculate(forCalculate, 0);
        StringBuilder sb = new StringBuilder(message);
        sb.append(LRCValue);
        return sb.toString();
    }
	
	public static String encodeMessage(String message)
    {
        message = message.replace(MessageStringConstants.STX, "\u0002");
        message = message.replace(MessageStringConstants.FS, "\u001C");
        message = message.replace(MessageStringConstants.ETX, "\u0003");
        message = message.replace(MessageStringConstants.RS, "\u001E");
        message = message.replace(MessageStringConstants.US, "\u001F");
        message = message.replace(MessageStringConstants.ACK, "\u0006");
        message = message.replace(MessageStringConstants.NAK, "\u0015");
        return message;
    }

    public static String decodeMessage(String message)
    {
        message = message.replace("\u0002", MessageStringConstants.STX);
        message = message.replace("\u0003", MessageStringConstants.ETX);
        message = message.replace("\u001c", MessageStringConstants.FS);
        message = message.replace("\u0006", MessageStringConstants.ACK);
        message = message.replace("\u0015", MessageStringConstants.NAK);
        return message;

    }

    public static String FindLRC(String message)
    {
        if (message.startsWith(MessageStringConstants.STX) && message.contains(MessageStringConstants.ETX))
        {
            int endOfText = message.indexOf(MessageStringConstants.ETX);
            endOfText += 5;
            String LRC = message.substring(endOfText, message.length() - endOfText);
            return LRC;
        }
        else
        {
            System.out.println("Wrong LRC");
        }
        return null;
    }
    
    public static String calculateLRCString(byte[] message, int offset, int length)
    {
        byte LRC = 0;
        for (int i = offset; i < offset + length; i++)
        {
            LRC ^= message[i];
        }

        return new String(new byte[] {LRC}, StandardCharsets.UTF_8);
    }
    
    public static byte calculateLRCByte(byte[] message, int offset, int length)
    {
        byte LRC = 0;
        for (int i = offset; i < offset + length; i++)
        {
            LRC ^= message[i];
        }

        return LRC;
    }

    public static String getIdentifier(byte[] message)
    {
        String decodedMessage = getDecodedMessage(message, true);
        String splitDecodedMessage = decodedMessage.split(MessageStringConstants.FS)[0];
        
        return splitDecodedMessage.substring(MagicNumbers.IDENTIFIER_RANGE[0], MagicNumbers.IDENTIFIER_RANGE[1]);
    }

    public static String getDecodedMessage(byte[] message, boolean onlyPayload)
    {
        String decodedMessage = decodeMessage(new String(message, StandardCharsets.UTF_8));

        if (onlyPayload)
        {
            decodedMessage = decodedMessage.replace(MessageStringConstants.STX, "").replace(MessageStringConstants.ETX, "");
        }
        
        return decodedMessage;
    }

}
