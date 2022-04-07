package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TicketPane extends Pane {
	
	private Label _br_tick_lbl = null;
	private ComboBox<String> _opcije_cmb = null;
	private Button _obrisi_btn = null;
	private TextField _ime_prezime_tf = null;
	private Label _datum_rodjenja_lbl = null;
	private ComboBox<String> _dan_cmb = null;
	private ComboBox<String> _mesec_cmb = null;
	private ComboBox<String> _godina_cmb = null;
	private Label _cena_karte_text_lbl = null;
	private Label _cena_karte_lbl = null;
	private Button _redovna_btn = null;
	private Button _sluzbena1_btn = null;
	private Button _sluzbena2_btn = null;
	private Button _dete_btn = null;
	private Button _pas_btn = null;
	private Button _novinar_btn = null;
	private TextField _novinar_tf = null;
	private TextField _sluzbena1_tf = null;
	private TextField _sluzbena2_tf = null;
	private ComboBox<String> _godina_dete_cmb = null;
	
	
	private void resetButtonGroup() {
		
		_redovna_btn.setVisible(true);
		_redovna_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		_sluzbena1_btn.setVisible(true);
		_sluzbena1_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		_sluzbena2_btn.setVisible(true);
		_sluzbena2_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		_dete_btn.setVisible(true);
		_dete_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		_pas_btn.setVisible(true);
		_pas_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		_novinar_btn.setVisible(false);
		_novinar_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");

		_novinar_tf.setVisible(false);
		_sluzbena1_tf.setVisible(false);
		_sluzbena2_tf.setVisible(false);
		_godina_dete_cmb.setVisible(false);
		
		
	}
	
	public TicketPane(int broj_tikceta) {
		init(broj_tikceta);
	}
	
	
	
	public Label get_br_tick_lbl(int broj_tikceta) {
		if(_br_tick_lbl == null) {
			_br_tick_lbl = new Label();
			_br_tick_lbl.setTextFill(Color.GRAY);
			_br_tick_lbl.setText("" + broj_tikceta);
			_br_tick_lbl.setFont(Font.font ("Montserrat", 50));
			_br_tick_lbl.setAlignment(Pos.CENTER);
			_br_tick_lbl.setTextAlignment(TextAlignment.CENTER);
			_br_tick_lbl.setLayoutX(50.0);
			_br_tick_lbl.setLayoutY(5.0);
			_br_tick_lbl.setPrefHeight(176.0);
			_br_tick_lbl.setPrefWidth(113.0);
		}
		return _br_tick_lbl;
	}
	
	public Label get_cena_karte_text_lbl() {
		if(_cena_karte_text_lbl == null) {
			_cena_karte_text_lbl = new Label();
			_cena_karte_text_lbl.setTextFill(Color.LIGHTGRAY);
			_cena_karte_text_lbl.setText("Cena Karte");
			_cena_karte_text_lbl.setFont(Font.font ("Montserrat", 12));
			_cena_karte_text_lbl.setAlignment(Pos.CENTER);
			_cena_karte_text_lbl.setTextAlignment(TextAlignment.CENTER);
			_cena_karte_text_lbl.setLayoutX(21.0);
			_cena_karte_text_lbl.setLayoutY(16.0);
			_cena_karte_text_lbl.setPrefHeight(176.0);
			_cena_karte_text_lbl.setPrefWidth(113.0);
		}
		return _cena_karte_text_lbl;
	}
	
	public Label get_cena_karte_lbl(double cena_karte) {
		if(_cena_karte_lbl == null) {
			_cena_karte_lbl = new Label();
			_cena_karte_lbl.setTextFill(Color.LIGHTGRAY);
			_cena_karte_lbl.setText("" + cena_karte);
			_cena_karte_lbl.setFont(Font.font ("Montserrat", 20));
			_cena_karte_lbl.setAlignment(Pos.CENTER);
			_cena_karte_lbl.setTextAlignment(TextAlignment.CENTER);
			_cena_karte_lbl.setLayoutX(21.0);
			_cena_karte_lbl.setLayoutY(16.0);
			_cena_karte_lbl.setPrefHeight(176.0);
			_cena_karte_lbl.setPrefWidth(113.0);
		}
		return _cena_karte_lbl;
	}
	
	
	public Button getRedovnaCenaBtn() {
		if(_redovna_btn == null) {
			_redovna_btn = new Button("REDOVNA CENA");
			_redovna_btn.setLayoutX(143.0);
			_redovna_btn.setLayoutY(50.0);
			_redovna_btn.setPrefHeight(55.0);
			_redovna_btn.setPrefWidth(180.0);
		}
		_redovna_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		
		EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	resetButtonGroup();
		    	_redovna_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
		        event.consume();
		    }
		};
		
		_redovna_btn.setOnAction(buttonHandler);
		
		
		return _redovna_btn;
	}
	
	public Button getSluzbena1CenaBtn() {
		if(_sluzbena1_btn == null) {
			_sluzbena1_btn = new Button("SRB+PLUS K-13");
			_sluzbena1_btn.setLayoutX(143.0);
			_sluzbena1_btn.setLayoutY(120.0);
			_sluzbena1_btn.setPrefHeight(55.0);
			_sluzbena1_btn.setPrefWidth(180.0);
		}
		_sluzbena1_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	resetButtonGroup();
		    	_sluzbena1_btn.setVisible(false);
		    	_sluzbena1_tf.setVisible(true);
		        event.consume();
		    }
		};
		
		_sluzbena1_btn.setOnAction(buttonHandler);
		
		
		return _sluzbena1_btn;
	}
	
	public TextField getSluzbena1TextFiels() {
		if(_sluzbena1_tf == null) {
			_sluzbena1_tf = new TextField("Unesi ID");
			_sluzbena1_tf.setLayoutX(143.0);
			_sluzbena1_tf.setLayoutY(120.0);
			_sluzbena1_tf.setPrefHeight(55.0);
			_sluzbena1_tf.setPrefWidth(180.0);
		}
		_sluzbena1_tf.setVisible(false);
		return _sluzbena1_tf;
	}
	
	public Button getSluzbena2CenaBtn() {
		if(_sluzbena2_btn == null) {
			_sluzbena2_btn = new Button("RAIL+PLUS K-30");
			_sluzbena2_btn.setLayoutX(143.0);
			_sluzbena2_btn.setLayoutY(195.0);
			_sluzbena2_btn.setPrefHeight(55.0);
			_sluzbena2_btn.setPrefWidth(180.0);
		}
		_sluzbena2_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	resetButtonGroup();
		    	_sluzbena2_btn.setVisible(false);
		    	_sluzbena2_tf.setVisible(true);
		        event.consume();
		    }
		};
		
		_sluzbena2_btn.setOnAction(buttonHandler);
		
		return _sluzbena2_btn;
	}
	
	public TextField getSluzbena2TextFiels() {
		if(_sluzbena2_tf == null) {
			_sluzbena2_tf = new TextField("Unesi ID");
			_sluzbena2_tf.setLayoutX(143.0);
			_sluzbena2_tf.setLayoutY(195.0);
			_sluzbena2_tf.setPrefHeight(55.0);
			_sluzbena2_tf.setPrefWidth(180.0);
		}
		_sluzbena2_tf.setVisible(false);
		return _sluzbena2_tf;
	}
	
	public Button getDeteCenaBtn() {
		if(_dete_btn == null) {
			_dete_btn = new Button("DETE");
			_dete_btn.setLayoutX(143.0);
			_dete_btn.setLayoutY(270.0);
			_dete_btn.setPrefHeight(55.0);
			_dete_btn.setPrefWidth(180.0);
		}
		_dete_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	resetButtonGroup();
		    	_dete_btn.setVisible(false);
		    	_godina_dete_cmb.setVisible(true);
		        event.consume();
		    }
		};
		
		_dete_btn.setOnAction(buttonHandler);
		return _dete_btn;
	}
	
	public ComboBox<String> get_godina_dete_cmb() {
		if(_godina_dete_cmb == null) {
			_godina_dete_cmb = new ComboBox<String>();
			for(int i = 2009; i<= 2021; i++) {
				_godina_dete_cmb.getItems().add(
		            "" + i);
			}
			_godina_dete_cmb.setLayoutX(143.0);
			_godina_dete_cmb.setLayoutY(270.0);
			_godina_dete_cmb.setPrefHeight(55.0);
			_godina_dete_cmb.setPrefWidth(180.0);
		}
		_godina_dete_cmb.setVisible(false);
		
		return _godina_dete_cmb;
		
	}
	
	public Button getPasCenaBtn() {
		if(_pas_btn == null) {
			_pas_btn = new Button("PAS");
			_pas_btn.setLayoutX(143.0);
			_pas_btn.setLayoutY(345.0);
			_pas_btn.setPrefHeight(55.0);
			_pas_btn.setPrefWidth(180.0);
		}
		_pas_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		
		EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	resetButtonGroup();
		    	_pas_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
		        event.consume();
		    }
		};
		
		_pas_btn.setOnAction(buttonHandler);
		
		
		
		
		return _pas_btn;
	}
	
	public Button getNovinarCenaBtn() {
		if(_novinar_btn == null) {
			_novinar_btn = new Button("NOVINAR");
			_novinar_btn.setLayoutX(143.0);
			_novinar_btn.setLayoutY(420.0);
			_novinar_btn.setPrefHeight(55.0);
			_novinar_btn.setPrefWidth(180.0);
		}
		_novinar_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	resetButtonGroup();
		    	_novinar_btn.setVisible(false);
		    	_novinar_tf.setVisible(false);
		        event.consume();
		    }
		};
		
		_novinar_btn.setOnAction(buttonHandler);
		return _novinar_btn;
	}
	
	public TextField getNovinarIDTextFiels() {
		if(_novinar_tf == null) {
			_novinar_tf = new TextField("Unesi ID");
			_novinar_tf.setLayoutX(143.0);
			_novinar_tf.setLayoutY(420.0);
			_novinar_tf.setPrefHeight(55.0);
			_novinar_tf.setPrefWidth(180.0);
		}
		_novinar_tf.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		_novinar_tf.setVisible(false);
		return _novinar_tf;
	}
	
	public ComboBox<String> get_opcije_cmb() {
		if(_opcije_cmb == null) {
			_opcije_cmb = new ComboBox<String>();
			_opcije_cmb.getItems().addAll(
		            "REDOVNA CENA",
		            "SRB+PLUS K-13",
		            "RAIL+PLUS K-30",
		            "DETE",
		            "PAS",
		            "NOVINAR");
			_opcije_cmb.setLayoutX(143.0);
			_opcije_cmb.setLayoutY(54.0);
			_opcije_cmb.setPrefHeight(28.0);
			_opcije_cmb.setPrefWidth(228.0);
		}
		
		
		return _opcije_cmb;
		
	}
	
	public Button get_obrisi_btn() {
		if(_obrisi_btn == null) {
			_obrisi_btn = new Button("Obriši polja");
			_obrisi_btn.setLayoutX(134.0);
			_obrisi_btn.setLayoutY(112.0);
			_obrisi_btn.setPrefHeight(32.0);
			_obrisi_btn.setPrefWidth(187.0);
		}
		
		return _obrisi_btn;
	} 
	
	
	public TextField get_ime_prezime_tf() {
		if(_ime_prezime_tf == null) {
			_ime_prezime_tf = new TextField();
			_ime_prezime_tf.setPromptText("Ime i Prezime");
			_ime_prezime_tf.setLayoutX(445.0);
			_ime_prezime_tf.setLayoutY(53.0);
			_ime_prezime_tf.setPrefHeight(31.0);
			_ime_prezime_tf.setPrefWidth(211.0);
		}
		
		return _ime_prezime_tf;
	}
	
	public Label get_datum_rodjenja_lbl() {
		if(_datum_rodjenja_lbl == null) {
			_datum_rodjenja_lbl = new Label();
			_datum_rodjenja_lbl.setTextFill(Color.LIGHTGRAY);
			_datum_rodjenja_lbl.setText("Datum Rođenja");
			_datum_rodjenja_lbl.setFont(Font.font ("Montserrat", 12));
			_datum_rodjenja_lbl.setAlignment(Pos.CENTER);
			_datum_rodjenja_lbl.setTextAlignment(TextAlignment.CENTER);
			_datum_rodjenja_lbl.setLayoutX(445.0);
			_datum_rodjenja_lbl.setLayoutY(102.0);
			_datum_rodjenja_lbl.setPrefHeight(18.0);
			_datum_rodjenja_lbl.setPrefWidth(93.0);
		}
		return _datum_rodjenja_lbl;
	}
	
	public ComboBox<String> get_dan_cmb() {
		if(_dan_cmb == null) {
			_dan_cmb = new ComboBox<String>();
			for(int i = 1; i<= 31; i++) {
			_dan_cmb.getItems().add(
		            "" + i);
			}
			_dan_cmb.setLayoutX(445.0);
			_dan_cmb.setLayoutY(140.0);
			_dan_cmb.setPrefHeight(29.0);
			_dan_cmb.setPrefWidth(74.0);
		}
		
		
		return _dan_cmb;
		
	}
	
	public ComboBox<String> get_mesec_cmb() {
		if(_mesec_cmb == null) {
			_mesec_cmb = new ComboBox<String>();
			_mesec_cmb.getItems().addAll(
		            "JANUAR",
		            "FEBRUAR",
		            "MART",
		            "APRIL",
		            "MAJ",
		            "JUN",
		            "JUL",
		            "AVGUST",
		            "SEPTEMBAR",
		            "OKTOBAR",
		            "NOVEMBAR",
		            "DECEMBAR");
			_mesec_cmb.setLayoutX(527.0);
			_mesec_cmb.setLayoutY(140.0);
			_mesec_cmb.setPrefHeight(29.0);
			_mesec_cmb.setPrefWidth(93.0);
		}
		
		
		return _mesec_cmb;
		
	}
	
	
	public ComboBox<String> get_godina_cmb() {
		if(_godina_cmb == null) {
			_godina_cmb = new ComboBox<String>();
			for(int i = 1912; i<= 2021; i++) {
				_godina_cmb.getItems().add(
		            "" + i);
			}
			_godina_cmb.setLayoutX(627.0);
			_godina_cmb.setLayoutY(140.0);
			_godina_cmb.setPrefHeight(29.0);
			_godina_cmb.setPrefWidth(93.0);
		}
		
		
		return _godina_cmb;
		
	}
	
	

	
	
	
	
	private void init(int broj_tikceta) {
//		setMinHeight(200);
//		super.getChildren().add(get_br_tick_lbl(broj_tikceta));
//		super.getChildren().add(get_cena_karte_text_lbl());
//		super.getChildren().add(get_cena_karte_lbl(234.0));
//		super.getChildren().add(get_opcije_cmb());
//		super.getChildren().add(get_obrisi_btn());
//		super.getChildren().add(get_ime_prezime_tf());
//		super.getChildren().add(get_datum_rodjenja_lbl());
//		super.getChildren().add(get_dan_cmb());
//		super.getChildren().add(get_mesec_cmb());
//		super.getChildren().add(get_godina_cmb());
		
		
		setMinWidth(250);
		setMinHeight(400);

		super.getChildren().add(get_br_tick_lbl(broj_tikceta));
		super.getChildren().add(getRedovnaCenaBtn());
		super.getChildren().add(getSluzbena1CenaBtn());
		super.getChildren().add(getSluzbena2CenaBtn());
		super.getChildren().add(getDeteCenaBtn());
		super.getChildren().add(getPasCenaBtn());
		super.getChildren().add(getNovinarCenaBtn());
		super.getChildren().add(getSluzbena1TextFiels());
		super.getChildren().add(getSluzbena2TextFiels());
		super.getChildren().add(getNovinarIDTextFiels());
		super.getChildren().add(get_godina_dete_cmb());

		super.setVisible(true);
	}
	
	
//    <Label alignment="CENTER" contentDisplay="CENTER" layoutX="21.0" layoutY="16.0" prefHeight="176.0" prefWidth="113.0" text="1" textAlignment="CENTER" />
//    <ComboBox layoutX="143.0" layoutY="54.0" prefHeight="28.0" prefWidth="228.0" />
//    <TextField layoutX="134.0" layoutY="112.0" prefHeight="31.0" prefWidth="237.0" promptText="ID Povlastice" />
//    <Button layoutX="134.0" layoutY="155.0" mnemonicParsing="false" prefHeight="32.0" prefWidth="87.0" text="Obriši polja" />
//    <TextField layoutX="445.0" layoutY="53.0" prefHeight="31.0" prefWidth="211.0" promptText="Ime i Prezime" />
//    <Label layoutX="445.0" layoutY="102.0" prefHeight="18.0" prefWidth="93.0" text="Datum Rođenja" />
//    <ComboBox layoutX="445.0" layoutY="140.0" prefHeight="29.0" prefWidth="74.0" promptText="Dan" />
//    <ComboBox layoutX="527.0" layoutY="140.0" prefHeight="29.0" prefWidth="93.0" promptText="Mesec" />
//    <ComboBox layoutX="627.0" layoutY="140.0" prefHeight="29.0" prefWidth="93.0" promptText="Godina" />
//    <Label layoutX="759.0" layoutY="58.0" prefHeight="18.0" prefWidth="101.0" text="Cena karte" />
//    <Label layoutX="759.0" layoutY="117.0" prefHeight="48.0" prefWidth="121.0" text="319,00 RSD" />
	
	

}
