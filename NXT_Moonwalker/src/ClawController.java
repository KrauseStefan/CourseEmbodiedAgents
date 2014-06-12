import lejos.nxt.*;

public class ClawController {
	private NXTRegulatedMotor motor;
	private ClawPositions state = ClawPositions.RELEASE;
	private int offset;

	public enum ClawPositions {
		RELEASE, // 0
		LOAD, // 1
		CARRY // 2
	};

	public ClawController(NXTRegulatedMotor m) throws Exception {
		motor = m;
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
				motor.setStallThreshold(20, 10);
				motor.setSpeed(70);
				motor.rotate(-270, true);
				while (motor.isMoving()) {
					Thread.yield();
				}
				motor.stop(true);
				offset = motor.getPosition();
				offset += 20;
				motor.setStallThreshold(50, 1000);
				TurnClawTo(0, 40);				
			}
		};
		Thread t = new Thread(runnable);
		t.start();

	}

	public void TurnClawTo(int deg, int speed, boolean immediate) {
		motor.setSpeed(speed);
		motor.rotateTo(deg + offset, immediate);
		
	}
	public void TurnClawTo(int deg, int speed) {
		TurnClawTo(deg, speed, true);
	}

	public void setState(ClawPositions newstate) {
		state = newstate;
		if (newstate == ClawPositions.RELEASE) {
			TurnClawTo(0, 100, false);
		} else if (newstate == ClawPositions.LOAD) {
			TurnClawTo(90, 80, false);
		} else if (newstate == ClawPositions.CARRY) {
			TurnClawTo(170, 50);
		}
	}

	public void setNextState() {
		if (state == ClawPositions.CARRY) {
			setState(ClawPositions.RELEASE);
		} else if (state == ClawPositions.LOAD) {
			setState(ClawPositions.CARRY);
		} else if (state == ClawPositions.RELEASE) {
			setState(ClawPositions.LOAD);
		}
	}
}
