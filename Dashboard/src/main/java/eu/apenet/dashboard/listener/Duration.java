package eu.apenet.dashboard.listener;

public class Duration {
	private static final long SECONDS = 1000l;
	private static final long MINUTES = SECONDS * 60;
	private static final long HOURS = MINUTES * 60;
	private long hours = 0;
	private long minutes = 0;
	private long seconds= 0;
	public Duration(long hours, long minutes, long seconds) {
		super();
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public long getMilliseconds(){
		return hours * HOURS + minutes * MINUTES + seconds * SECONDS;
	}
	
	
}
