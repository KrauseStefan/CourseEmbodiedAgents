import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;


public class PIDLineFollower {
	public static void main (String[] aArg) throws Exception {
		final int power = 50;
		final float Kp = 1.8f, Ki = 0, Kd = 0;
		float error, derivative, previous_error = 0, integral = 0, dt = 300;
		int sensorVal, powerDiff; 
		int powerLeft, powerRight;
			 
		BlackWhiteSensor sensor = new BlackWhiteSensor(SensorPort.S3);
			 
		sensor.calibrate();
			 
		LCD.clear();
		LCD.drawString("Light: ", 0, 2); 
			     
		LCD.drawString("pDiff: ", 0, 4); 
		//LCD.drawString("Right: ", 0, 5); 
			 
		while (! Button.ESCAPE.isDown()){
		sensorVal = sensor.light();
		  
		error = sensorVal - sensor.blackWhiteThreshold;
		integral = integral + error * dt;
		derivative = (error - previous_error) / dt; //dt??
		powerDiff = (int) (Kp * error + Ki * integral + Kd * derivative);
					 
		//powerLeft = power+powerDiff;
		//powerRight = power-powerDiff;
					 
		LCD.drawInt(sensorVal,4,10,2);
		LCD.drawInt(powerDiff, 4, 10, 4);
		//LCD.drawInt(powerRight,4,10,5);
					 
		LCD.refresh();
		if ( sensor.black() )
			Car.forward(power-powerDiff, 0);
		else
			Car.forward(0,power+powerDiff);
				     
		//Thread.sleep(10);

		Car.stop();
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		LCD.refresh();
	}
}
