package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

class TicketFinalPane extends Pane {
	
	private int _broj_ticketa = 0;
	private String _ime = "";
	private String _prezime = "";
	private String _datum_rodjenja = "";
	private String _povlastica = "";
	private String _broj_legitimacije = "";
	private double _cena = 0.0;
	private double _pdv_iznos = 0.0;
	
	
	private Label _br_tick_lbl = null;
	private Label _ime_prezime_txt_lbl = null;
	private Label _ime_prezime_lbl = null;
	private Label _datum_rodjenja_lbl = null;
	private Label _datum_rodjenja_txt_lbl = null;
	private Label _cena_karte_text_lbl = null;
	private Label _cena_karte_lbl = null;
	private Label _uracunat_pdv_txt_lbl = null;
	private Label _povlastica_lbl = null;
	private Label _povlastica_txt_lbl = null;
	private Label _broj_legitimacije_lbl = null;
	private Label _broj_legitimacije_txt_lbl = null;
	
	public TicketFinalPane(int broj_ticketa, String ime, String prezime, String datum_rodjenja, String povlastica, String broj_legitimacije,
			double cena, double pdv_iznos) {
		_broj_ticketa = broj_ticketa;
		_ime = ime;
		_prezime = prezime;
		_datum_rodjenja = datum_rodjenja;
		_povlastica = povlastica;
		_broj_legitimacije = broj_legitimacije;
		_cena = cena;
		_pdv_iznos = pdv_iznos;
		init();
	}
	
	
	public Label get_br_tick_lbl() {
		if(_br_tick_lbl == null) {
			_br_tick_lbl = new Label();
			_br_tick_lbl.setTextFill(Color.GRAY);
			_br_tick_lbl.setText("" + _broj_ticketa);
			_br_tick_lbl.setFont(Font.font ("Montserrat", 60));
			_br_tick_lbl.setAlignment(Pos.TOP_CENTER);
			_br_tick_lbl.setTextAlignment(TextAlignment.CENTER);
			_br_tick_lbl.setLayoutX(21.0);
			_br_tick_lbl.setLayoutY(5.0);
			_br_tick_lbl.setPrefHeight(126.0);
			_br_tick_lbl.setPrefWidth(113.0);
		}
		return _br_tick_lbl;
	}
	
	public Label get_ime_prezime_txt_lbl() {
		if(_ime_prezime_txt_lbl == null) {
			_ime_prezime_txt_lbl = new Label();
			_ime_prezime_txt_lbl.setTextFill(Color.GRAY);
			_ime_prezime_txt_lbl.setText("Tip karte");
			_ime_prezime_txt_lbl.setFont(Font.font ("Montserrat light", 15));
			_ime_prezime_txt_lbl.setAlignment(Pos.CENTER);
			_ime_prezime_txt_lbl.setTextAlignment(TextAlignment.CENTER);
			_ime_prezime_txt_lbl.setLayoutX(200.0);
			_ime_prezime_txt_lbl.setLayoutY(5.0);
			_ime_prezime_txt_lbl.setPrefHeight(20.0);
			_ime_prezime_txt_lbl.setPrefWidth(210.0);
		}
		return _ime_prezime_txt_lbl;
	}
	
	public Label get_ime_prezime_lbl() {
		if(_ime_prezime_lbl == null) {
			_ime_prezime_lbl = new Label();
			_ime_prezime_lbl.setTextFill(Color.BLACK);
			_ime_prezime_lbl.setText(_ime + " " + _prezime);
			_ime_prezime_lbl.setFont(Font.font ("Montserrat", FontWeight.BOLD, 15));
			_ime_prezime_lbl.setAlignment(Pos.CENTER);
			_ime_prezime_lbl.setTextAlignment(TextAlignment.CENTER);
			_ime_prezime_lbl.setLayoutX(400.0);
			_ime_prezime_lbl.setLayoutY(5.0);
			_ime_prezime_lbl.setPrefHeight(20.0);
			_ime_prezime_lbl.setPrefWidth(210.0);
		}
		return _ime_prezime_lbl;
	}
	
	public Label get_datum_rodjenja_txt_lbl() {
		if(_datum_rodjenja_txt_lbl == null) {
			_datum_rodjenja_txt_lbl = new Label();
			_datum_rodjenja_txt_lbl.setTextFill(Color.GRAY);
			_datum_rodjenja_txt_lbl.setText("Datum rođenja");
			_datum_rodjenja_txt_lbl.setFont(Font.font ("Montserrat light", 15));
			_datum_rodjenja_txt_lbl.setAlignment(Pos.CENTER);
			_datum_rodjenja_txt_lbl.setTextAlignment(TextAlignment.CENTER);
			_datum_rodjenja_txt_lbl.setLayoutX(200.0);
			_datum_rodjenja_txt_lbl.setLayoutY(25.0);
			_datum_rodjenja_txt_lbl.setPrefHeight(20.0);
			_datum_rodjenja_txt_lbl.setPrefWidth(210.0);
		}
		return _datum_rodjenja_txt_lbl;
	}
	
	public Label get_datum_rodjenja_lbl() {
		if(_datum_rodjenja_lbl == null) {
			_datum_rodjenja_lbl = new Label();
			_datum_rodjenja_lbl.setTextFill(Color.BLACK);
			_datum_rodjenja_lbl.setText(_datum_rodjenja);
			_datum_rodjenja_lbl.setFont(Font.font ("Montserrat", FontWeight.BOLD, 15));
			_datum_rodjenja_lbl.setAlignment(Pos.CENTER);
			_datum_rodjenja_lbl.setTextAlignment(TextAlignment.CENTER);
			_datum_rodjenja_lbl.setLayoutX(400.0);
			_datum_rodjenja_lbl.setLayoutY(25.0);
			_datum_rodjenja_lbl.setPrefHeight(20.0);
			_datum_rodjenja_lbl.setPrefWidth(210.0);
		}
		return _datum_rodjenja_lbl;
	}
	
	
	public Label get_povlastica_lbl() {
		if(_povlastica_lbl == null) {
			_povlastica_lbl = new Label();
			_povlastica_lbl.setTextFill(Color.BLACK);
			_povlastica_lbl.setText(_povlastica);
			_povlastica_lbl.setFont(Font.font ("Montserrat", FontWeight.BOLD, 15));
			_povlastica_lbl.setAlignment(Pos.CENTER);
			_povlastica_lbl.setTextAlignment(TextAlignment.CENTER);
			_povlastica_lbl.setLayoutX(400.0);
			_povlastica_lbl.setLayoutY(45.0);
			_povlastica_lbl.setPrefHeight(20.0);
			_povlastica_lbl.setPrefWidth(210.0);
		}
		return _povlastica_lbl;
	}
	
	public Label get_povlastica_txt_lbl() {
		if(_povlastica_txt_lbl == null) {
			_povlastica_txt_lbl = new Label();
			_povlastica_txt_lbl.setTextFill(Color.GRAY);
			_povlastica_txt_lbl.setText("Povlastica");
			_povlastica_txt_lbl.setFont(Font.font ("Montserrat light", 15));
			_povlastica_txt_lbl.setAlignment(Pos.CENTER);
			_povlastica_txt_lbl.setTextAlignment(TextAlignment.CENTER);
			_povlastica_txt_lbl.setLayoutX(200.0);
			_povlastica_txt_lbl.setLayoutY(45.0);
			_povlastica_txt_lbl.setPrefHeight(20.0);
			_povlastica_txt_lbl.setPrefWidth(210.0);
		}
		return _povlastica_txt_lbl;
	}
	
	public Label get_broj_legitimacije_txt_lbl() {
		if(_broj_legitimacije_txt_lbl == null) {
			_broj_legitimacije_txt_lbl = new Label();
			_broj_legitimacije_txt_lbl.setTextFill(Color.GRAY);
			_broj_legitimacije_txt_lbl.setText("Broj legitimacije:");
			_broj_legitimacije_txt_lbl.setFont(Font.font ("Montserrat light", 15));
			_broj_legitimacije_txt_lbl.setAlignment(Pos.CENTER);
			_broj_legitimacije_txt_lbl.setTextAlignment(TextAlignment.CENTER);
			_broj_legitimacije_txt_lbl.setLayoutX(200.0);
			_broj_legitimacije_txt_lbl.setLayoutY(65.0);
			_broj_legitimacije_txt_lbl.setPrefHeight(20.0);
			_broj_legitimacije_txt_lbl.setPrefWidth(210.0);
		}
		return _broj_legitimacije_txt_lbl;
	}
	
	public Label get_broj_legitimacije_lbl() {
		if(_broj_legitimacije_lbl == null) {
			_broj_legitimacije_lbl = new Label();
			_broj_legitimacije_lbl.setTextFill(Color.BLACK);
			_broj_legitimacije_lbl.setText(_broj_legitimacije); 
			_broj_legitimacije_lbl.setFont(Font.font ("Montserrat", FontWeight.BOLD, 15));
			_broj_legitimacije_lbl.setAlignment(Pos.CENTER);
			_broj_legitimacije_lbl.setTextAlignment(TextAlignment.CENTER);
			_broj_legitimacije_lbl.setLayoutX(400.0);
			_broj_legitimacije_lbl.setLayoutY(65.0);
			_broj_legitimacije_lbl.setPrefHeight(20.0);
			_broj_legitimacije_lbl.setPrefWidth(210.0);
		}
		return _broj_legitimacije_lbl;
	}
	
	public Label get_cena_karte_txt_lbl() {
		if(_cena_karte_text_lbl == null) {
			_cena_karte_text_lbl = new Label();
			_cena_karte_text_lbl.setTextFill(Color.GRAY);
			_cena_karte_text_lbl.setText("Cena karte:");
			_cena_karte_text_lbl.setFont(Font.font ("Montserrat light", 15));
			_cena_karte_text_lbl.setAlignment(Pos.BASELINE_LEFT);
			_cena_karte_text_lbl.setTextAlignment(TextAlignment.LEFT);
			_cena_karte_text_lbl.setLayoutX(650.0);
			_cena_karte_text_lbl.setLayoutY(5.0);
			_cena_karte_text_lbl.setPrefHeight(20.0);
			_cena_karte_text_lbl.setPrefWidth(110.0);
		}
		return _cena_karte_text_lbl;
	}
	

	
	
	public Label get_cena_karte_lbl() {
		if(_cena_karte_lbl == null) {
			_cena_karte_lbl = new Label();
			_cena_karte_lbl.setTextFill(Color.BLACK);
			_cena_karte_lbl.setText("" + _cena);
			_cena_karte_lbl.setFont(Font.font ("Montserrat", FontWeight.BOLD, 15));
			_cena_karte_lbl.setAlignment(Pos.CENTER);
			_cena_karte_lbl.setTextAlignment(TextAlignment.CENTER);
			_cena_karte_lbl.setLayoutX(770.0);
			_cena_karte_lbl.setLayoutY(5.0);
			_cena_karte_lbl.setPrefHeight(20.0);
			_cena_karte_lbl.setPrefWidth(80.0);
		}
		return _cena_karte_lbl;
	}
	
	
	public Label get_uracunat_pdv_txt_lbl() {
		if(_uracunat_pdv_txt_lbl == null) {
			_uracunat_pdv_txt_lbl = new Label();
			_uracunat_pdv_txt_lbl.setTextFill(Color.GRAY);
			_uracunat_pdv_txt_lbl.setText("PDV uračunat u cenu karte u iznosu od " +_pdv_iznos+ " RSD");
			_uracunat_pdv_txt_lbl.setFont(Font.font ("Montserrat light", 15));
			_uracunat_pdv_txt_lbl.setAlignment(Pos.BASELINE_LEFT);
			_uracunat_pdv_txt_lbl.setTextAlignment(TextAlignment.LEFT);
			_uracunat_pdv_txt_lbl.setLayoutX(650.0);
			_uracunat_pdv_txt_lbl.setLayoutY(25.0);
			_uracunat_pdv_txt_lbl.setPrefHeight(20.0);
			_uracunat_pdv_txt_lbl.setPrefWidth(500.0);
		}
		return _uracunat_pdv_txt_lbl;
	}
	
	
	
	private void init() {
		setPrefHeight(100);
		setPrefWidth(1087);
		super.setLayoutX(20);
		int offset = _broj_ticketa == 1 ? 120 : 130;
		super.setLayoutY(_broj_ticketa * 100 + offset);
		setStyle("	-fx-border-color: gray ;" + 
				"    -fx-border-width: 0.5 ;");
//		super.setBorder(new Border(new BorderStroke(Color.GRAY, 
//	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
		super.getChildren().add(get_br_tick_lbl());
		super.getChildren().add(get_ime_prezime_txt_lbl());
		super.getChildren().add(get_ime_prezime_lbl());
//		super.getChildren().add(get_datum_rodjenja_txt_lbl());
//		super.getChildren().add(get_datum_rodjenja_lbl());
		super.getChildren().add(get_povlastica_txt_lbl());
		super.getChildren().add(get_povlastica_lbl());
		super.getChildren().add(get_broj_legitimacije_txt_lbl());
		super.getChildren().add(get_broj_legitimacije_lbl());
		super.getChildren().add(get_cena_karte_txt_lbl());
		super.getChildren().add(get_cena_karte_lbl());
		super.getChildren().add(get_uracunat_pdv_txt_lbl());

		super.setVisible(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	

}
