

/**
 * A locomotion interface with methods to drive
 * a differential car with two independent motors. 
 *  
 * @author  Ole Caprani
 * @version 21.3.14
 */
public interface Car 
{
    public void stop();   
    public void forward(int leftPower, int rightPower);  
    public void backward(int leftPower, int rightPower);
}
