
/*
 *  Arbiter pass the highest priority car command
 *  from the behaviors to the car driver.   
 */
public class Arbiter extends Thread
{
	private SharedCar [] car;
	private CarDriver cd;
	private int winner;
	
    public Arbiter(SharedCar [] car, CarDriver cd)
    {
    	this.car = car;
    	this.cd = cd;
    }
    	
	public void run()
	{
	    while ( true )
	    {
		    for (int i=0; i < car.length; i++)
		    {
		    	CarCommand carCommand = car[i].getCommand();
		    	if ( carCommand != null)
		    	{
		    		cd.perform(carCommand);
		    		winner = i;
		    		break;
		    	}
		    }
		   		   
	    }

    }
	
	public int winner()
	{
		return winner;
	}
}
	

