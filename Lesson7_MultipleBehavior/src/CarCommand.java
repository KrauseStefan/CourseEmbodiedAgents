
public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP 
	}
	public volatile Command command;
	public volatile int leftPower, rightPower;

}
