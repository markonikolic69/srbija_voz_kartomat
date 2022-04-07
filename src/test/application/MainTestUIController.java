package test.application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.StageStyle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;

public class MainTestUIController {
	
	@FXML private Button posalji_btn;
	@FXML private TextField plastika_tf;
	@FXML private TextField staklo_tf;
	@FXML private TextField metal_tf;
	@FXML private AnchorPane root_pane;
	@FXML private Label ukupno_lbl;
	
	
//	Timeline flasher ;
//    PseudoClass flashHighlight;
    

	
	
	
	public void handlePosalji() {
//		int plastika = Integer.parseInt(plastika_tf.getText());
//		int staklo = Integer.parseInt(staklo_tf.getText());
//		int metal = Integer.parseInt(metal_tf.getText());
//		String to_send = buildJson(plastika, staklo, metal);
//		send(to_send.getBytes(Charset.forName("ASCII")));
		send("Komanda=Pass".getBytes(Charset.forName("ASCII")));
	}
	
	public void handleIzmeniPozadinu(){
		//root_pane.setStyle("-fx-background-image: url('/application/img/ognjen/initial.gif')");
		send("ErrorCode = 0; Message = Prethodni iznos: 100".getBytes(Charset.forName("ASCII")));
		//send("ErrorCode = 0; Message = Personalizovana".getBytes(Charset.forName("ASCII")));
	}
	
	public void handlePosaljiPlastika() {
		send("Tip=Plastika".getBytes(Charset.forName("ASCII")));
	}
	
	public void handlePosaljiStaklo() {
		send("Tip=Staklo".getBytes(Charset.forName("ASCII")));
	}
	
	public void handlePosaljiMetal() {
		send("Tip=Metal".getBytes(Charset.forName("ASCII")));
	}
	
	public void handlePosaljiTetrapak() {
		send("Tip=Tetrapak".getBytes(Charset.forName("ASCII")));
	}
	
	public void handlePopunjeno() {
		send("Komanda=Popunjena".getBytes(Charset.forName("ASCII")));
	}
	
	public void handleIspraznjeno() {
		send("Komanda=Ispraznjena".getBytes(Charset.forName("ASCII")));
	}
	
	public void handleVrataOtvorena() {
		send("Vrata=Otvorena".getBytes(Charset.forName("ASCII")));
	}
	
	public void handleVrataZatvorena() {
		send("Vrata=Zatvorena".getBytes(Charset.forName("ASCII")));
	}
	
	public void handleHWProblem() {
		send("Problem=Kamera(1)".getBytes(Charset.forName("ASCII")));
	}
	
	
	public void initDone() {
//    	flashHighlight = PseudoClass.getPseudoClass("flash-highlight");
//    	flasher = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.5), e -> {
//    		ukupno_lbl.pseudoClassStateChanged(flashHighlight, true);
//    	}),
//    			new KeyFrame(javafx.util.Duration.seconds(1.0), e -> {
//    				ukupno_lbl.pseudoClassStateChanged(flashHighlight, false);
//    			})
//    			);
//    	flasher.setCycleCount(Animation.INDEFINITE);
//		flasher.play();
		send("Komanda=init_done".getBytes(Charset.forName("ASCII")));
	}
	
	
	public void handleSendTerminalInfo() {
		//"3107200005210271"
		//testno 3107200005206360
		send("ErrorCode = 0; Message = REQ_TYPE=EHO;QPROX_TERMINAL_INFO=05206360:3107200005206360:107:RV1.0.7   :0098899:ValSAM v2.9    ;IP_ADDRESSES=fe80:0:0:0:41de:6338:f834:643f%eth0/10.12.14.165/0:0:0:0:0:0:0:1%lo/127.0.0.1;VERSION=1.1.1".getBytes(Charset.forName("ASCII")));
	}
	
	
	public void handleAlert() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initStyle(StageStyle.UNDECORATED);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("Alert.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");
        alert.setContentText("КО РЕЦИКЛИРА ЗЛО НЕ МИСЛИ");
        alert.getDialogPane().setPrefSize(380.0, 266.0 );
        alert.setHeaderText("");
//        alert.setContentText("Choose your o.");

//        ButtonType buttonTypeCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);
//        alert.getButtonTypes().setAll(buttonTypeCancel);
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        alert.setGraphic(null);	
        okButton.setVisible(false);
        //Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);

        Thread thread = new Thread(() -> {
            try {
                // Wait for 5 secs
                Thread.sleep(5000);
                if (alert.isShowing()) {
                    Platform.runLater(() -> alert.close());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        Optional<ButtonType> result = alert.showAndWait();
	}
	
	
	
	
	private String buildJson(int plastika, int staklo, int metal) {
		int points = (plastika + staklo + metal) * 7;
		return "{\"point\":"+points+",\"Glas\":"+staklo+",\"Plst\":"+plastika+",\"Can\":"+metal+"}";
	}
	
	
	public void send(byte[] data) {
		String hostname = "127.0.0.1";
        int port = 9090;
 
        try {
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();
//            while (true) {
 
                DatagramPacket request = new DatagramPacket(data, data.length, address, port);
                socket.send(request);
 
//                byte[] buffer = new byte[512];
//                DatagramPacket response = new DatagramPacket(buffer, buffer.length, );
//                socket.receive(response);
// 
//                String quote = new String(buffer, 0, response.getLength());
// 
//                System.out.println(quote);
//                System.out.println();
// 
//                Thread.sleep(10000);
//            }
                socket.close();
        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        } 
//        catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
	}
	
}
