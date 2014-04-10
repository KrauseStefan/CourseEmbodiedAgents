
public class CarCommand 
{
	public enum Command {
	    FORWARD, BACKWARD, STOP, SPIN, BACKSPIN, TURN_LIGHT_SENSOR_LEFT, TURN_LIGHT_SENSOR_RIGHT, STOP_LIGHT_SENSOR
	}
	public volatile Command command;
	public volatile int leftPower, rightPower, spinDelay, lightPower;
	public volatile String direction;
}
