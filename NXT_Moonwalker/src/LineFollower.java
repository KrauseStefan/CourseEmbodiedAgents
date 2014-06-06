import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;



public class LineFollower {
	
	private BlackWhiteSensor sensor;
	private boolean isStarted;
	private ReversibleDifferentialPilot pilot;
	
	public LineFollower(BlackWhiteSensor _sensor, ReversibleDifferentialPilot _pilot) {
		this.sensor = _sensor;
		this.isStarted = false;
		this.pilot = _pilot;
	}
	
	public void start() throws Exception {
		 this.isStarted = true;
	     final int power = 300;
		 final float Kp = 1.8f, Ki = 0, Kd = 0;
		 float error, derivative, previous_error = 0, integral = 0, dt = 300;
		 int sensorVal, powerDiff; 
		 int powerLeft, powerRight;
		 			 	
	     sensor.calibrate();
		 
	     LCD.clear();
	     LCD.drawString("Light: ", 0, 2); 
	     
	     LCD.drawString("pDiff: ", 0, 4); 
	     //LCD.drawString("Right: ", 0, 5); 
		 
	     while (isStarted)
	     {
	    	 sensorVal = sensor.light();
		     
			 error = sensorVal - sensor.blackWhiteThreshold;
			 integral = integral + error * dt;
			 derivative = (error - previous_error) / dt; //dt??
			 powerDiff = (int) (Kp * error + Ki * integral + Kd * derivative);
			 
			 //powerLeft = power+powerDiff;
			 //powerRight = power-powerDiff;
			 
			 if(powerDiff > 10)
				 powerDiff = 10;
			 else if (powerDiff < -10)
				 powerDiff = -10;
			 
			 LCD.drawInt(sensorVal,4,10,2);
			 LCD.drawInt(powerDiff, 4, 10, 4);
			 //LCD.drawInt(powerRight,4,10,5);
			 
			 LCD.refresh();
			// if ( sensor.isBlack() )
		         pilot.forward(power-powerDiff, power+powerDiff);
		    // else
		    //	 pilot.forward(power+powerDiff,power+powerDiff);
		     
		     
		     //Thread.sleep(10);
	     }
	     
	     //Car.stop();
	     LCD.clear();
	     LCD.drawString("Program stopped", 0, 0);
	     LCD.refresh();
	   }
	
	public void stop() {
		this.isStarted = false;
	}
}
