import java.util.LinkedList;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;

public class ReversibleDifferentialPilot implements RegulatedMotorListener, ArcRotateMoveController, MoveListener {

	DifferentialPilot pFwd;
	DifferentialPilot pBck;
	DifferentialPilot pCur;

	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;

	LinkedList<MoveListener> moveListeners = new LinkedList<>();

	public ReversibleDifferentialPilot(final double wheelDiameter, final double trackWidth, final RegulatedMotor leftMotor,
			final RegulatedMotor rightMotor) {
		this(wheelDiameter, trackWidth, leftMotor, rightMotor, false);
	}

	public ReversibleDifferentialPilot(final double wheelDiameter, final double trackWidth, final RegulatedMotor leftMotor,
			final RegulatedMotor rightMotor, final boolean reverse) {

		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;

		pBck = 			new DifferentialPilot(wheelDiameter, trackWidth, rightMotor, leftMotor, !reverse);
		pCur = pFwd = 	new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);

		pFwd.addMoveListener(this);
		pBck.addMoveListener(this);

	}

	public void reverse() {
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();

		if (pFwd == pCur) {
			pCur = pBck;
		} else {
			pCur = pFwd;
		}

	}

	public void setAcceleration(int acceleration) {
		pFwd.setAcceleration(acceleration);
		pBck.setAcceleration(acceleration);
	}

	@Override
	public double getMinRadius() {
		return pCur.getMinRadius();
	}

	@Override
	public void setMinRadius(double radius) {
		pFwd.setMinRadius(radius);
		pBck.setMinRadius(radius);
	}

	@Override
	public void arcForward(double radius) {
		pCur.arcForward(radius);

	}

	@Override
	public void arcBackward(double radius) {
		pCur.arcBackward(radius);

	}

	@Override
	public void arc(double radius, double angle) {
		pCur.arc(radius, angle);

	}

	@Override
	public void arc(double radius, double angle, boolean immediateReturn) {
		pCur.arc(radius, angle, immediateReturn);

	}

	@Override
	public void travelArc(double radius, double distance) {
		pCur.travelArc(radius, distance);

	}

	@Override
	public void travelArc(double radius, double distance, boolean immediateReturn) {
		pCur.travelArc(radius, distance, immediateReturn);

	}

	@Override
	public void forward() {
		pCur.forward();

	}

	@Override
	public void backward() {
		pCur.backward();

	}

	@Override
	public void stop() {
		pCur.stop();

	}

	@Override
	public boolean isMoving() {

		return pCur.isMoving();
	}

	@Override
	public void travel(double distance) {
		pCur.travel(distance);

	}

	@Override
	public void travel(double distance, boolean immediateReturn) {
		pCur.travel(distance, immediateReturn);

	}

	@Override
	public void setTravelSpeed(double speed) {
		pFwd.setTravelSpeed(speed);
		pBck.setTravelSpeed(speed);

	}

	@Override
	public double getTravelSpeed() {

		return pCur.getTravelSpeed();
	}

	@Override
	public double getMaxTravelSpeed() {
		return pCur.getMaxTravelSpeed();
	}

	@Override
	public Move getMovement() {
		return pCur.getMovement();
	}

	@Override
	public void addMoveListener(MoveListener listener) {
		moveListeners.add(listener);
	}

	@Override
	public void rotate(double angle) {
		pCur.rotate(angle);

	}

	@Override
	public void rotate(double angle, boolean immediateReturn) {
		pCur.rotate(angle, immediateReturn);

	}

	@Override
	public void setRotateSpeed(double speed) {
		pFwd.setRotateSpeed(speed);
		pBck.setRotateSpeed(speed);

	}

	@Override
	public double getRotateSpeed() {
		return pCur.getRotateSpeed();
	}

	@Override
	public double getRotateMaxSpeed() {
		return pCur.getRotateMaxSpeed();
	}

	@Override
	public void rotationStarted(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
		pCur.rotationStarted(motor, tachoCount, stalled, timeStamp);

	}

	@Override
	public void rotationStopped(RegulatedMotor motor, int tachoCount, boolean stalled, long timeStamp) {
		pCur.rotationStopped(motor, tachoCount, stalled, timeStamp);

	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {

		if (pCur == mp) {
			for (MoveListener listner : moveListeners) {
				listner.moveStarted(event, mp);
			}
		}
	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		if (pCur == mp) {
			for (MoveListener listner : moveListeners) {
				listner.moveStopped(event, mp);
			}
		}

	}
}
