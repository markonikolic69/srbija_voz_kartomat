package test.application;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;

public class PrinterService implements Printable {

    public List<String> getPrinters(){

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);

        List<String> printerList = new ArrayList<String>();
        for(PrintService printerService: printServices){
            printerList.add( printerService.getName());
        }

        return printerList;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page)
            throws PrinterException {
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /*
         * User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        /* Now we perform our rendering */

        g.setFont(new Font("Roman", 0, 8));
        g.drawString("Hello world !", 0, 10);

        return PAGE_EXISTS;
    }

    public void printString(String printerName, String text) {

        // find the printService of name printerName
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);
        PrintService service = findPrintService(printerName, printService);

        DocPrintJob job = service.createPrintJob();

        try {

            byte[] bytes;

            // important for umlaut chars
            bytes = text.getBytes("CP437");

            Doc doc = new SimpleDoc(bytes, flavor, null);


            job.print(doc, null);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

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

    private PrintService findPrintService(String printerName,
            PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }

        return null;
    }
    
    
    public String getPrinterStatus(String pritnerName ) {
    	DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                flavor, pras);
    	PrintService service = findPrintService(pritnerName, printService);
    	
    	AttributeSet attributes = service.getAttributes();
    	String printerState = attributes.get(PrinterState.class).toString();
    	String printerStateReason = attributes.get(PrinterStateReason.class).toString();

    	System.out.println("printerState = " + printerState); // May be IDLE, PROCESSING, STOPPED or UNKNOWN
    	System.out.println("printerStateReason = " + printerStateReason); // If your printer state returns STOPPED, for example, you can identify the reason 

    	if (printerState.equals(PrinterState.STOPPED.toString())){

    	    if (printerStateReason.equals(PrinterStateReason.TONER_LOW.toString())) {

    	        System.out.println("Toner level is low.");
    	    }
    	}
    	
    	return "printerState="+ printerState + ":printerStateReason=" + printerStateReason;
    }
    
    
    public static void main(String[] args) throws Exception{

        PrinterService printerService = new PrinterService();
        
        
//        System.out.println("printer state and status; " + printerService.getPrinterStatus("NPI Integration Driver"));

        //System.out.println(printerService.getPrinters());

        //print some stuff. Change the printer name to your thermal printer name.
        printerService.printString("NPI Integration Driver", "\n\n testing testing 1 2 3eeeee \n\n\n\n\n\n\n\n\n\n\n\n");
        
        
//		File outputfile = new File("C:\\npi_printer_srbija_voz\\K_2b2_Test_output.bmp");
//		BufferedImage image = ImageIO.read(outputfile);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(image, "bmp", baos);
//		  byte[] bytes = baos.toByteArray();
//		
//		  
//		  printerService.printBytes("NPI Integration Driver", bytes);

        // cut that paper!
        //byte[] cutP = new byte[] { 0x1b, 'm' };

        //printerService.printBytes("NPI Integration Driver", cutP);

    }
    
    
    
}
