package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import javafx.application.Platform;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.GroupLayout.Alignment;

import com.sun.corba.se.spi.ior.MakeImmutable;
import com.sun.jmx.remote.util.OrderClassLoaders;

import application.data.CenaBean;
import application.data.EtKartaBean;
import application.data.FrekventneStaniceBean;
import application.data.KartaBean;
import application.data.KartomatBean;
import application.data.LegitimacijaBean;
import application.data.PovlasticaBean;
import application.data.PutniciBean;
import application.data.PutniciBeanPovratna;
import application.data.PutnikIface;
import application.data.SifraBean;
import application.data.StanicaIDBean;
import application.data.VozBean;
import application.data.VozDataBean;
import application.https.CommunicationException;
import application.https.ETKartaResponseBean;
import application.https.EtKartaResponse;
import application.https.KartaStampaBean;
import application.https.SrbijaVozIfaceFactory;
import application.https.SrbijaVozPopustID;
import application.payment.PayTenPaymentIface;
import application.payment.PaymentException;
import application.payment.PaymentFactory;
import application.payment.impl.PayTenNetworkClient;
import application.payment.impl.models.TransactionDataResponse;
import application.util.PowerShellPrinterStatus;
import application.util.PrinterService;
import application.util.SessionTimer;
import application.util.StanicaNames;




public class MainUIController extends AbstractController implements Initializable, IPaymentCallbackInfo, SessionControlIface{
	
	


	private static int SESSION_DURATION_SECONDS = 90;
	
	private static  String MAC_ADDRESS  = "";//"E8-38-73-6C-73-69-4C-90-8F-73-E8-3E-5F-02-43-D8";
	private static final int DEFAULT_BROJ_PUTNIKA = 1;
	private static final int DEFAULT_RAZRED = 2;
	
	
	public static  String PAYMENT_IP_ADDRESS = "";//"192.168.100.109";
	public static  int PAYMENT_PORT = 3000;
	
	public static String SV_API_URL = "";
	public static int SV_API_CONN_TIME = 3000;
	public static int SV_API_READ_TIME = 12000;
	
	private static String KARTOMAT_STANICA_NAZIV_LATIN = "";
	private static String KARTOMAT_STANICA_NAZIV_CIR = "";
	
	public static String KARTOMAT_GRAD = "Beograd";
	
	private List<String> _vozovi_sa_rezervacijom = new ArrayList<String>();
	
	private static KartomatBean _kartomat = null;
	private static List<FrekventneStaniceBean> _frekventne_stanice = null;
	private static List<StanicaIDBean> _ostale_stanice = null;
	private static List<StanicaIDBean> _ostale_stanice_filtered = new ArrayList<StanicaIDBean>();
	private static List<VozBean> _svi_polasci = null;
	private static List<VozBean> _svi_povratci = null;
	private static List<VozBean> _svi_povratci_filtered_rezervacije = new ArrayList<VozBean>();
	private static List<VozBean> _svi_povratci_filtered_bez_rezervacije = new ArrayList<VozBean>();
	private static VozBean _selected_voz = null;
	private static VozBean _selected_voz_povratak = null;
	
/////////////////////////////////////////////////////cacne potreban za kupovinu karte - protokol
	private static LegitimacijaBean _prvi_putnik_legitimacija = null;
	private static LegitimacijaBean _drugi_putnik_legitimacija = null;
	private static LegitimacijaBean _treci_putnik_legitimacija = null;
	private static LegitimacijaBean _cetvrti_putnik_legitimacija = null;
	private static LegitimacijaBean _peti_putnik_legitimacija = null;
	
	private static PovlasticaBean _prvi_putnik_povlastica = null;
	private static PovlasticaBean _drugi_putnik_povlastica = null;
	private static PovlasticaBean _treci_putnik_povlastica = null;
	private static PovlasticaBean _cetvrti_putnik_povlastica = null;
	private static PovlasticaBean _peti_putnik_povlastica = null;
	
	private static CenaBean _prvi_putnik_cena = null;
	private static CenaBean _drugi_putnik_cena = null;
	private static CenaBean _treci_putnik_cena = null;
	private static CenaBean _cetvrti_putnik_cena = null;
	private static CenaBean _peti_putnik_cena = null;
	
	private static CenaBean _prvi_putnik_povratna_cena = null;
	private static CenaBean _drugi_putnik_povratna_cena = null;
	private static CenaBean _treci_putnik_povratna_cena = null;
	private static CenaBean _cetvrti_putnik_povratna_cena = null;
	private static CenaBean _peti_putnik_povratna_cena = null;
	
//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private Locale eng_locale = new Locale("en","EN");
	private Locale srb_locale = new Locale("sr","RS");
	private Locale srb_cir_locale = new Locale("cir","RS");
	private ResourceBundle resources_eng = ResourceBundle.getBundle("resources.language.MessageBundle",eng_locale );
	private ResourceBundle resources_srb = ResourceBundle.getBundle("resources.language.MessageBundle",srb_locale );
	private ResourceBundle resources_cir = ResourceBundle.getBundle("resources.language.MessageBundle",srb_cir_locale );



	
	
	
	@FXML private ResourceBundle resources;
	@FXML private ProgressBar progressBar;
	@FXML private Label preostalo_lbl;
	@FXML private Label kartomat_stanica_text_lbl1;
	@FXML private Label kartomat_stanica_text_lbl2;
	@FXML private Label kartomat_stanica_text_lbl3;
	@FXML private Label kartomat_stanica_text_lbl4;
	@FXML private Label kartomat_stanica_text_lbl5;
	@FXML private Label kartomat_stanica_text_lbl6;
	@FXML private Label kartomat_stanica_text_lbl7;
	@FXML private Label kartomat_stanica_text_lbl8;
	@FXML private Label kartomat_stanica_text_lbl9;
	@FXML private Label kartomat_stanica_text_lbl10;
	@FXML private Label kartomat_stanica_text_lbl11;
	@FXML private Label kartomat_stanica_text_lbl12;
	@FXML private Label kartomat_stanica_text_lbl13;
	@FXML private Label kartomat_stanica_text_lbl14;
	@FXML private Label kartomat_stanica_text_lbl15;
	@FXML private Label kartomat_stanica_text_lbl16;
	@FXML private Label kartomat_stanica_text_lbl17;
	@FXML private Label kartomat_stanica_text_lbl18;
	@FXML private Label kartomat_stanica_text_lbl19;
	@FXML private Label kartomat_stanica_text_lbl20;
	@FXML private Label kartomat_stanica_text_lbl21;
	@FXML private Label kartomat_stanica_text_lbl22;
	
	@FXML private Label kartomat_stanica2_text_lbl1;
	@FXML private Label kartomat_stanica2_text_lbl2;
	@FXML private Label kartomat_stanica2_text_lbl3;
	@FXML private Label kartomat_stanica2_text_lbl4;
	@FXML private Label kartomat_stanica2_text_lbl5;
	@FXML private Label kartomat_stanica2_text_lbl6;
	@FXML private Label kartomat_stanica2_text_lbl7;
	@FXML private Label kartomat_stanica2_text_lbl8;
	@FXML private Label kartomat_stanica2_text_lbl9;
	@FXML private Label kartomat_stanica2_text_lbl10;
	@FXML private Label kartomat_stanica2_text_lbl11;
	@FXML private Label kartomat_stanica2_text_lbl12;
	@FXML private Label kartomat_stanica2_text_lbl13;
	@FXML private Label kartomat_stanica2_text_lbl14;
	@FXML private Label kartomat_stanica2_text_lbl15;
	@FXML private Label kartomat_stanica2_text_lbl16;
	@FXML private Label kartomat_stanica2_text_lbl17;
	@FXML private Label kartomat_stanica2_text_lbl18;
	@FXML private Label kartomat_stanica2_text_lbl19;
	@FXML private Label kartomat_stanica2_text_lbl20;
	@FXML private Label kartomat_stanica2_text_lbl21;
	@FXML private Label kartomat_stanica2_text_lbl22;
	
	@FXML private Label kartomat_stanica3_text_lbl1;
	@FXML private Label kartomat_stanica3_text_lbl2;
	@FXML private Label kartomat_stanica3_text_lbl3;
	@FXML private Label kartomat_stanica3_text_lbl4;
	@FXML private Label kartomat_stanica3_text_lbl5;
	@FXML private Label kartomat_stanica3_text_lbl6;
	@FXML private Label kartomat_stanica3_text_lbl7;
	@FXML private Label kartomat_stanica3_text_lbl8;
	@FXML private Label kartomat_stanica3_text_lbl9;
	@FXML private Label kartomat_stanica3_text_lbl10;
	@FXML private Label kartomat_stanica3_text_lbl11;
	@FXML private Label kartomat_stanica3_text_lbl12;
	@FXML private Label kartomat_stanica3_text_lbl13;
	@FXML private Label kartomat_stanica3_text_lbl14;
	@FXML private Label kartomat_stanica3_text_lbl15;
	@FXML private Label kartomat_stanica3_text_lbl16;
	@FXML private Label kartomat_stanica3_text_lbl17;
	@FXML private Label kartomat_stanica3_text_lbl18;
	@FXML private Label kartomat_stanica3_text_lbl19;
	@FXML private Label kartomat_stanica3_text_lbl20;
	@FXML private Label kartomat_stanica3_text_lbl21;
	@FXML private Label kartomat_stanica3_text_lbl22;
	
	@FXML private Label trenutno_vreme_lbl;
	
	@FXML private Label drugi_naslov_lbl;
	
	
	
	@FXML private Label polaziste_lbl;
	@FXML private Label polazak_lbl;
	@FXML private Label dolazak_lbl;
	
	@FXML private Label broj_voza_lbl;
	@FXML private Label razred_lbl;
	@FXML private Label povlastice_lbl;
	@FXML private Label povlastice_polazak_lbl;
	@FXML private Label ukupno_lbl;
	@FXML private Label rsd_lbl;
	@FXML private Label rsd_ukupno_lbl;
	@FXML private Label polazak_datum_lbl;
	@FXML private Label vreme_lbl;
	@FXML private Button vreme_polazak_btn;
	@FXML private Label povratak_datum_lbl;
	@FXML private Button vreme_dolazak_btn;
	@FXML private Label izaberi_tip_karte_lbl;
	@FXML private Label ukupno_za_naplatu_lbl;
	@FXML private Button plati_kartu_final_btn;
	@FXML private Label placanje_preostalo_vreme_lbl;
	
	
	
	

//	@FXML private ComboBox<String> lista_od_cmb;
//	@FXML private ComboBox<String> lista_do_cmb;
//	@FXML private ComboBox<String> razred_od_cmb;
//	@FXML private ComboBox<String> razred_do_cmb;
//	@FXML private RadioButton jedan_smer_rb;
//	@FXML private RadioButton povratna_smer_rb;
	
	
//	@FXML private TableView<VozBean> polasci_table;
//	@FXML private TableView<VozBean> dolasci_table;
	
	
	
//	@FXML private Pane polasci_pn;
//	@FXML private Pane poslednja_provera_pn;
//	@FXML private Pane placanje_karticom_pn;

//	@FXML private TableColumn broj_voza_tblcol_pol;
//	@FXML private TableColumn dat_vre_pol_tblcol_pol;
//	@FXML private TableColumn dat_vre_dol_tblcol_pol;
//	@FXML private TableColumn trajanje_putovanja_tblcol_pol;
//	@FXML private TableColumn cena_tblcol_pol;
//	@FXML private TableColumn izaberi_tblcol_pol;
//	
//	@FXML private TableColumn broj_voza_tblcol_odl;
//	@FXML private TableColumn dat_vre_pol_tblcol_odl;
//	@FXML private TableColumn dat_vre_dol_tblcol_odl;
//	@FXML private TableColumn trajanje_putovanja_tblcol_odl;
//	@FXML private TableColumn cena_tblcol_odl;
//	@FXML private TableColumn izaberi_tblcol_odl;
	
//	@FXML private Polygon prvi_pol;
//	@FXML private Polygon drugi_pol;
//	@FXML private Polygon treci_pol;
//	@FXML private Polygon cetvrti_pol;
//	@FXML private Polygon peti_pol;
//
//	@FXML private Circle prvi_cir;
//	@FXML private Circle drugi_cir;
//	@FXML private Circle treci_cir;
//	@FXML private Circle cetvrti_cir;
//	@FXML private Circle peti_peti;
	
	
	@FXML private AnchorPane root_pane;


	@FXML private Label biraj_odrediste_lbl;
	
	private Label stanica_lbl_slide_1;
	private Label stanica_lbl_slide_2;
	private Label stanica_lbl_slide_3;
	@FXML private Label kartomat_stanica_lbl;
	@FXML private Label broj_putnika_lbl;
	@FXML private Label broj_putnika_naslov_lbl;
//	@FXML private Pane ticket_pn;
	
	
	@FXML private Pane novi_izmeni_pn;
	@FXML private Button novi_kupi_karte_btn;
	@FXML private Label broj_putnika_text_lbl;
	@FXML private Label placanje_posruka_1_lbl;
	@FXML private Label placanje_posruka_2_lbl;
	@FXML private Label placanje_posruka_3_lbl;
	@FXML private Label placanje_posruka_4_lbl;
	@FXML private Label placanje_posruka_5_lbl;
	
	@FXML private Label prva_polaziste_lbl;
	@FXML private Label prva_smer_lbl;
	@FXML private Label prva_odrediste_lbl;		
	@FXML private Label prva_datum_polaziste_lbl;				
	@FXML private Label prva_polaziste_broj_voza_lbl;
	@FXML private Label prva_tip_lbl;
	@FXML private Label prva_razred_lbl;
	@FXML private Label prva_cena_lbl;
	
	@FXML private Label druga_polaziste_lbl;
	@FXML private Label druga_smer_lbl;
	@FXML private Label druga_odrediste_lbl;		
	@FXML private Label druga_datum_polaziste_lbl;				
	@FXML private Label druga_polaziste_broj_voza_lbl;
	@FXML private Label druga_tip_lbl;
	@FXML private Label druga_razred_lbl;
	@FXML private Label druga_cena_lbl;
	
	@FXML private Label treca_polaziste_lbl;
	@FXML private Label treca_smer_lbl;
	@FXML private Label treca_odrediste_lbl;		
	@FXML private Label treca_datum_polaziste_lbl;				
	@FXML private Label treca_polaziste_broj_voza_lbl;
	@FXML private Label treca_tip_lbl;
	@FXML private Label treca_razred_lbl;
	@FXML private Label treca_cena_lbl;
	
	@FXML private Label cetvrta_polaziste_lbl;
	@FXML private Label cetvrta_smer_lbl;
	@FXML private Label cetvrta_odrediste_lbl;		
	@FXML private Label cetvrta_datum_polaziste_lbl;				
	@FXML private Label cetvrta_polaziste_broj_voza_lbl;
	@FXML private Label cetvrta_tip_lbl;
	@FXML private Label cetvrta_razred_lbl;
	@FXML private Label cetvrta_cena_lbl;
	
	
	@FXML private Label peta_polaziste_lbl;
	@FXML private Label peta_smer_lbl;
	@FXML private Label peta_odrediste_lbl;		
	@FXML private Label peta_datum_polaziste_lbl;				
	@FXML private Label peta_polaziste_broj_voza_lbl;
	@FXML private Label peta_tip_lbl;
	@FXML private Label peta_razred_lbl;
	@FXML private Label peta_cena_lbl;
	
	
	
	
	
	
//	@FXML private Button tip_karte_dalje_btn;
//	@FXML private Button tip_karte_odustani_btn;
	
	
	
//	@FXML private VBox ticket_vbx;
//	@FXML private ScrollPane ticket_scrp;
//	@FXML private AnchorPane ticket_ancp;
	
	@FXML private Button izmeni_polaske_btn;
	@FXML private Button izmeni_tip_karte_btn;
	
	
    
//	@FXML private Button izmeni_polazke_dalje_btn;
//	@FXML private Button izmeni_polazke_nazad_btn;
	
	
	
	@FXML private Button destinacija1_btn;
	@FXML private Button destinacija2_btn;
	@FXML private Button destinacija3_btn;
	@FXML private Button destinacija4_btn;
	@FXML private Button destinacija5_btn;
	@FXML private Button destinacija6_btn;
	@FXML private Button destinacija7_btn;
	@FXML private Button destinacija8_btn;
	@FXML private Button ostale_destinacije_btn;
	@FXML private Button ostale_stanice_1;
	@FXML private Button ostale_stanice_2;
	@FXML private Button ostale_stanice_3;
	@FXML private Button ostale_stanice_4;
	@FXML private Button ostale_stanice_5;
	@FXML private Button ostale_stanice_6;
	@FXML private Button ostale_stanice_7;
	@FXML private Button ostale_stanice_8;
	@FXML private Button ostale_stanice_9;
	@FXML private Button ostale_stanice_10;
	@FXML private Button ostale_stanice_11;
	@FXML private Button ostale_stanice_12;
	@FXML private Button ostale_stanice_13;
	@FXML private Button ostale_stanice_14;
	@FXML private Button ostale_stanice_15;
	@FXML private Button ostale_stanice_16;
	
	private static final int OSTALE_STANICE_SIZE = 16;
	
	private static final int SVI_POLASCI_SIZE = 5;
	
	@FXML private Button ostale_napred_pagination_btn;
	@FXML private Button ostale_nazad_pagination_btn;
    @FXML private Button ostale_odustani_btn;
	
	@FXML private Pane ostale_stanice_pn;
	
	
	@FXML private Pane datum_povratka_izmena_pn;
	@FXML private Pane razred_karta_izmena_povratak_pn;
	
	@FXML private Button jedan_btn;
	@FXML private Button dva_btn;
	@FXML private Button tri_btn;
	@FXML private Button cetiri_btn;
	@FXML private Button pet_btn;
	@FXML private Button smer_u_jednom_btn;
	@FXML private Button smer_povratni_btn;
	@FXML private Button datum_polazak_btn;
	@FXML private Button razred_prvi_polazak_btn;
	@FXML private Button datum_dolazak_btn;
	@FXML private Button razred_prvi_dolazak_btn;
	@FXML private Button razred_drugi_polazak_btn;
	@FXML private Button razred_drugi_dolazak_btn;
	@FXML private Label odrediste_lbl;
	@FXML private Label odrediste_polazak_lbl;
	@FXML private Label odrediste_value_lbl;
	@FXML private Label odrediste_izmena_value_lbl;
	@FXML private Label datum_polaska_lbl;
	@FXML private Label datum_polaska_value_lbl;
	@FXML private Label datum_izmena_value_lbl;
	@FXML private Label razred_polazak_lbl;
	@FXML private Label razred_polazak_value_lbl;
	@FXML private Label razred_dolazak_lbl;
	@FXML private Label razred_odlazak_value_lbl;
	@FXML private Label smer_lbl;
	@FXML private Label smer_polazak_lbl;
	
	@FXML private Label smer_value_lbl;
	@FXML private Label relacija_polazak_lbl;
	@FXML private Label relacija_polaza_value_lbl;
	@FXML private Label broj_voza_polazak_lbl;
	@FXML private Label broj_voza_polazak_value_lbl;
	@FXML private Label odrediste_dolazak_value_lbl;
	@FXML private Label vreme_polazak_lbl;
	@FXML private Label vreme_polazak_value_lbl;
	@FXML private Label vreme_izmena_value_lbl;
	@FXML private Label datum_polazak_dolazak_value_lbl;
	@FXML private Label vreme_polazak_dolazak_lbl;
	@FXML private Label vreme_polazak_dolazak_value_lbl;
	@FXML private Label tip_karte_polazak_lbl;
	@FXML private Label tip_karte_polazak_value_lbl;
	@FXML private Label tip_karte_izmena_value_lbl;
//	@FXML private Label cena_poalzak_lbl;
	@FXML private Label ukupno_cena_value_lbl;
	@FXML private Label razred_povratak_lbl;
	
	
	@FXML private Label broj_voza_dolazak_lbl;
	@FXML private Label broj_voza_dolazak_value_lbl;
	@FXML private Label vreme_dolazak_value_lbl;
	@FXML private Label vreme_povratka_izmena_value_lbl;
	@FXML private Label tip_karte_dolazak_lbl;
	@FXML private Label tip_karte_dolazak_value_lbl;
	
	
	@FXML private Label relacija_dolazak_polaziste_lbl;
	@FXML private Label relacija_dolazak_odrediste_lbl;
	@FXML private Label relacija_dolazak_value_lbl;
	@FXML private Label vreme_dolazak_dolazak_value_lbl;

	
	@FXML private Label datum_povratka_polazak_lbl;
	@FXML private Label datum_povratka_dolazak_lbl;
	@FXML private Label datum_povratka_polazak_value_lbl;
	@FXML private Label datum_povratka_izmena_value_lbl;
	@FXML private Label datum_povratka_dolazak_value_lbl;
	@FXML private Label datum_povratka_separator1_lbl;
	@FXML private Label datum_povratka_separator2_lbl;
	
	
//	@FXML private Label cena_dolazak_lbl;
//	@FXML private Label cena_dolazak_value_lbl;
	
//	@FXML private Button izmeni_opcije_btn;
//	@FXML private Button kupi_kartu_opcije_btn;
	

//	@FXML private Pane izmeni_opcije_pn;
			
	@FXML private Pane odrediste_pn;
	@FXML private Pane red_header_pn;
	@FXML private Pane red_header_placanje_pn;
	@FXML private Pane red_header_izmeni_pn;
//	@FXML private Pane odabrane_opcije_pn;
	
	
	@FXML private Pane calendar_pn;
	@FXML private Button sled_ned_cal_btn;
	@FXML private Button calendar_odustani_btn;
	@FXML private Button pret_ned_cal_btn;
	@FXML private Label izaberi_datum_cal_lbl;
	@FXML private Pane pon_pn;
	@FXML private Button pon_izab_btn;
	@FXML private Label pon_dat_lbl;
	@FXML private Label pon_lbl;
	@FXML private Pane uto_pn;
	@FXML private Button uto_izab_btn;
	@FXML private Label uto_dat_lbl;
	@FXML private Label uto_lbl;
	@FXML private Pane sre_pn;
	@FXML private Button sre_izab_btn;
	@FXML private Label sre_dat_lbl;
	@FXML private Label sre_lbl;
	@FXML private Pane cet_pn;
	@FXML private Button cet_izab_btn;
	@FXML private Label cet_dat_lbl;
	@FXML private Label cet_lbl;
	@FXML private Pane pet_pn;
	@FXML private Button pet_izab_btn;
	@FXML private Label pet_dat_lbl;
	@FXML private Label pet_lbl;
	@FXML private Pane sub_pn;
	@FXML private Button sub_izab_btn;
	@FXML private Label sub_dat_lbl;
	@FXML private Label sub_lbl;
	@FXML private Pane ned_pn;
	@FXML private Button ned_izab_btn;
	@FXML private Label ned_dat_lbl;
	@FXML private Label ned_lbl;
	
	

	@FXML private Button tip_karte_izmeni_btn;
	@FXML private Pane prvi_pol_pn;
	@FXML private Button prvi_pol_btn;
	@FXML private Label prvi_pol_br_voz_lbl;
	@FXML private Label prvi_pol_br_voz_value_lbl;
	@FXML private Label prvi_pol_vreme_polaska_lbl;
	@FXML private Label prvi_pol_vreme_polaska_value_lbl;
	@FXML private Label prvi_pol_vreme_dolaska_lbl;
	@FXML private Label prvi_pol_vreme_dolaska_value_lbl;
	@FXML private Label prvi_pol_trajanje_lbl;
	@FXML private Label prvi_pol_trajanje_value_lbl;
	@FXML private Label prvi_pol_cena_lbl;
	@FXML private Label prvi_pol_cena_value_lbl;
	@FXML private Label prvi_pol_rang_lbl;
	@FXML private Label prvi_pol_rang_value_lbl;
	@FXML private Label prvi_pol_rang_opis_lbl;
	@FXML private Label prvi_pol_rang_opis_value_lbl;
	
	@FXML private Pane drugi_pol_pn;
	@FXML private Button drugi_pol_btn;
	@FXML private Label drugi_pol_br_voz_lbl;
	@FXML private Label drugi_pol_br_voz_value_lbl;
	@FXML private Label drugi_pol_vreme_polaska_lbl;
	@FXML private Label drugi_pol_vreme_polaska_value_lbl;
	@FXML private Label drugi_pol_vreme_dolaska_lbl;
	@FXML private Label drugi_pol_vreme_dolaska_value_lbl;
	@FXML private Label drugi_pol_trajanje_lbl;
	@FXML private Label drugi_pol_trajanje_value_lbl;
	@FXML private Label drugi_pol_cena_lbl;
	@FXML private Label drugi_pol_cena_value_lbl;
	@FXML private Label drugi_pol_rang_lbl;
	@FXML private Label drugi_pol_rang_value_lbl;
	@FXML private Label drugi_pol_rang_opis_lbl;
	@FXML private Label drugi_pol_rang_opis_value_lbl;
	
	@FXML private Pane treci_pol_pn;
	@FXML private Button treci_pol_btn;
	@FXML private Label treci_pol_br_voz_lbl;
	@FXML private Label treci_pol_br_voz_value_lbl;
	@FXML private Label treci_pol_vreme_polaska_lbl;
	@FXML private Label treci_pol_vreme_polaska_value_lbl;
	@FXML private Label treci_pol_vreme_dolaska_lbl;
	@FXML private Label treci_pol_vreme_dolaska_value_lbl;
	@FXML private Label treci_pol_trajanje_lbl;
	@FXML private Label treci_pol_trajanje_value_lbl;
	@FXML private Label treci_pol_cena_lbl;
	@FXML private Label treci_pol_cena_value_lbl;
	@FXML private Label treci_pol_rang_lbl;
	@FXML private Label treci_pol_rang_value_lbl;
	@FXML private Label treci_pol_rang_opis_lbl;
	@FXML private Label treci_pol_rang_opis_value_lbl;
	
	@FXML private Pane cetvrti_pol_pn;
	@FXML private Button cetvrti_pol_btn;
	@FXML private Label cetvrti_pol_br_voz_lbl;
	@FXML private Label cetvrti_pol_br_voz_value_lbl;
	@FXML private Label cetvrti_pol_vreme_polaska_lbl;
	@FXML private Label cetvrti_pol_vreme_polaska_value_lbl;
	@FXML private Label cetvrti_pol_vreme_dolaska_lbl;
	@FXML private Label cetvrti_pol_vreme_dolaska_value_lbl;
	@FXML private Label cetvrti_pol_trajanje_lbl;
	@FXML private Label cetvrti_pol_trajanje_value_lbl;
	@FXML private Label cetvrti_pol_cena_lbl;
	@FXML private Label cetvrti_pol_cena_value_lbl;
	@FXML private Label cetvrti_pol_rang_lbl;
	@FXML private Label cetvrti_pol_rang_value_lbl;
	@FXML private Label cetvrti_pol_rang_opis_lbl;
	@FXML private Label cetvrti_pol_rang_opis_value_lbl;
	
	@FXML private Pane peti_pol_pn;
	@FXML private Button peti_pol_btn;
	@FXML private Label peti_pol_br_voz_lbl;
	@FXML private Label peti_pol_br_voz_value_lbl;
	@FXML private Label peti_pol_vreme_polaska_lbl;
	@FXML private Label peti_pol_vreme_polaska_value_lbl;
	@FXML private Label peti_pol_vreme_dolaska_lbl;
	@FXML private Label peti_pol_vreme_dolaska_value_lbl;
	@FXML private Label peti_pol_trajanje_lbl;
	@FXML private Label peti_pol_trajanje_value_lbl;
	@FXML private Label peti_pol_cena_lbl;
	@FXML private Label peti_pol_cena_value_lbl;
	@FXML private Label peti_pol_rang_lbl;
	@FXML private Label peti_pol_rang_value_lbl;
	@FXML private Label peti_pol_rang_opis_lbl;
	@FXML private Label peti_pol_rang_opis_value_lbl;
	
	@FXML private Pane polasci_nova_pn;
	@FXML private Button sled_polasci_btn;
	@FXML private Button polasci_odustani_btn;
	@FXML private Button pret_polasci_btn;
	@FXML private Label izaberi_polazak_lbl;
	@FXML private Label izaberi_polazak_na_dan_lbl;
	@FXML private Label izaberi_polazak_datum_lbl;
	
	@FXML private Label prvi_tip_putnik_lbl;
	@FXML private Label prvi_tip_putnik_br_lbl;
	@FXML private TextField prvi_tip_tf;
	@FXML private Line prvi_tip_ln;
	@FXML private Button prvi_tip_redovna_btn;
	@FXML private Button prvi_tip_srb_k13_btn;
	@FXML private Button prvi_tip_rail_k30_btn;
	@FXML private Button prvi_tip_dete_btn;
	@FXML private Button prvi_tip_pas_btn;
	
	@FXML private Label drugi_tip_putnik_lbl;
	@FXML private Label drugi_tip_putnik_br_lbl;
	@FXML private TextField drugi_tip_tf;
	@FXML private Line drugi_tip_ln;
	@FXML private Button drugi_tip_redovna_btn;
	@FXML private Button drugi_tip_srb_k13_btn;
	@FXML private Button drugi_tip_rail_k30_btn;
	@FXML private Button drugi_tip_dete_btn;
	@FXML private Button drugi_tip_pas_btn;
	
	@FXML private Label treci_tip_putnik_lbl;
	@FXML private Label treci_tip_putnik_br_lbl;
	@FXML private TextField treci_tip_tf;
	@FXML private Line treci_tip_ln;
	@FXML private Button treci_tip_redovna_btn;
	@FXML private Button treci_tip_srb_k13_btn;
	@FXML private Button treci_tip_rail_k30_btn;
	@FXML private Button treci_tip_dete_btn;
	@FXML private Button treci_tip_pas_btn;
	
	
	@FXML private Label cetvrti_tip_putnik_lbl;
	@FXML private Label cetvrti_tip_putnik_br_lbl;
	@FXML private TextField cetvrti_tip_tf;
	@FXML private Line cetvrti_tip_ln;
	@FXML private Button cetvrti_tip_redovna_btn;
	@FXML private Button cetvrti_tip_srb_k13_btn;
	@FXML private Button cetvrti_tip_rail_k30_btn;
	@FXML private Button cetvrti_tip_dete_btn;
	@FXML private Button cetvrti_tip_pas_btn;
	
	@FXML private Label peti_tip_putnik_lbl;
	@FXML private Label peti_tip_putnik_br_lbl;
	@FXML private TextField peti_tip_tf;
	@FXML private Line peti_tip_ln;
	@FXML private Pane tip_karte_pn;
	@FXML private Pane prvi_tip_karte_pn;
	@FXML private Pane drugi_tip_karte_pn;
	@FXML private Pane treci_tip_karte_pn;
	@FXML private Pane cetvrti_tip_karte_pn;
	@FXML private Pane peti_tip_karte_pn;
	@FXML private Button peti_tip_redovna_btn;
	@FXML private Button peti_tip_srb_k13_btn;
	@FXML private Button peti_tip_rail_k30_btn;
	@FXML private Button peti_tip_dete_btn;
	@FXML private Button peti_tip_pas_btn;
	
	
	@FXML private Button tip_karte_odustani_btn;
	
	
	@FXML private Pane placanje_pn;
	@FXML private Label ukupna_cena_value_lbl;
	
	@FXML private Pane prva_karta_pn;
	@FXML private Pane druga_karta_pn;
	@FXML private Pane treca_karta_pn;
	@FXML private Pane cetvrta_karta_pn;
	@FXML private Pane peta_karta_pn;
	
	@FXML private Label prva_polaziste_value_lbl;
	@FXML private Label prva_smer_value_lbl;
	@FXML private Label prva_odrediste_value_lbl;
	@FXML private Label prva_datum_polaziste_value_lbl;
	@FXML private Label prva_vreme_polaziste_value_lbl;
	@FXML private Label prva_polaziste_broj_voza_value_lbl;       		
	@FXML private Label prva_datum_povratak_value_lbl;
	@FXML private Label prva_vreme_povratak_value_lbl;
	@FXML private Label prva_povratak_broj_voza_value_lbl;
	@FXML private Label prva_tip_value_lbl;
	@FXML private Label prva_razred_value_lbl;
	@FXML private Label prva_cena_value_lbl;
	@FXML private Label prva_datum_povratak_lbl;           		
	@FXML private Label prva_datum_povratak_separator_lbl;
	@FXML private Label prva_povratak_broj_voza_lbl;

	
	
	@FXML private Label druga_polaziste_value_lbl;
	@FXML private Label druga_smer_value_lbl;
	@FXML private Label druga_odrediste_value_lbl;
	@FXML private Label druga_datum_polaziste_value_lbl;
	@FXML private Label druga_vreme_polaziste_value_lbl;
	@FXML private Label druga_polaziste_broj_voza_value_lbl;       		
	@FXML private Label druga_datum_povratak_value_lbl;
	@FXML private Label druga_vreme_povratak_value_lbl;
	@FXML private Label druga_povratak_broj_voza_value_lbl;
	@FXML private Label druga_tip_value_lbl;
	@FXML private Label druga_razred_value_lbl;
	@FXML private Label druga_cena_value_lbl;
	@FXML private Label druga_datum_povratak_lbl;           		
	@FXML private Label druga_datum_povratak_separator_lbl;
	@FXML private Label druga_povratak_broj_voza_lbl;
	
	@FXML private Label treca_polaziste_value_lbl;
	@FXML private Label treca_smer_value_lbl;
	@FXML private Label treca_odrediste_value_lbl;
	@FXML private Label treca_datum_polaziste_value_lbl;
	@FXML private Label treca_vreme_polaziste_value_lbl;
	@FXML private Label treca_polaziste_broj_voza_value_lbl;       		
	@FXML private Label treca_datum_povratak_value_lbl;
	@FXML private Label treca_vreme_povratak_value_lbl;
	@FXML private Label treca_povratak_broj_voza_value_lbl;
	@FXML private Label treca_tip_value_lbl;
	@FXML private Label treca_razred_value_lbl;
	@FXML private Label treca_cena_value_lbl;
	@FXML private Label treca_datum_povratak_lbl;           		
	@FXML private Label treca_datum_povratak_separator_lbl;
	@FXML private Label treca_povratak_broj_voza_lbl;
	
	@FXML private Label cetvrta_polaziste_value_lbl;
	@FXML private Label cetvrta_smer_value_lbl;
	@FXML private Label cetvrta_odrediste_value_lbl;
	@FXML private Label cetvrta_datum_polaziste_value_lbl;
	@FXML private Label cetvrta_vreme_polaziste_value_lbl;
	@FXML private Label cetvrta_polaziste_broj_voza_value_lbl;       		
	@FXML private Label cetvrta_datum_povratak_value_lbl;
	@FXML private Label cetvrta_vreme_povratak_value_lbl;
	@FXML private Label cetvrta_povratak_broj_voza_value_lbl;
	@FXML private Label cetvrta_tip_value_lbl;
	@FXML private Label cetvrta_razred_value_lbl;
	@FXML private Label cetvrta_cena_value_lbl;
	@FXML private Label cetvrta_datum_povratak_lbl;           		
	@FXML private Label cetvrta_datum_povratak_separator_lbl;
	@FXML private Label cetvrta_povratak_broj_voza_lbl;
	
	@FXML private Label peta_polaziste_value_lbl;
	@FXML private Label peta_smer_value_lbl;
	@FXML private Label peta_odrediste_value_lbl;
	@FXML private Label peta_datum_polaziste_value_lbl;
	@FXML private Label peta_vreme_polaziste_value_lbl;
	@FXML private Label peta_polaziste_broj_voza_value_lbl;       		
	@FXML private Label peta_datum_povratak_value_lbl;
	@FXML private Label peta_vreme_povratak_value_lbl;
	@FXML private Label peta_povratak_broj_voza_value_lbl;
	@FXML private Label peta_tip_value_lbl;
	@FXML private Label peta_razred_value_lbl;
	@FXML private Label peta_cena_value_lbl;
	@FXML private Label peta_datum_povratak_lbl;           		
	@FXML private Label peta_datum_povratak_separator_lbl;
	@FXML private Label peta_povratak_broj_voza_lbl;
	
	
	@FXML private Pane povratna_karta_pn;
	
	@FXML private Button placanje_nazad_btn;
	
	@FXML private Button novi_pomoc_btn;
	
	@FXML private Pane error_pn;
	@FXML private Label error_lbl_1;
	@FXML private Label error_lbl_2;
	@FXML private Label error_lbl_3;
	@FXML private Label error_lbl_4;
	@FXML private Label error_lbl_5;
	@FXML private Label error_lbl_6;
	@FXML private Label error_lbl_7;
	@FXML private Label error_lbl_8;
	
	
	@FXML private Button tast_A_btn;
	
	@FXML private Button tast_B_btn;
	@FXML private Button tast_C_btn;
	@FXML private Button tast_D_btn;
	@FXML private Button tast_E_btn;
	@FXML private Button tast_F_btn;
	@FXML private Button tast_G_btn;
	@FXML private Button tast_H_btn;
	@FXML private Button tast_I_btn;
	@FXML private Button tast_J_btn;
    
	@FXML private Button tast_K_btn;
	@FXML private Button tast_L_btn;
	@FXML private Button tast_M_btn;
	@FXML private Button tast_N_btn;
	@FXML private Button tast_O_btn;
	@FXML private Button tast_P_btn;
	@FXML private Button tast_R_btn;
	@FXML private Button tast_S_btn;
	@FXML private Button tast_T_btn;
	@FXML private Button tast_Ć_btn;
    
	@FXML private Button tast_Č_btn;
	@FXML private Button tast_U_btn;
	@FXML private Button tast_V_btn;
	@FXML private Button tast_Š_btn;
	@FXML private Button tast_Z_btn;
	@FXML private Button tast_Ž_btn;
	@FXML private Button tast_PONISTI_btn;
	
	@FXML private Button strana1_pomoc_btn;

	@FXML private Button tast_ok_btn;

	@FXML private Pane tastatura_pn;
	
	private SimpleDateFormat _sdfddMMyyyy = new SimpleDateFormat("dd.MM.yyyy");
	
	
	@FXML private Label trenutno_vreme_value_lbl;
	@FXML private Label trenutno_vreme_value_izmeni_lbl;
	@FXML private Label trenutno_vreme_value_placanje_lbl;
	
	
	@FXML private Label placanje_preostalo_vreme_value_lbl;
	
	
	
	@FXML private Pane placanje_result_pn;
	@FXML private Label message_lbl_1;
	@FXML private Label message_lbl_2;
	@FXML private Label message_lbl_3;
	@FXML private Label message_lbl_4;
	@FXML private Label message_lbl_5;
	@FXML private Label message_lbl_6;
	@FXML private Label message_lbl_7;
	@FXML private Label message_lbl_8;
	
	
	
	@FXML private Pane polasci_calendar_pn;
	
	
	@FXML private Pane  help_1_pn;
	@FXML private Pane  help_2_pn;
	@FXML private Pane  help_3_pn;
	
	@FXML private Label period_vaznosti_lbl;
	@FXML private Label vaznost_value_lbl;
	
	
	@FXML private Label tastatura_unesi_id_povlastice;
	
	@FXML private Label id_povlastice_value_lbl;
	
	@FXML private Button placanje_pocetna_btn;
	
	
	@FXML private Button novi_izmeni_nazad_btn;
	@FXML private Button novi_izmeni_pocetna_btn;
	@FXML private Label novi_izmeni_preostalo_vreme_lbl;
	@FXML private Label novi_izmeni_vreme_value_lbl;
	
	
	
    

	
//	@SuppressWarnings("rawtypes")
//	@FXML private TableView stanica_do_tblw;

//	final ToggleGroup smer_putovanja_group = new ToggleGroup();

	//private Timer _timer = null;

	private boolean _is_english = false;
	private boolean _is_cirilica = true;
	
//	private Date _datum_polaska = new Date();
//	private Date _datum_povratka = new Date();
	private int _selected_razred_polazak = DEFAULT_RAZRED;
	private int _selected_razred_povratak = DEFAULT_RAZRED;

	
	
	private List<Button> _stanica_button_group = new ArrayList<Button>();
	private List<Button> _smer_button_group = new ArrayList<Button>();
	private List<Button> _razred_polazak_button_group = new ArrayList<Button>();
	private List<Button> _razred_dolazak_button_group = new ArrayList<Button>();
	private List<Button> _broj_putnika_button_group = new ArrayList<Button>();
	private List<Button> _polazak_button_group = new ArrayList<Button>();
	
	
	private List<Button> _prva_tip_karte_button_group = new ArrayList<Button>();
	private List<Button> _druga_tip_karte_button_group = new ArrayList<Button>();
	private List<Button> _treca_tip_karte_button_group = new ArrayList<Button>();
	private List<Button> _cetvrta_tip_karte_button_group = new ArrayList<Button>();
	private List<Button> _peta_tip_karte_button_group = new ArrayList<Button>();

	private List<Button> _calendar_button_group = new ArrayList<Button>();
	
	//private List<String> _lista_svih_stanica = new ArrayList<String>();
	
	private int _lista_svih_stanica_current_position = 0;
	
	
	private int _lista_svih_polazaka_current_position = 0;
	
	private int _lista_svih_povrataka_current_position = 0;
	
	
	SimpleDateFormat _sdf = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat _sdf_stampa = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat sat_min = new SimpleDateFormat("HH:mm"); 
	SimpleDateFormat sat_min_ss = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat sat_min_no_separator = new SimpleDateFormat("HH mm");
	SimpleDateFormat sat = new SimpleDateFormat("HH"); 
	SimpleDateFormat min = new SimpleDateFormat("mm");
	
//	private ObservableList<VozBean> _data_pol = null;
//	
//	private ObservableList<VozBean> _data_odl = null;
	
	public static final int TIP_REDOVNA_CENA = 0;
	public static final int TIP_SRB_K13 = 1;
	public static final int TIP_RAIL_K30 = 2;
	public static final int TIP_DETE = 3;
	public static final int TIP_PAS = 4;
	public static final int TIP_POVRATNA = 5;
	
	
	private int _prva_karta_tip = TIP_REDOVNA_CENA;
	private int _druga_karta_tip = TIP_REDOVNA_CENA;
	private int _treca_karta_tip = TIP_REDOVNA_CENA;
	private int _cetvrta_karta_tip = TIP_REDOVNA_CENA;
	private int _peta_karta_tip = TIP_REDOVNA_CENA;
	
	private int _prva_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
	private int _druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
	private int _treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
	private int _cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
	private int _peta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
	
	
	private Map<Integer, PovlasticaBean> _popusti_jedan_smer = new HashMap<Integer, PovlasticaBean>();
	private Map<Integer, PovlasticaBean> _popusti_povratna =  new HashMap<Integer, PovlasticaBean>();
	

	//private Clip alert_clip;
	private List<Button> _current_tip_karte_button_group = null;
	private TextField _currentBindTastTF = null;	
	private int _current_bind_tip_karte = -1;
	

	
	private String _ostale_stanice_filter = "";
	
	private Timer _timer = null;
	private SessionTimer _session_timer = null;
	
	private boolean _polazak_true_povratak_false = true;
	
	
	private void resetButtonGroup(List<Button> group, boolean blue_background) {
		
		for(Button current: group) {
			setButtonUnselected(current, blue_background);
		}
		
	}
	
	
	private void setButtonUnselected(Button unselected, boolean blue_background) {
	    

    
		if(blue_background) {
			//tamno braon 555555
			//plavo 3a6dcf
			unselected.setStyle("-fx-background-color: #3a6dcf;-fx-text-fill: white;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}else {
			unselected.setStyle("-fx-background-color: white;-fx-text-fill: black;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}
	}

	private void setButtonSelected(Button selected, boolean ble_background) {
		if(ble_background) {
			//tamno braon 555555
			//plavo 3a6dcf
			selected.setStyle("-fx-background-color: lightgray;-fx-text-fill: black;fx-font-weight: bold;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}else {
			selected.setStyle("-fx-background-color: #3a6dcf;-fx-text-fill: white;fx-font-weight: bold;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}
	}
	
	
	private void setButtonUnselectedGray(Button unselected, boolean gray_background) {
	    

	    
		if(gray_background) {
			//tamno braon 555555
			//plavo 3a6dcf
			unselected.setStyle("-fx-background-color: #555555;-fx-text-fill: white;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}else {
			unselected.setStyle("-fx-background-color: white;-fx-text-fill: black;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}
	}

	private void setButtonSelectedGray(Button selected, boolean gray_background) {
		if(gray_background) {
			//tamno braon 555555
			//plavo 3a6dcf
			selected.setStyle("-fx-background-color: lightgray;-fx-text-fill: black;fx-font-weight: bold;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}else {
			selected.setStyle("-fx-background-color: #555555;-fx-text-fill: white;fx-font-weight: bold;-fx-background-radius: 10px;-fx-border-radius: 10px;");
		}
	}
	
	
	private void setCalendarButtonSelected(int selected) {
		_calendar_button_group.add(pon_izab_btn);
		_calendar_button_group.add(uto_izab_btn);
		_calendar_button_group.add(sre_izab_btn);
		_calendar_button_group.add(cet_izab_btn);
		_calendar_button_group.add(pet_izab_btn);
		_calendar_button_group.add(sub_izab_btn);
		_calendar_button_group.add(ned_izab_btn);
		switch(selected) {
		case 0: pon_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");pon_dat_lbl.setStyle("-fx-text-fill: white;");pon_lbl.setStyle("-fx-text-fill: white;");break;
		case 1: uto_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");uto_dat_lbl.setStyle("-fx-text-fill: white;");uto_lbl.setStyle("-fx-text-fill: white;");break;
		case 2: sre_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");sre_dat_lbl.setStyle("-fx-text-fill: white;");sre_lbl.setStyle("-fx-text-fill: white;");break;
		case 3: cet_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");cet_dat_lbl.setStyle("-fx-text-fill: white;");cet_lbl.setStyle("-fx-text-fill: white;");break;
		case 4: pet_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");pet_dat_lbl.setStyle("-fx-text-fill: white;");pet_lbl.setStyle("-fx-text-fill: white;");break;
		case 5: sub_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");sub_dat_lbl.setStyle("-fx-text-fill: white;");sub_lbl.setStyle("-fx-text-fill: white;");break;
		case 6: ned_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");ned_dat_lbl.setStyle("-fx-text-fill: white;");ned_lbl.setStyle("-fx-text-fill: white;");break;
		}

	}




	//	private void deactivateSessionTimer() {
	//		System.out.println("deactivateSessionTimer");
	//		
	//		resetProgressBar();
	//		progressBar.setVisible(false);
	//		preostalo_lbl.setVisible(false);
	//		
	//	}
	
	
	private void init_frekventne_stanice() {
		if(_frekventne_stanice.size() >= 1) {
			destinacija1_btn.setText(_frekventne_stanice.get(0).getNaziV_UPUTNE_STANICE());
			destinacija1_btn.setVisible(true);
		}
		if(_frekventne_stanice.size() >= 2) {
			destinacija2_btn.setText(_frekventne_stanice.get(1).getNaziV_UPUTNE_STANICE());
			destinacija2_btn.setVisible(true);
		}
		if(_frekventne_stanice.size() >= 3) {
			destinacija3_btn.setText(_frekventne_stanice.get(2).getNaziV_UPUTNE_STANICE());
			destinacija3_btn.setVisible(true);
		}
		if(_frekventne_stanice.size() >= 4) {
			destinacija4_btn.setText(_frekventne_stanice.get(3).getNaziV_UPUTNE_STANICE());
			destinacija4_btn.setVisible(true);
		}
		if(_frekventne_stanice.size() >= 5) {
			destinacija5_btn.setText(_frekventne_stanice.get(4).getNaziV_UPUTNE_STANICE());
			destinacija5_btn.setVisible(true);
		}
		if(_frekventne_stanice.size() >= 6) {
			destinacija6_btn.setText(_frekventne_stanice.get(5).getNaziV_UPUTNE_STANICE());
			destinacija6_btn.setVisible(true);
		}
	}

	//////////////////////IPaymentCallbackInfo/////////////////////
	
	@Override
	public void setPaymentSessionMessage(boolean is_uspesna_kupovina, String message, int broj_putnika) {
		
		     	


		Thread thread = new Thread(() -> {
			try {
				//only for testing try{Thread.sleep(5000);}catch(Exception e) {}
				Platform.runLater(() -> {  
					if(is_uspesna_kupovina) {
						//showSlanjeZahteva("USPEŠNO plaćanje","Hvala što koristite kartomat");
						placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("karticq_za_placanje_uspesna_gif")+"')");
						message_lbl_1.setText("");
						message_lbl_2.setText("");
						message_lbl_3.setText("");
						message_lbl_4.setText("");
						message_lbl_5.setText("");
					}else {
						//showSlanjeZahteva("NEUSPEŠNO plaćanje","Hvala što koristite kartomat");
						placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("karticq_za_placanje_neuspesna_gif")+"')");
						String jednina_mnozina = broj_putnika > 1 ? "karata" : "karte";
						message_lbl_1.setText("Vaša kupovina "+jednina_mnozina+" je NEUSPEŠNA");
						if(message.equals("")) {
							message_lbl_2.setText("Plaćanje vašom kreditnom karticom je ODBIJENO");
							message_lbl_3.setText("Hvala što koristite usluge kartomata Srbija Voza");
							message_lbl_4.setText("");
							message_lbl_5.setText("");
						}else {
							message_lbl_2.setText("Plaćanje vašom kreditnom karticom je ODBIJENO");
							message_lbl_3.setText(message);
							message_lbl_4.setText("");
							message_lbl_5.setText("");
						}
					}
				});
				Thread.sleep(10000);
				placanje_pn.setVisible(false);
				odrediste_pn.setVisible(true);
				Platform.runLater(() -> { 
					//						closeSession();
					stop_recycling_session();
				});

			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				placanje_result_pn.setVisible(false);
			}
		});
		thread.setDaemon(true);
		thread.start();

	}
	
	
	//////////////////////end of IPaymentCallbackInfo//////////////


	//////////////////////Initializable impl	
	

	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		SESSION_DURATION_SECONDS = Integer.parseInt(getProperties().getProperty("session.duration", "90"));
		System.out.println("SESSION_DURATION_SECONDS = " + SESSION_DURATION_SECONDS);
		
		MAC_ADDRESS  = getProperties().getProperty("mac.address");
		System.out.println("MAC_ADDRESS = " + MAC_ADDRESS);
		
		PAYMENT_IP_ADDRESS = getProperties().getProperty("payment.ip.address");
		System.out.println("PAYMENT_IP_ADDRESS = " + PAYMENT_IP_ADDRESS);
		PAYMENT_PORT = Integer.parseInt(getProperties().getProperty("payment.ip.port", "3000"));
		System.out.println("PAYMENT_PORT = " + PAYMENT_PORT);
		
		
		SV_API_URL = getProperties().getProperty("srbija.voz.api.url");
		System.out.println("SV_API_URL = " + SV_API_URL);
		SV_API_CONN_TIME = Integer.parseInt(getProperties().getProperty("srbija.voz.api.conn.timeout", "3000"));
		SV_API_READ_TIME = Integer.parseInt(getProperties().getProperty("srbija.voz.api.read.timeout", "12000"));
		
		
		KARTOMAT_STANICA_NAZIV_LATIN = getProperties().getProperty("naziv.stanice.latin");
		KARTOMAT_STANICA_NAZIV_CIR = getProperties().getProperty("naziv.stanice.cir");
		KARTOMAT_GRAD = getProperties().getProperty("kartomat.grad", "Beograd");
		
		
		String lista_brzih_vozova = getProperties().getProperty("voz.br.rezervacija", "540,541,542,543,544,545,546,547,548,549,740,741,742,743,744,745,746,747");
		String[] lista_brzih_vozova_splt = lista_brzih_vozova.split(",");
		for(String current_br_voz : lista_brzih_vozova_splt) {
			_vozovi_sa_rezervacijom.add(current_br_voz);
		}
		
		System.out.println("Lista vozova sa rezervacijom je: " + _vozovi_sa_rezervacijom);
		
		Calendar today = Calendar.getInstance();
		datum_polaska_value_lbl.setText(_sdf.format(today.getTime()));
		datum_izmena_value_lbl.setText(_sdf.format(today.getTime()));
		datum_povratka_polazak_value_lbl.setText(_sdf.format(today.getTime()));
		datum_povratka_izmena_value_lbl.setText(_sdf.format(today.getTime()));


		this.resources = resources;
		
		


//		jedan_smer_rb.setToggleGroup(smer_putovanja_group);
//		povratna_smer_rb.setToggleGroup(smer_putovanja_group);
		init_clock();
		try {
			_kartomat = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getKartomat(MAC_ADDRESS);
			//Label b = new Label(_kartomat.getNaziV_STANICE().toUpperCase());
			kartomat_stanica_lbl.setText(KARTOMAT_STANICA_NAZIV_LATIN.substring("Polazna stanica : ".length())/*this.resources.getString("beograd_centar")*//*_kartomat.getNaziV_STANICE().toUpperCase()*/);
//			b.setLayoutX(150.0);
//			b.setLayoutY(20.0);
//			b.setPrefHeight(31.0);
//			b.setPrefWidth(400.0);
//			b.setAlignment(Pos.CENTER);
//			b.setStyle("-fx-font-family: 'Montserrat';-fx-text-fill: black;-fx-font-size: 20pt;-fx-font-weight: bold;-fx-text-alignment:left;");
			//root_pane.getChildren().add(b);
			
			_frekventne_stanice = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getFrekventneStanice(MAC_ADDRESS);
			StanicaNames.loadCirLatinFS(_frekventne_stanice);
			_ostale_stanice = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getStanicaIDs();
			StanicaNames.loadCirLatinOS(_ostale_stanice);
			azuriraj_listu_ostalinh_filtered_stanica("");
			
			//_ostale_stanice_filtered = new ArrayList<StanicaIDBean>(_ostale_stanice);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception when try to init data from SV api, details: " + e.getMessage() + ", application will exit");
			System.exit(-1);
		}
		
		init_frekventne_stanice();
//		init_polasci();
		setPopusti();
//		addButtonToTable();
//		init_main_cmb();
//		init_table_view();
//		//_block_recycling_manager.block();
//		resetProgressBar();
//		handleOsvezi();

		_stanica_button_group.add(destinacija1_btn);
		_stanica_button_group.add(destinacija2_btn);
		_stanica_button_group.add(destinacija3_btn);
		_stanica_button_group.add(destinacija4_btn);
		_stanica_button_group.add(destinacija5_btn);
		_stanica_button_group.add(destinacija6_btn);
		_stanica_button_group.add(ostale_stanice_1);
		_stanica_button_group.add(ostale_stanice_2);
		_stanica_button_group.add(ostale_stanice_3);
		_stanica_button_group.add(ostale_stanice_4);
		_stanica_button_group.add(ostale_stanice_5);
		_stanica_button_group.add(ostale_stanice_6);
		_stanica_button_group.add(ostale_stanice_7);
		_stanica_button_group.add(ostale_stanice_8);
		_stanica_button_group.add(ostale_stanice_9);
		_stanica_button_group.add(ostale_stanice_10);
		_stanica_button_group.add(ostale_stanice_11);
		_stanica_button_group.add(ostale_stanice_12);
		_stanica_button_group.add(ostale_stanice_13);
		_stanica_button_group.add(ostale_stanice_14);
		_stanica_button_group.add(ostale_stanice_15);
		_stanica_button_group.add(ostale_stanice_16);
		
		
	

		 
		 
		_smer_button_group.add(smer_u_jednom_btn); 
		_smer_button_group.add(smer_povratni_btn);

		_razred_polazak_button_group.add(razred_prvi_polazak_btn); 
		_razred_polazak_button_group.add(razred_drugi_polazak_btn); 

		
		
		_razred_dolazak_button_group.add(razred_prvi_dolazak_btn); 
		_razred_dolazak_button_group.add(razred_drugi_dolazak_btn);

		_broj_putnika_button_group.add(jedan_btn);
		_broj_putnika_button_group.add(dva_btn);
		_broj_putnika_button_group.add(tri_btn);
		_broj_putnika_button_group.add(cetiri_btn);
		_broj_putnika_button_group.add(pet_btn);
		
		
		_polazak_button_group.add(prvi_pol_btn);
		_polazak_button_group.add(drugi_pol_btn);
		_polazak_button_group.add(treci_pol_btn);
		_polazak_button_group.add(cetvrti_pol_btn);
		_polazak_button_group.add(peti_pol_btn);
		
		
		_prva_tip_karte_button_group.add(prvi_tip_redovna_btn);
		_prva_tip_karte_button_group.add(prvi_tip_srb_k13_btn);
		_prva_tip_karte_button_group.add(prvi_tip_rail_k30_btn);
		_prva_tip_karte_button_group.add(prvi_tip_dete_btn);
		_prva_tip_karte_button_group.add(prvi_tip_pas_btn);
		
		_druga_tip_karte_button_group.add(drugi_tip_redovna_btn);
		_druga_tip_karte_button_group.add(drugi_tip_srb_k13_btn);
		_druga_tip_karte_button_group.add(drugi_tip_rail_k30_btn);
		_druga_tip_karte_button_group.add(drugi_tip_dete_btn);
		_druga_tip_karte_button_group.add(drugi_tip_pas_btn);
		
		_treca_tip_karte_button_group.add(treci_tip_redovna_btn);
		_treca_tip_karte_button_group.add(treci_tip_srb_k13_btn);
		_treca_tip_karte_button_group.add(treci_tip_rail_k30_btn);
		_treca_tip_karte_button_group.add(treci_tip_dete_btn);
		_treca_tip_karte_button_group.add(treci_tip_pas_btn);
		
		_cetvrta_tip_karte_button_group.add(cetvrti_tip_redovna_btn);
		_cetvrta_tip_karte_button_group.add(cetvrti_tip_srb_k13_btn);
		_cetvrta_tip_karte_button_group.add(cetvrti_tip_rail_k30_btn);
		_cetvrta_tip_karte_button_group.add(cetvrti_tip_dete_btn);
		_cetvrta_tip_karte_button_group.add(cetvrti_tip_pas_btn);
		
		_peta_tip_karte_button_group.add(peti_tip_redovna_btn);
		_peta_tip_karte_button_group.add(peti_tip_srb_k13_btn);
		_peta_tip_karte_button_group.add(peti_tip_rail_k30_btn);
		_peta_tip_karte_button_group.add(peti_tip_dete_btn);
		_peta_tip_karte_button_group.add(peti_tip_pas_btn);

		_calendar_button_group.add(pon_izab_btn);
		_calendar_button_group.add(uto_izab_btn);
		_calendar_button_group.add(sre_izab_btn);
		_calendar_button_group.add(cet_izab_btn);
		_calendar_button_group.add(pet_izab_btn);
		_calendar_button_group.add(sub_izab_btn);
		_calendar_button_group.add(ned_izab_btn);
		
		handle_1_btn();
		handle_razred_drugi_polazak();
		handle_smer(false);
//		kupi_kartu_opcije_btn.setVisible(false);
		

		

		
		handle_ostale_napred_pagination();
		resetButtonGroup(_stanica_button_group, true);
		
		
			stanica_lbl_slide_1 = new Label(KARTOMAT_STANICA_NAZIV_CIR);//_kartomat.getNaziV_STANICE());
			stanica_lbl_slide_1.setLayoutX(470.0);
			stanica_lbl_slide_1.setLayoutY(50.0);
			stanica_lbl_slide_1.setPrefHeight(31.0);
			stanica_lbl_slide_1.setPrefWidth(600.0);
			stanica_lbl_slide_1.setStyle("-fx-font-family: 'Montserrat'; -fx-font-size: 20pt; -fx-text-fill: white; -fx-font-weight: bold; -fx-text-alignment:center;");
			
			red_header_pn.getChildren().add(stanica_lbl_slide_1);
			
			stanica_lbl_slide_2 = new Label(KARTOMAT_STANICA_NAZIV_CIR);//_kartomat.getNaziV_STANICE());
			stanica_lbl_slide_2.setLayoutX(470.0);
			stanica_lbl_slide_2.setLayoutY(50.0);
			stanica_lbl_slide_2.setPrefHeight(31.0);
			stanica_lbl_slide_2.setPrefWidth(600.0);
			stanica_lbl_slide_2.setStyle("-fx-font-family: 'Montserrat'; -fx-font-size: 20pt; -fx-text-fill: white; -fx-font-weight: bold; -fx-text-alignment:center;");
			
			red_header_izmeni_pn.getChildren().add(stanica_lbl_slide_2);
			
			stanica_lbl_slide_3 = new Label(KARTOMAT_STANICA_NAZIV_CIR);//_kartomat.getNaziV_STANICE());
			stanica_lbl_slide_3.setLayoutX(470.0);
			stanica_lbl_slide_3.setLayoutY(50.0);
			stanica_lbl_slide_3.setPrefHeight(31.0);
			stanica_lbl_slide_3.setPrefWidth(600.0);
			stanica_lbl_slide_3.setStyle("-fx-font-family: 'Montserrat'; -fx-font-size: 20pt; -fx-text-fill: white; -fx-font-weight: bold; -fx-text-alignment:center;");
			red_header_placanje_pn.getChildren().add(stanica_lbl_slide_3);
			
			
//			-fx-text-fill: white;
//			 
//			
//			
//			prefHeight="31.0" prefWidth="500.0" styleClass="velika_bela_slova"
		
//		datum_polaska_value_lbl.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//               System.out.println("Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
//               if(!t1.equals("")) {
//            	   setPodaciOdVoza();
//               }
//            }
//        }); 
		
		datum_polaska_value_lbl.textProperty().addListener(datum_polaska_changeListener);
		
//		datum_povratka_polazak_value_lbl.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//               System.out.println("Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
//               if(!t1.equals("")) {
//            	   setPodaciOdVozaPovratak();
//               }
//            }
//        }); 
		
		datum_povratka_polazak_value_lbl.textProperty().addListener(datum_povratka_polazak_changeListener);
		
//		razred_polazak_value_lbl.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//               System.out.println("Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
//               if(!t1.equals("")) {
//            	   setPodaciOdVoza();
//               }
//            }
//        }); 
		
		razred_polazak_value_lbl.textProperty().addListener(rezred_polazak_changeListener);
			
//		razred_odlazak_value_lbl.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//               System.out.println("Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
//               if(!t1.equals("")) {
//            	   setPodaciOdVozaPovratak();
//               }
//            }
//        }); 
		
//		razred_odlazak_value_lbl.textProperty().addListener(rezred_odlazak_changeListener);
		
//		smer_value_lbl.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//               System.out.println("Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
//               if(!t1.equals("")) {
//            	   setCena();
//               }
//            }
//        });
		smer_value_lbl.textProperty().addListener(smer_changeListener);
		
		tip_karte_polazak_value_lbl.textProperty().addListener(tip_karte_changeListener);
		
		
		setSerbianCir();
		
				
		
	}
	
	private ChangeListener datum_polaska_changeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
           System.out.println("datum_polaska_changeListener Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
           if(!t1.equals("")) {
        	   setPodaciOdVoza();
           }
        }
    }; 
	
	private ChangeListener datum_povratka_polazak_changeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
           System.out.println("datum_povratka_polazak_changeListener Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
           if(!t1.equals("")) {
        	   setPodaciOdVozaPovratak();
           }
        }
    }; 
	
	private ChangeListener rezred_polazak_changeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
           System.out.println("rezred_polazak_changeListener Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
           if(!t1.equals("")) {
        	   if((t.equals("Други") && t1.equals("Drugi")) ||
        			   (t.equals("Drugi") && t1.equals("Други")) || 
        			   (t.equals("Први") && t1.equals("Prvi")) ||
        			   (t.equals("Prvi") && t1.equals("Први")) ||

        			   (t.equals("Други") && t1.equals("Second")) ||
        			   (t.equals("Second") && t1.equals("Други")) || 
        			   (t.equals("Први") && t1.equals("First")) ||
        			   (t.equals("First") && t1.equals("Први")) ||

        			   (t.equals("Second") && t1.equals("Drugi")) ||
        			   (t.equals("Drugi") && t1.equals("Second")) || 
        			   (t.equals("First") && t1.equals("Prvi")) ||
        			   (t.equals("Prvi") && t1.equals("First")) 
        			   ){
        		   
        	   }else {
        		   //setPodaciOdVoza();
        		   setCena(_selected_voz_povratak != null);
        	   }//end if-else

           }//end if
        }
    }; 
	
	private ChangeListener rezred_odlazak_changeListener =  new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
           System.out.println("rezred_odlazak_changeListener Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
           if(!t1.equals("")) {
        	   if((t.equals("Други") && t1.equals("Drugi")) ||
        			   (t.equals("Drugi") && t1.equals("Други")) || 
        			   (t.equals("Први") && t1.equals("Prvi")) ||
        			   (t.equals("Prvi") && t1.equals("Први")) ||

        			   (t.equals("Други") && t1.equals("Second")) ||
        			   (t.equals("Second") && t1.equals("Други")) || 
        			   (t.equals("Први") && t1.equals("First")) ||
        			   (t.equals("First") && t1.equals("Први")) ||

        			   (t.equals("Second") && t1.equals("Drugi")) ||
        			   (t.equals("Drugi") && t1.equals("Second")) || 
        			   (t.equals("First") && t1.equals("Prvi")) ||
        			   (t.equals("Prvi") && t1.equals("First")) 
        			   ){
        		   
        	   }else {
        		   setPodaciOdVozaPovratak();
        	   }//end if-else
        	   
           }
        }
    };
	
	private ChangeListener smer_changeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
           System.out.println("smer_changeListener Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
           if(!t1.equals("")) {
        	   setCena(t1.equals(resources.getString("smer_povratni_btn")));
           }
        }
	};
	
	private ChangeListener tip_karte_changeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
           System.out.println("tip_karte_changeListener Label Text Changed, ov = " + ov.getValue() + " t = " + t + ", t1 = " + t1);
           if(!t1.equals("")) {
        	   //setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));
           }
        }
	};
	
	
	private boolean isVozWithReservations(String voz_id) {
		return _vozovi_sa_rezervacijom.contains( voz_id);
	}
	
	
	private List<StanicaIDBean> getNextPagination(boolean is_napred){
		List<StanicaIDBean> to_return = new ArrayList<StanicaIDBean>();
		int start_list = is_napred ? _lista_svih_stanica_current_position :
			Math.max(_lista_svih_stanica_current_position - OSTALE_STANICE_SIZE- OSTALE_STANICE_SIZE, 0);
		if( start_list >  _ostale_stanice_filtered.size() || start_list < 0) return null;
		for(int i = start_list ; i < Math.min (_ostale_stanice_filtered.size() , start_list + OSTALE_STANICE_SIZE ) ; i++ ) {
			to_return.add(_ostale_stanice_filtered.get(i));
		}
		_lista_svih_stanica_current_position = ( start_list + OSTALE_STANICE_SIZE) > _ostale_stanice_filtered.size() ? _lista_svih_stanica_current_position :  start_list + OSTALE_STANICE_SIZE  ;
		return to_return;
	}
	
	private List<VozBean> getNextPaginationpPolasci(boolean is_napred){
		List<VozBean> to_return = new ArrayList<VozBean>();
		
		List<VozBean> polasci_povratci = 	_polazak_true_povratak_false ? _svi_polasci :/*_svi_povratci*/getListaPovrataka();
		
		boolean is_pagination = false;
		
		
		if(_polazak_true_povratak_false) {
			//polasci
			if(is_napred) {
				if((_lista_svih_polazaka_current_position + SVI_POLASCI_SIZE) > polasci_povratci.size()){
					is_pagination = false;
				}else {
					_lista_svih_polazaka_current_position = _lista_svih_polazaka_current_position + SVI_POLASCI_SIZE;
					is_pagination = true;
				}
			}else {
				if(_lista_svih_polazaka_current_position < SVI_POLASCI_SIZE) {
					is_pagination = false;
				}else {
					_lista_svih_polazaka_current_position = _lista_svih_polazaka_current_position - SVI_POLASCI_SIZE;
					is_pagination = true;
				}
			}
		}else {
			//povratci
			if(is_napred) {
				if((_lista_svih_povrataka_current_position + SVI_POLASCI_SIZE) > polasci_povratci.size()){
					is_pagination = false;
				}else {
					_lista_svih_povrataka_current_position = _lista_svih_povrataka_current_position + SVI_POLASCI_SIZE;
					is_pagination = true;
				}
			}else {
				if(_lista_svih_povrataka_current_position < SVI_POLASCI_SIZE) {
					is_pagination = false;
				}else {
					_lista_svih_povrataka_current_position = _lista_svih_povrataka_current_position - SVI_POLASCI_SIZE;
					is_pagination = true;
				}
			}
		}
		
		int lista_current_position = 	_polazak_true_povratak_false ? _lista_svih_polazaka_current_position : _lista_svih_povrataka_current_position ;
			
		
		for(int i = lista_current_position ; i < Math.min (polasci_povratci.size() , lista_current_position + SVI_POLASCI_SIZE ) ; i++ ) {
			to_return.add(polasci_povratci.get(i));
		}

//		int start_list = is_napred ? lista_current_position :
//			Math.max(lista_current_position - SVI_POLASCI_SIZE- SVI_POLASCI_SIZE, 0);
//		if( start_list >  polasci_povratci.size() || start_list < 0) return null;
//		for(int i = start_list ; i < Math.min (polasci_povratci.size() , start_list + SVI_POLASCI_SIZE ) ; i++ ) {
//			to_return.add(polasci_povratci.get(i));
//		}
//		if(polazak_true_povratak_false) {
//			_lista_svih_polazaka_current_position = ( start_list + SVI_POLASCI_SIZE) > polasci_povratci.size() ? 
//					_lista_svih_polazaka_current_position :  start_list + SVI_POLASCI_SIZE  ;
//		}else {
//			_lista_svih_povrataka_current_position = ( start_list + SVI_POLASCI_SIZE) > polasci_povratci.size() ? 
//					_lista_svih_povrataka_current_position :  start_list + SVI_POLASCI_SIZE  ;
//		}
		System.out.println("_lista_svih_polazaka_current_position = " + _lista_svih_polazaka_current_position + ", is_pagination = " + is_pagination);
		System.out.println("_lista_svih_povrataka_current_position = " + _lista_svih_povrataka_current_position + ", is_pagination = " + is_pagination);
		return to_return;
	}
	
	private String from_tip_karte_id(int id) {
		switch(id) {
		case TIP_REDOVNA_CENA: return resources.getString("redovna_cena");
		case TIP_SRB_K13: return "SRB K-13";
		case TIP_RAIL_K30: return "RAIL K-30";
		case TIP_DETE: return resources.getString("dete");
		case TIP_PAS: return resources.getString("pas");
		case TIP_POVRATNA: return resources.getString("povratna");
		}
		return "";
	}
	
	
	private void show_poslednja_provera_slide() {
		handleOsvezi();
		int broj_karata = Integer.parseInt(broj_putnika_lbl.getText());
		boolean is_povratna = _selected_voz_povratak != null;

		if(broj_karata > 0) {
			
			prva_polaziste_value_lbl.setText(_kartomat.getNaziV_STANICE());
			prva_smer_value_lbl.setText(smer_value_lbl.getText());
			prva_odrediste_value_lbl.setText(odrediste_value_lbl.getText());
			prva_datum_polaziste_value_lbl.setText(datum_polaska_value_lbl.getText());
			prva_vreme_polaziste_value_lbl.setText(vreme_polazak_value_lbl.getText());
			prva_polaziste_broj_voza_value_lbl.setText("" + _selected_voz.getBrvoz());       
			if(is_povratna) {
				prva_datum_povratak_value_lbl.setText(datum_povratka_polazak_value_lbl.getText());
				prva_vreme_povratak_value_lbl.setText(vreme_dolazak_dolazak_value_lbl.getText());
				prva_povratak_broj_voza_value_lbl.setText("" + _selected_voz_povratak.getBrvoz());
			}
			prva_datum_povratak_lbl.setVisible(false);        		
			prva_datum_povratak_separator_lbl.setVisible(false);  
			prva_povratak_broj_voza_lbl.setVisible(false);  
			prva_datum_povratak_value_lbl.setVisible(false); 
			prva_vreme_povratak_value_lbl.setVisible(false); 
			prva_povratak_broj_voza_value_lbl.setVisible(false); 

			prva_tip_value_lbl.setText(from_tip_karte_id(_prva_karta_tip));
			prva_razred_value_lbl.setText(razred_polazak_value_lbl.getText());
			//prva_cena_value_lbl.setText(prvi_pol_cena_value_lbl.getText());
			
			prva_karta_pn.setVisible(true);
		}
		
		if(broj_karata > 1) {
			
			druga_polaziste_value_lbl.setText(_kartomat.getNaziV_STANICE());
			druga_smer_value_lbl.setText(smer_value_lbl.getText());
			druga_odrediste_value_lbl.setText(odrediste_value_lbl.getText());
			druga_datum_polaziste_value_lbl.setText(datum_polaska_value_lbl.getText());
			druga_vreme_polaziste_value_lbl.setText(vreme_polazak_value_lbl.getText());
			druga_polaziste_broj_voza_value_lbl.setText("" + _selected_voz.getBrvoz());       
			if(is_povratna) {
				druga_datum_povratak_value_lbl.setText(datum_povratka_polazak_value_lbl.getText());
				druga_vreme_povratak_value_lbl.setText(vreme_dolazak_dolazak_value_lbl.getText());
				druga_povratak_broj_voza_value_lbl.setText("" + _selected_voz_povratak.getBrvoz());
			}
			druga_datum_povratak_lbl.setVisible(false);        		
			druga_datum_povratak_separator_lbl.setVisible(false);  
			druga_povratak_broj_voza_lbl.setVisible(false);  
			druga_datum_povratak_value_lbl.setVisible(false); 
			druga_vreme_povratak_value_lbl.setVisible(false); 
			druga_povratak_broj_voza_value_lbl.setVisible(false); 

			druga_tip_value_lbl.setText(from_tip_karte_id(_druga_karta_tip));
			druga_razred_value_lbl.setText(razred_polazak_value_lbl.getText());
			//druga_cena_value_lbl.setText(drugi_pol_cena_value_lbl.getText());
			
			druga_karta_pn.setVisible(true);
		}else {
			druga_karta_pn.setVisible(false);
		}
		
		if(broj_karata > 2) {
			
			treca_polaziste_value_lbl.setText(_kartomat.getNaziV_STANICE());
			treca_smer_value_lbl.setText(smer_value_lbl.getText());
			treca_odrediste_value_lbl.setText(odrediste_value_lbl.getText());
			treca_datum_polaziste_value_lbl.setText(datum_polaska_value_lbl.getText());
			treca_vreme_polaziste_value_lbl.setText(vreme_polazak_value_lbl.getText());
			treca_polaziste_broj_voza_value_lbl.setText("" + _selected_voz.getBrvoz());       
			if(is_povratna) {
				treca_datum_povratak_value_lbl.setText(datum_povratka_polazak_value_lbl.getText());
				treca_vreme_povratak_value_lbl.setText(vreme_dolazak_dolazak_value_lbl.getText());
				treca_povratak_broj_voza_value_lbl.setText("" + _selected_voz_povratak.getBrvoz());
			}
			treca_datum_povratak_lbl.setVisible(false);        		
			treca_datum_povratak_separator_lbl.setVisible(false);  
			treca_povratak_broj_voza_lbl.setVisible(false);  
			treca_datum_povratak_value_lbl.setVisible(false); 
			treca_vreme_povratak_value_lbl.setVisible(false); 
			treca_povratak_broj_voza_value_lbl.setVisible(false); 

			treca_tip_value_lbl.setText(from_tip_karte_id(_treca_karta_tip));
			treca_razred_value_lbl.setText(razred_polazak_value_lbl.getText());
			//treca_cena_value_lbl.setText(treci_pol_cena_value_lbl.getText());
			
			treca_karta_pn.setVisible(true);
		}else {
			treca_karta_pn.setVisible(false);
		}
		
		if(broj_karata > 3) {
			
			cetvrta_polaziste_value_lbl.setText(_kartomat.getNaziV_STANICE());
			cetvrta_smer_value_lbl.setText(smer_value_lbl.getText());
			cetvrta_odrediste_value_lbl.setText(odrediste_value_lbl.getText());
			cetvrta_datum_polaziste_value_lbl.setText(datum_polaska_value_lbl.getText());
			cetvrta_vreme_polaziste_value_lbl.setText(vreme_polazak_value_lbl.getText());
			cetvrta_polaziste_broj_voza_value_lbl.setText("" + _selected_voz.getBrvoz());       
			if(is_povratna) {
				cetvrta_datum_povratak_value_lbl.setText(datum_povratka_polazak_value_lbl.getText());
				cetvrta_vreme_povratak_value_lbl.setText(vreme_dolazak_dolazak_value_lbl.getText());
				cetvrta_povratak_broj_voza_value_lbl.setText("" + _selected_voz_povratak.getBrvoz());
			}
			cetvrta_datum_povratak_lbl.setVisible(false);        		
			cetvrta_datum_povratak_separator_lbl.setVisible(false);  
			cetvrta_povratak_broj_voza_lbl.setVisible(false);  
			cetvrta_datum_povratak_value_lbl.setVisible(false); 
			cetvrta_vreme_povratak_value_lbl.setVisible(false); 
			cetvrta_povratak_broj_voza_value_lbl.setVisible(false); 

			cetvrta_tip_value_lbl.setText(from_tip_karte_id(_cetvrta_karta_tip));
			cetvrta_razred_value_lbl.setText(razred_polazak_value_lbl.getText());
			//cetvrta_cena_value_lbl.setText(cetvrti_pol_cena_value_lbl.getText());
			
			cetvrta_karta_pn.setVisible(true);
		}else {
			cetvrta_karta_pn.setVisible(false);
		}
		
		if(broj_karata > 4) {
			
			peta_polaziste_value_lbl.setText(_kartomat.getNaziV_STANICE());
			peta_smer_value_lbl.setText(smer_value_lbl.getText());
			peta_odrediste_value_lbl.setText(odrediste_value_lbl.getText());
			peta_datum_polaziste_value_lbl.setText(datum_polaska_value_lbl.getText());
			peta_vreme_polaziste_value_lbl.setText(vreme_polazak_value_lbl.getText());
			peta_polaziste_broj_voza_value_lbl.setText("" + _selected_voz.getBrvoz());       
			if(is_povratna) {
				peta_datum_povratak_value_lbl.setText(datum_povratka_polazak_value_lbl.getText());
				peta_vreme_povratak_value_lbl.setText(vreme_dolazak_dolazak_value_lbl.getText());
				peta_povratak_broj_voza_value_lbl.setText("" + _selected_voz_povratak.getBrvoz());
			}
			peta_datum_povratak_lbl.setVisible(false);        		
			peta_datum_povratak_separator_lbl.setVisible(false);  
			peta_povratak_broj_voza_lbl.setVisible(false);  
			peta_datum_povratak_value_lbl.setVisible(false); 
			peta_vreme_povratak_value_lbl.setVisible(false); 
			peta_povratak_broj_voza_value_lbl.setVisible(false); 

			peta_tip_value_lbl.setText(from_tip_karte_id(_peta_karta_tip));
			peta_razred_value_lbl.setText(razred_polazak_value_lbl.getText());
			//peta_cena_value_lbl.setText(peti_pol_cena_value_lbl.getText());
			
			peta_karta_pn.setVisible(true);
		}else {
			peta_karta_pn.setVisible(false);
		}

		ukupna_cena_value_lbl.setText(ukupno_cena_value_lbl.getText());

		placanje_pn.setVisible(true);
//		restartSessionExpiration();
		
	}










	//////////////////////Initializable impl END



	///////////////////////InitializationIface start



	private void setSerbian() {
		System.out.println("setLocale on locale = SRPSKI");
		_is_english= false;
		_is_cirilica = false;
		
		setLocale(resources_srb);
		stanica_lbl_slide_1.setText(KARTOMAT_STANICA_NAZIV_LATIN);
		stanica_lbl_slide_2.setText(KARTOMAT_STANICA_NAZIV_LATIN);
		stanica_lbl_slide_3.setText(KARTOMAT_STANICA_NAZIV_LATIN);
		kartomat_stanica_lbl.setText(KARTOMAT_STANICA_NAZIV_LATIN.substring("Polazna stanica : ".length())/*this.resources.getString("beograd_centar")*//*_kartomat.getNaziV_STANICE().toUpperCase()*/);
	}

	private void setEnglish() {
		System.out.println("setLocale on locale = ENGLISH");
		_is_english = true;
		_is_cirilica = false;
		setLocale(resources_eng);
		stanica_lbl_slide_1.setText(KARTOMAT_STANICA_NAZIV_LATIN);
		stanica_lbl_slide_2.setText(KARTOMAT_STANICA_NAZIV_LATIN);
		stanica_lbl_slide_3.setText(KARTOMAT_STANICA_NAZIV_LATIN);
		kartomat_stanica_lbl.setText(KARTOMAT_STANICA_NAZIV_LATIN.substring("Polazna stanica : ".length())/*this.resources.getString("beograd_centar")*//*_kartomat.getNaziV_STANICE().toUpperCase()*/);
	}
	
	private void setSerbianCir() {
		System.out.println("setLocale on locale = SRPSKI cirilica");
		_is_english = false;
		_is_cirilica = true;
		setLocale(resources_cir);
		stanica_lbl_slide_1.setText(KARTOMAT_STANICA_NAZIV_CIR);
		stanica_lbl_slide_2.setText(KARTOMAT_STANICA_NAZIV_CIR);
		stanica_lbl_slide_3.setText(KARTOMAT_STANICA_NAZIV_CIR);
		kartomat_stanica_lbl.setText(KARTOMAT_STANICA_NAZIV_CIR.substring("Polazna stanica : ".length())/*this.resources.getString("beograd_centar")*//*_kartomat.getNaziV_STANICE().toUpperCase()*/);
	}

	private void setLocale(ResourceBundle bundle) {

		ResourceBundle.clearCache();
		this.resources = bundle;
		System.out.println("resetLocale finished");
		
		biraj_odrediste_lbl.setText(resources.getString("biraj_odrediste_lbl"));
		ostale_destinacije_btn.setText(resources.getString("ostale_destinacije_btn"));
		kartomat_stanica_text_lbl1.setText(resources.getString("kartomat_stanica_text_lbl1"));
		kartomat_stanica_text_lbl2.setText(resources.getString("kartomat_stanica_text_lbl2"));
		kartomat_stanica_text_lbl3.setText(resources.getString("kartomat_stanica_text_lbl3"));
		kartomat_stanica_text_lbl4.setText(resources.getString("kartomat_stanica_text_lbl4"));
		kartomat_stanica_text_lbl5.setText(resources.getString("kartomat_stanica_text_lbl5"));
		kartomat_stanica_text_lbl6.setText(resources.getString("kartomat_stanica_text_lbl6"));
		kartomat_stanica_text_lbl7.setText(resources.getString("kartomat_stanica_text_lbl7"));
		kartomat_stanica_text_lbl8.setText(resources.getString("kartomat_stanica_text_lbl8"));
		kartomat_stanica_text_lbl9.setText(resources.getString("kartomat_stanica_text_lbl9"));
		kartomat_stanica_text_lbl10.setText(resources.getString("kartomat_stanica_text_lbl10"));
		kartomat_stanica_text_lbl11.setText(resources.getString("kartomat_stanica_text_lbl11"));
		
		kartomat_stanica_text_lbl12.setText(resources.getString("kartomat_stanica_text_lbl12"));
		kartomat_stanica_text_lbl13.setText(resources.getString("kartomat_stanica_text_lbl13"));
		kartomat_stanica_text_lbl14.setText(resources.getString("kartomat_stanica_text_lbl14"));
		kartomat_stanica_text_lbl15.setText(resources.getString("kartomat_stanica_text_lbl15"));
		kartomat_stanica_text_lbl16.setText(resources.getString("kartomat_stanica_text_lbl16"));
		kartomat_stanica_text_lbl17.setText(resources.getString("kartomat_stanica_text_lbl17"));
		kartomat_stanica_text_lbl18.setText(resources.getString("kartomat_stanica_text_lbl18"));
		kartomat_stanica_text_lbl19.setText(resources.getString("kartomat_stanica_text_lbl19"));
		kartomat_stanica_text_lbl20.setText(resources.getString("kartomat_stanica_text_lbl20"));
		kartomat_stanica_text_lbl21.setText(resources.getString("kartomat_stanica_text_lbl21"));
		kartomat_stanica_text_lbl22.setText(resources.getString("kartomat_stanica_text_lbl22"));
		
		kartomat_stanica2_text_lbl1.setText(resources.getString("kartomat_stanica2_text_lbl1"));
		kartomat_stanica2_text_lbl2.setText(resources.getString("kartomat_stanica2_text_lbl2"));
		kartomat_stanica2_text_lbl3.setText(resources.getString("kartomat_stanica2_text_lbl3"));
		kartomat_stanica2_text_lbl4.setText(resources.getString("kartomat_stanica2_text_lbl4"));
		kartomat_stanica2_text_lbl5.setText(resources.getString("kartomat_stanica2_text_lbl5"));
		kartomat_stanica2_text_lbl6.setText(resources.getString("kartomat_stanica2_text_lbl6"));
		kartomat_stanica2_text_lbl7.setText(resources.getString("kartomat_stanica2_text_lbl7"));
		kartomat_stanica2_text_lbl8.setText(resources.getString("kartomat_stanica2_text_lbl8"));
		kartomat_stanica2_text_lbl9.setText(resources.getString("kartomat_stanica2_text_lbl9"));
		kartomat_stanica2_text_lbl10.setText(resources.getString("kartomat_stanica2_text_lbl10"));
		kartomat_stanica2_text_lbl11.setText(resources.getString("kartomat_stanica2_text_lbl11"));
		
		kartomat_stanica2_text_lbl12.setText(resources.getString("kartomat_stanica2_text_lbl12"));
		kartomat_stanica2_text_lbl13.setText(resources.getString("kartomat_stanica2_text_lbl13"));
		kartomat_stanica2_text_lbl14.setText(resources.getString("kartomat_stanica2_text_lbl14"));
		kartomat_stanica2_text_lbl15.setText(resources.getString("kartomat_stanica2_text_lbl15"));
		kartomat_stanica2_text_lbl16.setText(resources.getString("kartomat_stanica2_text_lbl16"));
		kartomat_stanica2_text_lbl17.setText(resources.getString("kartomat_stanica2_text_lbl17"));
		kartomat_stanica2_text_lbl18.setText(resources.getString("kartomat_stanica2_text_lbl18"));
		kartomat_stanica2_text_lbl19.setText(resources.getString("kartomat_stanica2_text_lbl19"));
		kartomat_stanica2_text_lbl20.setText(resources.getString("kartomat_stanica2_text_lbl20"));
		kartomat_stanica2_text_lbl21.setText(resources.getString("kartomat_stanica2_text_lbl21"));
		kartomat_stanica2_text_lbl22.setText(resources.getString("kartomat_stanica2_text_lbl22"));
		
		kartomat_stanica3_text_lbl1.setText(resources.getString("kartomat_stanica3_text_lbl1"));
		kartomat_stanica3_text_lbl2.setText(resources.getString("kartomat_stanica3_text_lbl2"));
		kartomat_stanica3_text_lbl3.setText(resources.getString("kartomat_stanica3_text_lbl3"));
		kartomat_stanica3_text_lbl4.setText(resources.getString("kartomat_stanica3_text_lbl4"));
		kartomat_stanica3_text_lbl5.setText(resources.getString("kartomat_stanica3_text_lbl5"));
		kartomat_stanica3_text_lbl6.setText(resources.getString("kartomat_stanica3_text_lbl6"));
		kartomat_stanica3_text_lbl7.setText(resources.getString("kartomat_stanica3_text_lbl7"));
		kartomat_stanica3_text_lbl8.setText(resources.getString("kartomat_stanica3_text_lbl8"));
		kartomat_stanica3_text_lbl9.setText(resources.getString("kartomat_stanica3_text_lbl9"));
		kartomat_stanica3_text_lbl10.setText(resources.getString("kartomat_stanica3_text_lbl10"));
		kartomat_stanica3_text_lbl11.setText(resources.getString("kartomat_stanica3_text_lbl11"));
		
		kartomat_stanica3_text_lbl12.setText(resources.getString("kartomat_stanica3_text_lbl12"));
		kartomat_stanica3_text_lbl13.setText(resources.getString("kartomat_stanica3_text_lbl13"));
		kartomat_stanica3_text_lbl14.setText(resources.getString("kartomat_stanica3_text_lbl14"));
		kartomat_stanica3_text_lbl15.setText(resources.getString("kartomat_stanica3_text_lbl15"));
		kartomat_stanica3_text_lbl16.setText(resources.getString("kartomat_stanica3_text_lbl16"));
		kartomat_stanica3_text_lbl17.setText(resources.getString("kartomat_stanica3_text_lbl17"));
		kartomat_stanica3_text_lbl18.setText(resources.getString("kartomat_stanica3_text_lbl18"));
		kartomat_stanica3_text_lbl19.setText(resources.getString("kartomat_stanica3_text_lbl19"));
		kartomat_stanica3_text_lbl20.setText(resources.getString("kartomat_stanica3_text_lbl20"));
		kartomat_stanica3_text_lbl21.setText(resources.getString("kartomat_stanica3_text_lbl21"));
		kartomat_stanica3_text_lbl22.setText(resources.getString("kartomat_stanica3_text_lbl22"));
		
		trenutno_vreme_lbl.setText(resources.getString("trenutno_vreme_lbl"));
		//stanica_lbl.setText(resources.getString("stanica_lbl"));
		
		novi_pomoc_btn.setText(resources.getString("novi_pomoc_btn"));
		drugi_naslov_lbl.setText(resources.getString("drugi_naslov_lbl"));
		polaziste_lbl.setText(resources.getString("polaziste_lbl"));
		odrediste_lbl.setText(resources.getString("odrediste_lbl"));
		polazak_lbl.setText(resources.getString("polazak_lbl"));
		dolazak_lbl.setText(resources.getString("dolazak_lbl"));
		odrediste_polazak_lbl.setText(resources.getString("odrediste"));
		broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		razred_lbl.setText(resources.getString("razred_lbl"));
		povlastice_lbl.setText(resources.getString("povlastice_lbl"));
		povlastice_polazak_lbl.setText(resources.getString("povlastice_lbl")); 
		razred_polazak_value_lbl.setText(resources.getString("razred_polazak_value_lbl"));
//		broj_putnika_naslov_lbl.setText(resources.getString(""));
		smer_lbl.setText(resources.getString("smer_lbl"));
		smer_polazak_lbl.setText(resources.getString("smer_lbl"));
		relacija_dolazak_polaziste_lbl.setText(resources.getString("relacija_dolazak_polaziste_lbl"));
		relacija_dolazak_odrediste_lbl.setText(resources.getString("relacija_dolazak_odrediste_lbl"));
		
		datum_povratka_polazak_lbl.setText(resources.getString("datum_povratka_polazak_lbl"));
		datum_povratka_dolazak_lbl.setText(resources.getString("datum_povratka_dolazak_lbl"));
		broj_voza_dolazak_lbl.setText(resources.getString("broj_voza_dolazak_lbl"));
		razred_dolazak_lbl.setText(resources.getString("razred_dolazak_lbl"));
		tip_karte_dolazak_lbl.setText(resources.getString("tip_karte_dolazak_lbl"));
		razred_odlazak_value_lbl.setText(resources.getString("razred_odlazak_value_lbl"));
		tip_karte_dolazak_value_lbl.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		
		ukupno_lbl.setText(resources.getString("ukupno_lbl"));
		novi_kupi_karte_btn.setText(resources.getString("novi_kupi_karte_btn"));
		rsd_lbl.setText(resources.getString("rsd_lbl"));
		rsd_ukupno_lbl.setText(resources.getString("rsd_lbl"));
		
		odrediste_izmena_value_lbl.setText(resources.getString("odrediste_izmena_value_lbl"));
		izmeni_polaske_btn.setText(resources.getString("izmeni_polaske_btn"));
		smer_u_jednom_btn.setText(resources.getString("smer_u_jednom_btn"));
		smer_povratni_btn.setText(resources.getString("smer_povratni_btn"));
		broj_putnika_naslov_lbl.setText(resources.getString("broj_putnika_naslov_lbl"));
		izmeni_tip_karte_btn.setText(resources.getString("izmeni_tip_karte_btn"));
		polazak_datum_lbl.setText(resources.getString("polazak_datum_lbl"));
		datum_polazak_btn.setText(resources.getString("datum_polazak_btn"));
		vreme_lbl.setText(resources.getString("vreme_lbl"));
		vreme_polazak_btn.setText(resources.getString("vreme_polazak_btn"));
		razred_polazak_lbl.setText(resources.getString("razred_polazak_lbl"));
		razred_prvi_polazak_btn.setText(resources.getString("razred_prvi_polazak_btn"));
		razred_drugi_polazak_btn.setText(resources.getString("razred_drugi_polazak_btn"));
		povratak_datum_lbl.setText(resources.getString("povratak_datum_lbl"));
		datum_dolazak_btn.setText(resources.getString("datum_dolazak_btn"));
		vreme_dolazak_btn.setText(resources.getString("vreme_dolazak_btn"));
		razred_povratak_lbl.setText(resources.getString("razred_povratak_lbl"));
		razred_prvi_dolazak_btn.setText(resources.getString("razred_prvi_dolazak_btn"));
		razred_drugi_dolazak_btn.setText(resources.getString("razred_drugi_dolazak_btn"));
		broj_putnika_text_lbl.setText(resources.getString("broj_putnika_text_lbl"));
		
		ostale_napred_pagination_btn.setText(resources.getString("ostale_napred_pagination_btn"));
		ostale_nazad_pagination_btn.setText(resources.getString("ostale_nazad_pagination_btn"));
		ostale_odustani_btn.setText(resources.getString("ostale_odustani_btn"));
		tast_PONISTI_btn.setText(resources.getString("tast_PONISTI_btn"));
		
		sled_ned_cal_btn.setText(resources.getString("sled_ned_cal_btn"));
		calendar_odustani_btn.setText(resources.getString("calendar_odustani_btn"));
		pret_ned_cal_btn.setText(resources.getString("pret_ned_cal_btn"));
		izaberi_datum_cal_lbl.setText(resources.getString("izaberi_datum_cal_lbl"));
		//pon_izab_btn.setText(resources.getString("izaberi"));
		pon_lbl.setText(resources.getString("pon_lbl"));
		//uto_izab_btn.setText(resources.getString("izaberi"));
		uto_lbl.setText(resources.getString("uto_lbl"));
		//sre_izab_btn.setText(resources.getString("izaberi"));
		sre_lbl.setText(resources.getString("sre_lbl"));
		//cet_izab_btn.setText(resources.getString("izaberi"));
		cet_lbl.setText(resources.getString("cet_lbl"));
		//pet_izab_btn.setText(resources.getString("izaberi"));
		pet_lbl.setText(resources.getString("pet_lbl"));
		//sub_izab_btn.setText(resources.getString("izaberi"));
		sub_lbl.setText(resources.getString("sub_lbl"));
		//ned_izab_btn.setText(resources.getString("izaberi"));
		ned_lbl.setText(resources.getString("ned_lbl"));
		
		sled_polasci_btn.setText(resources.getString("sled_polasci_btn"));
		polasci_odustani_btn.setText(resources.getString("odustani"));
		pret_polasci_btn.setText(resources.getString("pret_polasci"));
		izaberi_polazak_lbl.setText(resources.getString("izaberi_polazak_lbl"));
		izaberi_polazak_na_dan_lbl.setText(resources.getString("izaberi_polazak_na_dan_lbl"));
		
		prvi_pol_br_voz_lbl.setText(resources.getString("broj_voza"));
        prvi_pol_vreme_polaska_lbl.setText(resources.getString("vreme_polaska"));
        prvi_pol_vreme_dolaska_lbl.setText(resources.getString("vreme_dolaska"));
        prvi_pol_trajanje_lbl.setText(resources.getString("trajanje_putovanja"));
        prvi_pol_cena_lbl.setText(resources.getString("cena"));
        prvi_pol_rang_lbl.setText(resources.getString("rang"));
        prvi_pol_btn.setText(resources.getString("izaberi"));
        
		drugi_pol_br_voz_lbl.setText(resources.getString("broj_voza"));
        drugi_pol_vreme_polaska_lbl.setText(resources.getString("vreme_polaska"));
        drugi_pol_vreme_dolaska_lbl.setText(resources.getString("vreme_dolaska"));
        drugi_pol_trajanje_lbl.setText(resources.getString("trajanje_putovanja"));
        drugi_pol_cena_lbl.setText(resources.getString("cena"));
        drugi_pol_rang_lbl.setText(resources.getString("rang"));
        drugi_pol_btn.setText(resources.getString("izaberi"));
        
		treci_pol_br_voz_lbl.setText(resources.getString("broj_voza"));
        treci_pol_vreme_polaska_lbl.setText(resources.getString("vreme_polaska"));
        treci_pol_vreme_dolaska_lbl.setText(resources.getString("vreme_dolaska"));
        treci_pol_trajanje_lbl.setText(resources.getString("trajanje_putovanja"));
        treci_pol_cena_lbl.setText(resources.getString("cena"));
        treci_pol_rang_lbl.setText(resources.getString("rang"));
        treci_pol_btn.setText(resources.getString("izaberi"));
        
		cetvrti_pol_br_voz_lbl.setText(resources.getString("broj_voza"));
        cetvrti_pol_vreme_polaska_lbl.setText(resources.getString("vreme_polaska"));
        cetvrti_pol_vreme_dolaska_lbl.setText(resources.getString("vreme_dolaska"));
        cetvrti_pol_trajanje_lbl.setText(resources.getString("trajanje_putovanja"));
        cetvrti_pol_cena_lbl.setText(resources.getString("cena"));
        cetvrti_pol_rang_lbl.setText(resources.getString("rang"));
        cetvrti_pol_btn.setText(resources.getString("izaberi"));
        
		peti_pol_br_voz_lbl.setText(resources.getString("broj_voza"));
        peti_pol_vreme_polaska_lbl.setText(resources.getString("vreme_polaska"));
        peti_pol_vreme_dolaska_lbl.setText(resources.getString("vreme_dolaska"));
        peti_pol_trajanje_lbl.setText(resources.getString("trajanje_putovanja"));
        peti_pol_cena_lbl.setText(resources.getString("cena"));
        peti_pol_rang_lbl.setText(resources.getString("rang"));
        peti_pol_btn.setText(resources.getString("izaberi"));
		
        izaberi_tip_karte_lbl.setText(resources.getString("izaberi_primenjenje_povlastice"));
        prvi_tip_putnik_lbl.setText(resources.getString("putnik"));
        prvi_tip_redovna_btn.setText(resources.getString("redovna_cena"));
        prvi_tip_dete_btn.setText(resources.getString("dete"));
        prvi_tip_pas_btn.setText(resources.getString("pas"));
        
        izaberi_tip_karte_lbl.setText(resources.getString("placanje"));
        ukupno_za_naplatu_lbl.setText(resources.getString("ukupno_za_naplatu"));
        placanje_posruka_1_lbl.setText(resources.getString("placanje_posruka_1_lbl"));
        placanje_posruka_2_lbl.setText(resources.getString("placanje_posruka_2_lbl"));
        placanje_posruka_3_lbl.setText(resources.getString("placanje_posruka_3_lbl"));
        placanje_posruka_4_lbl.setText(resources.getString("placanje_posruka_4_lbl"));
        placanje_posruka_5_lbl.setText(resources.getString("placanje_posruka_5_lbl"));
        
        plati_kartu_final_btn.setText(resources.getString("plati_kartu_final_btn"));
        placanje_preostalo_vreme_lbl.setText(resources.getString("placanje_preostalo_vreme_lbl"));
        
        tip_karte_odustani_btn.setText(resources.getString("odustani"));
        
        
        prva_polaziste_lbl.setText(resources.getString("polaziste"));
		prva_smer_lbl.setText(resources.getString("smer"));
		prva_odrediste_lbl.setText(resources.getString("odrediste"));		
		prva_datum_polaziste_lbl.setText(resources.getString("datum_vreme_polaska"));				
      	prva_polaziste_broj_voza_lbl.setText(resources.getString("broj_voza_lbl")); 	
      	prva_datum_povratak_lbl.setText(resources.getString("datum_vreme_povratka"));           		
      	prva_povratak_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		prva_tip_lbl.setText(resources.getString("primenjene_povlastice"));
		prva_razred_lbl.setText(resources.getString("razred"));
		prva_cena_lbl.setText(resources.getString("cena"));
		
        druga_polaziste_lbl.setText(resources.getString("polaziste"));
		druga_smer_lbl.setText(resources.getString("smer"));
		druga_odrediste_lbl.setText(resources.getString("odrediste"));		
		druga_datum_polaziste_lbl.setText(resources.getString("datum_vreme_polaska"));				
      	druga_polaziste_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
      	druga_datum_povratak_lbl.setText(resources.getString("datum_vreme_povratka"));           		
      	druga_povratak_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
      	druga_tip_lbl.setText(resources.getString("primenjene_povlastice"));
      	druga_razred_lbl.setText(resources.getString("razred"));
      	druga_cena_lbl.setText(resources.getString("cena"));
		
        treca_polaziste_lbl.setText(resources.getString("polaziste"));
		treca_smer_lbl.setText(resources.getString("smer"));
		treca_odrediste_lbl.setText(resources.getString("odrediste"));		
		treca_datum_polaziste_lbl.setText(resources.getString("datum_vreme_polaska"));				
      	treca_polaziste_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
      	treca_datum_povratak_lbl.setText(resources.getString("datum_vreme_povratka"));           		
      	treca_povratak_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		treca_tip_lbl.setText(resources.getString("primenjene_povlastice"));
		treca_razred_lbl.setText(resources.getString("razred"));
		treca_cena_lbl.setText(resources.getString("cena"));
		
		cetvrta_polaziste_lbl.setText(resources.getString("polaziste"));
		cetvrta_smer_lbl.setText(resources.getString("smer"));
		cetvrta_odrediste_lbl.setText(resources.getString("odrediste"));		
		cetvrta_datum_polaziste_lbl.setText(resources.getString("datum_vreme_polaska"));				
		cetvrta_polaziste_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		cetvrta_datum_povratak_lbl.setText(resources.getString("datum_vreme_povratka"));           		
		cetvrta_povratak_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		cetvrta_tip_lbl.setText(resources.getString("primenjene_povlastice"));
		cetvrta_razred_lbl.setText(resources.getString("razred"));
		cetvrta_cena_lbl.setText(resources.getString("cena"));
		
		peta_polaziste_lbl.setText(resources.getString("polaziste"));
		peta_smer_lbl.setText(resources.getString("smer"));
		peta_odrediste_lbl.setText(resources.getString("odrediste"));		
		peta_datum_polaziste_lbl.setText(resources.getString("datum_vreme_polaska"));				
		peta_polaziste_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		peta_datum_povratak_lbl.setText(resources.getString("datum_vreme_povratka"));           		
		peta_povratak_broj_voza_lbl.setText(resources.getString("broj_voza_lbl"));
		peta_tip_lbl.setText(resources.getString("primenjene_povlastice"));
		peta_razred_lbl.setText(resources.getString("razred"));
		peta_cena_lbl.setText(resources.getString("cena"));
		
		
		placanje_nazad_btn.setText(resources.getString("nazad"));
		tip_karte_izmeni_btn.setText(resources.getString("potvrdi"));
		
		
		tip_karte_polazak_value_lbl.setText(resources.getString("redovna_cena"));
		tip_karte_izmena_value_lbl.setText(resources.getString("redovna_cena"));
		tip_karte_dolazak_value_lbl.setText(resources.getString("redovna_cena"));
		smer_value_lbl.setText(resources.getString("smer_u_jednom"));
		
		
		period_vaznosti_lbl.setText(resources.getString("period_vaznosti_lbl"));
		
		tast_ok_btn.setText(resources.getString("potvrdi_broj_btn"));
		
		tastatura_unesi_id_povlastice.setText(resources.getString("tastatura_unesi_id_povlastice"));

		placanje_pocetna_btn.setText(resources.getString("pocetna"));
		
		strana1_pomoc_btn.setText(resources.getString("novi_pomoc_btn"));

	}



	
	//////////////////////SessionControlIface impl

	public void stop_recycling_session() {

		//if(operation_result.equals(OperationResultENUM.SESSION_TIMEOUT)) {
		////_block_recycling_manager.block();
		//}

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				Platform.runLater(() -> {

					_session_timer.stopCounting();
					resetForNewSession();

				});


				return null;
			}
		};

		new Thread(task).start();

	}


	//public void start_recycling_session() {
	//
	//reset_recycling_session();
	//}


	public void update_recycling_session(int broj_sekundi) {
		System.out.println("update_recycling_session, broj_sekundi = " + broj_sekundi);

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				Platform.runLater(() -> {
        			String trenutno_sesija_istek = "" + broj_sekundi + " sekundi";
        			placanje_preostalo_vreme_value_lbl.setText(trenutno_sesija_istek); 
        			novi_izmeni_vreme_value_lbl.setText(trenutno_sesija_istek);					
				});


				return null;
			}
		};

		new Thread(task).start();
	}

	public int get_session_duration() {
		return SESSION_DURATION_SECONDS;
	}

	/////////////////////SessionControlIface impl END


	///////////////////////////////button handlers START


	public void handlePomoc() {
		System.out.println("handlePomoc");
	}

	public void handleIzmeniPolaske() {
		System.out.println("handleIzmeniPolaske");
//		prvi_panel_set_visible(false);
		//polasci_pn.setVisible(!polasci_pn.isVisible());
//		polasci_nova_pn.setVisible(true);
//		polasci_nova_pn.toFront();
		odrediste_pn.setVisible(true);
		odrediste_pn.toFront();
		
//		odabrane_opcije_pn.setVisible(false);
//		izmeni_opcije_pn.setVisible(false);
//		izmeni_polazke_dalje_btn.setText("IZMENI");
//		izmeni_polazke_nazad_btn.setText("ODUSTANI");

	}
	
	public void handleIzmeniTipKarte() {
		System.out.println("handleIzmeniTipKarte");

		int broj_karata = Integer.parseInt(broj_putnika_lbl.getText());
		prvi_tip_karte_pn.setVisible(broj_karata > 0);
		prvi_tip_putnik_lbl.setVisible(broj_karata > 0);
		prvi_tip_putnik_br_lbl.setVisible(broj_karata > 0);
		prvi_tip_tf.setVisible(broj_karata > 0 && _prva_karta_tip == TIP_SRB_K13 || _prva_karta_tip == TIP_RAIL_K30);
		prvi_tip_ln.setVisible(broj_karata > 0);
		
		drugi_tip_karte_pn.setVisible(broj_karata > 1);
		drugi_tip_putnik_lbl.setVisible(broj_karata > 1);
		drugi_tip_putnik_br_lbl.setVisible(broj_karata > 1);
		drugi_tip_tf.setVisible(broj_karata > 1 && _druga_karta_tip == TIP_SRB_K13 || _druga_karta_tip == TIP_RAIL_K30);
		drugi_tip_ln.setVisible(broj_karata > 1);
		
		treci_tip_karte_pn.setVisible(broj_karata > 2);
		treci_tip_putnik_lbl.setVisible(broj_karata > 2);
		treci_tip_putnik_br_lbl.setVisible(broj_karata > 2);
		treci_tip_tf.setVisible(broj_karata > 2 && _treca_karta_tip == TIP_SRB_K13 || _treca_karta_tip == TIP_RAIL_K30);
		treci_tip_ln.setVisible(broj_karata > 2);
		
		cetvrti_tip_karte_pn.setVisible(broj_karata > 3);
		cetvrti_tip_putnik_lbl.setVisible(broj_karata > 3);
		cetvrti_tip_putnik_br_lbl.setVisible(broj_karata > 3);
		cetvrti_tip_tf.setVisible(broj_karata > 3 && _cetvrta_karta_tip == TIP_SRB_K13 || _cetvrta_karta_tip == TIP_RAIL_K30) ;
		cetvrti_tip_ln.setVisible(broj_karata > 3);
		
		peti_tip_karte_pn.setVisible(broj_karata > 4);
		peti_tip_putnik_lbl.setVisible(broj_karata > 4);
		peti_tip_putnik_br_lbl.setVisible(broj_karata > 4);
		peti_tip_tf.setVisible(broj_karata > 4 && _peta_karta_tip == TIP_SRB_K13 || _peta_karta_tip == TIP_RAIL_K30);
		peti_tip_ln.setVisible(broj_karata > 4);
		
		boolean is_drugi_razred = _selected_razred_polazak == DEFAULT_RAZRED && _selected_razred_povratak == DEFAULT_RAZRED;
		prvi_tip_pas_btn.setVisible(is_drugi_razred);
		drugi_tip_pas_btn.setVisible(is_drugi_razred);
		treci_tip_pas_btn.setVisible(is_drugi_razred);
		cetvrti_tip_pas_btn.setVisible(is_drugi_razred);
		peti_tip_pas_btn.setVisible(is_drugi_razred);
		_prva_karta_tip_not_confirmed = _prva_karta_tip;
		_druga_karta_tip_not_confirmed = _druga_karta_tip;
		_treca_karta_tip_not_confirmed = _treca_karta_tip;
		_cetvrta_karta_tip_not_confirmed = _cetvrta_karta_tip;
		_peta_karta_tip_not_confirmed = _peta_karta_tip;
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, _prva_karta_tip);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, _druga_karta_tip);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, _treca_karta_tip);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, _cetvrta_karta_tip);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, _peta_karta_tip);
		
		tip_karte_izmeni_btn.setVisible(false);
		tip_karte_pn.setVisible(true);
		
	}

//	public void handleIncerease() {
////		System.out.println("handleIncerease");
////		int br_put = Integer.parseInt(broj_putnika_tf.getText());
////		broj_putnika_tf.setText("" + ++br_put);
//	}

//	public void handleDecerease() {
////		System.out.println("handleDecerease");
////		int br_put = Integer.parseInt(broj_putnika_tf.getText());
////		if(br_put > 1) {
////			broj_putnika_tf.setText("" + --br_put);
////		}
//	}

//	public void handleDaljePolasci(){
//		if(izmeni_polazke_dalje_btn.getText().startsWith("DALJE")) {
//			prvi_pol.setFill(Color.web("#ba4a4a"));
//			prvi_cir.setFill(Color.web("#ba4a4a"));
//			drugi_pol.setFill(Color.web("#ff1f2e"));
//			drugi_cir.setFill(Color.web("#ff1f2e"));
////			polasci_pn.setVisible(false);
//
//
//
//			TicketPane first_tick_pn = new TicketPane(1);
//			first_tick_pn.setLayoutX(10);
//			first_tick_pn.setLayoutY(50);
//			//		ticket_vbx.getChildren().add(first_tick_pn);
//			TicketPane second_tick_pn = new TicketPane(2);
//			second_tick_pn.setLayoutX(260);
//			second_tick_pn.setLayoutY(50);
//			//		ticket_vbx.getChildren().add(second_tick_pn);
//
//			//		ticket_vbx.setMinHeight(600);
//			//		ticket_ancp.setMinHeight(600);
//			ticket_pn.getChildren().add(first_tick_pn);
//			ticket_pn.getChildren().add(second_tick_pn);
//
//			ticket_pn.setVisible(true);
//		}else {
//			//TODO - izmeni polaske iz setovanih vrednosti u tabeli
//			
//			handleZatvoriPolasci();
//		}
//	}
	
//	public void handleZatvoriPolasci(){
//		polasci_pn.setVisible(false);
//		handleNazadTipKarte();
//		izmeni_polazke_dalje_btn.setText("DALJE >>");
//		izmeni_polazke_nazad_btn.setText("NAZAD <<");
//	}

//	public void handleDaljeTipKarte(){
//		System.out.println("handleDaljeTipKarte");
//		if(tip_karte_dalje_btn.getText().startsWith("DALJE")) {
//			prvi_pol.setFill(Color.web("#ba4a4a"));
//			prvi_cir.setFill(Color.web("#ba4a4a"));
//			drugi_pol.setFill(Color.web("#ba4a4a"));
//			drugi_cir.setFill(Color.web("#ba4a4a"));
//			treci_pol.setFill(Color.web("#ff1f2e"));
//			treci_cir.setFill(Color.web("#ff1f2e"));
//			ticket_pn.setVisible(false);
//			placanje_karticom_pn.setVisible(false);
////			odabrane_opcije_pn.setVisible(false);
//			izmeni_opcije_pn.setVisible(false);
//			odrediste_pn.setVisible(false);
//			show_poslednja_provera_slide();
//			
//		}else {
//			//TODO - update selektovanih podataka za tipove karata
//			handleNazadTipKarte();
//			tip_karte_dalje_btn.setText("DALJE >>");
//			tip_karte_odustani_btn.setText("NAZAD <<");
//
//		}
//	}
	
//	public void handleNazadTipKarte(){
//		
//		System.out.println("handleNazadTipKarte");
////		ticket_pn.setVisible(false);
//		prvi_panel_set_visible(true);
////		ticket_pn.setVisible(true);
////		odabrane_opcije_pn.setVisible(true);
////		izmeni_opcije_pn.setVisible(true);
//
//
//	}
	
//	public void handleDaljeKarte() {
//		prvi_pol.setFill(Color.web("#ba4a4a"));
//		prvi_cir.setFill(Color.web("#ba4a4a"));
//		drugi_pol.setFill(Color.web("#ba4a4a"));
//		drugi_cir.setFill(Color.web("#ba4a4a"));
//		treci_pol.setFill(Color.web("#ba4a4a"));
//		treci_cir.setFill(Color.web("#ba4a4a"));
//		cetvrti_pol.setFill(Color.web("#ff1f2e"));
//		cetvrti_cir.setFill(Color.web("#ff1f2e"));
//		ticket_pn.setVisible(false);
////		polasci_pn.setVisible(false);
////		odabrane_opcije_pn.setVisible(false);
//		izmeni_opcije_pn.setVisible(false);
//		placanje_karticom_pn.setVisible(true);
//	}
	
//	public void  handleNazadKarte() {
//		
//	}
	
//	public void  handleNazadPlacanje() {
//		handleDaljeTipKarte();
//	}
	

	public void handleLanguage(){
		if(!_is_cirilica) {
			setSerbianCir();
		}else {
			if(_is_english) {
				setSerbian();
			}else {
				setEnglish();
			}
		}

	}
	

	
	public void handle_destinacija1() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(0);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija1_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija1_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
		

	}
	
	public void handle_destinacija2() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(1);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija2_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija2_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_destinacija3() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(2);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija3_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija3_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_destinacija4() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(3);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija4_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija4_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_destinacija5() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(4);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija5_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija5_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_destinacija6() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(5);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija6_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija6_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_destinacija7() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(6);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija6_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija7_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_destinacija8() {
		FrekventneStaniceBean dest = _frekventne_stanice.get(7);
		if(dest != null) {
			_selected_voz = null;
			odrediste_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			odrediste_izmena_value_lbl.setText(dest.getNaziV_UPUTNE_STANICE());
			resetButtonGroup(_stanica_button_group, true);
			//destinacija6_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
			setButtonSelected(destinacija8_btn, true);
			//uslov_za_drugi_korak();
			setPodaciOdVoza();
			handleOsvezi();
		}
	}
	
	public void handle_ostale() {
//		System.out.println("jebeni naziv stanice je: " + _kartomat.getNaziV_STANICE());
//		stanica_lbl.setText(_kartomat.getNaziV_STANICE());
		ostale_stanice_pn.setVisible(true);
		ostale_stanice_pn.toFront();
		handleOsvezi();
	}
	
	public void handle_ostale_stanice_1() {
		handle_ostale_common(ostale_stanice_1);
	}
	
	public void handle_ostale_stanice_2() {
		handle_ostale_common(ostale_stanice_2);
	}
	
	public void handle_ostale_stanice_3() {
		handle_ostale_common(ostale_stanice_3);
	}
	
	public void handle_ostale_stanice_4() {
		handle_ostale_common(ostale_stanice_4);
	}
	
	public void handle_ostale_stanice_5() {
		handle_ostale_common(ostale_stanice_5);
	}
	
	public void handle_ostale_stanice_6() {
		handle_ostale_common(ostale_stanice_6);
	}
	
	public void handle_ostale_stanice_7() {
		handle_ostale_common(ostale_stanice_7);
	}
	
	public void handle_ostale_stanice_8() {
		handle_ostale_common(ostale_stanice_8);
	}
	
	public void handle_ostale_stanice_9() {
		handle_ostale_common(ostale_stanice_9);
	}
	
	public void handle_ostale_stanice_10() {
		handle_ostale_common(ostale_stanice_10);
	}
	
	public void handle_ostale_stanice_11() {
		handle_ostale_common(ostale_stanice_11);
	}
	
	public void handle_ostale_stanice_12() {
		handle_ostale_common(ostale_stanice_12);
	}
	
	public void handle_ostale_stanice_13() {
		handle_ostale_common(ostale_stanice_13);
	}
	
	public void handle_ostale_stanice_14() {
		handle_ostale_common(ostale_stanice_14);
	}
	
	public void handle_ostale_stanice_15() {
		handle_ostale_common(ostale_stanice_15);
	}
	
	public void handle_ostale_stanice_16() {
		handle_ostale_common(ostale_stanice_16);
	}
	
	
	private void setPodaciOdVoza() {
		_selected_voz = null;
		try {
			if(_selected_voz == null) {
				List<VozBean> lista_vozova = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getListaVozovaNaTrasi(_kartomat.getSifrA_STANICE(), 
						getSifraStaniceFromNaziv(odrediste_value_lbl.getText()), datum_polaska_value_lbl.getText(), 
						Integer.parseInt(broj_putnika_lbl.getText()), _selected_razred_polazak);
				_svi_polasci = lista_vozova;

				if(lista_vozova.size() > 0) {

					for( VozBean current : lista_vozova) {
						String[] splt = current.getVremep().split(":");
						int vreme_pol = Integer.parseInt(splt[0]) *100 + Integer.parseInt(splt[1]);
						int current_time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) * 100 + Integer.parseInt(new SimpleDateFormat("mm").format(new Date()));
						if(true/*vreme_pol > current_time + 5/*5 minuta*/) {
							_selected_voz = current;
							
							break;
						} 
						
						
						//current.getVremep()
					}
				}else {
					run_info("Ne postoje polasci za izabrano", 
							"odredište " + odrediste_value_lbl.getText() , "i izabrani datum " + datum_polaska_value_lbl.getText(), 
							"i izabrabi broj putnika " + broj_putnika_lbl.getText(), "i izabrani razred. Promenite ", "neki od parametara i ", "pokušajte ponovo", 10);
					
				}
			}// end if _selected_voz == null
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception when try to find selected voz, details: " + e.getMessage());
		}
		
		if(_selected_voz_povratak != null) {
			// za sucaj da je prethodno bila setovana povratna karta
			setPodaciOdVozaPovratak();
		}
		
		
		uslov_za_drugi_korak();
		handle_pagination_polasci_povratci(true);
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));
	}
	
	
	private void setPodaciOdVozaPovratak() {
		
		_selected_voz_povratak = null;
		//setuj uvek jedan dan posle da bi uhvatio makar jedan polazak su povratku jer nije bitno kada je polazak zbog tarifnog sistema
		//zato cemo uraditi od sutrasnjeg dana
		String datum_polaska = datum_polaska_value_lbl.getText();
		Calendar datum_povratka_date = Calendar.getInstance();
		try {
		datum_povratka_date.setTime(_sdf.parse(datum_polaska));
		}catch(Exception e) {
			//onda uzimamo danasnji dan - ovo nikad ne bi trebalo da se desi
			System.out.println("PARSE EXCEPTION when parse datum_polaska_value_lbl.getText() = " + 
					datum_polaska_value_lbl.getText() + ", will use todays date for return date");
		}
		if(!isVozWithReservations("" + _selected_voz.getBrvoz())) {
			datum_povratka_date.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		datum_povratka_polazak_value_lbl.setText(_sdf.format(datum_povratka_date.getTime()));
		/////////////////u slucaju da je polazni voz
		try {
			List<VozBean> lista_vozova = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getListaVozovaNaTrasi(getSifraStaniceFromNaziv(odrediste_value_lbl.getText()), 
					 _kartomat.getSifrA_STANICE(), datum_povratka_polazak_value_lbl.getText(), 
					Integer.parseInt(broj_putnika_lbl.getText()), _selected_razred_povratak);
			_svi_povratci = lista_vozova;
			_svi_povratci_filtered_rezervacije.clear();
			_svi_povratci_filtered_bez_rezervacije.clear();
			////////////////filterovano/////////////////
			for (VozBean current : lista_vozova) {
				if(isVozWithReservations("" + current.getBrvoz())) {
					//ovde imamo dodatak, ubacicemo samo one koji su posle vremena dolaska polaznog voza
					String vreme_dolaska_polaznog_voza = _selected_voz.getVremed();
					
					String vreme_polaska_povratnog_voza = current.getVremep();
					//treba ih uporediti, u formi su HH:mm
					vreme_dolaska_polaznog_voza = vreme_dolaska_polaznog_voza.replace(":", "");
					vreme_polaska_povratnog_voza = vreme_polaska_povratnog_voza.replace(":", "");
					int vreme_dolaska_polaznog_voza_int = Integer.parseInt(vreme_dolaska_polaznog_voza);
					int vreme_polaska_povratnog_voza_int = Integer.parseInt(vreme_polaska_povratnog_voza);
					if(vreme_polaska_povratnog_voza_int > vreme_dolaska_polaznog_voza_int) {
						_svi_povratci_filtered_rezervacije.add(current);
					}
				}else {
					_svi_povratci_filtered_bez_rezervacije.add(current);
				}
			}
			
			/////////////////end filterovano////////////
			
			///ovo se radi jer je dogovor da ako polazak ima rezervaciju, mora i povratak da je ima
			if(isVozWithReservations("" + _selected_voz.getBrvoz())) {
				lista_vozova = _svi_povratci_filtered_rezervacije;
			}else {
				lista_vozova = _svi_povratci_filtered_bez_rezervacije;
			}
//			setPovratakTable(lista_vozova);
			if(lista_vozova.size() > 0) {
				for( VozBean current : lista_vozova) {
//					String[] splt = current.getVremep().split(":");
//					int vreme_pol = Integer.parseInt(splt[0]) *100 + Integer.parseInt(splt[1]);
//					int current_time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) * 100 + Integer.parseInt(new SimpleDateFormat("mm").format(new Date()));
					if(true/*vreme_pol > current_time + 5/*5 minuta*/) {
						_selected_voz_povratak = current;
						//stigla izmena - ukoliko je bez rezervacija, onda povratni voz mora biti ranga regio
						if(!isVozWithReservations("" + _selected_voz.getBrvoz())) {
							if(current.getRang() == 4 || current.getRang() == 6 ) {
								//nasli smo ga, ako ne, nastavljamo sa pretragom u loop-u
								System.out.println("############ nasli smo POVRATNI sa rangom = " + current.getRang());
							}else {
								System.out.println("############ NISMO nasli POVRATNI, rang je = " + current.getRang() + " , nastavljamo dalje u petlji");
								continue;
							}
						}
						if(_selected_voz_povratak == null) {
							_selected_voz_povratak = lista_vozova.get(0);
						}
						if(_selected_voz_povratak.getRelkm() <= 100/*daljina u kilometrima*/) {
							vaznost_value_lbl.setText(resources.getString("na_dan_kupovine") );
						}else {
							vaznost_value_lbl.setText( resources.getString("narednih_petnaest_dana") );
						}
						break;
					} 
					//current.getVremep()
				}
			}else {
				run_info("Ne postoje povratni polasci za", 
						"izabrano odredište " + odrediste_value_lbl.getText() , "i izabrani datum " + datum_polaska_value_lbl.getText(), 
						"i izabrabi broj putnika " + broj_putnika_lbl.getText(), "i izabrani razred. Promenite ", "neki od parametara i ", "pokušajte ponovo", 10);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception when try to find selected voz, details: " + e.getMessage());
		}
		
		
		uslov_za_drugi_korak();
		handle_pagination_polasci_povratci(true);
		setCena(true/*smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn"))*/);
	}
	
	
	private List<VozBean> getListaPovrataka(){
		System.out.println("_svi_povratci_filtered_rezervacije size = " + _svi_povratci_filtered_rezervacije.size());
		System.out.println("_svi_povratci_filtered_bez_rezervacije size = " + _svi_povratci_filtered_bez_rezervacije.size());
		if(isVozWithReservations(broj_voza_polazak_value_lbl.getText())) {
			
			return _svi_povratci_filtered_rezervacije;
		}else {
			
			return _svi_povratci_filtered_bez_rezervacije;
		}
	}
	
	
	private void setCena(boolean is_povratna) {
		System.out.println("set cena, is_povratna = " + is_povratna);
		if(is_povratna) {
			setCenaPovratna();
		}else {
			setCenaJedanSmer();
		}
	}
	
	private void setCenaJedanSmer() {
		System.out.println("setCena , _prva_karta_tip = " + _prva_karta_tip);
		double ukupna_cena = 0;
		if( _selected_voz != null) {
			int broj_karata = Integer.parseInt(broj_putnika_lbl.getText());
			//cena_poalzak_lbl.setText(broj_karata > 1 ? "UKUPNA CENA KARATA" : "UKUPNA CENA KARTE");
			try {
				CenaBean cena = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaJedanSmer( 1
						, _selected_razred_polazak, _selected_voz.getRelkm(), 
						_selected_voz.getRang());
				 double jedinicna_cena = cena .getCenau();
				for(int i=0 ; i < broj_karata ; i++) {
					int current_tip = _prva_karta_tip;
					System.out.println("setCena, current_tip = " + current_tip);
					switch(i) {
					case 0 : _prvi_putnik_cena = getCenaJedanSmerFromAPI(_prva_karta_tip, cena) ; jedinicna_cena = _prvi_putnik_cena .getCenau(); break;
					case 1 : _drugi_putnik_cena =  getCenaJedanSmerFromAPI(_druga_karta_tip, cena);
							jedinicna_cena = _drugi_putnik_cena .getCenau(); current_tip = _druga_karta_tip;break;
					case 2 : _treci_putnik_cena = getCenaJedanSmerFromAPI(_treca_karta_tip, cena);
							jedinicna_cena = _treci_putnik_cena .getCenau(); current_tip = _treca_karta_tip;break;
					case 3 : _cetvrti_putnik_cena = getCenaJedanSmerFromAPI(_cetvrta_karta_tip, cena);
							jedinicna_cena = _cetvrti_putnik_cena .getCenau(); current_tip = _cetvrta_karta_tip;break;
					case 4 : _peti_putnik_cena = getCenaJedanSmerFromAPI(_peta_karta_tip, cena);
							jedinicna_cena = _peti_putnik_cena .getCenau(); current_tip = _peta_karta_tip;break;
					}
					


					//PovlasticaBean popust = getPopustBean(current_tip, false)/*_popusti_jedan_smer.get(new Integer(current_tip))*/;
					//System.out.println("setCena, popust = " + popust);
					//int procenat = 0;
					//if(popust != null) {
					//	procenat = _selected_razred_polazak == 1 ? popust.getProcenaT_RAZ_1() : popust.getProcenaT_RAZ_2();
					//}
					double cena_karte_sa_popustom = jedinicna_cena;// - (jedinicna_cena * procenat)/100;	

					switch(i) {
					case 0 : prva_cena_value_lbl.setText("" + cena_karte_sa_popustom);_prvi_putnik_cena.setCenau(cena_karte_sa_popustom);break;
					case 1 : druga_cena_value_lbl.setText("" + cena_karte_sa_popustom);_drugi_putnik_cena.setCenau(cena_karte_sa_popustom);break;
					case 2 : treca_cena_value_lbl.setText("" + cena_karte_sa_popustom);_treci_putnik_cena.setCenau(cena_karte_sa_popustom);break;
					case 3 : cetvrta_cena_value_lbl.setText("" + cena_karte_sa_popustom);_cetvrti_putnik_cena.setCenau(cena_karte_sa_popustom);break;
					case 4 : peta_cena_value_lbl.setText("" + cena_karte_sa_popustom);_peti_putnik_cena.setCenau(cena_karte_sa_popustom);break;
					}
					
					ukupna_cena = ukupna_cena + cena_karte_sa_popustom;

				}
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Exception when try to find cena povratne karte, details: " + e.getMessage());
			}
		}
		
//		System.out.println("####_prvi_putnik_cena = " + _prvi_putnik_cena);
//		System.out.println("####_drugi_putnik_cena = " + _drugi_putnik_cena);
//		System.out.println("####_treci_putnik_cena = " + _treci_putnik_cena);
//		System.out.println("####_cetvrti_putnik_cena = " + _cetvrti_putnik_cena);
//		System.out.println("####_peti_putnik_cena = " + _peti_putnik_cena);
		ukupno_cena_value_lbl.setText( "" + ukupna_cena);

	}
	
	private CenaBean getCenaJedanSmerFromAPI(int karta_tip, CenaBean osnovna_cena) throws CommunicationException {
		if(karta_tip == TIP_REDOVNA_CENA) {
			return (CenaBean)osnovna_cena.clone();
		}
		if(karta_tip == TIP_PAS) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaJedanSmerPSE( 1
					, _selected_razred_polazak, _selected_voz.getRelkm(), _selected_voz.getRang());
		}
		if(karta_tip == TIP_DETE) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaJedanSmerDETE( 1
					, _selected_razred_polazak, _selected_voz.getRelkm(), _selected_voz.getRang());
		}
		if(karta_tip == TIP_RAIL_K30) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaJedanSmerRAIL_PLUS_K_30( 1
					, _selected_razred_polazak, _selected_voz.getRelkm(), _selected_voz.getRang());
		}
		if(karta_tip == TIP_SRB_K13) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaJedanSmerSRB_PLUS_K_13( 1
					, _selected_razred_polazak, _selected_voz.getRelkm(), _selected_voz.getRang());
		}
		return null;

	}
	
	private PovlasticaBean getPopustBean(int selected_popust, boolean is_povratna) {
		System.out.println("getPopustBean, selected_popust = " + selected_popust + ", is_povratna = " + is_povratna);
		Map<Integer, PovlasticaBean> source = is_povratna ? _popusti_povratna : _popusti_jedan_smer;
		switch(selected_popust) {
		case TIP_REDOVNA_CENA : return (PovlasticaBean)source.get(SrbijaVozPopustID.REDOVNA_CENA._app_id);
		case TIP_SRB_K13 : return (PovlasticaBean)source.get(SrbijaVozPopustID.SRB_PLUS_K_13._app_id);
		case TIP_RAIL_K30 : return (PovlasticaBean)source.get(SrbijaVozPopustID.RAIL_PLUS_K_30._app_id);
		case TIP_DETE : return (PovlasticaBean)source.get(SrbijaVozPopustID.DETE._app_id);
		case TIP_PAS : return (PovlasticaBean)source.get(SrbijaVozPopustID.PAS._app_id);
		case TIP_POVRATNA : return (PovlasticaBean)source.get(SrbijaVozPopustID.POVRATNA._app_id);
		}
		return null;

	}
	
	private void setCenaPovratna() {
		System.out.println("setCenaPovratna");
		double ukupna_cena = 0;
		if(_selected_voz_povratak != null) {
			int broj_karata = Integer.parseInt(broj_putnika_lbl.getText());
			//cena_poalzak_lbl.setText(broj_karata > 1 ? "UKUPNA CENA POVRATNIH KARATA" : "UKUPNA CENA POVRATNE KARTE");
			try {
				//osnovna cena
				 CenaBean cena = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaPovratna(1, _selected_voz.getRelkm(), 
						_selected_razred_polazak, _selected_voz.getRang(), _selected_razred_povratak, _selected_voz_povratak.getRang());
				 double jedinicna_cena = cena.getCenau();
				for(int i=0 ; i < broj_karata ; i++) {
					int current_tip = _prva_karta_tip;
					System.out.println("setCena, current_tip = " + current_tip);
					switch(i) {
					case 0 :_prvi_putnik_povratna_cena = getCenaPovratnaFromAPI(_prva_karta_tip ,cena); jedinicna_cena = _prvi_putnik_povratna_cena .getCenau(); break;
					case 1 :_drugi_putnik_povratna_cena = getCenaPovratnaFromAPI(_druga_karta_tip ,cena);current_tip = _druga_karta_tip; jedinicna_cena = _drugi_putnik_povratna_cena .getCenau(); break;
					case 2 :_treci_putnik_povratna_cena = getCenaPovratnaFromAPI(_treca_karta_tip ,cena);current_tip = _treca_karta_tip; jedinicna_cena = _treci_putnik_povratna_cena .getCenau(); break;
					case 3 :_cetvrti_putnik_povratna_cena = getCenaPovratnaFromAPI(_cetvrta_karta_tip ,cena);current_tip = _cetvrta_karta_tip; jedinicna_cena = _cetvrti_putnik_povratna_cena .getCenau(); break;
					case 4 :_peti_putnik_povratna_cena = getCenaPovratnaFromAPI(_peta_karta_tip ,cena);current_tip = _peta_karta_tip; jedinicna_cena = _peti_putnik_povratna_cena .getCenau(); break;
					}
				
					

					//REKLI MI IZ SRBIJA VOZA DA NE TREBA DA SAM IZRACUNAVAM vec mi se vraca cena
					//PovlasticaBean popust = getPopustBean(current_tip, true)/*_popusti_jedan_smer.get(new Integer(current_tip))*/;
					//System.out.println("setCena, popust = " + popust);
					//int procenat = 0;
					//if(popust != null && (popust.getiD_POVLASTICE() != SrbijaVozPopustID.ID_POVRATNA)) {
					//	procenat = _selected_razred_polazak == 1 ? popust.getProcenaT_RAZ_1() : popust.getProcenaT_RAZ_2();
					//
					//}
					double cena_karte_sa_popustom = jedinicna_cena;// - (jedinicna_cena * procenat)/100;	

					switch(i) {
					case 0 : prva_cena_value_lbl.setText("" + cena_karte_sa_popustom);_prvi_putnik_povratna_cena.setCenau(cena_karte_sa_popustom);break;
					case 1 : druga_cena_value_lbl.setText("" + cena_karte_sa_popustom);_drugi_putnik_povratna_cena.setCenau(cena_karte_sa_popustom);break;
					case 2 : treca_cena_value_lbl.setText("" + cena_karte_sa_popustom);_treci_putnik_povratna_cena.setCenau(cena_karte_sa_popustom);break;
					case 3 : cetvrta_cena_value_lbl.setText("" + cena_karte_sa_popustom);_cetvrti_putnik_povratna_cena.setCenau(cena_karte_sa_popustom);break;
					case 4 : peta_cena_value_lbl.setText("" + cena_karte_sa_popustom);_peti_putnik_povratna_cena.setCenau(cena_karte_sa_popustom);break;
					}
					
					ukupna_cena = ukupna_cena + cena_karte_sa_popustom;
					System.out.println("ukupna_cena = " + ukupna_cena);
				}
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Exception when try to find cena povratne karte, details: " + e.getMessage());
			}
		}
		System.out.println("ukupno_cena_value_lbl set ukupna_cena = " + ukupna_cena);
		ukupno_cena_value_lbl.setText( "" + ukupna_cena);

	}
	
	
	private CenaBean getCenaPovratnaFromAPI(int karta_tip, CenaBean osnovna_cena) throws CommunicationException {
		if(karta_tip == TIP_POVRATNA) {
			return (CenaBean)osnovna_cena.clone();
		}
		if(karta_tip == TIP_PAS) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaPovratnaPSE(1, _selected_voz.getRelkm(), 
					_selected_razred_polazak, _selected_voz.getRang(), _selected_razred_povratak, _selected_voz_povratak.getRang());
		}
		if(karta_tip == TIP_DETE) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaPovratnaDETE(1, _selected_voz.getRelkm(), 
					_selected_razred_polazak, _selected_voz.getRang(), _selected_razred_povratak, _selected_voz_povratak.getRang());
		}
		if(karta_tip == TIP_RAIL_K30) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaPovratnaRAIL_PLUS_K_30(1, _selected_voz.getRelkm(), 
					_selected_razred_polazak, _selected_voz.getRang(), _selected_razred_povratak, _selected_voz_povratak.getRang());
		}
		if(karta_tip == TIP_SRB_K13) {
			return 	SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getCenaPovratnaSRB_PLUS_K_13(1, _selected_voz.getRelkm(), 
					_selected_razred_polazak, _selected_voz.getRang(), _selected_razred_povratak, _selected_voz_povratak.getRang());
		}
		return null;

	}
	
	private void setPopusti() {
		try {
			List<PovlasticaBean> povlastice_jedan_smer = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getPovlastice(1);
			for(PovlasticaBean current : povlastice_jedan_smer) {
				switch(current.getiD_POVLASTICE()) {
				case SrbijaVozPopustID.ID_REDOVNA_CENA : _popusti_jedan_smer.put(SrbijaVozPopustID.REDOVNA_CENA._app_id, current);break;
				case SrbijaVozPopustID.ID_SRB_PLUS_K_13 : _popusti_jedan_smer.put(SrbijaVozPopustID.SRB_PLUS_K_13._app_id, current);break;
				case SrbijaVozPopustID.ID_RAIL_PLUS_K_30 : _popusti_jedan_smer.put(SrbijaVozPopustID.RAIL_PLUS_K_30._app_id, current);break;
				case SrbijaVozPopustID.ID_DETE : _popusti_jedan_smer.put(SrbijaVozPopustID.DETE._app_id, current);break;
				case SrbijaVozPopustID.ID_PAS : _popusti_jedan_smer.put(SrbijaVozPopustID.PAS._app_id, current);break;
				}
				
			}
			List<PovlasticaBean> povlastice_povratna = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).getPovlastice(2);
			for(PovlasticaBean current : povlastice_povratna) {
				switch(current.getiD_POVLASTICE()) {
				case SrbijaVozPopustID.ID_POVRATNA : _popusti_povratna.put(SrbijaVozPopustID.POVRATNA._app_id, current);break;
				case SrbijaVozPopustID.ID_SRB_PLUS_K_13 : _popusti_povratna.put(SrbijaVozPopustID.SRB_PLUS_K_13._app_id, current);break;
				case SrbijaVozPopustID.ID_RAIL_PLUS_K_30 : _popusti_povratna.put(SrbijaVozPopustID.RAIL_PLUS_K_30._app_id, current);break;
				case SrbijaVozPopustID.ID_DETE : _popusti_povratna.put(SrbijaVozPopustID.DETE._app_id, current);break;
				case SrbijaVozPopustID.ID_PAS : _popusti_povratna.put(SrbijaVozPopustID.PAS._app_id, current);break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception when try to find cena povratne karte, details: " + e.getMessage());
		}
	}
	

	

	

	
	private int getSifraStaniceFromNaziv(String naziv_stanice) {

		for(FrekventneStaniceBean current : _frekventne_stanice) {
			if(current.getNaziV_UPUTNE_STANICE().equalsIgnoreCase(naziv_stanice)  ) {
				return current.getSifrA_UPUTNE_STANICE();
			}
		}
		for(StanicaIDBean current : _ostale_stanice) {
			if(current.getNaziv().equalsIgnoreCase(naziv_stanice) ) {
				return current.getSifra();
			}
		}
		
		return -1;
	}
	

	private void handle_ostale_common(Button target_btn) {
		_selected_voz = null;
		odrediste_value_lbl.setText(target_btn.getText());
		odrediste_izmena_value_lbl.setText(target_btn.getText());
		resetButtonGroup(_stanica_button_group, true);
		//target_btn.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
		setButtonSelected(target_btn, true);
		ostale_stanice_pn.setVisible(false);
		setPodaciOdVoza();
		//uslov_za_drugi_korak();
	}

	
	public void handle_ostale_odustani() {
		_lista_svih_stanica_current_position = 0;
		handle_ostale_napred_pagination();
		ostale_stanice_pn.setVisible(false);
		uslov_za_drugi_korak();
	}



	
	
	
	
	
	
	public void handle_1_btn() {
		broj_putnika_lbl.setText("1");
		resetButtonGroup(_broj_putnika_button_group, false);
		//jedan_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(jedan_btn, false);
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));
		//setTipKarteIzmeniVisible(true);
	}
	
	public void handle_2_btn() {

		broj_putnika_lbl.setText("2");
		resetButtonGroup(_broj_putnika_button_group, false);
		//dva_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(dva_btn, false);
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));

		//setTipKarteIzmeniVisible(true);

		
	}
	
	public void handle_3_btn() {

		broj_putnika_lbl.setText("3");
		resetButtonGroup(_broj_putnika_button_group, false);
		//tri_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(tri_btn, false);
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));

		//setTipKarteIzmeniVisible(true);

	}
	
	public void handle_4_btn() {
		
		broj_putnika_lbl.setText("4");
		resetButtonGroup(_broj_putnika_button_group, false);
		//cetiri_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(cetiri_btn, false);
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));

		//setTipKarteIzmeniVisible(true);

	}
	
	public void handle_5_btn() {
		
		broj_putnika_lbl.setText("5");
		resetButtonGroup(_broj_putnika_button_group, false);
		//pet_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(pet_btn, false);
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));
		
		//setTipKarteIzmeniVisible(true);
		
	}
	
	public void handle_smer(boolean is_povratna) {
		
		resetButtonGroup(_smer_button_group, is_povratna);
		if(is_povratna) {//#362c2d tamno braon // manje tamna braon #555555
			//#3a6dcf plava
			smer_povratni_btn.setStyle("-fx-background-color: #555555;-fx-text-fill: white;fx-font-weight: bold;");
			smer_u_jednom_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			smer_value_lbl.setText(resources.getString("smer_povratni_btn"));
			vaznost_value_lbl.setVisible(true);
			period_vaznosti_lbl.setVisible(true);
			tip_karte_dolazak_value_lbl.setText(resources.getString("povratna"));
		}else {
			smer_povratni_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			smer_u_jednom_btn.setStyle("-fx-background-color: #555555;-fx-text-fill: white;fx-font-weight: bold;");
		    _selected_voz_povratak = null;
			smer_value_lbl.setText(resources.getString("smer_u_jednom"));
			razred_odlazak_value_lbl.setText("Drugi");
			broj_voza_dolazak_value_lbl.setText("");
			vreme_dolazak_value_lbl.setText("");
			vreme_povratka_izmena_value_lbl.setText("");
			vreme_dolazak_dolazak_value_lbl.setText("");
			tip_karte_dolazak_value_lbl.setText(resources.getString("redovna_cena"));
			vaznost_value_lbl.setVisible(false);
			period_vaznosti_lbl.setVisible(false);
		}
		
		
		datum_povratka_izmena_pn.setVisible(is_povratna &&  isVozWithReservations( broj_voza_polazak_value_lbl.getText() ) );
		
		
		razred_karta_izmena_povratak_pn.setVisible(false);
		razred_povratak_lbl.setVisible(false);

		


	    uslov_za_drugi_korak();
	}
	
	public void handle_smer_povratni() {
		
		
		System.out.println("DA LI JE VOZ SA REZERVACIJAMA, broj voza = " + broj_voza_polazak_value_lbl.getText() 
				+ " isVozWithReservations = " + isVozWithReservations( broj_voza_polazak_value_lbl.getText() ));
		
		if( isVozWithReservations( broj_voza_polazak_value_lbl.getText() ) ) {
			datum_povratka_izmena_pn.setVisible(true);
		}
		
		
		_prva_karta_tip = TIP_POVRATNA;
		_druga_karta_tip = TIP_POVRATNA;
		_treca_karta_tip = TIP_POVRATNA;
		_cetvrta_karta_tip = TIP_POVRATNA;
		_peta_karta_tip = TIP_POVRATNA;
		
		_prva_karta_tip_not_confirmed = TIP_POVRATNA;
		_druga_karta_tip_not_confirmed = TIP_POVRATNA;
		_treca_karta_tip_not_confirmed = TIP_POVRATNA;
		_cetvrta_karta_tip_not_confirmed = TIP_POVRATNA;
		_peta_karta_tip_not_confirmed = TIP_POVRATNA;
		
		tip_karte_izmena_value_lbl.setText(resources.getString("povratna"));
		prvi_tip_redovna_btn.setText(resources.getString("povratna"));
		drugi_tip_redovna_btn.setText(resources.getString("povratna"));
		treci_tip_redovna_btn.setText(resources.getString("povratna"));
		cetvrti_tip_redovna_btn.setText(resources.getString("povratna"));
		peti_tip_redovna_btn.setText(resources.getString("povratna"));
		
		resetButtonGroup(_smer_button_group, false);
		handle_razred_drugi_dolazak();
		setPodaciOdVozaPovratak();
		//smer_povratni_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");

		setButtonSelected(smer_povratni_btn, false);
		handle_smer(true);
	    uslov_za_drugi_korak();
	}
	
	public void handle_datum_polazakc() {
		System.out.println("handle_datum_polazakc");
		open_datum_set_dialog(true);
	}
	
	public void handle_vreme_polazakc() {
		System.out.println("handle_vreme_polazakc");
		izaberi_polazak_lbl.setText(resources.getString("izaberi_polazak"));
		izaberi_polazak_datum_lbl.setText(datum_izmena_value_lbl.getText());
		polasci_calendar_pn.setVisible(true);
		polasci_calendar_pn.toFront();
//		polasci_nova_pn.setVisible(true);
//		polasci_nova_pn.toFront();
	}
	
	private void open_datum_set_dialog(boolean polazak_true_povratak_false) {
		_polazak_true_povratak_false = polazak_true_povratak_false;
		Calendar cal_pol = Calendar.getInstance();
		try {
			if(polazak_true_povratak_false) {
				izaberi_datum_cal_lbl.setText(resources.getString("izaberi_datum_polaska"));
				cal_pol.setTime(_sdf.parse(datum_polaska_value_lbl.getText()));
			}else {
				izaberi_datum_cal_lbl.setText(resources.getString("izaberi_datum_povratka"));
				cal_pol.setTime(_sdf.parse(datum_povratka_polazak_value_lbl.getText()));
			}
		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("parse exception in handle_datum_polazakc, details: " + e.getMessage());
		}
		String datum_polaska_povratka = _sdfddMMyyyy.format(cal_pol.getTime());
		Calendar monday = Calendar.getInstance();
		//monday.add(Calendar.DAY_OF_WEEK, (Calendar.DAY_OF_WEEK -2 ) * -1);cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
//		monday.clear(Calendar.MINUTE);
//		monday.clear(Calendar.SECOND);
//		monday.clear(Calendar.MILLISECOND);
//		System.out.println("day of week = " + monday.get(Calendar.DAY_OF_WEEK));
		// get start of this week in milliseconds
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String day_in_week = _sdfddMMyyyy.format(monday.getTime());
		pon_dat_lbl.setText(day_in_week);
		//pon_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			pon_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			pon_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 0 );
		}

		monday.add(Calendar.DAY_OF_YEAR, 1);
		day_in_week = _sdfddMMyyyy.format(monday.getTime());
		uto_dat_lbl.setText(day_in_week);
		
		//uto_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			uto_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			uto_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 1 );
		}
		
		monday.add(Calendar.DAY_OF_YEAR, 1);
		day_in_week = _sdfddMMyyyy.format(monday.getTime());
		sre_dat_lbl.setText(day_in_week);
		//sre_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			sre_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			sre_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 2 );
		}
		
		monday.add(Calendar.DAY_OF_YEAR, 1);
		day_in_week = _sdfddMMyyyy.format(monday.getTime());
		cet_dat_lbl.setText(day_in_week);
		//cet_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			cet_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			cet_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 3 );
		}
		
		monday.add(Calendar.DAY_OF_YEAR, 1);
		day_in_week = _sdfddMMyyyy.format(monday.getTime());
		pet_dat_lbl.setText(day_in_week);
		//pet_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			pet_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			pet_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 4 );
		}
		
		monday.add(Calendar.DAY_OF_YEAR, 1);
		day_in_week = _sdfddMMyyyy.format(monday.getTime());
		sub_dat_lbl.setText(day_in_week);
		//sub_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			sub_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			sub_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 5 );
		}

		monday.add(Calendar.DAY_OF_YEAR, 1);
		day_in_week = _sdfddMMyyyy.format(monday.getTime());
		ned_dat_lbl.setText(day_in_week);
		//ned_izab_btn.setVisible(!cal_pol.after(monday) || monday.equals(cal_pol));
		if(polazak_true_povratak_false) {
			ned_pn.setVisible(!cal_pol.after(monday) || datum_polaska_povratka.equals(day_in_week));
		}else {
			ned_pn.setVisible(datum_polaska_povratka.equals(day_in_week));
		}
		if(datum_polaska_povratka.equals(day_in_week)) {
			resetCalendarButtonGroup( 6 );
		}
		
		//calendar_pn.setVisible(true);

		//ne mozes da pomeras kalendar akoje povratak u pitanju, mora istog dana kad i polazak
		sled_ned_cal_btn.setVisible(polazak_true_povratak_false);
		//inicijano je uvek invisible
		pret_ned_cal_btn.setVisible(false/*polazak_true_povratak_false*/);
		
		handle_pagination_polasci_povratci(false);

		polasci_calendar_pn.setVisible(true);
	}

	public void handle_razred_prvi_polazak() {
		
		resetButtonGroup(_razred_polazak_button_group, false);		
		//razred_prvi_polazak_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(razred_prvi_polazak_btn, false);
		_selected_razred_polazak = 1;
		razred_polazak_value_lbl.setText(resources.getString("prvi_razred"));
		razred_odlazak_value_lbl.setText(resources.getString("prvi_razred"));
		//setPodaciOdVoza();
		//uslov_za_drugi_korak();
	}
	public void handle_datum_dolazak() {
		System.out.println("handle_datum_dolazak");
		open_datum_set_dialog(false);
	}
	
	public void handle_vreme_dolazak() {
		System.out.println("handle_vreme_dolazak");
		izaberi_polazak_lbl.setText(resources.getString("izaberi_povratak"));
		polasci_calendar_pn.setVisible(true);
		polasci_calendar_pn.toFront();
//		polasci_nova_pn.setVisible(true);
//		polasci_nova_pn.toFront();
	}
	
	
	public void handle_razred_prvi_dolazak() {
		
		resetButtonGroup(_razred_dolazak_button_group, false);		
		_selected_razred_povratak = 1;
		
		//razred_prvi_dolazak_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(razred_prvi_dolazak_btn, false);
		razred_polazak_value_lbl.setText(resources.getString("prvi_razred"));
		razred_odlazak_value_lbl.setText(resources.getString("prvi_razred"));
		//uslov_za_drugi_korak();
	}
	public void handle_razred_drugi_polazak() {
		
		resetButtonGroup(_razred_polazak_button_group, false);		
		//razred_drugi_polazak_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(razred_drugi_polazak_btn, false);
		_selected_razred_polazak = 2;
		razred_polazak_value_lbl.setText(resources.getString("drugi_razred"));
		razred_odlazak_value_lbl.setText(resources.getString("drugi_razred"));
		//setPodaciOdVoza();
		//uslov_za_drugi_korak();
	}
	public void handle_razred_drugi_dolazak() {
		
		resetButtonGroup(_razred_dolazak_button_group, false);		
		//razred_drugi_dolazak_btn.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
		setButtonSelectedGray(razred_drugi_dolazak_btn, false);
		_selected_razred_povratak = 2;

		razred_odlazak_value_lbl.setText(resources.getString("drugi_razred"));
		//uslov_za_drugi_korak();
	}
	
	
//	public void handleIzmeniOpcije() {
//		
//		if(izmeni_opcije_btn.getText().equals("IZMENI")) {
//			izmeni_opcije_pn.setVisible(true);
//			odrediste_pn.setVisible(false);
//			izmeni_opcije_btn.setText("IZMENI ODREDIŠTE");
//		}else {
//			izmeni_opcije_pn.setVisible(false);
//			odrediste_pn.setVisible(true);
//			izmeni_opcije_btn.setText("IZMENI");
//		}
//		
//	}
	
	public void handleKupiKartuOpcije() {
		System.out.println("handleKupiKartuOpcije");
		//handleDaljePolasci();
//		odabrane_opcije_pn.setVisible(false);
//		izmeni_opcije_pn.setVisible(false);
		odrediste_pn.setVisible(false);
		show_poslednja_provera_slide();
	}
	
	public void handle_ostale_napred_pagination(){
		handle_pagination(true);
	}
	

	public void handle_ostale_nazad_pagination(){
		handle_pagination(false);
	}
	
	private void refresh_ostale_stanice() {
		if(_ostale_stanice_filtered == null || _ostale_stanice_filtered.isEmpty())return;
		//ost stan 1
		if(_ostale_stanice_filtered.size() > 0) {
			ostale_stanice_1.setVisible(true);
			ostale_stanice_1.setText(_ostale_stanice_filtered.get(0).getNaziv());
			if(ostale_stanice_1.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_1, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_1, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_1.setVisible(false);
		}
		//ost stan 2
		if(_ostale_stanice_filtered.size() > 1) {
			ostale_stanice_2.setVisible(true);
			ostale_stanice_2.setText(_ostale_stanice_filtered.get(1).getNaziv());
			if(ostale_stanice_2.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_2, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_2, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_2.setVisible(false);
		}
		//ost stan 3
		if(_ostale_stanice_filtered.size() > 2) {
			ostale_stanice_3.setVisible(true);
			ostale_stanice_3.setText(_ostale_stanice_filtered.get(2).getNaziv());
			if(ostale_stanice_3.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_3, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_3, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_3.setVisible(false);
		}
		//ost stan 4
		if(_ostale_stanice_filtered.size() > 3) {
			ostale_stanice_4.setVisible(true);
			ostale_stanice_4.setText(_ostale_stanice_filtered.get(3).getNaziv());
			if(ostale_stanice_4.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_4, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_4, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_4.setVisible(false);
		}
		//ost stan 5
		if(_ostale_stanice_filtered.size() > 4) {
			ostale_stanice_5.setVisible(true);
			ostale_stanice_5.setText(_ostale_stanice_filtered.get(4).getNaziv());
			if(ostale_stanice_5.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_5, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_5, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_5.setVisible(false);
		}
		//ost stan 6
		if(_ostale_stanice_filtered.size() > 5) {
			ostale_stanice_6.setVisible(true);
			ostale_stanice_6.setText(_ostale_stanice_filtered.get(5).getNaziv());
			if(ostale_stanice_6.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_6, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_6, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_6.setVisible(false);
		}
		//ost stan 7
		if(_ostale_stanice_filtered.size() > 6) {
			ostale_stanice_7.setVisible(true);
			ostale_stanice_7.setText(_ostale_stanice_filtered.get(6).getNaziv());
			if(ostale_stanice_7.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_7, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_7, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_7.setVisible(false);
		}
		//ost stan 8
		if(_ostale_stanice_filtered.size() > 7) {
			ostale_stanice_8.setVisible(true);
			ostale_stanice_8.setText(_ostale_stanice_filtered.get(7).getNaziv());
			if(ostale_stanice_8.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_8, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_8, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_8.setVisible(false);
		}
		//ost stan 9
		if(_ostale_stanice_filtered.size() > 8) {
			ostale_stanice_9.setVisible(true);
			ostale_stanice_9.setText(_ostale_stanice_filtered.get(8).getNaziv());
			if(ostale_stanice_9.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_9, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_9, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_9.setVisible(false);
		}
		//ost stan 10
		if(_ostale_stanice_filtered.size() > 9) {
			ostale_stanice_10.setVisible(true);
			ostale_stanice_10.setText(_ostale_stanice_filtered.get(9).getNaziv());
			if(ostale_stanice_10.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_10, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_10, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_10.setVisible(false);
		}
		//ost stan 11
		if(_ostale_stanice_filtered.size() > 10) {
			ostale_stanice_11.setVisible(true);
			ostale_stanice_11.setText(_ostale_stanice_filtered.get(10).getNaziv());
			if(ostale_stanice_11.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_11, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_11, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_11.setVisible(false);
		}
		//ost stan 12
		if(_ostale_stanice_filtered.size() > 11) {
			ostale_stanice_12.setVisible(true);
			ostale_stanice_12.setText(_ostale_stanice_filtered.get(11).getNaziv());
			if(ostale_stanice_12.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_12, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_12, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_12.setVisible(false);
		}
		//ost stan 13
		if(_ostale_stanice_filtered.size() > 12) {
			ostale_stanice_13.setVisible(true);
			ostale_stanice_13.setText(_ostale_stanice_filtered.get(12).getNaziv());
			if(ostale_stanice_13.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_13, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_13, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_13.setVisible(false);
		}
		//ost stan 14
		if(_ostale_stanice_filtered.size() > 13) {
			ostale_stanice_14.setVisible(true);
			ostale_stanice_14.setText(_ostale_stanice_filtered.get(13).getNaziv());
			if(ostale_stanice_14.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_14, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_14, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_14.setVisible(false);
		}
		//ost stan 15
		if(_ostale_stanice_filtered.size() > 14) {
			ostale_stanice_15.setVisible(true);
			ostale_stanice_15.setText(_ostale_stanice_filtered.get(14).getNaziv());
			if(ostale_stanice_15.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_15, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_15, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_15.setVisible(false);
		}
		//ost stan 16
		if(_ostale_stanice_filtered.size() > 15) {
			ostale_stanice_16.setVisible(true);
			ostale_stanice_16.setText(_ostale_stanice_filtered.get(15).getNaziv());
			if(ostale_stanice_16.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_16, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_16, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_16.setVisible(false);
		}

	}
	
	private void handle_pagination(boolean is_napred) {
		List<StanicaIDBean> ostale_stanice = getNextPagination(is_napred);
		if(ostale_stanice == null) return;
		if(ostale_stanice.size() > 0) {
			ostale_stanice_1.setVisible(true);
			ostale_stanice_1.setText(ostale_stanice.get(0).getNaziv());
			if(ostale_stanice_1.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_1, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_1, true);
				//ostale_stanice_1.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_1.setVisible(false);
		}
		
		if(ostale_stanice.size() > 1) {
			ostale_stanice_2.setVisible(true);
			ostale_stanice_2.setText(ostale_stanice.get(1).getNaziv());
			if(ostale_stanice_2.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_2, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_2, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_2.setVisible(false);
		}
		
		if(ostale_stanice.size() > 2) {
			ostale_stanice_3.setVisible(true);
			ostale_stanice_3.setText(ostale_stanice.get(2).getNaziv());
			if(ostale_stanice_3.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_3, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_3, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_3.setVisible(false);
		}
		
		if(ostale_stanice.size() > 3) {
			ostale_stanice_4.setVisible(true);
			ostale_stanice_4.setText(ostale_stanice.get(3).getNaziv());
			if(ostale_stanice_4.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_4, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_4, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_4.setVisible(false);
		}
		
		if(ostale_stanice.size() > 4) {
			if(ostale_stanice_5.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_5, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_5, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
			ostale_stanice_5.setVisible(true);
			ostale_stanice_5.setText(ostale_stanice.get(4).getNaziv());
		}else {
			ostale_stanice_5.setVisible(false);
		}
		
		if(ostale_stanice.size() > 5) {
			ostale_stanice_6.setVisible(true);
			ostale_stanice_6.setText(ostale_stanice.get(5).getNaziv());
			if(ostale_stanice_6.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_6, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_6, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_6.setVisible(false);
		}
		
		if(ostale_stanice.size() > 6) {
			ostale_stanice_7.setVisible(true);
			ostale_stanice_7.setText(ostale_stanice.get(6).getNaziv());
			if(ostale_stanice_7.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_7, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_7, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_7.setVisible(false);
		}
		
		if(ostale_stanice.size() > 7) {
			ostale_stanice_8.setVisible(true);
			ostale_stanice_8.setText(ostale_stanice.get(7).getNaziv());
			if(ostale_stanice_8.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_8, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_8, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_8.setVisible(false);
		}
		
		if(ostale_stanice.size() > 8) {
			ostale_stanice_9.setVisible(true);
			ostale_stanice_9.setText(ostale_stanice.get(8).getNaziv());
			if(ostale_stanice_9.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_9, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_9, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_9.setVisible(false);
		}
		
		if(ostale_stanice.size() > 9) {
			ostale_stanice_10.setVisible(true);
			ostale_stanice_10.setText(ostale_stanice.get(9).getNaziv());
			if(ostale_stanice_10.getText().equals(odrediste_value_lbl.getText())) {
				setButtonSelected(ostale_stanice_10, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_10, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_10.setVisible(false);
		}
		
		if(ostale_stanice.size() > 10) {
			ostale_stanice_11.setVisible(true);
			ostale_stanice_11.setText(ostale_stanice.get(10).getNaziv());
			if(ostale_stanice_11.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_11, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_11, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_11.setVisible(false);
		}
		
		if(ostale_stanice.size() > 11) {
			ostale_stanice_12.setVisible(true);
			ostale_stanice_12.setText(ostale_stanice.get(11).getNaziv());
			if(ostale_stanice_12.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_12, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_12, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_12.setVisible(false);
		}
		
		if(ostale_stanice.size() > 12) {
			ostale_stanice_13.setVisible(true);
			ostale_stanice_13.setText(ostale_stanice.get(12).getNaziv());
			if(ostale_stanice_13.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_13, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_13, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_13.setVisible(false);
		}
		
		if(ostale_stanice.size() > 13) {
			ostale_stanice_14.setVisible(true);
			ostale_stanice_14.setText(ostale_stanice.get(13).getNaziv());
			if(ostale_stanice_14.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_14, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_14, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_14.setVisible(false);
		}
		
		if(ostale_stanice.size() > 14) {
			ostale_stanice_15.setVisible(true);
			ostale_stanice_15.setText(ostale_stanice.get(14).getNaziv());
			if(ostale_stanice_15.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_15, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_15, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_15.setVisible(false);
		}
		
		if(ostale_stanice.size() > 15) {
			ostale_stanice_16.setVisible(true);
			ostale_stanice_16.setText(ostale_stanice.get(15).getNaziv());
			if(ostale_stanice_16.getText().equals(odrediste_value_lbl.getText()) ) {
				setButtonSelected(ostale_stanice_16, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(ostale_stanice_16, true);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			ostale_stanice_16.setVisible(false);
		}
		
		
		
	}
	
	private void handle_pagination_polasci_povratci(boolean is_napred) {
		List<VozBean> svi_vozovi = getNextPaginationpPolasci(is_napred);
		if(svi_vozovi == null) return;
		String vreme_polaska = _polazak_true_povratak_false ? vreme_polazak_value_lbl.getText() : vreme_dolazak_dolazak_value_lbl.getText();
		if(svi_vozovi.size() > 0) {
			
			prvi_pol_pn.setVisible(true);
			prvi_pol_br_voz_value_lbl.setText("" + svi_vozovi.get(0).getBrvoz());
			prvi_pol_vreme_polaska_value_lbl.setText("" + svi_vozovi.get(0).getVremep());
			prvi_pol_vreme_dolaska_value_lbl.setText("" + svi_vozovi.get(0).getVremed());
			prvi_pol_trajanje_value_lbl.setText("" + svi_vozovi.get(0).getTrajanje_putovanja());
			prvi_pol_cena_value_lbl.setText("" + svi_vozovi.get(0).getCenau());
			prvi_pol_rang_value_lbl.setText("" + svi_vozovi.get(0).getRangNaziv());
			prvi_pol_rang_opis_value_lbl.setText("" + svi_vozovi.get(0).getRangOpis());
			if(prvi_pol_vreme_polaska_value_lbl.getText().equals(vreme_polaska)) {
				setButtonSelected(prvi_pol_btn, false);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(prvi_pol_btn, false);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}
		}else {
			prvi_pol_pn.setVisible(false);
		}
		
		if(svi_vozovi.size() > 1) {
			
			drugi_pol_pn.setVisible(true);
			drugi_pol_br_voz_value_lbl.setText("" + svi_vozovi.get(1).getBrvoz());
			drugi_pol_vreme_polaska_value_lbl.setText("" + svi_vozovi.get(1).getVremep());
			drugi_pol_vreme_dolaska_value_lbl.setText("" + svi_vozovi.get(1).getVremed());
			drugi_pol_trajanje_value_lbl.setText("" + svi_vozovi.get(1).getTrajanje_putovanja());
			drugi_pol_cena_value_lbl.setText("" + svi_vozovi.get(1).getCenau());
			drugi_pol_rang_value_lbl.setText("" + svi_vozovi.get(1).getRangNaziv());
			drugi_pol_rang_opis_value_lbl.setText("" + svi_vozovi.get(1).getRangOpis());
			if(drugi_pol_vreme_polaska_value_lbl.getText().equals(vreme_polaska)) {
				setButtonSelected(drugi_pol_btn, false);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(drugi_pol_btn, false);
			}
		}else {
			drugi_pol_pn.setVisible(false);
		}
		
		if(svi_vozovi.size() > 2) {
			
			treci_pol_pn.setVisible(true);
			treci_pol_br_voz_value_lbl.setText("" + svi_vozovi.get(2).getBrvoz());
			treci_pol_vreme_polaska_value_lbl.setText("" + svi_vozovi.get(2).getVremep());
			treci_pol_vreme_dolaska_value_lbl.setText("" + svi_vozovi.get(2).getVremed());
			treci_pol_trajanje_value_lbl.setText("" + svi_vozovi.get(2).getTrajanje_putovanja());
			treci_pol_cena_value_lbl.setText("" + svi_vozovi.get(2).getCenau());
			treci_pol_rang_value_lbl.setText("" + svi_vozovi.get(2).getRangNaziv());
			treci_pol_rang_opis_value_lbl.setText("" + svi_vozovi.get(2).getRangOpis());
			if(treci_pol_vreme_polaska_value_lbl.getText().equals(vreme_polaska)) {
				setButtonSelected(treci_pol_btn, false);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(treci_pol_btn, false);
			}
		}else {
			treci_pol_pn.setVisible(false);
		}
		
		if(svi_vozovi.size() > 3) {
			
			cetvrti_pol_pn.setVisible(true);
			cetvrti_pol_br_voz_value_lbl.setText("" + svi_vozovi.get(3).getBrvoz());
			cetvrti_pol_vreme_polaska_value_lbl.setText("" + svi_vozovi.get(3).getVremep());
			cetvrti_pol_vreme_dolaska_value_lbl.setText("" + svi_vozovi.get(3).getVremed());
			cetvrti_pol_trajanje_value_lbl.setText("" + svi_vozovi.get(3).getTrajanje_putovanja());
			cetvrti_pol_cena_value_lbl.setText("" + svi_vozovi.get(3).getCenau());
			cetvrti_pol_rang_value_lbl.setText("" + svi_vozovi.get(3).getRangNaziv());
			cetvrti_pol_rang_opis_value_lbl.setText("" + svi_vozovi.get(3).getRangOpis());
			if(cetvrti_pol_vreme_polaska_value_lbl.getText().equals(vreme_polaska)) {
				setButtonSelected(cetvrti_pol_btn, false);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(cetvrti_pol_btn, false);
			}
		}else {
			cetvrti_pol_pn.setVisible(false);
		}
		
		if(svi_vozovi.size() > 4) {
			
			peti_pol_pn.setVisible(true);
			peti_pol_br_voz_value_lbl.setText("" + svi_vozovi.get(4).getBrvoz());
			peti_pol_vreme_polaska_value_lbl.setText("" + svi_vozovi.get(4).getVremep());
			peti_pol_vreme_dolaska_value_lbl.setText("" + svi_vozovi.get(4).getVremed());
			peti_pol_trajanje_value_lbl.setText("" + svi_vozovi.get(4).getTrajanje_putovanja());
			peti_pol_cena_value_lbl.setText("" + svi_vozovi.get(4).getCenau());
			peti_pol_rang_value_lbl.setText("" + svi_vozovi.get(4).getRangNaziv());
			peti_pol_rang_opis_value_lbl.setText("" + svi_vozovi.get(4).getRangOpis());
			if(peti_pol_vreme_polaska_value_lbl.getText().equals(vreme_polaska)) {
				setButtonSelected(peti_pol_btn, false);
				//ostale_stanice_2.setStyle("-fx-background-color: white;-fx-text-fill: black;fx-font-weight: bold;");
			}else {
				setButtonUnselected(peti_pol_btn, false);
			}
		}else {
			peti_pol_pn.setVisible(false);
		}	
		
	}
	
	
	private void resetPolazakButtonGroup(int selected_id) {
		setButtonUnselected(prvi_pol_btn, false);
		setButtonUnselected(drugi_pol_btn, false);
		setButtonUnselected(treci_pol_btn, false);
		setButtonUnselected(cetvrti_pol_btn, false);
		setButtonUnselected(peti_pol_btn, false);
//		prvi_pol_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
//		drugi_pol_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
//		treci_pol_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
//		cetvrti_pol_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
//		peti_pol_btn.setStyle("-fx-background-color: darkgray;-fx-text-fill: white;");
		Button selected_one = prvi_pol_btn;
		switch(selected_id) {
		case 0: break;
		case 1: selected_one = drugi_pol_btn;break;
		case 2: selected_one = treci_pol_btn;break;
		case 3: selected_one = cetvrti_pol_btn;break;
		case 4: selected_one = peti_pol_btn;break;
		}
		setButtonSelected(selected_one, false);
		//selected_one.setStyle("-fx-background-color: #108f64;-fx-text-fill: white;");
	}
	
	
	private void resetTipKarteButtonGroup(List<Button> tip_karte_button_group, int selected_id) {
		tip_karte_button_group.get(0).setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
		tip_karte_button_group.get(1).setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
		tip_karte_button_group.get(2).setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
		tip_karte_button_group.get(3).setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
		tip_karte_button_group.get(4).setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
		Button selected_one = tip_karte_button_group.get(0);
		switch(selected_id) {
		case 0: break;
		case 1: selected_one = tip_karte_button_group.get(1);break;
		case 2: selected_one = tip_karte_button_group.get(2);break;
		case 3: selected_one = tip_karte_button_group.get(3);break;
		case 4: selected_one = tip_karte_button_group.get(4);break;
		}
		setButtonSelected(selected_one, false);
		//selected_one.setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
	}
	
	
	private void resetCalendarButtonGroup( int selected_id) {
		
		
	pon_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");pon_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");pon_lbl.setStyle("-fx-text-fill:  #362c2d;");
	uto_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");uto_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");uto_lbl.setStyle("-fx-text-fill:  #362c2d;");
	sre_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");sre_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");sre_lbl.setStyle("-fx-text-fill:  #362c2d;");
	cet_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");cet_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");cet_lbl.setStyle("-fx-text-fill:  #362c2d;");
	pet_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");pet_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");pet_lbl.setStyle("-fx-text-fill:  #362c2d;");
	sub_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");sub_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");sub_lbl.setStyle("-fx-text-fill:  #362c2d;");
	ned_izab_btn.setStyle("-fx-background-color: #2880ec;-fx-text-fill: white;");ned_dat_lbl.setStyle("-fx-text-fill:  #362c2d;");ned_lbl.setStyle("-fx-text-fill:  #362c2d;");

		_calendar_button_group.get(0).setStyle("-fx-background-color: white;");
		_calendar_button_group.get(1).setStyle("-fx-background-color: white;");
		_calendar_button_group.get(2).setStyle("-fx-background-color: white;");
		_calendar_button_group.get(3).setStyle("-fx-background-color: white;");
		_calendar_button_group.get(4).setStyle("-fx-background-color: white;");
		_calendar_button_group.get(5).setStyle("-fx-background-color: white;");
		_calendar_button_group.get(6).setStyle("-fx-background-color: white;");
//		Button selected_one = _calendar_button_group.get(0);
//		switch(selected_id) {
//		case 0: break;
//		case 1: selected_one = _calendar_button_group.get(1);break;
//		case 2: selected_one = _calendar_button_group.get(2);break;
//		case 3: selected_one = _calendar_button_group.get(3);break;
//		case 4: selected_one = _calendar_button_group.get(4);break;
//		case 5: selected_one = _calendar_button_group.get(5);break;
//		case 6: selected_one = _calendar_button_group.get(6);break;
//		}
		setCalendarButtonSelected(selected_id);
		//selected_one.setStyle("-fx-background-color: white;-fx-text-fill: #362c2d;");
	}

	
	
	public void handle_change_pon() {
		resetCalendarButtonGroup( 0);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(pon_dat_lbl.getText());
			datum_izmena_value_lbl.setText(pon_dat_lbl.getText());
			
		}else {
			datum_povratka_polazak_value_lbl.setText(pon_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(pon_dat_lbl.getText());
			
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_change_uto() {
		resetCalendarButtonGroup( 1);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(uto_dat_lbl.getText());
			datum_izmena_value_lbl.setText(uto_dat_lbl.getText());
		}else {
			datum_povratka_polazak_value_lbl.setText(uto_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(uto_dat_lbl.getText());
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_change_sre() {
		resetCalendarButtonGroup( 2);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(sre_dat_lbl.getText());
			datum_izmena_value_lbl.setText(sre_dat_lbl.getText());
		}else {
			datum_povratka_polazak_value_lbl.setText(sre_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(sre_dat_lbl.getText());
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_change_cet() {
		resetCalendarButtonGroup( 3);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(cet_dat_lbl.getText());
			datum_izmena_value_lbl.setText(cet_dat_lbl.getText());
		}else {
			datum_povratka_polazak_value_lbl.setText(cet_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(cet_dat_lbl.getText());
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_change_pet() {
		resetCalendarButtonGroup( 4);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(pet_dat_lbl.getText());
			datum_izmena_value_lbl.setText(pet_dat_lbl.getText());
		}else {
			datum_povratka_polazak_value_lbl.setText(pet_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(pet_dat_lbl.getText());
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_change_sub() {
		resetCalendarButtonGroup( 5);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(sub_dat_lbl.getText());
			datum_izmena_value_lbl.setText(sub_dat_lbl.getText());
		}else {
			datum_povratka_polazak_value_lbl.setText(sub_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(sub_dat_lbl.getText());
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_change_ned() {
		resetCalendarButtonGroup( 6);
		if(izaberi_datum_cal_lbl.getText().contains("POLASKA") || izaberi_datum_cal_lbl.getText().contains("ПОЛАСКА")
				|| izaberi_datum_cal_lbl.getText().contains("DEPARTURE")) {
			datum_polaska_value_lbl.setText(ned_dat_lbl.getText());
			datum_izmena_value_lbl.setText(ned_dat_lbl.getText());
		}else {
			datum_povratka_polazak_value_lbl.setText(ned_dat_lbl.getText());
			datum_povratka_izmena_value_lbl.setText(ned_dat_lbl.getText());
		}
		//calendar_pn.setVisible(false);
	}
	
	public void handle_calendar_odustani() {
		polasci_calendar_pn.setVisible(false);
		//calendar_pn.setVisible(false);
	}
	
	public void handle_calendar_pre_ned() {
		Calendar cal_pol = Calendar.getInstance();
		try {
			System.out.println("######datum_polaska_value_lbl = " + datum_polaska_value_lbl);
			cal_pol.setTime(_sdf.parse(datum_polaska_value_lbl.getText()));
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("parse exeption in handle_calendar_pre_ned , details; " + e.getMessage());
		}
		Calendar monday_prev_week = Calendar.getInstance();
		try {
			monday_prev_week.setTime(_sdfddMMyyyy.parse(pon_dat_lbl.getText()));
			monday_prev_week.add(Calendar.DATE, -7);
		}catch(Exception e) {

		}
		
		
		
		
		
		
		
		pon_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//pon_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		pon_pn.setVisible(!cal_pol.after(monday_prev_week));

		monday_prev_week.add(Calendar.HOUR, 24);
		uto_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//uto_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		uto_pn.setVisible(!cal_pol.after(monday_prev_week));
		
		monday_prev_week.add(Calendar.HOUR, 24);
		sre_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//sre_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		sre_pn.setVisible(!cal_pol.after(monday_prev_week));
		
		monday_prev_week.add(Calendar.HOUR, 24);
		cet_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//cet_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		cet_pn.setVisible(!cal_pol.after(monday_prev_week));
		
		monday_prev_week.add(Calendar.HOUR, 24);
		pet_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//pet_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		pet_pn.setVisible(!cal_pol.after(monday_prev_week));
		
		monday_prev_week.add(Calendar.HOUR, 24);
		sub_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//sub_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		sub_pn.setVisible(!cal_pol.after(monday_prev_week));
		
		monday_prev_week.add(Calendar.HOUR, 24);
		ned_dat_lbl.setText(_sdfddMMyyyy.format(monday_prev_week.getTime()));
		//ned_izab_btn.setVisible(!cal_pol.after(monday_prev_week));
		ned_pn.setVisible(!cal_pol.after(monday_prev_week));
			
		System.out.println("### is ponedeljak dugme visible: " + pon_pn.isVisible());
		pret_ned_cal_btn.setVisible(pon_pn.isVisible());
		sled_ned_cal_btn.setVisible(true);

	}
	
	public void handle_calendar_pos_ned() {
		
		Calendar cal_pol = Calendar.getInstance();
		
		Calendar cal_dva_meseca = Calendar.getInstance();
		cal_dva_meseca.add(Calendar.MONTH, 2);
		pret_ned_cal_btn.setVisible(true);
		try {
			cal_pol.setTime(_sdf.parse(datum_polaska_value_lbl.getText()));
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("parse exeption in handle_calendar_pre_ned , details; " + e.getMessage());
		}
		Calendar monday_pos_week = Calendar.getInstance();
		try {
			monday_pos_week.setTime(_sdfddMMyyyy.parse(pon_dat_lbl.getText()));
			monday_pos_week.add(Calendar.DATE, 7);
		}catch(Exception e) {

		}
		pon_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//pon_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		pon_pn.setVisible(!cal_pol.after(monday_pos_week));
		pon_pn.setVisible(monday_pos_week.before(cal_dva_meseca));
		
		monday_pos_week.add(Calendar.HOUR, 24);
		uto_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//uto_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		uto_pn.setVisible(!cal_pol.after(monday_pos_week));
		uto_pn.setVisible(monday_pos_week.before(cal_dva_meseca));
		
		monday_pos_week.add(Calendar.HOUR, 24);
		sre_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//sre_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		sre_pn.setVisible(!cal_pol.after(monday_pos_week));
		sre_pn.setVisible(monday_pos_week.before(cal_dva_meseca));
		
		monday_pos_week.add(Calendar.HOUR, 24);
		cet_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//cet_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		cet_pn.setVisible(!cal_pol.after(monday_pos_week));
		cet_pn.setVisible(monday_pos_week.before(cal_dva_meseca));
		
		monday_pos_week.add(Calendar.HOUR, 24);
		pet_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//pet_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		pet_pn.setVisible(!cal_pol.after(monday_pos_week));
		pet_pn.setVisible(monday_pos_week.before(cal_dva_meseca));
		
		monday_pos_week.add(Calendar.HOUR, 24);
		sub_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//sub_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		sub_pn.setVisible(!cal_pol.after(monday_pos_week));
		sub_pn.setVisible(monday_pos_week.before(cal_dva_meseca));
		
		monday_pos_week.add(Calendar.HOUR, 24);
		ned_dat_lbl.setText(_sdfddMMyyyy.format(monday_pos_week.getTime()));
		//ned_izab_btn.setVisible(!cal_pol.after(monday_pos_week));
		ned_pn.setVisible(!cal_pol.after(monday_pos_week));
		ned_pn.setVisible(monday_pos_week.before(cal_dva_meseca));	
		
		sled_ned_cal_btn.setVisible(ned_pn.isVisible());
		

	}
	
	
	public void handle_sled_polasci_pagination() {
		handle_pagination_polasci_povratci(true);
	}
	
	public void handle_polasci_odustani() {
		//polasci_nova_pn.setVisible(false);
		polasci_calendar_pn.setVisible(false);
	}
	
	public void handle_pred_polasci_pagination() {
		handle_pagination_polasci_povratci(false);
	}
	
	public void handle_prvi_pol() {
		System.out.println("handle_prvi_pol, _lista_svih_polazaka_current_position = " + _lista_svih_polazaka_current_position);
		System.out.println("handle_prvi_pol, _lista_svih_povrataka_current_position = " + _lista_svih_povrataka_current_position);
		if(_polazak_true_povratak_false/*izaberi_polazak_lbl.getText().contains("POLAZAK") || izaberi_polazak_lbl.getText().contains("ПОЛАЗАК")
				|| izaberi_polazak_lbl.getText().contains("DEPARTURE")*/) {
			_selected_voz = _svi_polasci.get(_lista_svih_polazaka_current_position + 0 );
			_selected_voz.setRang(Integer.parseInt(prvi_pol_rang_value_lbl.getText()));
			setCena(false);
		}else {
			_selected_voz_povratak = /*_svi_povratci*/getListaPovrataka().get(_lista_svih_povrataka_current_position + 0 );
			_selected_voz_povratak.setRang(Integer.parseInt(prvi_pol_rang_value_lbl.getText()));
			setCena(true);
		}
		resetPolazakButtonGroup(0);
		uslov_za_drugi_korak();
		//polasci_nova_pn.setVisible(false);
		polasci_calendar_pn.setVisible(false);
		 
	}
	
	public void handle_drugi_pol() {
		System.out.println("handle_drugi_pol, _lista_svih_polazaka_current_position = " + _lista_svih_polazaka_current_position);
		System.out.println("handle_drugi_pol, _lista_svih_povrataka_current_position = " + _lista_svih_povrataka_current_position);
		if(_polazak_true_povratak_false/*izaberi_polazak_lbl.getText().contains("POLAZAK") || izaberi_polazak_lbl.getText().contains("ПОЛАЗАК")
				|| izaberi_polazak_lbl.getText().contains("DEPARTURE")*/) {
			_selected_voz = _svi_polasci.get(_lista_svih_polazaka_current_position + 1 );
			_selected_voz.setRang(Integer.parseInt(drugi_pol_rang_value_lbl.getText()));
			setCena(false);
		}else {
			_selected_voz_povratak =/* _svi_povratci*/getListaPovrataka().get(_lista_svih_povrataka_current_position + 1 );
			_selected_voz_povratak.setRang(Integer.parseInt(drugi_pol_rang_value_lbl.getText()));
			setCena(true);
		}
		resetPolazakButtonGroup(1);
		uslov_za_drugi_korak();
		//polasci_nova_pn.setVisible(false);
		polasci_calendar_pn.setVisible(false);

		 
	}

	public void handle_treci_pol() {
		System.out.println("handle_treci_pol, _lista_svih_polazaka_current_position = " + _lista_svih_polazaka_current_position);
		System.out.println("handle_treci_pol, _lista_svih_povrataka_current_position = " + _lista_svih_povrataka_current_position);
		if(_polazak_true_povratak_false/*izaberi_polazak_lbl.getText().contains("POLAZAK") || izaberi_polazak_lbl.getText().contains("ПОЛАЗАК")
				|| izaberi_polazak_lbl.getText().contains("DEPARTURE")*/) {
			_selected_voz = _svi_polasci.get(_lista_svih_polazaka_current_position + 2 );
			_selected_voz.setRang(Integer.parseInt(treci_pol_rang_value_lbl.getText()));
			setCena(false);
		}else {
			_selected_voz_povratak = /*_svi_povratci*/getListaPovrataka().get(_lista_svih_povrataka_current_position + 2 );
			_selected_voz_povratak.setRang(Integer.parseInt(treci_pol_rang_value_lbl.getText()));
			setCena(true);
		}
		resetPolazakButtonGroup(2);
		uslov_za_drugi_korak();
		//polasci_nova_pn.setVisible(false);
		polasci_calendar_pn.setVisible(false);

		 
	}

	public void handle_cetvrti_pol() {
		System.out.println("handle_cetvrti_pol, _lista_svih_polazaka_current_position = " + _lista_svih_polazaka_current_position);
		System.out.println("handle_cetvrti_pol, _lista_svih_povrataka_current_position = " + _lista_svih_povrataka_current_position);
		if(_polazak_true_povratak_false/*izaberi_polazak_lbl.getText().contains("POLAZAK") || izaberi_polazak_lbl.getText().contains("ПОЛАЗАК")
				|| izaberi_polazak_lbl.getText().contains("DEPARTURE")*/) {
			_selected_voz = _svi_polasci.get(_lista_svih_polazaka_current_position + 3 );
			_selected_voz.setRang(Integer.parseInt(cetvrti_pol_rang_value_lbl.getText()));
			setCena(false);
		}else {
			_selected_voz_povratak = /*_svi_povratci*/getListaPovrataka().get(_lista_svih_povrataka_current_position + 3 );
			_selected_voz_povratak.setRang(Integer.parseInt(cetvrti_pol_rang_value_lbl.getText()));
			setCena(true);
		}
		resetPolazakButtonGroup(3);
		uslov_za_drugi_korak();
		//polasci_nova_pn.setVisible(false);
		polasci_calendar_pn.setVisible(false);
		 
	}
	
	public void handle_peti_pol() {
		System.out.println("handle_peti_pol, _lista_svih_polazaka_current_position = " + _lista_svih_polazaka_current_position);
		System.out.println("handle_peti_pol, _lista_svih_povrataka_current_position = " + _lista_svih_povrataka_current_position);
		if(_polazak_true_povratak_false/*izaberi_polazak_lbl.getText().contains("POLAZAK") || izaberi_polazak_lbl.getText().contains("ПОЛАЗАК")
				|| izaberi_polazak_lbl.getText().contains("DEPARTURE")*/) {
			_selected_voz = _svi_polasci.get(_lista_svih_polazaka_current_position + 4 );
			_selected_voz.setRang(Integer.parseInt(peti_pol_rang_value_lbl.getText()));
			setCena(false);
		}else {
			_selected_voz_povratak = /*_svi_povratci*/getListaPovrataka().get(_lista_svih_povrataka_current_position + 4 );
			_selected_voz_povratak.setRang(Integer.parseInt(peti_pol_rang_value_lbl.getText()));
			setCena(true);
		}
		
		resetPolazakButtonGroup(4);
		uslov_za_drugi_korak();
		//polasci_nova_pn.setVisible(false);
		polasci_calendar_pn.setVisible(false);
		 
	}
	
	
	public void handle_tip_karte_izmeni() {
//		restartSessionExpiration();
		handleOsvezi();
		_prva_karta_tip = _prva_karta_tip_not_confirmed;
		_druga_karta_tip = _druga_karta_tip_not_confirmed;
		_treca_karta_tip = _treca_karta_tip_not_confirmed;
		_cetvrta_karta_tip = _cetvrta_karta_tip_not_confirmed;
		_peta_karta_tip = _peta_karta_tip_not_confirmed;
		setCena(smer_value_lbl.getText().equals(resources.getString("smer_povratni_btn")));
		tip_karte_pn.setVisible(false);
	}
	
	
	public void handle_tip_karte_odustani() {
//		restartSessionExpiration();
		handleOsvezi();
		_prva_karta_tip_not_confirmed = _prva_karta_tip;
		_druga_karta_tip_not_confirmed = _druga_karta_tip;
		_treca_karta_tip_not_confirmed = _treca_karta_tip;
		_cetvrta_karta_tip_not_confirmed = _cetvrta_karta_tip;
		_peta_karta_tip_not_confirmed = _peta_karta_tip;
		tip_karte_pn.setVisible(false);
	}
	
	public void handle_prvi_tip_redovna() {
		_prva_karta_tip_not_confirmed=TIP_REDOVNA_CENA;
		prvi_tip_tf.setVisible(false);
		prvi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, 0);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_prvi_tip_srb_k13() {
		_prva_karta_tip_not_confirmed=TIP_SRB_K13;
		setTipKarteIzmeniVisible(false);
		prvi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		prvi_tip_tf.setVisible(true);
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, 1);
		bindWithTastatura(prvi_tip_tf, 120, _prva_tip_karte_button_group, 1);
	}
	
	public void handle_prvi_tip_rail_k30() {
		_prva_karta_tip_not_confirmed = TIP_RAIL_K30;
		setTipKarteIzmeniVisible(false);
		prvi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		prvi_tip_tf.setVisible(true);
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, 2);
		bindWithTastatura(prvi_tip_tf, 120, _prva_tip_karte_button_group, 1);
	}
	
	public void handle_prvi_tip_dete() { 
		_prva_karta_tip_not_confirmed = TIP_DETE;
		prvi_tip_tf.setVisible(false);
		prvi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, 3);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_prvi_tip_pas() {
		_prva_karta_tip_not_confirmed = TIP_PAS;
		prvi_tip_tf.setVisible(false);
		prvi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, 4);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_drugi_tip_redovna() {
		_druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		drugi_tip_tf.setVisible(false);
		drugi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, 0);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_drugi_tip_srb_k13() {
		_druga_karta_tip_not_confirmed = TIP_SRB_K13;
		drugi_tip_tf.setVisible(true);
		drugi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, 1);
		bindWithTastatura(drugi_tip_tf, 120, _druga_tip_karte_button_group, 2);
	}
	
	public void handle_drugi_tip_rail_k30() {
		_druga_karta_tip_not_confirmed = TIP_RAIL_K30;
		drugi_tip_tf.setVisible(true);
		drugi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, 2);
		bindWithTastatura(drugi_tip_tf, 120, _druga_tip_karte_button_group, 2);
	}
	
	public void handle_drugi_tip_dete() {
		_druga_karta_tip_not_confirmed = TIP_DETE;
		drugi_tip_tf.setVisible(false);
		drugi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, 3);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_drugi_tip_pas() {
		_druga_karta_tip_not_confirmed = TIP_PAS;
		drugi_tip_tf.setVisible(false);
		drugi_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, 4);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_treci_tip_redovna() {
		_treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		treci_tip_tf.setVisible(false);
		treci_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, 0);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_treci_tip_srb_k13() {
		_treca_karta_tip_not_confirmed = TIP_SRB_K13;
		treci_tip_tf.setVisible(true);
		treci_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, 1);
		bindWithTastatura(treci_tip_tf, 120, _treca_tip_karte_button_group, 3);
		
	}
	
	public void handle_treci_tip_rail_k30() {
		_treca_karta_tip_not_confirmed = TIP_RAIL_K30;
		treci_tip_tf.setVisible(true);
		treci_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, 2);
		bindWithTastatura(treci_tip_tf, 120, _treca_tip_karte_button_group, 3);
	}
	
	public void handle_treci_tip_dete() {
		_treca_karta_tip_not_confirmed = TIP_DETE;
		treci_tip_tf.setVisible(false);
		treci_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, 3);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_treci_tip_pas() {
		_treca_karta_tip_not_confirmed = TIP_PAS;
		treci_tip_tf.setVisible(false);
		treci_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, 4);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_cetvrti_tip_redovna() {
		_cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		cetvrti_tip_tf.setVisible(false);
		cetvrti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, 0);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_cetvrti_tip_srb_k13() {
		_cetvrta_karta_tip_not_confirmed = TIP_SRB_K13;
		cetvrti_tip_tf.setVisible(true);
		cetvrti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, 1);
		bindWithTastatura(cetvrti_tip_tf, -350, _cetvrta_tip_karte_button_group, 4);
		
	}
	
	public void handle_cetvrti_tip_rail_k30() {
		_cetvrta_karta_tip_not_confirmed = TIP_RAIL_K30;
		cetvrti_tip_tf.setVisible(true);
		cetvrti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, 2);
		bindWithTastatura(cetvrti_tip_tf, -350, _cetvrta_tip_karte_button_group, 4);
	}
	
	public void handle_cetvrti_tip_dete() {
		_cetvrta_karta_tip_not_confirmed = TIP_DETE;
		cetvrti_tip_tf.setVisible(false);
		cetvrti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, 3);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_cetvrti_tip_pas() {
		_cetvrta_karta_tip_not_confirmed = TIP_PAS;
		cetvrti_tip_tf.setVisible(false);
		cetvrti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, 4);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_peti_tip_redovna() {

		setTipKarteIzmeniVisible(false);
		peti_tip_tf.setVisible(false);
		peti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, 0);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_peti_tip_srb_k13() { 
		_peta_karta_tip_not_confirmed = TIP_SRB_K13;
		peti_tip_tf.setVisible(true);
		peti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, 1);
		bindWithTastatura(peti_tip_tf, -350, _peta_tip_karte_button_group, 5);
	}
	
	public void handle_peti_tip_rail_k30() {
		_peta_karta_tip_not_confirmed = TIP_RAIL_K30;
		peti_tip_tf.setVisible(true);
		peti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, 2);
		bindWithTastatura(peti_tip_tf, -350, _peta_tip_karte_button_group, 5);
	}
	
	public void handle_peti_tip_dete() {
		_peta_karta_tip_not_confirmed = TIP_DETE;
		peti_tip_tf.setVisible(false);
		peti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, 3);
		tastatura_pn.setVisible(false);
	}
	
	public void handle_peti_tip_pas() {
		_peta_karta_tip_not_confirmed = TIP_PAS;
		peti_tip_tf.setVisible(false);
		peti_tip_tf.setText("");
		id_povlastice_value_lbl.setText("");
		setTipKarteIzmeniVisible(false);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, 4);
		tastatura_pn.setVisible(false);
	}


	public void handle_placanje_nazad_btn() {
		placanje_pn.setVisible(false);
	}
	public void handleTastJedan() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "1");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastDva() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "2");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastTri() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "3");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastCetiri() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "4");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastPet() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "5");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastSest() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "6");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastSedam() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "7");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastOsam() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "8");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastDevet() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "9");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	public void handleTastBackSpace() {
		if(_currentBindTastTF.getText().length() > 0) {
			_currentBindTastTF.setText(_currentBindTastTF.getText().substring(0,_currentBindTastTF.getText().length() -1));
		}
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}
	
	public void handleTastOK() {
		//TODO poziv za proveru
		//ako je dobar, zatvori, ukoliko nije dobar, mora da bira neki drugi
		int popust_id = _currentBindTastTF.getId().endsWith("30") ? 30 : 37;
		try {
			if(SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).provera_legitimacije(Integer.parseInt(_currentBindTastTF.getText()), popust_id)) {
				run_info("Popust odobren", "", "", "",  "", "", "", 2);
			}else {
				resetTipKarteButtonGroup(_current_tip_karte_button_group, 0);
				_currentBindTastTF.setText("");
				id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
				_currentBindTastTF.setVisible(false);

				switch(_current_bind_tip_karte) {
				case 1: _prva_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
				case 2: _druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
				case 3: _treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
				case 4: _cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
				case 5: _peta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
				}
				setTipKarteIzmeniVisible(false);
				run_error("Popust nije odobren", "ID legitimacije nije validan", "ili ne legitimacija istekla", "",  "", "", "", 4);

			}
		}catch(Exception e) {
			resetTipKarteButtonGroup(_current_tip_karte_button_group, 0);
			_currentBindTastTF.setText("");
			_currentBindTastTF.setVisible(false);
			switch(_current_bind_tip_karte) {
			case 1: _prva_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
			case 2: _druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
			case 3: _treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
			case 4: _cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
			case 5: _peta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
			}
			setTipKarteIzmeniVisible(false);
			run_error("Popust nije odobren", "greska u komunikaciji prilikom", "provere validnosti", "",  "", "", "", 4);

		}
		tastatura_pn.setVisible(false);
	}
	
	public void handleTastNula() {
		_currentBindTastTF.setText(_currentBindTastTF.getText() + "0");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
	}

	public void handle_smer_u_jednom() {
		
		_prva_karta_tip = TIP_REDOVNA_CENA;
		_druga_karta_tip = TIP_REDOVNA_CENA;
		_treca_karta_tip = TIP_REDOVNA_CENA;
		_cetvrta_karta_tip = TIP_REDOVNA_CENA;
		_peta_karta_tip = TIP_REDOVNA_CENA;
		
		_prva_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_peta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		
		tip_karte_izmena_value_lbl.setText(resources.getString("redovna_cena"));
		prvi_tip_redovna_btn.setText(resources.getString("redovna_cena"));
		drugi_tip_redovna_btn.setText(resources.getString("redovna_cena"));
		treci_tip_redovna_btn.setText(resources.getString("redovna_cena"));
		cetvrti_tip_redovna_btn.setText(resources.getString("redovna_cena"));
		peti_tip_redovna_btn.setText(resources.getString("redovna_cena"));
		handle_smer(false);
	}
	
	public void handle_plati_final_btn(){
		try {
			PowerShellPrinterStatus.checkPrinterStatus();
			placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("prisloni_karticu_za_placanje_gif")+"')");
			placanje_result_pn.setVisible(true);
			placanje_result_pn.toFront();
			
			boolean is_petnaest_dana = false;
			if(_selected_voz_povratak != null) {
				is_petnaest_dana = _selected_voz_povratak.getRelkm() > 100;
			}
			
			
			KartaPaymentControler controller = new KartaPaymentControler(this,_kartomat.getiD_USER(), _kartomat.getiD_TERMINALA(),_kartomat.getNaziV_STANICE(),
					_selected_voz, _selected_voz_povratak, _selected_razred_polazak, _selected_razred_povratak, Integer.parseInt( broj_putnika_lbl.getText() ),
			Double.parseDouble(ukupna_cena_value_lbl.getText()), _prvi_putnik_cena, _drugi_putnik_cena, _treci_putnik_cena, _cetvrti_putnik_cena, _peti_putnik_cena,
			_prvi_putnik_povratna_cena, _drugi_putnik_povratna_cena, _treci_putnik_povratna_cena, _cetvrti_putnik_povratna_cena, _peti_putnik_povratna_cena, 
			_prva_karta_tip, _druga_karta_tip, _treca_karta_tip, _treca_karta_tip, _peta_karta_tip, is_petnaest_dana);
			

			
			
			Thread thread = new Thread(controller);
			thread.start();
		}catch(Exception e) {

			error_pn.setVisible(true);
			error_pn.toFront();
			run_error("GREŠKA NA ŠTAMPAČU", e.getMessage(), "POZOVITE OSOBLJE STANICE", "",  "", "", "", 10);

			Thread thread3 = new Thread(() -> {


				try {
					Thread.sleep(10000);

				}catch(Exception ee) {}
				finally {
					placanje_pn.setVisible(false);
					odrediste_pn.setVisible(true);
					stop_recycling_session();
				}
			});

			thread3.setDaemon(true);
			thread3.start();

		}
		
		


			//platu_kartu_sequence_threaded();

	}
	
	public void handleA() {
		azuriraj_listu_ostalinh_filtered_stanica("A");
	}
	
	private void azuriraj_listu_ostalinh_filtered_stanica(String slovo) {
		_ostale_stanice_filter = slovo.equals("") ? "" : _ostale_stanice_filter + slovo;
		_ostale_stanice_filtered.clear();
		Set<String> filter_slova = new HashSet<String>();
		if(slovo.equals("")) {
			_ostale_stanice_filtered = new ArrayList<StanicaIDBean>(_ostale_stanice);
		}
		
		for(StanicaIDBean current : _ostale_stanice) {
			if(_ostale_stanice_filter.equals("")) {
				filter_slova.add(current.getNaziv().substring(0,1).toUpperCase());
			}else {
				if(current.getNaziv().toUpperCase().startsWith(_ostale_stanice_filter)) {
					_ostale_stanice_filtered.add(current);
					if(current.getNaziv().length() > _ostale_stanice_filter.length()) {
						filter_slova.add(current.getNaziv().substring(_ostale_stanice_filter.length(),_ostale_stanice_filter.length() + 1).toUpperCase());
					}
				}
			}
		}

		if(!filter_slova.contains("A")){tast_A_btn.setStyle("-fx-background-color: darkgray;");tast_A_btn.setDisable(true);} else {tast_A_btn.setStyle("-fx-background-color: #3a6dcf;");tast_A_btn.setDisable(false);}
		if(!filter_slova.contains("B")){tast_B_btn.setStyle("-fx-background-color: darkgray;");tast_B_btn.setDisable(true);} else {tast_B_btn.setStyle("-fx-background-color: #3a6dcf;");tast_B_btn.setDisable(false);}
		if(!filter_slova.contains("C")){tast_C_btn.setStyle("-fx-background-color: darkgray;");tast_C_btn.setDisable(true);} else {tast_C_btn.setStyle("-fx-background-color: #3a6dcf;");tast_C_btn.setDisable(false);}
		if(!filter_slova.contains("D")){tast_D_btn.setStyle("-fx-background-color: darkgray;");tast_D_btn.setDisable(true);} else {tast_D_btn.setStyle("-fx-background-color: #3a6dcf;");tast_D_btn.setDisable(false);}
		if(!filter_slova.contains("E")){tast_E_btn.setStyle("-fx-background-color: darkgray;");tast_E_btn.setDisable(true);} else {tast_E_btn.setStyle("-fx-background-color: #3a6dcf;");tast_E_btn.setDisable(false);}
		if(!filter_slova.contains("F")){tast_F_btn.setStyle("-fx-background-color: darkgray;");tast_F_btn.setDisable(true);} else {tast_F_btn.setStyle("-fx-background-color: #3a6dcf;");tast_F_btn.setDisable(false);}
		if(!filter_slova.contains("G")){tast_G_btn.setStyle("-fx-background-color: darkgray;");tast_G_btn.setDisable(true);} else {tast_G_btn.setStyle("-fx-background-color: #3a6dcf;");tast_G_btn.setDisable(false);}
		if(!filter_slova.contains("H")){tast_H_btn.setStyle("-fx-background-color: darkgray;");tast_H_btn.setDisable(true);} else {tast_H_btn.setStyle("-fx-background-color: #3a6dcf;");tast_H_btn.setDisable(false);}
		if(!filter_slova.contains("I")){tast_I_btn.setStyle("-fx-background-color: darkgray;");tast_I_btn.setDisable(true);} else {tast_I_btn.setStyle("-fx-background-color: #3a6dcf;");tast_I_btn.setDisable(false);}
		if(!filter_slova.contains("J")){tast_J_btn.setStyle("-fx-background-color: darkgray;");tast_J_btn.setDisable(true);} else {tast_J_btn.setStyle("-fx-background-color: #3a6dcf;");tast_J_btn.setDisable(false);}
		if(!filter_slova.contains("K")){tast_K_btn.setStyle("-fx-background-color: darkgray;");tast_K_btn.setDisable(true);} else {tast_K_btn.setStyle("-fx-background-color: #3a6dcf;");tast_K_btn.setDisable(false);}
		if(!filter_slova.contains("L")){tast_L_btn.setStyle("-fx-background-color: darkgray;");tast_L_btn.setDisable(true);} else {tast_L_btn.setStyle("-fx-background-color: #3a6dcf;");tast_L_btn.setDisable(false);}
		if(!filter_slova.contains("M")){tast_M_btn.setStyle("-fx-background-color: darkgray;");tast_M_btn.setDisable(true);} else {tast_M_btn.setStyle("-fx-background-color: #3a6dcf;");tast_M_btn.setDisable(false);}
		if(!filter_slova.contains("N")){tast_N_btn.setStyle("-fx-background-color: darkgray;");tast_N_btn.setDisable(true);} else {tast_N_btn.setStyle("-fx-background-color: #3a6dcf;");tast_N_btn.setDisable(false);}
		if(!filter_slova.contains("O")){tast_O_btn.setStyle("-fx-background-color: darkgray;");tast_O_btn.setDisable(true);} else {tast_O_btn.setStyle("-fx-background-color: #3a6dcf;");tast_O_btn.setDisable(false);}
		if(!filter_slova.contains("P")){tast_P_btn.setStyle("-fx-background-color: darkgray;");tast_P_btn.setDisable(true);} else {tast_P_btn.setStyle("-fx-background-color: #3a6dcf;");tast_P_btn.setDisable(false);}
		if(!filter_slova.contains("R")){tast_R_btn.setStyle("-fx-background-color: darkgray;");tast_R_btn.setDisable(true);} else {tast_R_btn.setStyle("-fx-background-color: #3a6dcf;");tast_R_btn.setDisable(false);}
		if(!filter_slova.contains("S")){tast_S_btn.setStyle("-fx-background-color: darkgray;");tast_S_btn.setDisable(true);} else {tast_S_btn.setStyle("-fx-background-color: #3a6dcf;");tast_S_btn.setDisable(false);}
		if(!filter_slova.contains("T")){tast_T_btn.setStyle("-fx-background-color: darkgray;");tast_T_btn.setDisable(true);} else {tast_T_btn.setStyle("-fx-background-color: #3a6dcf;");tast_T_btn.setDisable(false);}
		if(!filter_slova.contains("Ć")){tast_Ć_btn.setStyle("-fx-background-color: darkgray;");tast_Ć_btn.setDisable(true);} else {tast_Ć_btn.setStyle("-fx-background-color: #3a6dcf;");tast_Ć_btn.setDisable(false);}
		if(!filter_slova.contains("Č")){tast_Č_btn.setStyle("-fx-background-color: darkgray;");tast_Č_btn.setDisable(true);} else {tast_Č_btn.setStyle("-fx-background-color: #3a6dcf;");tast_Č_btn.setDisable(false);}
		if(!filter_slova.contains("U")){tast_U_btn.setStyle("-fx-background-color: darkgray;");tast_U_btn.setDisable(true);} else {tast_U_btn.setStyle("-fx-background-color: #3a6dcf;");tast_U_btn.setDisable(false);}
		if(!filter_slova.contains("V")){tast_V_btn.setStyle("-fx-background-color: darkgray;");tast_V_btn.setDisable(true);} else {tast_V_btn.setStyle("-fx-background-color: #3a6dcf;");tast_V_btn.setDisable(false);}
		if(!filter_slova.contains("Š")){tast_Š_btn.setStyle("-fx-background-color: darkgray;");tast_Š_btn.setDisable(true);} else {tast_Š_btn.setStyle("-fx-background-color: #3a6dcf;");tast_Š_btn.setDisable(false);}
		if(!filter_slova.contains("Z")){tast_Z_btn.setStyle("-fx-background-color: darkgray;");tast_Z_btn.setDisable(true);} else {tast_Z_btn.setStyle("-fx-background-color: #3a6dcf;");tast_Z_btn.setDisable(false);}
		if(!filter_slova.contains("Ž")){tast_Ž_btn.setStyle("-fx-background-color: darkgray;");tast_Ž_btn.setDisable(true);} else {tast_Ž_btn.setStyle("-fx-background-color: #3a6dcf;");tast_Ž_btn.setDisable(false);}
		refresh_ostale_stanice();
	}
	
	public void handleB() {
		azuriraj_listu_ostalinh_filtered_stanica("B");
	}
	
	public void handleC() {
		azuriraj_listu_ostalinh_filtered_stanica("C");
	}
	
	public void handleD() {
		azuriraj_listu_ostalinh_filtered_stanica("D");
	}
	
	public void handleE() {
		azuriraj_listu_ostalinh_filtered_stanica("E");
	}
	
	public void handleF() {
		azuriraj_listu_ostalinh_filtered_stanica("F");
	}
	
	public void handleG() {
		azuriraj_listu_ostalinh_filtered_stanica("G");
	}
	
	public void handleH() {
		azuriraj_listu_ostalinh_filtered_stanica("H");
	}
	
	public void handleI() {
		azuriraj_listu_ostalinh_filtered_stanica("I");
	}
	
	public void handleJ() {
		azuriraj_listu_ostalinh_filtered_stanica("J");
	}
	
	public void handleK() {
		azuriraj_listu_ostalinh_filtered_stanica("K");
	}
	public void handleL() {
		azuriraj_listu_ostalinh_filtered_stanica("L");
	}
	public void handleM() {
		azuriraj_listu_ostalinh_filtered_stanica("M");
	}
	public void handleN() {
		azuriraj_listu_ostalinh_filtered_stanica("N");
	}
	public void handleO() {
		azuriraj_listu_ostalinh_filtered_stanica("O");
	}
	public void handleP() {
		azuriraj_listu_ostalinh_filtered_stanica("P");
	}
	public void handleR() {
		azuriraj_listu_ostalinh_filtered_stanica("R");
	}
	public void handleS() {
		azuriraj_listu_ostalinh_filtered_stanica("S");
	}
	public void handleT() {
		azuriraj_listu_ostalinh_filtered_stanica("T");
	}
	public void handleCS() {
		azuriraj_listu_ostalinh_filtered_stanica("Ć");
	}
    
	public void handleCH() {
		azuriraj_listu_ostalinh_filtered_stanica("Č");
	}
	public void handleU() {
		azuriraj_listu_ostalinh_filtered_stanica("U");
	}
	public void handleV() {
		azuriraj_listu_ostalinh_filtered_stanica("V");
	} 
	public void handleSH() {
		azuriraj_listu_ostalinh_filtered_stanica("Š");
	}
	public void handleZ() {
		azuriraj_listu_ostalinh_filtered_stanica("Z");
	}
	public void handleZH() {
		azuriraj_listu_ostalinh_filtered_stanica("Ž");
	}
	public void handlePONISTI() {
		azuriraj_listu_ostalinh_filtered_stanica("");
	}
	
	
	
	public void handle_cir() {
		setSerbianCir();
	}
	
	public void handle_lat() {
		setSerbian();
	}
	
	public void handle_eng() {
		setEnglish();
	}
	
	public void handle_strana1_pomoc_btn() {
		
		Platform.runLater(() -> {       	
			help_1_pn.setVisible(true);
			help_1_pn.toFront();
            Thread thread = new Thread(() -> {
                try {
                    // Wait for 15 secs
                    Thread.sleep(15000);

                } catch (Exception exp) {
                    exp.printStackTrace();
                } finally {
                	help_1_pn.setVisible(false);
                	
                }
            });
            thread.setDaemon(true);
            thread.start();
    				
        });
		
	}
	
	public void handle_strana1_pomoc_zatvori() {
		help_1_pn.setVisible(false);
	}
	
	public void handle_novi_pomoc_btn() {
		
		Platform.runLater(() -> {       	
			help_2_pn.setVisible(true);
			help_2_pn.toFront();
            Thread thread = new Thread(() -> {
                try {
                    // Wait for 15 secs
                    Thread.sleep(15000);

                } catch (Exception exp) {
                    exp.printStackTrace();
                } finally {
                	help_2_pn.setVisible(false);
                	
                }
            });
            thread.setDaemon(true);
            thread.start();
    				
        });
		
	}

	public void handle_strana2_pomoc_zatvori() {
		help_2_pn.setVisible(false);
	}
	
	public void handle_strana3_pomoc_btn() {
		
		Platform.runLater(() -> {       	
			help_3_pn.setVisible(true);
			help_3_pn.toFront();
            Thread thread = new Thread(() -> {
                try {
                    // Wait for 15 secs
                    Thread.sleep(15000);

                } catch (Exception exp) {
                    exp.printStackTrace();
                } finally {
                	help_3_pn.setVisible(false);
                	
                }
            });
            thread.setDaemon(true);
            thread.start();
    				
        });
		
	}
	
	public void handle_strana3_pomoc_zatvori() {
		help_3_pn.setVisible(false);
	}
	
	
	public void handle_tastatura_zatvori(){

		resetTipKarteButtonGroup(_current_tip_karte_button_group, 0);
		_currentBindTastTF.setText("");
		id_povlastice_value_lbl.setText(_currentBindTastTF.getText());
		_currentBindTastTF.setVisible(false);

		switch(_current_bind_tip_karte) {
		case 1: _prva_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
		case 2: _druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
		case 3: _treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
		case 4: _cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
		case 5: _peta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;break;
		}


		tastatura_pn.setVisible(false);
	}
	
	
	public void handle_pocetna_btn() {

		stop_recycling_session();
		
	}
	
	public void handle_novi_izmeni_nazad_btn(){
		handleIzmeniPolaske();
	}
	
	
   
//////////////////////////////////////////end of handle buttons///////////////////////////////////////	
	

	
	
	void setTipKarteIzmeniVisible(boolean is_broj_putnika_izmenjen) {
		boolean izmenjeno = is_broj_putnika_izmenjen || _prva_karta_tip != _prva_karta_tip_not_confirmed ||
				_druga_karta_tip != _druga_karta_tip_not_confirmed ||
						_treca_karta_tip != _treca_karta_tip_not_confirmed ||
								_cetvrta_karta_tip != _cetvrta_karta_tip_not_confirmed ||
										_peta_karta_tip != _peta_karta_tip_not_confirmed;
		tip_karte_izmeni_btn.setVisible(izmenjeno);
		
		if(izmenjeno) {
			
			Set<Integer> temp = new HashSet<Integer>();
			int broj_karata = Integer.parseInt(broj_putnika_lbl.getText());
			if(broj_karata > 0)temp.add(_prva_karta_tip_not_confirmed);
			if(broj_karata > 1)temp.add(_druga_karta_tip_not_confirmed);
			if(broj_karata > 2)temp.add(_treca_karta_tip_not_confirmed);
			if(broj_karata > 3)temp.add(_cetvrta_karta_tip_not_confirmed);
			if(broj_karata > 4)temp.add(_peta_karta_tip_not_confirmed);
			String tip = "";
			boolean is_first = true;
			for(Integer cut_tip_id : temp) {
				if(!is_first)tip = tip + ", ";
				switch(cut_tip_id) {
				case TIP_REDOVNA_CENA : tip = tip + resources.getString("redovna_cena");break;
				case TIP_RAIL_K30 : tip = tip + "Rail K30 ";break;
				case TIP_SRB_K13 : tip = tip + "SRB K13";break;
				case TIP_PAS : tip = tip + resources.getString("pas");break;
				case TIP_DETE : tip = tip + resources.getString("dete");break;
				case TIP_POVRATNA : tip = tip + resources.getString("povratna");break;
				}
				is_first = false;
			}
			tip_karte_polazak_value_lbl.setText(tip);
			tip_karte_izmena_value_lbl.setText(tip);
			tip_karte_dolazak_value_lbl.setText(tip);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void uslov_za_drugi_korak() {


		
		relacija_polaza_value_lbl.setText(_kartomat.getNaziV_STANICE());
		povratna_karta_pn.setVisible(_selected_voz_povratak != null && isVozWithReservations("" + _selected_voz.getBrvoz()));
		
		if(_selected_voz != null) {
			broj_voza_polazak_value_lbl.setText("" + _selected_voz.getBrvoz());
			datum_polaska_value_lbl.setText(_selected_voz.getDatumPolaska());
			datum_izmena_value_lbl.setText(_selected_voz.getDatumPolaska());
			vreme_polazak_value_lbl.setText(_selected_voz.getVremep());
			vreme_izmena_value_lbl.setText(_selected_voz.getVremep());
			vreme_polazak_dolazak_value_lbl.setText(_selected_voz.getVremed());
			datum_polazak_dolazak_value_lbl.setText(_selected_voz.getDatum_dolaska());
			tip_karte_polazak_value_lbl.setText(resources.getString("redovna_cena"));
			tip_karte_izmena_value_lbl.setText(resources.getString("redovna_cena"));
			//ukupno_cena_value_lbl.setText("" + (Integer.parseInt(broj_putnika_lbl.getText()) * _selected_voz.getCenau()));
			
			if(smer_value_lbl.getText().equals(resources.getString("povratna"))) {
				
				
//				broj_voza_dolazak_lbl.setVisible(_selected_voz_povratak != null);
//				broj_voza_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				relacija_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				odrediste_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_separator1_lbl.setVisible(_selected_voz_povratak != null);
//				vreme_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_dolazak_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				relacija_dolazak_polaziste_lbl.setVisible(_selected_voz_povratak != null);
//				relacija_dolazak_odrediste_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_polazak_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_dolazak_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_polazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_separator1_lbl.setVisible(_selected_voz_povratak != null);
//				datum_povratka_separator2_lbl.setVisible(_selected_voz_povratak != null);
//				vreme_dolazak_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);
//				razred_dolazak_lbl.setVisible(_selected_voz_povratak != null);
//				razred_odlazak_value_lbl.setVisible(_selected_voz_povratak != null);
//
//				tip_karte_dolazak_lbl.setVisible(_selected_voz_povratak != null);
//				tip_karte_dolazak_value_lbl.setVisible(_selected_voz_povratak != null);

				


				if(_selected_voz_povratak != null) {
					if(_selected_voz_povratak.getRelkm() <= 100/*daljina u kilometrima*/) {
						vaznost_value_lbl.setText(resources.getString("na_dan_kupovine") );
					}else {
						vaznost_value_lbl.setText( resources.getString("narednih_petnaest_dana") );
					}
					relacija_dolazak_value_lbl.setText(odrediste_value_lbl.getText());
					odrediste_dolazak_value_lbl.setText(relacija_polaza_value_lbl.getText());
					broj_voza_dolazak_value_lbl.setText("" + _selected_voz_povratak.getBrvoz());
					vreme_dolazak_value_lbl.setText(_selected_voz_povratak.getVremep());
					vreme_povratka_izmena_value_lbl.setText(_selected_voz_povratak.getVremep());
					vreme_dolazak_dolazak_value_lbl.setText(_selected_voz_povratak.getVremed());
					datum_povratka_dolazak_value_lbl.setText(_selected_voz_povratak.getDatumPolaska());
					datum_povratka_polazak_value_lbl.setText(_selected_voz_povratak.getDatum_dolaska());
					datum_povratka_izmena_value_lbl.setText(_selected_voz_povratak.getDatum_dolaska());
					tip_karte_polazak_value_lbl.setText(resources.getString("povratna"));
					tip_karte_izmena_value_lbl.setText(resources.getString("povratna"));
				}
			}
		}
		if(!odrediste_value_lbl.getText().equals("")) {
			odrediste_pn.setVisible(false);
			novi_izmeni_pn.setVisible(true);
//			restartSessionExpiration();
		}
		
		datum_povratka_izmena_pn.setVisible(_selected_voz_povratak != null && isVozWithReservations("" + _selected_voz.getBrvoz()));



	}
	
	private void bindWithTastatura(TextField tf, int y_offset, List<Button> tip_karte_button_group, int tip_karte_ID) {
		if(tastatura_pn.isVisible()){
			tastatura_pn.setVisible(false);
		}
		_currentBindTastTF = tf;
		_current_tip_karte_button_group = tip_karte_button_group;
		_current_bind_tip_karte = tip_karte_ID;
//		tastatura_pn.setLayoutX(_currentBindTastTF.getLayoutX());
//		tastatura_pn.setLayoutY(_currentBindTastTF.getLayoutY() + y_offset);
		tastatura_pn.setVisible(true);
		tastatura_pn.toFront();
	}
	
	
	private void run_error(String prvi_red, String drugi_red, String treci_red, 
			String cetvrti_red,String peti_red,String sesti_red,String sedmi_red,  int number_of_seconds) {
		run_message("GREŠKA:", prvi_red, drugi_red, treci_red, cetvrti_red, peti_red, 
				sesti_red, sedmi_red, number_of_seconds);
	}
	
	private void run_info(String prvi_red, String drugi_red, String treci_red, 
			String cetvrti_red,String peti_red,String sesti_red,String sedmi_red, int number_of_seconds) {
		run_message("INFO:", prvi_red, drugi_red, treci_red, cetvrti_red, peti_red, 
				sesti_red, sedmi_red, number_of_seconds);
	}
	
	private void run_message(String naslov, String prvi_red, String drugi_red, String treci_red, 
			String cetvrti_red,String peti_red,String sesti_red,String sedmi_red, int number_of_seconds) {
		error_lbl_1.setText(naslov);
		error_lbl_2.setText(prvi_red);
		error_lbl_3.setText(drugi_red);
		error_lbl_4.setText(treci_red);
		error_lbl_5.setText(cetvrti_red);
		error_lbl_6.setText(peti_red);
		error_lbl_7.setText(sesti_red);
		error_lbl_8.setText(sedmi_red);
		if(prvi_red.equals("")) {
			
		}else if(drugi_red.equals("")) {
			error_pn.setPrefHeight(110);
		}else if(treci_red.equals("")) {
			error_pn.setPrefHeight(150);
		}else if(cetvrti_red.equals("")) {
			error_pn.setPrefHeight(190);
		}else if(peti_red.equals("")) {
			error_pn.setPrefHeight(230);
		}else if(sesti_red.equals("")) {
			error_pn.setPrefHeight(270);
		}else if(sedmi_red.equals("")) {
			error_pn.setPrefHeight(310);
		}
		
		Platform.runLater(() -> {       	
			error_pn.setVisible(true);
            Thread thread = new Thread(() -> {
                try {
                    // Wait for 6 secs
                    Thread.sleep(number_of_seconds * 1000);

                } catch (Exception exp) {
                    exp.printStackTrace();
                } finally {
                	error_pn.setVisible(false);
                }
            });
            thread.setDaemon(true);
            thread.start();
    				
        });
	}
	
	
//	private void printSlip() {
//		KartaBean karta = new KartaBean();
//		karta.set_broj_putnika(broj_putnika_lbl.getText());
//		karta.set_gde_kupljena(_kartomat.getNaziV_STANICE());
//		karta.set_id_karte("12345 678 12141");
//		if(_selected_voz_povratak != null) {
//			karta.set_povlastice("20%");
//			karta.set_povlastice_osnov("POVRATNA");
//			karta.set_povratna_rang(/*_selected_voz_povratak.getRang()*/"REGIO");
//			karta.set_povratna_voz_id("" + _selected_voz_povratak.getIdvoz());
//			karta.setRAZREDR(razred_odlazak_value_lbl.getText());
//			karta.setSMER("POVRATNA");
//		}else {
//			karta.set_povlastice("0%");
//			karta.set_povlastice_osnov("NEMA");
//			karta.setSMER(resources.getString("smer_u_jednom_btn"));
//		}
//		karta.set_rang(/*_selected_voz.getRang()*/"REGIO");
//		Date now = new Date();
//		
//		SimpleDateFormat dan_mesec_godina = new SimpleDateFormat("dd.MM.yyyy");
//		karta.set_vreme_kupovine(sat_min.format(now));
//		karta.setDATUM_KUPOVINE(dan_mesec_godina.format(now));
//		karta.set_naziv_stanice_od(relacija_polaza_value_lbl.getText());
//		karta.set_naziv_stanice_do(odrediste_value_lbl.getText());
//		karta.setRAZREDA(razred_polazak_value_lbl.getText());
//		karta.set_ukupna_cena(ukupno_cena_value_lbl.getText());
//		try {
//			now = dan_mesec_godina.parse(datum_polaska_value_lbl.getText());
//		}catch(Exception e) {
//			
//		}
//		Calendar dat_pol_cal = Calendar.getInstance();
//		dat_pol_cal.setTime(now);
//		dat_pol_cal.add(Calendar.DAY_OF_YEAR, 14);
//		
//		
//		karta.setVAZI_OD(datum_polaska_value_lbl.getText());
//		karta.setVAZI_DO(dan_mesec_godina.format(dat_pol_cal.getTime()));
//		
//		//new PrinterService().printKartu(karta);
//		try {
//		new PrinterService().printKartu(_kartomat.getNaziV_STANICE().toUpperCase(), _ticket_ID, karta.getDATUM_KUPOVINE(), 
//				karta.get_vreme_kupovine(), "" + karta.getVOZA(), ""/*a_voz_br_sedista*/, karta.get_povratna_voz_id(), 
//				""/*b_voz_br_sedista*/, karta.get_naziv_stanice_od(), 
//				_selected_voz_povratak != null ? karta.get_naziv_stanice_do() : "" /*b_stanica_od*/,
//				karta.get_naziv_stanice_do(), 
//				_selected_voz_povratak != null ? karta.get_naziv_stanice_od() : ""  /*b_stanica_do*/, 
//						""/*a_via*/, ""/*b_via*/, karta.getVAZI_OD(), karta.get_rang(), karta.getRAZREDA(), karta.getVAZI_DO(), 
//						karta.get_povratna_rang(), karta.getRAZREDR(), "" + karta.get_broj_putnika(), 
//						karta.get_povlastice(), karta.get_povlastice_osnov(), karta.get_ukupna_cena());
//		}catch(Exception e) {
//			//TODO - rollback transaction and payment
//			e.printStackTrace();
//			System.out.println("Exception when try to print ticket, details: " +
//			e.getMessage());
//		}
//						
//	}
	
	
	
	private void resetForNewSession() {

		

		datum_polaska_value_lbl.textProperty().removeListener(datum_polaska_changeListener);
		datum_povratka_polazak_value_lbl.textProperty().removeListener(datum_povratka_polazak_changeListener);
		razred_polazak_value_lbl.textProperty().removeListener(rezred_polazak_changeListener);
//		razred_odlazak_value_lbl.textProperty().removeListener(rezred_odlazak_changeListener);
		smer_value_lbl.textProperty().removeListener(smer_changeListener);
		tip_karte_polazak_value_lbl.textProperty().removeListener(tip_karte_changeListener);
		
		setSerbian();
		
		_prvi_putnik_legitimacija = null;
		_drugi_putnik_legitimacija = null;
		_treci_putnik_legitimacija = null;
		_cetvrti_putnik_legitimacija = null;
		_peti_putnik_legitimacija = null;
		
		_prvi_putnik_povlastica = null;
		_drugi_putnik_povlastica = null;
		_treci_putnik_povlastica = null;
		_cetvrti_putnik_povlastica = null;
		_peti_putnik_povlastica = null;
		
		_prvi_putnik_cena = null;
		_drugi_putnik_cena = null;
		_treci_putnik_cena = null;
		_cetvrti_putnik_cena = null;
		_peti_putnik_cena = null;
		
		_prvi_putnik_povratna_cena = null;
		_drugi_putnik_povratna_cena = null;
		_treci_putnik_povratna_cena = null;
		_cetvrti_putnik_povratna_cena = null;
		_peti_putnik_povratna_cena = null;
		
		



		
		//first button groups
		resetButtonGroup(_stanica_button_group,true);
		resetButtonGroup(_smer_button_group,false);
		resetButtonGroup(_razred_polazak_button_group,false);
		resetButtonGroup(_razred_dolazak_button_group,false);
		resetButtonGroup(_broj_putnika_button_group,false);
		resetButtonGroup(_polazak_button_group,false);
		resetButtonGroup(_prva_tip_karte_button_group,false);
		resetButtonGroup(_druga_tip_karte_button_group,false);
		resetButtonGroup(_treca_tip_karte_button_group,false);
		resetButtonGroup(_cetvrta_tip_karte_button_group,false);
		resetButtonGroup(_peta_tip_karte_button_group,false);
		resetCalendarButtonGroup(-1);
		if(_current_tip_karte_button_group != null) {
			resetButtonGroup(_current_tip_karte_button_group,false);
		}
		
		_selected_voz = null;
		_selected_voz_povratak = null;
		_svi_polasci = null;
		_svi_povratci = null;

		_svi_povratci_filtered_rezervacije.clear();
		_svi_povratci_filtered_bez_rezervacije.clear();
		
		
		
		

		broj_putnika_lbl.setText("" + DEFAULT_BROJ_PUTNIKA);
		odrediste_value_lbl.setText("");
		odrediste_izmena_value_lbl.setText("");
		datum_polaska_value_lbl.setText("");
		datum_izmena_value_lbl.setText("");
		razred_polazak_value_lbl.setText(resources.getString("drugi_razred"));
		razred_odlazak_value_lbl.setText(resources.getString("drugi_razred"));
		smer_value_lbl.setText("");
		relacija_polaza_value_lbl.setText("");
		broj_voza_polazak_value_lbl.setText("");
		odrediste_dolazak_value_lbl.setText("");
		vreme_polazak_value_lbl.setText("");
		vreme_izmena_value_lbl.setText("");
		datum_polazak_dolazak_value_lbl.setText("");
		tip_karte_polazak_value_lbl.setText(resources.getString("redovna_cena"));
		tip_karte_izmena_value_lbl.setText(resources.getString("redovna_cena"));
		ukupno_cena_value_lbl.setText("0.0");
		broj_voza_dolazak_value_lbl.setText("");
		vreme_dolazak_value_lbl.setText("");
		vreme_povratka_izmena_value_lbl.setText("");
		tip_karte_dolazak_value_lbl.setText(resources.getString("redovna_cena"));
		relacija_dolazak_value_lbl.setText("");
		vreme_dolazak_dolazak_value_lbl.setText("");
		datum_povratka_polazak_value_lbl.setText("");
		datum_povratka_izmena_value_lbl.setText("");
		datum_povratka_dolazak_value_lbl.setText("");

		
		ukupna_cena_value_lbl.setText("");

		error_pn.setPrefHeight(340.0);
		error_pn.setPrefWidth(510.0);
		error_lbl_1.setText("");
		error_lbl_2.setText("");
		error_lbl_3.setText("");
		error_lbl_4.setText("");
		error_lbl_5.setText("");
		error_lbl_6.setText("");
		error_lbl_7.setText("");
		error_lbl_8.setText("");


		_selected_razred_polazak = DEFAULT_RAZRED;
		_selected_razred_povratak = DEFAULT_RAZRED;

		
		_lista_svih_stanica_current_position = 0;
		
		_lista_svih_polazaka_current_position = 0;
		
		_lista_svih_povrataka_current_position = 0;
		

		
		 _prva_karta_tip = TIP_REDOVNA_CENA;
		_druga_karta_tip = TIP_REDOVNA_CENA;
		_treca_karta_tip = TIP_REDOVNA_CENA;
		_cetvrta_karta_tip = TIP_REDOVNA_CENA;
		_peta_karta_tip = TIP_REDOVNA_CENA;
		
		_prva_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_druga_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_treca_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_cetvrta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		_peta_karta_tip_not_confirmed = TIP_REDOVNA_CENA;
		
		
		resetTipKarteButtonGroup(_prva_tip_karte_button_group, _prva_karta_tip);
		resetTipKarteButtonGroup(_druga_tip_karte_button_group, _druga_karta_tip);
		resetTipKarteButtonGroup(_treca_tip_karte_button_group, _treca_karta_tip);
		resetTipKarteButtonGroup(_cetvrta_tip_karte_button_group, _cetvrta_karta_tip);
		resetTipKarteButtonGroup(_peta_tip_karte_button_group, _peta_karta_tip);
		
		
		tip_karte_izmena_value_lbl.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		prvi_tip_redovna_btn.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		drugi_tip_redovna_btn.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		treci_tip_redovna_btn.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		cetvrti_tip_redovna_btn.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		peti_tip_redovna_btn.setText(resources.getString("tip_karte_dolazak_value_lbl"));
		
		
//		_popusti_jedan_smer = new HashMap<Integer, PovlasticaBean>();
//		_popusti_povratna =  new HashMap<Integer, PovlasticaBean>();
		

		_current_tip_karte_button_group = null;
		_currentBindTastTF = null;	
		_current_bind_tip_karte = -1;
		
		
		Calendar today = Calendar.getInstance();
		datum_polaska_value_lbl.setText(_sdf.format(today.getTime()));
		datum_izmena_value_lbl.setText(_sdf.format(today.getTime()));
		datum_povratka_polazak_value_lbl.setText(_sdf.format(today.getTime()));
		datum_povratka_izmena_value_lbl.setText(_sdf.format(today.getTime()));
		
		handle_1_btn();
		handle_razred_drugi_polazak();
		handle_smer(false);
		
		handlePONISTI();
//		kupi_kartu_opcije_btn.setVisible(false);
		

		

		
		handle_ostale_napred_pagination();	
//		stanica_lbl = new Label("Stanica : " + _kartomat.getNaziV_STANICE());
			
		


		
    	novi_izmeni_pn.setVisible(false);
    	placanje_pn.setVisible(false);
    	error_pn.setVisible(false);
    	tastatura_pn.setVisible(false);
    	tip_karte_pn.setVisible(false);
//    	polasci_nova_pn.setVisible(false);
//    	calendar_pn.setVisible(false);
    	ostale_stanice_pn.setVisible(false);

    	help_1_pn.setVisible(false);
    	help_2_pn.setVisible(false);
    	help_3_pn.setVisible(false);
    	placanje_result_pn.setVisible(false);
    	polasci_calendar_pn.setVisible(false);
    	
    	odrediste_pn.setVisible(true);
		

    	
    	prvi_tip_tf.setText("");
    	drugi_tip_tf.setText("");
    	treci_tip_tf.setText("");
    	cetvrti_tip_tf.setText("");
    	peti_tip_tf.setText("");
    	id_povlastice_value_lbl.setText("");
    	
		message_lbl_1.setText("");
		message_lbl_2.setText("");
		message_lbl_3.setText("");
		message_lbl_4.setText("");
		message_lbl_5.setText("");
		
		_polazak_true_povratak_false = true;
    	
		datum_polaska_value_lbl.textProperty().addListener(datum_polaska_changeListener);
		datum_povratka_polazak_value_lbl.textProperty().addListener(datum_povratka_polazak_changeListener);
		razred_polazak_value_lbl.textProperty().addListener(rezred_polazak_changeListener);
//		razred_odlazak_value_lbl.textProperty().addListener(rezred_odlazak_changeListener);
		smer_value_lbl.textProperty().addListener(smer_changeListener);
		tip_karte_polazak_value_lbl.textProperty().addListener(tip_karte_changeListener);
		
	}
	
	private String formirajDatumVreme(String datum, String vreme) {
		String[] datum_splt = datum.split("\\.");
		
		if(datum_splt.length < 3) {
			SimpleDateFormat sdf_send = new SimpleDateFormat("yyyy-MM-dd'T'");
			return sdf_send.format(new Date()) + vreme + ":00.727Z";
		}else {
			return datum_splt[2] +"-"+datum_splt[1] +"-"+datum_splt[0] +"T"+ vreme + ":00.727Z";
		}
		
	}
	
	
	private int getSifraPovlastice(int tip_karte) {

		int smer = _selected_voz_povratak == null ? 1: 2;
		switch(tip_karte) {
			case TIP_REDOVNA_CENA: return smer == 1 ? 1 : 20;
			case TIP_SRB_K13: return 37;
			case TIP_RAIL_K30: return 38;
			case TIP_DETE: return 50;
			case TIP_PAS: return 54;
			case TIP_POVRATNA: return 20;
				default: return 1;
		}

	}
	
	
//	private void platu_kartu_sequence_threaded() {
//		//Platform.runLater(() -> { 
//		
//		try {
//			//throws exception if printer is not in proper status or no paper
//			PowerShellPrinterStatus.checkPrinterStatus();
//
//			placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("prisloni_karticu_za_placanje_gif")+"')");
//			placanje_result_pn.setVisible(true);
//			placanje_result_pn.toFront();
//
//
//			//});
//			
//			
//			Task<Void> task = new Task<Void>() {
//				@Override
//				protected Void call() throws Exception {
//
//					Platform.runLater(() -> {
//
//						plati_kartu_sequence();
//
//					});
//
//
//					return null;
//				}
//			};
//
//			Thread thread = new Thread(task);
//			thread.setDaemon(true);
//			thread.start();
//
////			Thread thread2 = new Thread(() -> {
////				try {
////					Thread.sleep(1000);
////				}catch(Exception e) {}
////				plati_kartu_sequence();
////			});
////			thread2.setDaemon(true);
////			thread2.start();
//		}catch(Exception e) {
//
//
//			error_pn.setVisible(true);
//			error_pn.toFront();
//			run_error("GREŠKA NA ŠTAMPAČU", e.getMessage(), "POZOVITE OSOBLJE STANICE", "",  "", "", "", 10);
//
//			Thread thread3 = new Thread(() -> {
//				//				try {
//				//					Thread.sleep(1000);
//				//				}catch(Exception e) {}
//
//				try {
//					Thread.sleep(10000);
//
//				}catch(Exception ee) {}
//				finally {
//					placanje_pn.setVisible(false);
//					odrediste_pn.setVisible(true);
//					stop_recycling_session();
//				}
//			});
//
//
//
//			
//
//			thread3.setDaemon(true);
//			thread3.start();
//
//			
//		}
//
//
//		
//		
//	}
	

	
//	private void plati_kartu_sequence() {
//		System.out.println("--> plati_kartu_sequence");
//		
//		
//
//		Platform.runLater(() -> { 
//
//			_error_message = "";
//			int user_id = _kartomat.getiD_USER();
//			String datum_kupovine = _sdf_stampa.format(new Date());
//			//TODO pitati za ID RELACIJE
//			int ID_RELACIJE = _selected_voz.getIdrel();
//			int stanica_od = _selected_voz.getOdsifra();
//			int stanica_do = _selected_voz.getDosifra();
//			int smer = _selected_voz_povratak == null ? 1: 2;
//			String via = "";
//			//String vazi_od = datum_kupovine;
//			String vazi_do = _selected_voz.getDatumPolaska().replace(".", "-");
//			String vazi_od = vazi_do;
//			int voz_a = _selected_voz.getBrvoz();
//			int razred_a = _selected_razred_polazak;
//			String vreme_polaska_a = _selected_voz.getVremep();
//			String vreme_dolaska_a = _selected_voz.getVremed();
//			String voz_r = _selected_voz_povratak == null ? null : "" + _selected_voz_povratak.getBrvoz();
//			String razred_r =  _selected_voz_povratak ==  null ? null : "" + _selected_razred_povratak;
//			String vreme_polaska_r = _selected_voz_povratak ==  null ? null : _selected_voz_povratak.getVremep();
//			String vreme_dolaska_r = _selected_voz_povratak ==  null ? null : _selected_voz_povratak.getVremed();
//			int broj_putnika = Integer.parseInt( broj_putnika_lbl.getText() );
//			double ukupna_cena = Double.parseDouble(ukupna_cena_value_lbl.getText());
//
//			EtKartaBean et_karta_data = new EtKartaBean();
//			et_karta_data.setUkupnacena(ukupna_cena);
//			et_karta_data.setSmer(smer);
//
//			VozDataBean voz_data = new VozDataBean();
//			voz_data.setVoz(_selected_voz);
//			voz_data.setVozp(_selected_voz_povratak);
//			voz_data.setBrojputnika(broj_putnika);
//			voz_data.setCenauk((int)ukupna_cena);	
//
//			voz_data.setDatum1(formirajDatumVreme(_selected_voz.getDatumPolaska(), vreme_polaska_a));
//			voz_data.setDatum2(formirajDatumVreme(_selected_voz.getDatumPolaska(), vreme_dolaska_a));
//			System.out.println("datum1 = " + voz_data.getDatum1());
//			System.out.println("datum2 = " + voz_data.getDatum2());
////			voz_data.setDatum1("2021-12-14T08:08:14.727Z");
////			voz_data.setDatum2("2021-12-14T08:08:56.727Z");
//			voz_data.setRazred(razred_a);
//			voz_data.setRazredp(razred_a);	
//			et_karta_data.setVozData(voz_data);
//
//			SifraBean sifraOD = new SifraBean();
//			sifraOD.setNaziv(_selected_voz.getNazivod());
//			sifraOD.setSifra(_selected_voz.getOdsifra());
//			et_karta_data.setSifraod(sifraOD);
//
//			SifraBean sifraDO = new SifraBean();
//			sifraDO.setNaziv(_selected_voz.getNazivdo());
//			sifraDO.setSifra(_selected_voz.getDosifra());
//			
//			
//			List<String> uneseneleg = new ArrayList<String>();
//			uneseneleg.add("");
//			et_karta_data.setUneseneleg(uneseneleg);
//
//			List<CenaBean> cena1 = new ArrayList<CenaBean>();
//
//
//			
//
//			if(smer == 2) {
//				et_karta_data.setSifrado(sifraDO);
//				//povratna
//				List<CenaBean> cena2 = new ArrayList<CenaBean>();
//				if(_prvi_putnik_povratna_cena != null) {
//					cena2.add(_prvi_putnik_povratna_cena);
//				}
//				if(_drugi_putnik_povratna_cena != null) {
//					cena2.add(_drugi_putnik_povratna_cena);
//				}
//				if(_treci_putnik_cena != null) {
//					cena2.add(_treci_putnik_povratna_cena);
//				}
//				if(_cetvrti_putnik_povratna_cena != null) {
//					cena2.add(_cetvrti_putnik_povratna_cena);
//				}
//				if(_peti_putnik_povratna_cena != null) {
//					cena2.add(_peti_putnik_povratna_cena);
//				}
//				et_karta_data.setCenasmer2(cena2);
//			}else {
//				if(_prvi_putnik_cena != null) {
//					cena1.add(_prvi_putnik_cena);
//				}
//				if(_drugi_putnik_cena != null) {
//					cena1.add(_drugi_putnik_cena);
//				}
//				if(_treci_putnik_cena != null) {
//					cena1.add(_treci_putnik_cena);
//				}
//				if(_cetvrti_putnik_cena != null) {
//					cena1.add(_cetvrti_putnik_cena);
//				}
//				if(_peti_putnik_cena != null) {
//					cena1.add(_peti_putnik_cena);
//				}
//				
//			}
//			
//			et_karta_data.setCenasmer1(cena1);
//
//			///////////////////////////putnici /////////////////////////////////////////
//
//			List putnici =  smer == 1 ? new ArrayList<PutniciBean>() : new ArrayList<PutniciBeanPovratna>();
//			
//			PutnikIface putnik1 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
//			putnik1.setImeprezime("" + _kartomat.getiD_TERMINALA());
//			putnik1.setDatumrodjenja("2022.03.14");
//			putnik1.setIdleg("");
//
//			putnik1.setSifrapovlastice(getSifraPovlastice(_prva_karta_tip));
//			
//			List<CenaBean> cena_put_1 = new ArrayList<CenaBean>();
//			cena_put_1.add( smer == 2 ? _prvi_putnik_povratna_cena : _prvi_putnik_cena);
//			putnik1.setCenaa(cena_put_1);
//			putnici.add(putnik1);
//			if(broj_putnika > 1) {
//				PutnikIface putnik2 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
//				putnik2.setImeprezime("" + _kartomat.getiD_TERMINALA());
//				putnik2.setDatumrodjenja("2022.03.14");
//				putnik2.setIdleg("");
//				putnik2.setSifrapovlastice(getSifraPovlastice(_druga_karta_tip));
//				List<CenaBean> cena_put_2 = new ArrayList<CenaBean>();
//				cena_put_2.add( smer == 2 ? _drugi_putnik_povratna_cena : _drugi_putnik_cena);
//				putnik2.setCenaa(cena_put_2);
//				putnici.add(putnik2);
//			}
//
//			if(broj_putnika > 2) {
//				PutnikIface putnik3 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
//				putnik3.setImeprezime("" + _kartomat.getiD_TERMINALA());
//				putnik3.setDatumrodjenja("2022.03.14");
//				putnik3.setIdleg("");
//
//				putnik3.setSifrapovlastice(getSifraPovlastice(_treca_karta_tip));
//				List<CenaBean> cena_put_3 = new ArrayList<CenaBean>();
//				cena_put_3.add( smer == 2 ? _treci_putnik_povratna_cena : _treci_putnik_cena);
//				putnik3.setCenaa(cena_put_3);
//				putnici.add(putnik3);
//			}
//
//			if(broj_putnika > 3) {
//				PutnikIface putnik4 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
//				putnik4.setImeprezime("" + _kartomat.getiD_TERMINALA());
//				putnik4.setDatumrodjenja("2022.03.14");
//				putnik4.setIdleg("");
//				putnik4.setSifrapovlastice(getSifraPovlastice(_cetvrta_karta_tip));
//				List<CenaBean> cena_put_4 = new ArrayList<CenaBean>();
//				cena_put_4.add( smer == 2 ? _cetvrti_putnik_povratna_cena : _cetvrti_putnik_cena);
//				putnik4.setCenaa(cena_put_4);
//				putnici.add(putnik4);
//			}
//
//			if(broj_putnika > 4) {
//				PutnikIface putnik5 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
//				putnik5.setImeprezime("" + _kartomat.getiD_TERMINALA());
//				putnik5.setDatumrodjenja("2022.03.14");
//				putnik5.setIdleg("");
//				putnik5.setSifrapovlastice(getSifraPovlastice(_peta_karta_tip));
//				List<CenaBean> cena_put_5 = new ArrayList<CenaBean>();
//				cena_put_5.add( smer == 2 ? _peti_putnik_povratna_cena : _peti_putnik_cena);
//				putnik5.setCenaa(cena_put_5);
//				putnici.add(putnik5);
//			}
//			et_karta_data.setPutnici(putnici);
//			////////////////////////end putnici/////////////////////////////////////////
//			PayTenPaymentIface payment_iface = null;
//
//			try {
//
//
//				ETKartaResponseBean et_response = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).upisET_ORDER(user_id, datum_kupovine, ID_RELACIJE, stanica_od, stanica_do,
//						smer, via, vazi_od, vazi_do, voz_a, razred_a, vreme_polaska_a, vreme_dolaska_a,
//						voz_r, razred_r, vreme_polaska_r, vreme_dolaska_r, broj_putnika, ukupna_cena, 
//						et_karta_data);
//
//				//			ETKartaResponseBean et_response = new ETKartaResponseBean();
//				//			et_response.setAmount("" + ukupna_cena );
//				//			et_response.setOid("SVKOID0000031");
//				//			et_response.setClientid("13IN060595");
//
//				//placanje preko paytem-a
//
//				payment_iface = PaymentFactory.getPaymentInterface(PAYMENT_IP_ADDRESS, PAYMENT_PORT);
//
//
//				TransactionDataResponse tdr = payment_iface.payForTicket(
//						Double.parseDouble(et_response.getAmount()), et_response.get_order_id(), et_response.get_merchant_id());
//
//				EtKartaResponse et_karta = SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).upisUPIT_KA_BANCI(tdr.is_pozitivan(), user_id, et_response.get_order_id(), 
//						Double.parseDouble(et_response.getAmount()), 
//						tdr);
//				System.out.println("odgovor placanja je pozitivan : " + tdr.is_pozitivan());
//				if(tdr.is_pozitivan()){
//					//onda cheers
//					PrinterService printerService = new PrinterService();
//					int i = 0;
//					for(KartaStampaBean current : et_karta.getKarte()) {
//						System.out.println("printam : " + ++i + "kartu");
//						printerService.printKartu(_kartomat.getNaziV_STANICE(), "" + current.getBROJ_KARTE(), 
//								current.getDATUM_KUPOVINE(), 
//								sat_min.format(new Date()), current.getVOZA(), "", current.getVOZR(), "", 
//								current.getNAZIV_STANICE_ODA(), current.getNAZIV_STANICE_DOA(), current.getNAZIV_STANICE_DOA(), 
//								current.getNAZIV_STANICE_DOR(), current.getVIA(), current.getVIAR(), current.getVAZI_OD(),
//								_selected_voz.getRangNaziv(), current.getRAZREDA(), current.getVAZI_DO(), 
//								_selected_voz_povratak != null ? _selected_voz_povratak.getRangNaziv() : "",
//										current.getRAZREDR(), "1", "", current.getPOVLASTICA_NAZIV(), "" + current.getCENA(),
//										_selected_voz_povratak != null, current.getREZERVACIJA_A(), current.getREZERVACIJA_R(),
//										current.getVREME_POLASKAA(), current.getVREME_POLASKAR());
//						try {
//						SrbijaVozIfaceFactory.getIface(SV_API_URL, SV_API_CONN_TIME, SV_API_READ_TIME).upisKARTA_ODSTAMPANA("" + user_id, "" + current.getREDNI_BROJ());
//						}catch(Throwable e) {
//							System.out.println("Unknown exception when try to UPIS KARTA ODSTAMPONA call prema Srbija Voz API, details: " + 
//									e.getMessage());
//						}
//					}
//					uspesna_kupovina = true;
////					String datum, String vreme, String iznos, String tID, String mID, String companyName, String terminalId,
////		    		String cardNumber, String expirationData, String kartomat_ime, String kartomat_grad, String authorizationCode, String transactionNumber
//					printerService.printCreditCardSlip(tdr.get_transactionDate(), tdr.get_transactionTime()/*sat_min.format(new Date())*/,
//							 et_response.getAmount(), tdr.get_tidNumber(),tdr.get_midNumber(), tdr.get_companyName(), "" + _kartomat.getiD_TERMINALA(),
//							 tdr.get_cardNumber(), tdr.get_expirationData(), _kartomat.getNaziV_STANICE(), KARTOMAT_GRAD, 
//							 tdr.get_authorizationCode(), tdr.get_transactionNumber());
//				}else {
//					uspesna_kupovina = false;
//					//onda poruka o gresci
//					//payment_iface.cancelCurrentPayment(tdr.get_identifier());
//				}
//
//			}catch(CommunicationException ce) {
//				//TODO pozvati panel sa greskom - pokusajte ponovo
//				ce.printStackTrace();
//				System.out.println("CommunicationException when try to purchase a ticket, details: " + ce.getMessage()); 
//				_error_message = ce.getMessage();
//			}catch(PaymentException pe) {
//				//TODO pozvati panel sa greskom - pokusajte ponovo
//				pe.printStackTrace();
//				System.out.println("PaymentException when try to purchase a ticket, details: " + pe.getMessage());
//			}catch(Throwable e) {
//				//TODO pozvati panel sa greskom - pokusajte ponovo
//				//ovde rollback payment-a
//				e.printStackTrace();
//				System.out.println("Unknown Exception when try to purchase a ticket, details: " + e.getMessage());
//			}finally {
//				if(payment_iface != null)payment_iface.close();
//
//				
//
//				Platform.runLater(() -> {       	
//
//
//					Thread thread = new Thread(() -> {
//						try {
//							//only for testing try{Thread.sleep(5000);}catch(Exception e) {}
//							if(uspesna_kupovina) {
//								//showSlanjeZahteva("USPEŠNO plaćanje","Hvala što koristite kartomat");
//								placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("karticq_za_placanje_uspesna_gif")+"')");
//								message_lbl_1.setText("");
//								message_lbl_2.setText("");
//								message_lbl_3.setText("");
//								message_lbl_4.setText("");
//								message_lbl_5.setText("");
//							}else {
//								//showSlanjeZahteva("NEUSPEŠNO plaćanje","Hvala što koristite kartomat");
//								placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("karticq_za_placanje_neuspesna_gif")+"')");
//								String jednina_mnozina = broj_putnika > 1 ? "karata" : "karte";
//								message_lbl_1.setText("Vaša kupovina "+jednina_mnozina+" je NEUSPEŠNA");
//								if(_error_message.equals("")) {
//									message_lbl_2.setText("Plaćanje vašom kreditnom karticom je ODBIJENO");
//									message_lbl_3.setText("Hvala što koristite usluge kartomata Srbija Voza");
//									message_lbl_4.setText("");
//									message_lbl_5.setText("");
//								}else {
//									message_lbl_2.setText("Plaćanje vašom kreditnom karticom je ODBIJENO");
//									message_lbl_3.setText(_error_message);
//									message_lbl_4.setText("");
//									message_lbl_5.setText("");
//								}
//							}
//							Thread.sleep(10000);
//							placanje_pn.setVisible(false);
//							odrediste_pn.setVisible(true);
//							Platform.runLater(() -> { 
////								closeSession();
//								stop_recycling_session();
//							});
//
//						} catch (Exception exp) {
//							exp.printStackTrace();
//						} finally {
//							placanje_result_pn.setVisible(false);
//						}
//					});
//					thread.setDaemon(true);
//					thread.start();
//
//				});
//
//
//			}
//
//		});//end platform start
//			
//
//
//	}
	
	
	private void init_clock() {
		//final Label clock = new Label();
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler() {
		     


			@Override
			public void handle(Event arg0) {
		          Calendar cal = Calendar.getInstance();
		          
		          trenutno_vreme_value_lbl.setText(sat_min_ss.format(cal.getTime()));
		          trenutno_vreme_value_izmeni_lbl.setText(sat_min_ss.format(cal.getTime()));
		          trenutno_vreme_value_placanje_lbl.setText(sat_min_ss.format(cal.getTime()));
//		          if(cal.get(Calendar.SECOND)%2 == 0) {
//		        	  trenutno_vreme_value_lbl.setText(sat.format(cal.getTime()) + " : "+ min.format(cal.getTime()));
//		          }else {
//		        	  trenutno_vreme_value_lbl.setText(sat.format(cal.getTime()) + "   "+ min.format(cal.getTime()));
//		          }
		          
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	
	
	private void handleOsvezi() {
		System.out.println("ozvezi akcija");
		

		
		if(_session_timer == null) {
			_session_timer = new SessionTimer(this);
			_timer = new Timer();
			_timer.scheduleAtFixedRate(_session_timer, 0, 1000);
		}else {
			_session_timer.resetCounter();
		}
		
	}
	
	private void deactivateSessionTimer() {
		System.out.println("deactivateSessionTimer");
		
	}
	
	
	
	
	

	

	


}
