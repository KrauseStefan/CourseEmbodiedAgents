import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SoundSensor;


public class second {

	private final static int power = 80;
	
	public static void main (String[] aArg)
			throws Exception
			{

				
				BlackWhiteSensor sensorLeft = new BlackWhiteSensor(SensorPort.S2);
				//sensorLeft.calibrate();
				BlackWhiteSensor sensorRight = new BlackWhiteSensor(SensorPort.S1);
				sensorRight.blackLightValue = sensorLeft.blackLightValue = 467; 
				sensorRight.whiteLightValue = sensorLeft.whiteLightValue = 584; 
				sensorRight.blackWhiteThreshold = sensorLeft.blackWhiteThreshold = 525; 

				
				LCD.clear();
				Thread.sleep(1000);
				
				
				pdLineFolSecond lineFol = new pdLineFolSecond(sensorLeft, sensorRight);
				
				int power = 100;
				Thread.sleep(1000);
				
				while(true)
				{
					while(!Button.ENTER.isPressed())
					{
						while(Button.LEFT.isPressed())
						{
							power--;
							Thread.sleep(100);
						}
						while(Button.RIGHT.isPressed())
						{
							power++;
							Thread.sleep(100);
						}
						LCD.drawInt(power, 0, 0);
						Thread.sleep(100);
						LCD.clear();
					}
					Long startTime = System.currentTimeMillis();
					
				    LCD.clear();
				    LCD.drawString("op1: start", 0, 0);
					driveForward.forward(200, 80);
				
					
					LCD.clear();
				    LCD.drawString("op1: linefol", 0, 0);
					lineFol.start(1, 100, 1850); //90 // 100
				

				    LCD.clear();
				    LCD.drawString("op1: sving", 0, 0);
					driveForward.forward(690,100,80);
					

					LCD.clear();
				    LCD.drawString("op2: linefol", 0, 0);
					lineFol.start(1, 100, 1900);
					

				    LCD.clear();
				    LCD.drawString("op2: sving", 0, 0);
					driveForward.forward(850,75,100);
					

					LCD.clear();
				    LCD.drawString("op3: linefol", 0, 0);
					lineFol.start(1, 100, 1900);

					Car.stop();
					LCD.clear();
					LCD.drawString("time:", 0, 0);
					LCD.drawString("" + (System.currentTimeMillis()-startTime), 0, 1);
					
					while(!Button.ENTER.isPressed())
					{}

					Thread.sleep(1000);
				}
			}
}
