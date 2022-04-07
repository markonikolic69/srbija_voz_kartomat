package application.util;

import java.util.TimerTask;

import application.SessionControlIface;

public class SessionTimer extends TimerTask{
	
	
	private SessionControlIface _session;
	private static int _current_counter_value = 0;
	
	private boolean _run_progress = false;
	
	public SessionTimer(SessionControlIface session){
		_session = session;
		resetCounter();
	}
	
	
	public void resetCounter() {
		_current_counter_value = _session.get_session_duration();
		_run_progress = true;
	}
	
	public void stopCounting() {
		_current_counter_value = 0;
		_run_progress = false;
	}
	

	    @Override
	    public void run() {
	    	//System.out.println("SessionTimer run() , _current_counter_value = " + _current_counter_value);
	    	if(_current_counter_value == 0) {
	    		if(_run_progress) {
	    			_session.stop_recycling_session();
	    			_current_counter_value = 0;
	    		}
	    		_run_progress = false;
	    		
	    		
	    	}else {
	    		if(_run_progress) {
	    		_session.update_recycling_session(_current_counter_value);
	    		_current_counter_value--;
	    		}
	    	}
	    	
	    }


}
