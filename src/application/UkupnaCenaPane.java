package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class UkupnaCenaPane extends Pane {
	
	private double _ukupna_cena = 0.0;
	
	private Label _ukupna_cena_txt_lbl = null;
	private Label _ukupna_cena_lbl = null;
	private Label _horizontalna_linija_lbl = null;
	private Label _disclamer_1_lbl = null;
	private Label _disclamer_2_lbl = null;
	private Button _uslovi_open_btn = null;
	private CheckBox _slazem_se_cbx = null;
	
	public UkupnaCenaPane(double ukupna_cena) {
		_ukupna_cena = ukupna_cena;
		init();
	}
	
	
	
	public Label get_ukupna_cena_txt_lbl() {
		if(_ukupna_cena_txt_lbl == null) {
			_ukupna_cena_txt_lbl = new Label();
			_ukupna_cena_txt_lbl.setTextFill(Color.GRAY);
			_ukupna_cena_txt_lbl.setText("Ukupna cena:");
			_ukupna_cena_txt_lbl.setFont(Font.font (" light", 20));
			_ukupna_cena_txt_lbl.setAlignment(Pos.TOP_CENTER);
			_ukupna_cena_txt_lbl.setTextAlignment(TextAlignment.CENTER);
			_ukupna_cena_txt_lbl.setLayoutX(650.0);
			_ukupna_cena_txt_lbl.setLayoutY(20.0);
			_ukupna_cena_txt_lbl.setPrefHeight(25.0);
			_ukupna_cena_txt_lbl.setPrefWidth(220.0);
		}
		return _ukupna_cena_txt_lbl;
	}
	
	
	public Label get_ukupna_cena_lbl() {
		if(_ukupna_cena_lbl == null) {
			_ukupna_cena_lbl = new Label();
			_ukupna_cena_lbl.setTextFill(Color.BLACK);
			_ukupna_cena_lbl.setText("" + _ukupna_cena + " rsd");
			_ukupna_cena_lbl.setFont(Font.font ("Montserrat", FontWeight.BOLD, 20));
			_ukupna_cena_lbl.setAlignment(Pos.CENTER);
			_ukupna_cena_lbl.setTextAlignment(TextAlignment.CENTER);
			_ukupna_cena_lbl.setLayoutX(850.0);
			_ukupna_cena_lbl.setLayoutY(20.0);
			_ukupna_cena_lbl.setPrefHeight(25.0);
			_ukupna_cena_lbl.setPrefWidth(250.0);
		}
		return _ukupna_cena_lbl;
	}
	
	public Label get_horizontalna_linija_lbl() {
		if(_horizontalna_linija_lbl == null) {
			_horizontalna_linija_lbl = new Label();
			_horizontalna_linija_lbl.setTextFill(Color.GRAY);
			_horizontalna_linija_lbl.setText("________________________________________________________");
			_horizontalna_linija_lbl.setFont(Font.font ("Montserrat", 15));
			_horizontalna_linija_lbl.setAlignment(Pos.TOP_CENTER);
			_horizontalna_linija_lbl.setTextAlignment(TextAlignment.CENTER);
			_horizontalna_linija_lbl.setLayoutX(650.0);
			_horizontalna_linija_lbl.setLayoutY(40.0);
			_horizontalna_linija_lbl.setPrefHeight(10.0);
			_horizontalna_linija_lbl.setPrefWidth(420.0);
		}
		return _horizontalna_linija_lbl;
	}
	
	
	public Label get_disclamer_1_lbl() {
		if(_disclamer_1_lbl == null) {
			_disclamer_1_lbl = new Label();
			_disclamer_1_lbl.setTextFill(Color.GRAY);
			_disclamer_1_lbl.setText("Pre kupovine morate se složiti sa uslovima i pravilima kupovine");
			_disclamer_1_lbl.setFont(Font.font ("Montserrat light", 12));
			_disclamer_1_lbl.setAlignment(Pos.TOP_CENTER);
			_disclamer_1_lbl.setTextAlignment(TextAlignment.CENTER);
			_disclamer_1_lbl.setLayoutX(20.0);
			_disclamer_1_lbl.setLayoutY(50.0);
			_disclamer_1_lbl.setPrefHeight(15.0);
			_disclamer_1_lbl.setPrefWidth(420.0);
		}
		return _disclamer_1_lbl;
	}
	
	public Label get_disclamer_2_lbl() {
		if(_disclamer_2_lbl == null) {
			_disclamer_2_lbl = new Label();
			_disclamer_2_lbl.setTextFill(Color.BLACK);
			_disclamer_2_lbl.setText("Slažem se sa uslovima i pravilima kupovine");
			_disclamer_2_lbl.setFont(Font.font ("Montserrat light", 12));
			_disclamer_2_lbl.setAlignment(Pos.TOP_CENTER);
			_disclamer_2_lbl.setTextAlignment(TextAlignment.CENTER);
			_disclamer_2_lbl.setLayoutX(25.0);
			_disclamer_2_lbl.setLayoutY(70.0);
			_disclamer_2_lbl.setPrefHeight(15.0);
			_disclamer_2_lbl.setPrefWidth(320.0);
		}
		return _disclamer_2_lbl;
	}
	
	public Button get_obrisi_btn() {
		if(_uslovi_open_btn == null) {
			_uslovi_open_btn = new Button("(Opšti uslovi kupovine)");
			_uslovi_open_btn.setAlignment(Pos.TOP_CENTER);
			_uslovi_open_btn.setTextAlignment(TextAlignment.CENTER);
			_uslovi_open_btn.setLayoutX(420.0);
			_uslovi_open_btn.setLayoutY(45.0);
			_uslovi_open_btn.setPrefHeight(15.0);
			_uslovi_open_btn.setPrefWidth(200.0);
		}
		
		return _uslovi_open_btn;
	} 
	
	public CheckBox get_slazem_se_checkBox() {
		if(_slazem_se_cbx == null) {
			_slazem_se_cbx = new CheckBox();
			_slazem_se_cbx.setAlignment(Pos.TOP_CENTER);
			_slazem_se_cbx.setTextAlignment(TextAlignment.CENTER);
			_slazem_se_cbx.setLayoutX(20.0);
			_slazem_se_cbx.setLayoutY(70.0);
		}
		
		return _slazem_se_cbx;
	} 
	
	
	
	private void init() {
		setPrefHeight(100);
		setPrefWidth(1087);
		super.setLayoutX(20);
		super.setLayoutY(440);
		setStyle("	-fx-border-color: gray ;" + 
				"    -fx-border-width: 0.5 ;");
//		super.setBorder(new Border(new BorderStroke(Color.GRAY, 
//	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
		super.getChildren().add(get_ukupna_cena_txt_lbl());
		super.getChildren().add(get_ukupna_cena_lbl());
		super.getChildren().add(get_horizontalna_linija_lbl());
		super.getChildren().add(get_disclamer_1_lbl());
		super.getChildren().add(get_disclamer_2_lbl());
		super.getChildren().add(get_obrisi_btn());
		super.getChildren().add(get_slazem_se_checkBox());


		super.setVisible(true);
	}
	

}
