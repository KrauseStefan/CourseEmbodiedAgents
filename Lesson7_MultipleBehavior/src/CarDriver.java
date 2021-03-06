import lejos.nxt.*;
import lejos.util.Delay;

/*
 * A car driver module with a method to drive
 * a differential car with two independent motors. The left motor 
 * should be connected to port B and the right motor
 * to port C.
 * 
 * The car driver method turns car commands into actual commands
 * for the two physical motors.
 *  
 * @author  Ole Caprani
 * @version 21.3.14
 */
public class CarDriver {
	// Commands for the motors
	final int forward = 1, backward = 2, stop = 3;

	private MotorPort lightMotor = MotorPort.A;
	private MotorPort leftMotor = MotorPort.C;
	private MotorPort rightMotor = MotorPort.B;

	public CarDriver() {
	}

	private int ccToMc(CarCommand.Command carCommand) {
		switch (carCommand) {
		case FORWARD:
			return forward;
		case BACKWARD:
			return backward;
		case STOP:
			return stop;
		}
		return -1;
	}

	public void perform(CarCommand command) {

		if (command.command == CarCommand.Command.SPIN) {
			performSpin(command);
		} else if (command.command == CarCommand.Command.BACKSPIN) {
			performBackspin(command);
		} else if (command.command == CarCommand.Command.TURN_LIGHT_SENSOR_LEFT) {
			turnLightSensor(command.lightPower, "LEFT");
		} else if(command.command == CarCommand.Command.TURN_LIGHT_SENSOR_RIGHT) {
			turnLightSensor(command.lightPower, "RIGHT");
		} else if(command.command == CarCommand.Command.STOP_LIGHT_SENSOR) {
			lightMotor.controlMotor(0, stop);
		} else {
			leftMotor.controlMotor(command.leftPower, ccToMc(command.command));
			rightMotor.controlMotor(command.rightPower, ccToMc(command.command));
		}
	}

	private void performBackspin(CarCommand command) {
		CarCommand carCommand = new CarCommand();
		carCommand.command = CarCommand.Command.BACKWARD;
		carCommand.leftPower = command.leftPower;
		carCommand.rightPower = command.rightPower;
		perform(carCommand); // go backwards
		
		Delay.msDelay(command.spinDelay);
		
		performSpin(command); // spin the vehicle
		

	}

	private void performSpin(CarCommand command) {
		if (command.direction.equals("left")) {
			leftMotor.controlMotor(command.leftPower, forward);
			rightMotor.controlMotor(command.rightPower, backward);
			Delay.msDelay(command.spinDelay);
		} else if (command.direction.equals("right")) {
			leftMotor.controlMotor(command.leftPower, backward);
			rightMotor.controlMotor(command.rightPower, forward);
			Delay.msDelay(command.spinDelay);
		}
	}
	
    private void turnLightSensor(int power, String direction)
    {
    	if(direction.equals("LEFT")) {
    		lightMotor.controlMotor(power, forward);
    	} 
    	else if(direction.equals("RIGHT")) {
    		lightMotor.controlMotor(power, backward);
    	}
    }
}
