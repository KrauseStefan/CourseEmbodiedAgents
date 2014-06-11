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
					e.printStackTrace();
				}}
				motor.stop(true);
				offset = motor.getPosition();
				offset += 20;
				TurnClawTo(0, 40);				
			}
		};
		Thread t = new Thread(runnable);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
		

	}

	public void TurnClawTo(int deg, int speed) {
		motor.setSpeed(speed);
		motor.rotateTo(deg+offset, true);
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
