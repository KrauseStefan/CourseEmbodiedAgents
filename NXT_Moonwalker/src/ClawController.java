import lejos.nxt.*;

public class ClawController {
	NXTRegulatedMotor motor;
	int state = 0;
	
	public ClawController(NXTRegulatedMotor m) {
				
		motor = m;
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				TurnClaw(-20, 70);
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				TurnClaw(20,70);
			}
		});
		
		Button.ENTER.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				setNextState();
			}
		});	
	}
	
	public void TurnClaw(int deg, int speed) {
		motor.setSpeed(speed);
		motor.rotate(deg, true);
	}
	
	public void TurnClawTo(int deg, int speed){
		motor.setSpeed(speed);
		motor.rotateTo(deg);
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
}
