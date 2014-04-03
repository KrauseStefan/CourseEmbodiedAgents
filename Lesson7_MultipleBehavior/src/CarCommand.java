
public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP, SPIN
	}
	public volatile Command command;
	public volatile int leftPower, rightPower, spinDelay;

}
