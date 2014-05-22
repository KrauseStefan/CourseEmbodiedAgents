import lejos.nxt.*;

public class ClawController {

	boolean running;
	
	int state = 0;
	
	static void TurnClaw(int deg, int speed) {
		Motor.C.setSpeed(speed);
		Motor.C.rotate(deg, true);
	}
	
	static void TurnClawTo(int deg, int speed){
		Motor.C.setSpeed(speed);
		Motor.C.rotateTo(deg);
	}
	
	public void SetClawTo(int newstate)
	{
		if(newstate == 0){TurnClawTo(0, 100);}
		else if(newstate == 1){TurnClawTo(90, 80);}
		else if(newstate == 2){TurnClawTo(170, 50);}
	}
	
	public void setNextState()
	{
		if(state!=2){SetClawTo(state+1);
		state++;}
		else {SetClawTo(0);
		state=0;}
		
	}
		
	public void startTurn(boolean up)
	{
		running = true;
		Motor.C.setSpeed(1);
		if(up)
		{
			while(running)
			{
				Motor.C.rotate(-1);
			}
		}
		else{
			while(running)
			{
				Motor.C.rotate(1);
			}
		}
	}
	public void stopTurn()
	{
		running = false;
		}

	

}
