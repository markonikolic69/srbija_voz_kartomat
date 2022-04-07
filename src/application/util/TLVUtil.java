package application.util;

import java.util.HashMap;
import java.util.Map;

public class TLVUtil {
	
	public static Map<String, String> parseTLV(String tlv) {
	    if (tlv == null || tlv.length()%2!=0) {
	        throw new RuntimeException("Invalid tlv, null or odd length");
	    }
	    HashMap<String, String> hashMap = new HashMap<String, String>();
	    for (int i=0; i<tlv.length();) {
	        try {
	            String key = tlv.substring(i, i=i+2);

	            if ((Integer.parseInt(key,16) & 0x1F) == 0x1F) {
	                // extra byte for TAG field
	                key += tlv.substring(i, i=i+2);
	            }
	            String len = tlv.substring(i, i=i+2);
	            int length = Integer.parseInt(len,16);

	            if (length > 127) {
	                // more than 1 byte for lenth
	                int bytesLength = length-128;
	                len = tlv.substring(i, i=i+(bytesLength*2));
	                length = Integer.parseInt(len,16);
	            }
	            length*=2;

	            String value = tlv.substring(i, i=i+length);
	            //System.out.println(key+" = "+value);
	            hashMap.put(key, value);
	        } catch (NumberFormatException e) {
	            throw new RuntimeException("Error parsing number",e);
	        } catch (IndexOutOfBoundsException e) {
	            throw new RuntimeException("Error processing field",e);
	        }
	    }

	    return hashMap;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	
	public static void main(String[] args) {
		String emvData = "5F280206885F2A0209415F340101820219808407A00000000410108701018A0200008E0E000000000000000042031E031F038F0105950500000080019A032110289B0200009C01009F02060000000100009F03060000000000009F0702FFC09F080200029F090200029F0D05B4508400009F0E0500000000009F0F05B4708480009F10120110A04303A20000000000000000000000FF9F120A4D6173746572436172649F130200009F1401009F1701009F1A0206889F2301009F260840AC94784ABC8E179F2701809F33036008089F34031F03029F360201829F3704DEC93DA89F40056000F050019F420209419F5301529F6604000000009F6C020000";
		System.out.println("parse resutt = " + parseTLV(emvData));
	}

}
