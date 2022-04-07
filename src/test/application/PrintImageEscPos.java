package test.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.image.Bitonal;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImage;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import com.github.anastaciocintra.output.PrinterOutputStream;

public class PrintImageEscPos {
	
	
    public void printBytes(String printerName, byte[] bytes) {

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);
        PrintService service = findPrintService(printerName, printService);

        DocPrintJob job = service.createPrintJob();

        try {

            Doc doc = new SimpleDoc(bytes, flavor, null);

            job.print(doc, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printImage(String printerName) throws IOException{
    	// creating the EscPosImage, need buffered image and algorithm.
		File outputfile = new File("C:\\npi_printer_srbija_voz\\K_2b2_Test_output.bmp");
		BufferedImage image = ImageIO.read(outputfile);
		
		CoffeeImage coffeeImage = new CoffeeImageImpl(image);
		
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();


        // this wrapper uses esc/pos sequence: "GS 'v' '0'"
        RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);
        PrintService service = findPrintService(printerName, printService);

        EscPos escpos = new EscPos(new PrinterOutputStream(service));


        escpos.feed(5);
        escpos.writeLF("BitonalThreshold()");
        // using bitonal threshold for dithering
//        Bitonal algorithm = new BitonalThreshold(); 
//        EscPosImage escposImage = new EscPosImage(coffeeImage, algorithm);     
//        escpos.write(imageWrapper, escposImage);

        escpos.feed(5);

        escpos.cut(EscPos.CutMode.PART);
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
    
    
    public static void main(String[] args) throws Exception{

    	PrintImageEscPos printerService = new PrintImageEscPos();

        //System.out.println(printerService.getPrinters());

        //print some stuff. Change the printer name to your thermal printer name.
        //printerService.printString("NPI Integration Driver", "\n\n testing testing 1 2 3eeeee \n\n\n\n\n\n\n\n\n\n\n\n");
        
        
		
		  
		  printerService.printImage("NPI Integration Driver");

//        // cut that paper!
//        byte[] cutP = new byte[] { 0x1b, 'm' };
//
//        printerService.printBytes("NPI Integration Driver", cutP);

    }
	
	

}
