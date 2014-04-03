
import lejos.nxt.*;
/**
 * A locomotion module with methods to drive
 * a differential car with two independent motors. The left motor 
 * should be connected to port B and the right motor
 * to port C.
 *  
 * @author  Ole Caprani
 * @version 21.3.14
 */
public class PrivateCar implements Car 
{
    // Commands for the motors
    private final int forward  = 1,
                      backward = 2,
                      stop     = 3;
	                         
    private MotorPort leftMotor = MotorPort.B;
    private MotorPort rightMotor= MotorPort.C;
	
    public PrivateCar()
    {	
    } 
   
    public void stop() 
    {
	    leftMotor.controlMotor(0,stop);
	    rightMotor.controlMotor(0,stop);
    }
   
    public void forward(int leftPower, int rightPower)
    {
	    leftMotor.controlMotor(leftPower,forward);
	    rightMotor.controlMotor(rightPower,forward);
    }
   
    public void backward(int leftPower, int rightPower)
    {
	    leftMotor.controlMotor(leftPower,backward);
	    rightMotor.controlMotor(rightPower,backward);
    }
}
