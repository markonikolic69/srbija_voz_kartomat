package test.application;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;



public class PrintQueueCheckerJNA {

	public static boolean isPrintQueueEmpty(String printerName) {
		try {
			// Find the printer
			PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
			PrintService myPrinter = null;
			for (PrintService service : printServices) {
				if (service.getName().equals(printerName)) {
					myPrinter = service;
					break;
				}
			}

			if (myPrinter == null) {
				System.err.println("Printer '" + printerName + "' not found.");
				return false; // Printer not found, consider it not empty.
			}

			// Get the print job attributes
			PrintServiceAttributeSet attributes = myPrinter.getAttributes();

			// Check for the number of jobs
			NumberUp jobs = (NumberUp) attributes.get(NumberUp.class);

			if (jobs == null || jobs.getValue() == 0) {
				// Check if there are queued documents.
				DocPrintJob printJob = myPrinter.createPrintJob();
				PrintJobWatcher watcher = new PrintJobWatcher(printJob);

				// Create an empty dummy document to query the queue status.
				Doc dummyDoc = new SimpleDoc(new byte[0], DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

				try {
					printJob.print(dummyDoc, null);
					//printJob.cancel(); // Cancel the dummy job immediately.
					watcher.waitForDone();

					// If the dummy job went through without an exception related to an empty queue, the queue is not empty.
					return false;
				} catch (PrintException e) {
					if(e.getMessage().contains("No documents in print job")) {
						return true; //Queue is empty
					} else if(e.getMessage().contains("Print job was canceled")) {
						//This is expected, and does not indicate the queue state.
						return false;
					} else {
						System.err.println("Error checking print queue: " + e.getMessage());
						return false; // Error occurred, consider it not empty.
					}
				}
			} else {
				return false; // Jobs are present in the queue.
			}

		} catch (Exception e) {
			System.err.println("Error checking print queue: " + e.getMessage());
			return false; // Error occurred, consider it not empty.
		}
	}
	
	// Helper class to wait for print job completion
	static class PrintJobWatcher {
		boolean done = false;
		
		PrintJobWatcher(DocPrintJob job) {
			job.addPrintJobListener(new PrintJobAdapter() {
				public void printJobCanceled(PrintJobEvent pje) {
					signalDone();
				}
				
				public void printJobCompleted(PrintJobEvent pje) {
                    signalDone();
                }
				
				public void printJobFailed(PrintJobEvent pje) {
					signalDone();
				}
				
				public void printJobNoMoreEvents(PrintJobEvent pje){
	                    signalDone();
	            }
				
				void signalDone() {
                    synchronized (PrintJobWatcher.this) {
                        PrintJobWatcher.this.done = true;
                        PrintJobWatcher.this.notifyAll();
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
	
	public static void main(String[] args) {
		String printerName = "Your Printer Name"; // Replace with your printer name
		boolean isEmpty = isPrintQueueEmpty(printerName);
		System.out.println("Print queue for '" + printerName + "' is empty: " + isEmpty);
	}
}
	