package application.fiskalizacija;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	
	public static String encodeToString(BufferedImage image, String type) {
	    String imageString = null;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    try {
	        ImageIO.write(image, type, bos);
	        byte[] imageBytes = bos.toByteArray();

	        Base64.Encoder encoder = Base64.getEncoder();
	        imageString = encoder.encodeToString(imageBytes);

	        bos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imageString;
	}
	
	public static BufferedImage decodeToImage(String imageString) {
	    BufferedImage image = null;
	    byte[] imageByte;
	    try {
	        Base64.Decoder decoder = Base64.getDecoder();
	        imageByte = decoder.decode(imageString);
	        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	        image = ImageIO.read(bis);
	        bis.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return image;
	}
	
	
	public static void bufferedImageToFile(BufferedImage bi, String file_name_must_png) {
		try {

			File outputfile = new File(file_name_must_png);
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
