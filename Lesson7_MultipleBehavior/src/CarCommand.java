
public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP, SPIN, BACKSPIN
	}
	public volatile Command command;
	public volatile int leftPower, rightPower, spinDelay;
	public volatile String direction;

}
