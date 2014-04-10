import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.Sound;


public class pdLineFolSecond 
{
	BlackWhiteSensor sensorLeft;
	BlackWhiteSensor sensorRight;
	int state = 2; // 0 = {car|}, 1 = {|car|}, 2 = {|car}
	
	pdLineFolSecond(BlackWhiteSensor _sensorLeft, BlackWhiteSensor _sensorRight)
	{ 
		sensorLeft = _sensorLeft;
		sensorRight = _sensorRight;
	}
	
	void start(int _lightPort, int _initialPosition, int power, int tacho) throws InterruptedException
	{
		state = _initialPosition;
		BlackWhiteSensor sensor = _lightPort==1?sensorLeft:sensorRight;
		
		int initialTacho = MotorPort.B.getTachoCount();
		
		
	     int error=0;
	     int maxError = sensor.whiteLightValue - sensor.blackLightValue;
	     int turn = 0;
	     int lastError = 0;
	     float derivative = 0;
	     
	     float scale = maxError/68;
	     float kp = 0.3f;
	     float kd = 3f;
	     
	     int lightLeft = 0;
	     int lightRight = 0;
	     int lastLightLeft = 0;
	     int lastLightRight = 0;
	     
	     int powerLeft = 0;
	     int powerRight = 0;
	     
	     
	     //DataLogger dataLogger = new DataLogger("dpLine.txt");
	     
	     Car.forward(power, power);
	     
	     lastLightLeft = sensorLeft.light();
	     lastError = sensor.blackLightValue - lightLeft;
	     
	     while (MotorPort.B.getTachoCount()-initialTacho < tacho || (lightLeft > sensorLeft.blackLightValue+30*scale && lightRight > sensorLeft.blackLightValue+30*scale))
	     {
	    	long a = System.currentTimeMillis() + 10;
	    	
	    	while(System.currentTimeMillis() < a)
	    	{
	    		lightLeft = sensorLeft.light();
	    		lightRight = sensorRight.light();
	    		if(!(MotorPort.B.getTachoCount()-initialTacho < tacho || (lightLeft > sensorLeft.blackLightValue+30*scale && lightRight > sensorLeft.blackLightValue+30*scale)))
	    			break;
	    	}
			
			//error = (sensor.whiteLightValue - lightRight) - (sensor.whiteLightValue - lightLeft);//error = sensor.blackWhiteThreshold - light;
			error =  (sensor.whiteLightValue - lightRight) - (sensor.whiteLightValue - lightLeft);
			
		     derivative = error - lastError;
		     
		     turn = (int)(kp * error  + kd * derivative);
		     
		     
		     //Car.forward(turn < -20?0:(power + turn), turn>20?0:(power - turn));
		     Car.forward(turn + power > 100?100:(power + turn), power - turn>100?100:(power - turn));
			    
		     
		     lastError = error;
		     lastLightLeft = lightLeft;
		     lastLightRight = lightRight;
		     
	     }
	    // dataLogger.close();
	}
}
