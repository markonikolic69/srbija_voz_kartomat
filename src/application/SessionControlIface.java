package application;

public interface SessionControlIface {
	
	public void stop_recycling_session();
	
	//public void start_recycling_session();
	
	//public void reset_recycling_session();
	
	public void update_recycling_session(int broj_sekundi);
	
	//in seconds
	public int get_session_duration();

}
