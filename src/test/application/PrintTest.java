package test.application;
import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;

public class PrintTest {
    public static void main(String[] args) {
        try {
        	
        	PrintService service = null;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            System.out.println("Number of print services: " + printServices.length);

            for (PrintService printer : printServices)
                System.out.println("Printer: " + printer.getName());
        	
            for (PrintService printService : printServices) {
                if (printService.getName().equals("NPI Integration Driver")) {
                	service = printService;
                  break;
                }
              }
        	
        	if(service != null) {
        		System.out.println("pronasao NPI printer service: ");
        	}else {
        		System.out.println("NIJE pronasao NPI printer service: ");
        	}
            // Open the image file
//            InputStream is = new BufferedInputStream(
//            new FileInputStream("globaltel-logo.gif"));
            byte[] testdata = "hello world \n have a nice day.".getBytes("UTF-8");
            
            // Find the default service
             DocFlavor flavor = DocFlavor.BYTE_ARRAY.TEXT_PLAIN_UTF_8;
            // DocFlavor flavor = DocFlavor."text/plain";  <<-this will not compile
            //  DocFlavor flavor = DocFlavor.STRING;      //  <<-this will not compile
            //PrintService service = PrintServiceLookup.lookupDefaultPrintService();
             
            // Create the print job
            DocPrintJob job = service.createPrintJob();
            Doc doc= new SimpleDoc(testdata, flavor, null);
            
            // Monitor print job events; for the implementation of PrintJobWatcher,
            // see e702 Determining When a Print Job Has Finished
            PrintJobWatcher pjDone = new PrintJobWatcher(job);
            
            // Print it
            job.print(doc, null);
            
            // Wait for the print job to be done
            pjDone.waitForDone();
            
            // It is now safe to close the input stream
//            is.close();
        } catch (PrintException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        } 
//        catch (IOException e) {
//        }
    }
    
    static class PrintJobWatcher {
        // true iff it is safe to close the print job's input stream
        boolean done = false;
        
        PrintJobWatcher(DocPrintJob job) {
            // Add a listener to the print job
            job.addPrintJobListener(new PrintJobAdapter() {
                public void printJobCanceled(PrintJobEvent pje) {
                    allDone();
                }
                public void printJobCompleted(PrintJobEvent pje) {
                    allDone();
                }
                public void printJobFailed(PrintJobEvent pje) {
                    allDone();
                }
                public void printJobNoMoreEvents(PrintJobEvent pje) {
                    allDone();
                }
                void allDone() {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        PrintJobWatcher.this.notify();
                    }
                }
            });
        }
        public synchronized void waitForDone() {
            try {
                while (!done) {
                    wait();
                }
            } catch (InterruptedException e) {
            }
        }
    }
    
}