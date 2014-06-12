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

		//CalibrateClaw();
	}
	
	public void CalibrateClaw() throws InterruptedException {
		Runnable runnable = new Runnable() {			
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				motor.setStallThreshold(30, 20);
				motor.setSpeed(70);
				motor.rotate(-270, true);
				//motor.backward();
				while (motor.isMoving()) {
					Thread.yield();
				}
				motor.stop(true);
				offset = motor.getPosition();
				offset += 32;
				motor.setStallThreshold(50, 1000);
				//setState(ClawPositions.RELEASE);
				TurnClawTo(0, 40);				
			}
		};
		Thread t = new Thread(runnable);
		t.start();

	}

	public void TurnClawTo(int deg, int speed) {
		motor.setSpeed(speed);
		motor.rotateTo(deg+offset, true);
		motor.setStallThreshold(40, 50);
		while (motor.isMoving()) {
			Thread.yield();
		}
		motor.stop(true);
		motor.setStallThreshold(50, 1000);
		
		
	}
	public void TurnClawTo(int deg, int speed, boolean immediate) {
		motor.setSpeed(speed);
		motor.rotateTo(deg + offset, immediate);

	}

	public void setState(ClawPositions newstate) {
		state = newstate;
		if (newstate == ClawPositions.RELEASE) {
			TurnClawTo(0, 70, false); //100
		} else if (newstate == ClawPositions.LOAD) {
			TurnClawTo(90, 70, false); //200
		} else if (newstate == ClawPositions.CARRY) {
			TurnClawTo(170, 70); //50
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
