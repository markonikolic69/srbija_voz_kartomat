package test.application;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DatePickerPopupDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

//            DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
//            Node popupContent = datePickerSkin.getPopupContent();
//
//            root.setCenter(popupContent);
            
            DatePicker datePicker = new DatePicker(LocalDate.now());
            root.setCenter(datePicker);
//            datePicker.getEditor().setVisible(false);
//            datePicker.getEditor().setBackground(Background.EMPTY);
            datePicker.getEditor().setPrefWidth(-10);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
