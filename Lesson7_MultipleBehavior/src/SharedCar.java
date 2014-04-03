import lejos.util.Delay;


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
    
    public void turnLightSensor(int power, String direction)
    {
    	if(direction.equals("LEFT")) {
    		carCommand.command = CarCommand.Command.TURN_LIGHT_SENSOR_LEFT;
    		carCommand.lightPower = power;
    		commandReady = true;
    	} else if (direction.equals("RIGHT")) {
    		carCommand.command = CarCommand.Command.TURN_LIGHT_SENSOR_RIGHT;
    		carCommand.lightPower = power;
    		commandReady = true;
    	}
    }
    
    public void stopLightSensor()
    {
    	carCommand.command = CarCommand.Command.STOP_LIGHT_SENSOR;
    	carCommand.lightPower = 0;
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
    
    public void spin(int ms, int power)
    {
    	carCommand.command = CarCommand.Command.SPIN;
    	carCommand.leftPower = power;
    	carCommand.rightPower = power;
    	carCommand.spinDelay = ms;
    	commandReady = true;
    }
    
    public void backspin(int ms, int power, String direction){
    	carCommand.command = CarCommand.Command.BACKSPIN;
    	carCommand.leftPower = power;
    	carCommand.rightPower = power;
    	carCommand.spinDelay = ms;
    	carCommand.direction = direction;
    	commandReady = true;
    }
}
