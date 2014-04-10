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
				sensorRight.blackLightValue = sensorLeft.blackLightValue = 467; //392; // 403;
				sensorRight.whiteLightValue = sensorLeft.whiteLightValue = 584; //595; //541;
				sensorRight.blackWhiteThreshold = sensorLeft.blackWhiteThreshold = 525; //492; //470;

				
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
					
				    LCD.clear();
				    LCD.drawString("op1: start", 0, 0);
					driveForward.forward(200, 80);
				
					
					LCD.clear();
				    LCD.drawString("op1: linefol", 0, 0);
					lineFol.start(1, 1, 90, 1600); //90 // 100
					

				    LCD.clear();
				    LCD.drawString("op1: sving", 0, 0);
					sving.run(0, 80, 0); //80

					
					Sound.beep();
				    LCD.clear();
				    LCD.drawString("op2: run", 0, 0);
					catchLineSecond.run(1, 80, sensorLeft, sensorRight, 0);
					
					LCD.clear();
				    LCD.drawString("op2: linefol", 0, 0);
					lineFol.start(1, 2, 100, 800);
					
					LCD.clear();
				    LCD.drawString("op2: sving", 0, 0);
					sving.run(1, 80, 0);
					
					LCD.clear();
				    LCD.drawString("op3: run", 0, 0);
					catchLineSecond.run(0, 80, sensorLeft, sensorRight, 0);
					
					LCD.clear();
				    LCD.drawString("op3: linefol", 0, 0);
					lineFol.start(1, 1, 100, 1200);
					
					Sound.beep();
					LCD.clear();
				    LCD.drawString("top: forward", 0, 0);
					driveForward.forward(50, 80);
					
					LCD.clear();
				    LCD.drawString("top: linefol", 0, 0);
					lineFol.start(1, 1, 80, 500);
					
					LCD.clear();
				    LCD.drawString("top: backward", 0, 0);
					driveBackward.backward(50, 100,100);
					
					LCD.clear();
				    LCD.drawString("top: turn", 0, 0);
					turn.run(100, 60, sensorLeft);
					
					LCD.clear();
				    LCD.drawString("ned3: linefol", 0, 0);
					lineFolDown.start(1, 1, 80, 1200, 0);
					
					LCD.clear();
				    LCD.drawString("ned3: sving", 0, 0);
					sving.run(0, 80, 1);
					
					LCD.clear();
				    LCD.drawString("ned2: forward", 0, 0);
					driveForward.forward(100, 80);
					
					LCD.clear();
				    LCD.drawString("ned2: run", 0, 0);
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
