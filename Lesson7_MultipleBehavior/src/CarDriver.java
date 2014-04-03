
import lejos.nxt.*;
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
public class CarDriver
{
	// Commands for the motors
	final int forward  = 1,
	          backward = 2,
	          stop     = 3;
	
    private MotorPort leftMotor = MotorPort.C;
    private MotorPort rightMotor= MotorPort.B;
	
	public CarDriver()
	{
	}
	
	private int ccToMc(CarCommand.Command carCommand)
	{
		switch ( carCommand )
		{
		case FORWARD:  return forward;
		case BACKWARD: return backward;
		case STOP:     return stop;
		}
		return -1;
	}

	public void perform(CarCommand command)
	{
		leftMotor.controlMotor(command.leftPower, ccToMc(command.command));
		rightMotor.controlMotor(command.rightPower,ccToMc(command.command));
    }
}

