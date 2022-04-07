package test.application;

import java.io.File;  

import javafx.application.Application;  
import javafx.scene.Group;  
import javafx.scene.Scene;  
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;  
import javafx.stage.Stage; 

public class JavaFX_Media_Example extends Application  
{  
  
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        // TODO Auto-generated method stub  
        //Initialising path of the media file, replace this with your file path   
        //String path = "src/application/img/ognjen/novak_klip.mp4";  
        
    	String path = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4";
          
        //Instantiating Media class  
        Media media = new Media(path/*new File(path).toURI().toString()*/);  
          
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  
          
        //Instantiating MediaView class   
        MediaView mediaView = new MediaView(mediaPlayer);  
          
        //by setting this property to true, the Video will be played   
        mediaPlayer.setAutoPlay(true);  
          
        //setting group and scene   
        Group root = new Group();  
        root.getChildren().add(mediaView);  
        Scene scene = new Scene(root,500,400);  
        primaryStage.setScene(scene);  
        primaryStage.setTitle("Playing video");  
        primaryStage.show();  
    }  
    
    
    
    public static void main(String[] args) {  
        launch(args);  
    }  
      
}  
