package test.application.cardpayment.helpers;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class LCR {
	
	
	public static String Calculate(String message)
    {
        String lcrBase = message.substring(1, message.length() - 2);
        return calculate(lcrBase, 0);
    }
	
	
    public static String calculate(String message, int offset)
    {
    	ByteBuffer buffer = StandardCharsets.US_ASCII.encode(message);
    	byte[] ascii_bytes = buffer.array();
        return calculate(ascii_bytes, offset, ascii_bytes.length);
    }
	
	
	
    public static String calculate(byte[] message, int offset, int length)
    {
        byte LRC = 0;
        for (int i = offset; i < offset + length; i++)
        {
            LRC ^= message[i];
        }
        return new String(new byte[] {LRC}, StandardCharsets.UTF_8);
        //return Encoding.UTF8.GetString((new[] { LRC }));
    }

}
