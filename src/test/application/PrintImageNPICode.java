package test.application;

import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PrintImageNPICode {
	
	
	
	
    public static byte[] BitImageOutHex(final BufferedImage i_bmp, final int i_width, final int i_height) throws IOException{
        final int rowinf = 24;
        final NByteBuffer retBuf = new NByteBuffer();
        byte[] wk = null;
        final byte[] imgcmd = new byte[5];
        final byte[] lfcmd = new byte[3];
        imgcmd[0] = 27;
        imgcmd[1] = 42;
        imgcmd[2] = 35;
        imgcmd[3] = (byte)(i_width % 256);
        imgcmd[4] = (byte)(i_width / 256);
        lfcmd[0] = 27;
        lfcmd[1] = 74;
        lfcmd[2] = 0;
        wk = new byte[24 * i_width];
        Arrays.fill(wk, (byte)0);
        final int width = i_bmp.getWidth();
        final int height = i_bmp.getHeight();
        //final int[] pixels = new int[width * height];
        

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(i_bmp, "bmp", baos);
//        byte[] pixels = baos.toByteArray();
        
        
        byte[] pixels = (byte[])i_bmp.getRaster().getDataElements(0, 0, i_bmp.getWidth(), i_bmp.getHeight(), null);
        
        
        //i_bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        try {
            for (int y = 0; y < i_height; ++y) {
                for (int x = 0; x < i_width; ++x) {
                    final int color = pixels[x + y * width];
                    final int colorB = (color & 0xFF0000) >> 16;
                    final int colorG = (color & 0xFF00) >> 8;
                    final int colorR = color & 0xFF;
                    if ((colorR + colorG + colorB) / 3 < 140) {
                        wk[y % 24 * i_width + x] = 1;
                    }
                    else {
                        wk[y % 24 * i_width + x] = 0;
                    }
                }
                if (y % 24 == 23) {
                    retBuf.addBuffer(imgcmd);
                    for (int i = 0; i < i_width; ++i) {
                        byte owk = 0;
                        int cnt = 0;
                        for (int n = 0; n < rowinf; ++n) {
                            owk |= (byte)(wk[n * i_width + i] << 7 - cnt);
                            if (n != 0 && n % 8 == 7) {
                                retBuf.addBuffer(owk);
                                owk = 0;
                                cnt = -1;
                            }
                            ++cnt;
                        }
                    }
                    retBuf.addBuffer(lfcmd);
                    Arrays.fill(wk, (byte)0);
                }
            }
            if (i_height % rowinf > 0) {
                retBuf.addBuffer(imgcmd);
                for (int i = 0; i < i_width; ++i) {
                    byte owk = 0;
                    int cnt = 0;
                    for (int n = 0; n < rowinf; ++n) {
                        owk |= (byte)(wk[n * i_width + i] << 7 - cnt);
                        if (n != 0 && n % 8 == 7) {
                            retBuf.addBuffer(owk);
                            owk = 0;
                            cnt = -1;
                        }
                        ++cnt;
                    }
                }
                retBuf.addBuffer(lfcmd);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return retBuf.getBuffer();
    }
    
    
    public void printImage() throws Exception{
    	//new File("C:\\npi_printer_srbija_voz\\K_2b2_Test_resized.bmp")
    	//new File("C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\src\\smily.bmp")
    	BufferedImage  image = ImageIO.read(new File("C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\src\\smily.bmp"));
    	
    	System.out.println("width = " + image.getWidth() + ", height = " + image.getHeight());
    	
        ByteArrayInputStream inputStream = new ByteArrayInputStream(BitImageOutHex(image,400, 400));


        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);
        
        PrintService foundService =  findPrintService("NPI Integration Driver", printService);;
        DocPrintJob dpj = foundService.createPrintJob();
        
        
        Doc doc = new SimpleDoc(inputStream, flavor, null);
        dpj.print(doc, null);
    }
    
    private PrintService findPrintService(String printerName,
            PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }

        return null;
    }
    
    
    public static void main(String[] args) throws Exception {
    	PrintImageNPICode obj = new PrintImageNPICode();
        try {
            obj.printImage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    

}
