package application;

import java.util.List;

import application.data.VozBean;

public interface IGetListaVozovaPovratak {
	
	public void setListaVozovaPovratakCallbackSuccess(String datum_povratka, List<VozBean> lista_vozova);
	
	public void setListaVozovaPovratakCallbackNOTSuccess(String error);

}
