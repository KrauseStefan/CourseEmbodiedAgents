import lejos.nxt.LCD;



public class LineFollower implements Runnable{
	
	private BlackWhiteSensor sensor;
	private boolean isStarted;
	private ReversibleDifferentialPilot pilot;
	
	public LineFollower(BlackWhiteSensor _sensor, ReversibleDifferentialPilot _pilot) {
		this.sensor = _sensor;
		this.isStarted = false;
		this.pilot = _pilot;
	}
	
	public void calibrate(){
		sensor.calibrate();		
	}
	
	@Override
	public void run() {
		 this.isStarted = true;
	     final int power = 300;
		 final float Kp = 1.8f, Ki = 0, Kd = 0;
		 float error, derivative, previous_error = 0, integral = 0, dt = 300;
		 int sensorVal, powerDiff; 
//		 int powerLeft, powerRight;
		 			 			 
//	     LCD.clear();
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
			 
			 
			 LCD.drawInt(sensorVal,4,10,2);
			 LCD.drawInt(powerDiff, 4, 10, 4);
			 
			 LCD.refresh();
		         pilot.forward(power-powerDiff, power+powerDiff);
		     
		     
		     try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	     }	     
	   }
	
	public void stop() {
		this.isStarted = false;
	}

}
