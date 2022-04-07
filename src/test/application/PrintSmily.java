package test.application;




import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImage;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class PrintSmily {

    public void printImage() throws PrintException, IOException{
        String text_to_print = "Hello world!";


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        /// your legacy commands ini
        // initialize printer
        outputStream.write(27); // ESC
        outputStream.write('@');

        // print text
        outputStream.write(text_to_print.getBytes());

        // feed 5 lines
        outputStream.write(27); // ESC
        outputStream.write('d');
        outputStream.write(5);

        // cut paper
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);

        /// your legacy commands end

        /// fitting lib calls on same outputStream
        EscPos escpos = new EscPos(outputStream);

        RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
        BufferedImage  image = ImageIO.read(new File("C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\src\\smily.bmp"));
        CoffeeImage coffeeImage = new CoffeeImageImpl(image);
        EscPosImage escposImage = new EscPosImage(coffeeImage, new BitonalThreshold()); 
        // print smile image...
        escpos.write(imageWrapper, escposImage);        
        // lib end
        /// legacy continues from this point

        // print text
        outputStream.write(" Joaquim's Smile image".getBytes());

        // feed 5 lines
        outputStream.write(27); // ESC
        outputStream.write('d');
        outputStream.write(5);

        // cut
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);


        // do not forguet to close outputstream
        outputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());


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

    public static void main(String[] args) throws IOException {
    	PrintSmily obj = new PrintSmily();
        try {
            obj.printImage();
        } catch (PrintException ex) {
            ex.printStackTrace();
        }
    }

}
