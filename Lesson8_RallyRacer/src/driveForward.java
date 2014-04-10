import lejos.nxt.MotorPort;


public class driveForward {
	
	public static void forward(int tacho, int power)
	{
		int initialTacho = MotorPort.B.getTachoCount();
		Car.forward(power, power);
		
		while(tacho > MotorPort.B.getTachoCount() - initialTacho)
		{}
	}

}
