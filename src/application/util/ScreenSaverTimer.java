package application.util;

import java.util.TimerTask;

import application.IScreenSaverCallback;

public class ScreenSaverTimer extends TimerTask{
	
	private IScreenSaverCallback _callback;
	
	
	public ScreenSaverTimer( IScreenSaverCallback callback) {
		_callback = callback;
	}
	
    @Override
    public void run() {
    	_callback.showScreenSaver();
    }

}
