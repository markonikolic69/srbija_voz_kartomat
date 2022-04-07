package application.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SimpleTLVUtil {
	
	/**
     * Reads TLV values for a given hex string.
     */
    public static byte[][] readTLV(String tlvHexString, int tag) {
        return readTLV(hexStringToByteArray(tlvHexString), tag);
    }
 
    /**
     * Reads TLV values for a given byte array.
     */
    public static byte[][] readTLV(byte[] tlv, int tag) {
        if (tlv == null || tlv.length < 1) {
            throw new IllegalArgumentException("Invalid TLV");
        }
 
        int c = 0;
        ArrayList al = new ArrayList();
 
        ByteArrayInputStream is = null;
        try {
             is = new ByteArrayInputStream(tlv);
 
             while ((c = is.read()) != -1) {
                if (c == tag){
                    //log.debug("Got tag");
                    if ((c = is.read()) != -1){
                        byte[] value = new byte[c];
                        is.read(value,0,c);
                        al.add(value);
                    }
                }
            }
        } finally {
            if (is != null) {
                try{
                    is.close();
                }catch (IOException e){
                    //log.error(e);
                }
            }
        }
        //log.debug("Got " + al.size() + " values for tag " 
        //    + Integer.toHexString(tag));
        byte[][] vals = new byte[al.size()][];
        al.toArray(vals);
        return vals;
    }
 
    /**
     * Converts a hex string to byte array.
     */
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
		//System.out.println("parse resutt = " + );
		byte[][] parsed = readTLV(emvData,0);
		for(byte[] current : parsed) {
			System.out.println(new String(current));
		}
    }

}
