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
				sensorRight.blackLightValue = sensorLeft.blackLightValue = 403;
				sensorRight.blackWhiteThreshold = sensorLeft.blackWhiteThreshold = 470;
				sensorRight.whiteLightValue = sensorLeft.whiteLightValue = 541;
				
				LCD.clear();
				Thread.sleep(1000);
				while(!Button.ENTER.isPressed())
				{
					LCD.drawString("Left: "+sensorLeft.light(), 0, 0);
					LCD.drawString("Right: "+sensorRight.light(), 0, 1);
					
					Thread.sleep(100);
					LCD.clear();
				}
				
				
				pdLineFolSecond lineFol = new pdLineFolSecond(sensorLeft, sensorRight);
				pdLineFolSecondDown lineFolDown = new pdLineFolSecondDown(sensorLeft, sensorRight);
				
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
					
					
					driveForward.forward(50, 100);
					lineFol.start(1, 1, 100, 1200);
					sving.run(0, 80, 0);
					
					Sound.beep();
					
					catchLineSecond.run(1, 80, sensorLeft, sensorRight, 0);
					
					
					
					
					lineFol.start(1, 2, 100, 800);
					
					sving.run(1, 80, 0);
						
					catchLineSecond.run(0, 80, sensorLeft, sensorRight, 0);
					
					lineFol.start(1, 1, 100, 1200);
					
					Sound.beep();
					driveForward.forward(50, 80);
					lineFol.start(1, 1, 80, 500);
					
					driveBackward.backward(50, 100,100);
					turn.run(100, 60, sensorLeft);
					
					lineFolDown.start(1, 1, 80, 1200, 0);
					
					sving.run(0, 80, 1);
					driveForward.forward(100, 80);
					catchLineSecond.run(1, 75, sensorLeft, sensorRight, 1);
					
					
					lineFolDown.start(1, 2, 80, 1200, 0);
					
					sving.run(1, 80, 1);

					catchLineSecond.run(0, 75, sensorLeft, sensorRight, 1);
					
					lineFolDown.start(1, 2, 80, 1000, 1);
					
					driveForward.forward(500, 80);
					
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
