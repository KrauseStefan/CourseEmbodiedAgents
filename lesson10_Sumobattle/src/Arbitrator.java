
/**
 * Arbitrator controls which Behavior object will become active in
 * a behavior control system. 
 * 
 * Make sure to call start() after the Arbitrator is instantiated.<br>
 * 
 * This class has three major responsibilities: <br> 
 * 1. Determine the highest priority  behavior that returns <b> true </b> to takeControl()<br>   
 * 2. Suppress the active behavior if its priority is less than highest priority. <br>   
 * 3. When the action() method exits, call action() on the Behavior of highest priority.
 * <br>  The Arbitrator assumes that a Behavior is no longer active when action() exits,
 * <br>  therefore it will only call suppress() on the Behavior whose action() method is running.
 * <br>  It can make consecutive calls of action() on the same Behavior.
 * <br>  Requirements for a Behavior:
 * <br>    When suppress() is called, terminate  action() immediately.
 * <br>    When action() exits, the robot is in a safe state (e.g. motors stopped)
 * @see Behavior
 * @author Roger Glassey
 * 
 * Modified so the Behavior with the highest integer priority returned by
 * takeControl is the active behavior. Also modified so start() never exits,
 * there should always be a behavior that wants to be active.
 * 
 * Ole Caprani, 24-12-2012
 * 
 */
public class Arbitrator
{
	
    private final int NONE = -1;
    private Behavior[] behavior;
    private BehaviorAction actionThread;
    private int currentBehavior = NONE;
    private int currentPriority = NONE;
 
    public Arbitrator(Behavior[] behaviorList)
    {
        behavior = behaviorList;
        actionThread = new BehaviorAction();
        actionThread.setDaemon(true);
    }

    /**
     * This method starts the arbitration of Behaviors and runs an endless loop.
     * The start() method will never return
     */
     public void start()
     {
         int highest, maxPriority;
	     
         actionThread.start();
	     
         while (true)
         {   
             // Find behavior with highest priority
    	     maxPriority = -1; highest = -1;
    	     for (int i = 0; i < behavior.length; i++)
    	     {
    	    	 int priority = behavior[i].takeControl();
                 if (priority > maxPriority ) 
                 {
                    highest = i;
                    maxPriority = priority;
                 }
    	     }
             
    	     // Start highest priority process and update currentPriority
             if ( actionThread.current == NONE)  
             {
    	         currentBehavior = highest;
    	         currentPriority = maxPriority;
    	         actionThread.execute(highest);
             }
             else
             if (  currentPriority < maxPriority )
             {
    	         behavior[currentBehavior].suppress();
      	         currentBehavior = highest;
      	         currentPriority = maxPriority;
      	         actionThread.execute(highest);
    	  
             }
             else
            	 currentPriority = maxPriority;
             Thread.yield();
          }
    }

    /**
     * Local thread that runs the action method for the currently
     * highest priority behavior
     */
     private class BehaviorAction extends Thread
     {
         public int current = NONE;
 
         public void run()
         {
             while (true)
             {
                 synchronized (this)
                 {
                     if ( current != NONE )
                     {
        	             behavior[current].action();
        	             current = NONE;
                     }
                 }
                 Thread.yield();
             }
          }
    
          public synchronized void execute(int index)
          {
    	     current = index;
          }
      }
}
  