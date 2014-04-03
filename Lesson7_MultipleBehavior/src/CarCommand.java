
public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP, SPIN, TURN_LIGHT_SENSOR_LEFT, TURN_LIGHT_SENSOR_RIGHT
	}
	public volatile Command command;
	public volatile int leftPower, rightPower, spinDelay, lightPower;

}
