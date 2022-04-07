package test.application.cardpayment;

public class Util {
	
	public static String toHexString(byte[] packet) {


        String digits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                          "A", "B", "C", "D", "E", "F"};
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < packet.length; i++) {
            int current = packet[i];
            if (current < 0)
                current = 256 + current;
            int high = 0;
            if (current >= 16)
                high = (int) Math.floor(current / 16);
            int low = current - high * 16;
            buffer.append(digits[high] + digits[low] + " ");
        }



        return buffer.toString();
    }

}
