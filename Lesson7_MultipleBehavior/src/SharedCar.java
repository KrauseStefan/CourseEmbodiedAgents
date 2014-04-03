
/**
 * A locomotion module with methods to drive
 * a differential car with two independent motors.
 * The methods turns the parameters for the two motors
 * into commands for a car driver that will fetch the commands
 * and turn them into motor commands for the physical motors. 
 *  
 * @author  Ole Caprani
 * @version 26.3.14
 */
public class SharedCar implements Car 
{    
    private boolean commandReady;
    private CarCommand carCommand;
	                         	
    public SharedCar()
    {
    	commandReady = false;
    	carCommand = new CarCommand();
    } 
   
    public void stop() 
    {
    	carCommand.command = CarCommand.Command.STOP;
    	carCommand.leftPower  = 0;
    	carCommand.rightPower = 0;
    	commandReady = true;
    }
   
    public void forward(int leftPower, int rightPower)
    {
    	carCommand.command = CarCommand.Command.FORWARD;
    	carCommand.leftPower  = leftPower;
    	carCommand.rightPower = rightPower;
    	commandReady = true;
    }
   
    public void backward(int leftPower, int rightPower)
    {
    	carCommand.command = CarCommand.Command.BACKWARD;
    	carCommand.leftPower  = leftPower;
    	carCommand.rightPower = rightPower;
    	commandReady = true;
    }
    
    public void noCommand()
    {
    	commandReady = false;
    }
    
    public CarCommand getCommand()
    {
    	CarCommand result = null;
    	if ( commandReady )
    	{
    		result = carCommand;
    	}
    	return ( result);
    }
}
