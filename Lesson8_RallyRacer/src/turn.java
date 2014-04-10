import lejos.nxt.MotorPort;
import lejos.nxt.Sound;


public class turn 
{
	public static void run(int tacho, int power, BlackWhiteSensor sensorLeft)
	{
		Car.forward(-power, power);
		
		int initialTacho = MotorPort.B.getTachoCount();
		
		while(tacho + initialTacho > MotorPort.B.getTachoCount())
		{}
		while(sensorLeft.light() > sensorLeft.whiteLightValue-12)
		{}
	}
	
}
