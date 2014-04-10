import lejos.nxt.MotorPort;


public class driveBackward {

	public static void backward(int tacho, int powerLeft, int powerRight)
	{
		int initialTacho = MotorPort.B.getTachoCount() - tacho;
		Car.forward(-powerLeft, -powerRight);
		
		while(MotorPort.B.getTachoCount() > initialTacho)
		{}
	}
}
