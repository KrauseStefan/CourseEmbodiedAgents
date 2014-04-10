import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;


public class sving {
	
	public static void run(int retning, int power, int opNed) throws InterruptedException
	{
		int lastError = 0;
	     float derivative = 0;
	     
	     float kp = 1f;
	     float kd = 2;
	     int turn = 0;
	     int powerLeft =0;
	     int powerRight = 0;
	     
	     int iniTachoB = MotorPort.B.getTachoCount();
		int iniTachoC = MotorPort.C.getTachoCount();
		
		
		int error = 0;
		if(opNed == 0)
		{
		if(retning == 0)
		{
			driveForward.forward(100, 90);
			while(MotorPort.C.getTachoCount()- iniTachoC < 130)
			{}
			Car.stop();
			
			iniTachoB = MotorPort.B.getTachoCount();
			iniTachoC = MotorPort.C.getTachoCount();
			
			while(MotorPort.C.getTachoCount()- iniTachoC < 560)  //630)
			{
				error = (int) ((MotorPort.B.getTachoCount() - iniTachoB)*2.5 - (MotorPort.C.getTachoCount() - iniTachoC));
				
				derivative = error - lastError;
			     
			    turn = (int)(kp * error  + kd * derivative);
			     
			    Car.forward(turn + power > 100?100:(power + turn), power - turn>100?100:(power - turn));
			}
		}
		else //retning == 1
		{
			driveForward.forward(100, 90);
			while(MotorPort.C.getTachoCount()- iniTachoC < 35)
			{}
			Car.stop();
			
			iniTachoB = MotorPort.B.getTachoCount();
			iniTachoC = MotorPort.C.getTachoCount();			
			
		    while(MotorPort.B.getTachoCount()- iniTachoB < 520) //590)
			{
				error = (int) ((MotorPort.B.getTachoCount() - iniTachoB) - (MotorPort.C.getTachoCount() - iniTachoC)*2.5);
				
				derivative = error - lastError;
			     
			    turn = (int)(kp * error  + kd * derivative);
			     
			    powerRight =power - turn< 0?0: power - turn;
			    powerLeft = turn + power < 0?0:turn + power;
			    
			    Car.forward(powerLeft > 100?100:powerLeft, powerRight > 100?100:powerRight);
			}
		}
		}
		else
		{
			if(retning == 0)
			{
				driveForward.forward(100, 100);
				while(MotorPort.C.getTachoCount()- iniTachoC < 50)
				{}
				Car.stop();
				Thread.sleep(10);
				
				iniTachoB = MotorPort.B.getTachoCount();
				iniTachoC = MotorPort.C.getTachoCount();
				
				while(MotorPort.C.getTachoCount()- iniTachoC < 650)
				{
					error = (int) ((MotorPort.B.getTachoCount() - iniTachoB)*2.3 - (MotorPort.C.getTachoCount() - iniTachoC));
					
					derivative = error - lastError;
				     
				    turn = (int)(kp * error  + kd * derivative);
				     
				    Car.forward(turn + power > 100?100:(power + turn), power - turn>100?100:(power - turn));
				}
			}
			else
			{
				Car.stop();
				
				iniTachoB = MotorPort.B.getTachoCount();
				iniTachoC = MotorPort.C.getTachoCount();			
				
			    while(MotorPort.B.getTachoCount()- iniTachoB < 650)
				{
					error = (int) ((MotorPort.B.getTachoCount() - iniTachoB) - (MotorPort.C.getTachoCount() - iniTachoC)*2.2);
					
					derivative = error - lastError;
				     
				    turn = (int)(kp * error  + kd * derivative);
				     
				    powerRight =power - turn< 0?0: power - turn;
				    powerLeft = turn + power < 0?0:turn + power;
				    
				    Car.forward(powerLeft > 100?100:powerLeft, powerRight > 100?100:powerRight);
				}
			}
		}
	}

}
