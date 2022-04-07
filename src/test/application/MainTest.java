package test.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;


public class MainTest extends Application {
	
	
	
	

	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			String resource_view =  "MainTestUI.fxml";
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			Parent root = FXMLLoader.load(getClass().getResource(resource_view), null);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/css/application.css").toExternalForm());
//			Font font_monserat_light = Font.loadFont("file:resources/fonts/Montserrat-Light.ttf", 45);
//			Font font_monserat_bold = Font.loadFont("file:resources/fonts/Montserrat-Bold.ttf", 45);
			primaryStage.setTitle("Рециклирај и допуни");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}