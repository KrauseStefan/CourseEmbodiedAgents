import lejos.nxt.*;

public class ClawController {
	private NXTRegulatedMotor motor;
	private ClawPositions state = ClawPositions.RELEASE;
	private int offset;
	
	public enum ClawPositions {
		RELEASE, //0 
		LOAD, //1
		CARRY //2
	};

	public ClawController(NXTRegulatedMotor m) throws InterruptedException {

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
				TurnClaw(20, 70);
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
		CalibrateClaw();
	}
	
	public void CalibrateClaw() throws InterruptedException
	{		
		Runnable runnable =  new Runnable() {
			
			@Override
			public void run() {
				motor.setSpeed(100);
				motor.rotate(-270, true);
				while(motor.isMoving()){try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
				//Thread.sleep(1000);
				motor.stop(true);
				offset = motor.getPosition();
				offset += 20;
				TurnClawTo(0, 40);				
			}
		};
		Thread t = new Thread(runnable);
		t.start();
		

	}

	public void TurnClaw(int deg, int speed) {
		motor.setSpeed(speed);
		motor.rotate(deg, true);
	}

	public void TurnClawTo(int deg, int speed) {
		motor.setSpeed(speed);
		motor.rotateTo(deg+offset);
	}

	public void setState(ClawPositions newstate) {
		state = newstate;
		if (newstate == ClawPositions.RELEASE) {
			TurnClawTo(0, 100);
		} else if (newstate == ClawPositions.LOAD) {
			TurnClawTo(90, 80);
		} else if (newstate == ClawPositions.CARRY) {
			TurnClawTo(170, 50);
		}
	}

	public void setNextState() {
		if (state == ClawPositions.CARRY) {
			setState(ClawPositions.RELEASE);
		} else if (state == ClawPositions.LOAD){
			setState(ClawPositions.CARRY);
		} else if(state == ClawPositions.RELEASE){
			setState(ClawPositions.LOAD);
		}
	}
}
