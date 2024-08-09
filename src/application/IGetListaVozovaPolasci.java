package application;

import java.util.List;

import application.data.VozBean;

public interface IGetListaVozovaPolasci {
	
	public void setListaVozovaPolasciCallbackSuccess(List<VozBean> lista_vozova);
	
	public void setListaVozovaPolasciCallbackNOTSuccess(String error);

}
