package application;
	

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;

import application.data.KartomatBean;
import application.https.SrbijaVozIfaceFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Main extends Application {
	
	
	private Locale locale = new Locale("sr","RS");
	
	
	public static final Logger logger = Logger.getLogger("mainLogger");
	

	
	public Locale getLocale(){
	    return locale;
	}

	public void setLocale(Locale locale){
	    this.locale = locale;
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {

			String resource_view =  "MainUI"
					+ ".fxml";
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ResourceBundle bundle = ResourceBundle.getBundle("resources.language.MessageBundle",locale );
			//logovanje
			PropertyConfigurator.configure("log4j.properties");
			URL url = getClass().getResource(resource_view);
			Parent root = FXMLLoader.load(url, bundle);
			Scene scene = new Scene(root);
			

			
			
			
			scene.getStylesheets().add(getClass().getResource("/application/css/application.css").toExternalForm());
			
			scene.setCursor(Cursor.NONE);
			primaryStage.setResizable(false);
			primaryStage.setMaximized(true);
			

			
			primaryStage.setTitle("Srbija Voz - Prodaja karata na staničnim automatima");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			logger.info("-->Main() - program started");
			
		} catch(Exception e) {
			logger.error("-->Main() - program can't start, will exit. Details: " + 
					e.getMessage(), e);
			e.printStackTrace();
		}
	}
	

	
	

	
	public static void main(String[] args) {
		
		launch(args);
	}
}
