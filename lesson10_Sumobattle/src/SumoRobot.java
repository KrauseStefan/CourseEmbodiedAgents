import lejos.nxt.*;
import lejos.robotics.navigation.*;


public class SumoRobot {

	
	public static void main(String[] args) throws InterruptedException
	  {
	    Motor.A.setSpeed(400);
	    Motor.C.setSpeed(400);
	    Behavior b1 = new DriveForward(); // doAction
	    Behavior b2 = new DetectWall(); // LightSensor
	    Behavior b3 = new Exit(); // buttonpress
	    Behavior[] behaviorList =
	    {
	      b1, b2, b3
	    };
	    Arbitrator arbitrator = new Arbitrator(behaviorList);
	    LCD.drawString("Sumo Winner",0,0);
	    Button.waitForAnyPress();
	    Thread.sleep(3000);
		DifferentialPilot robot = new DifferentialPilot(5.475F, 16F, Motor.C, Motor.A, false);  //5.5cm 11.5cm width measured. 5.475cm and 10.75 calculated.
		robot.rotate(180);
	    arbitrator.start();
	  }
	}

	class UltraThread extends Thread
	{
		private boolean targetFound = false;
		
		private UltrasonicSensor sonar;
		private int sonarReading;
		private int deg;
		
		private boolean sweepRight = true;
		
		public UltraThread()
		{
			sonar = new UltrasonicSensor(SensorPort.S2);			
		}
		
		public boolean getTargetFound()
		{
			return targetFound;
		}
		public void setTargetFound(boolean bool)
		{
			targetFound = bool;
		}
		
		public int getDeg()
		{return deg;}
		
		public void run()
		{
			int degInit = Motor.B.getTachoCount();
			deg = 0;
			while(true)
			{
				sonarReading = sonar.getDistance();
				LCD.drawInt(sonarReading, 0, 3);
				if(sonarReading > 40) // continiue sweep
				{
					targetFound = false;
					if(sweepRight)
					{
						deg = deg+10;		
						Motor.B.rotateTo(degInit+deg);
						if(deg > 75)
						{sweepRight = false;}
						}
					else{
						deg = deg-10;		
						Motor.B.rotateTo(degInit+deg);
						if(deg < -75)
						{sweepRight = true;}
						}
					}
				else // Found target!
				{
					targetFound = true;
					//Sound.twoBeeps();
					if(deg>0)
					{sweepRight = false;
					}
					else{sweepRight = true;}

				}
				
			}
		}
	}

	class DriveForward implements Behavior
	{
		private UltrasonicSensor sonar;
		private UltraThread UT = new UltraThread();
		DifferentialPilot robot = new DifferentialPilot(5.475F, 16F, Motor.C, Motor.A, false);  //5.5cm 11.5cm width measured. 5.475cm and 10.75 calculated.
 

	  private boolean _suppressed = false;
	  
	  public DriveForward()
	  {
		  UT.start();
		  //Sound.twoBeeps();
		  robot.setRotateSpeed(80);
		  robot.setTravelSpeed(500);
	  }

	  public int takeControl()
	  {
		  if (UT.getTargetFound())
		  {
			  return 300;  
		  }
		  else{
			  return 100; // this behavior always wants control.
		  }
	  }

	  public void suppress()
	  {
	    _suppressed = true;// standard practice for suppress methods
	  }
	  

	  public void action()
	  {
	    _suppressed = false;
	    int deg = UT.getDeg();
	    int oldAspeed;
	    int oldBspeed;
	    
	    LCD.drawString("DriveForward", 0, 1);
	    
	    if(UT.getTargetFound())
	    {
	    	UT.setTargetFound(false);
			//Sound.twoBeeps();
			LCD.drawInt(deg, 0, 2);
	    	if(deg > 10 || deg < -10)
	    	{
		    	robot.rotate(-deg);
	    	}
	    	else{
	    		robot.travel(5);
	    		
	    		/*
			    while (!_suppressed)
			    {
			      Thread.yield(); //don't exit till suppressed
			    }*/
	    	}
	    	
	    }
	    else // no target found
	    {
	    	/*
	    	oldAspeed = Motor.A.getSpeed();
	    	oldBspeed = Motor.C.getSpeed();
	    	Motor.A.setSpeed(300);
	    	Motor.C.setSpeed(200);
	    	Motor.A.forward();
	    	Motor.C.forward();

		    while (!_suppressed || !UT.getTargetFound() )
		    {
		       Thread.yield(); //don't exit till suppressed or target found
		    }
		    UT.setTargetFound(false);
	    	Motor.A.setSpeed(oldAspeed);
	    	Motor.C.setSpeed(oldBspeed);
	    	LCD.drawString("notarget", 0, 2);*/
	    	//robot.setTravelSpeed(30);
	    	//robot.travel(999999, true);
	    	robot.travelArc(30, 2);
	    	
	    }
	    //robot.stop();
	  }
	  
	}


	class DetectWall extends Thread implements Behavior 
	{
	  private boolean _suppressed = false;
	  private boolean active = false;
	  private int lightVal = 255;
	  DifferentialPilot robot;

	  public DetectWall()
	  {
		DifferentialPilot robot = new DifferentialPilot(5.475F, 16F, Motor.C, Motor.A, false);  //5.5cm 11.5cm width measured. 5.475cm and 10.75 calculated.

	    light = new LightSensor(SensorPort.S1);
	    light.setFloodlight(true);
	    this.setDaemon(true);
	    this.start();
	  }
	  
	  public void run()
	  {
	    while ( true ) {
	    	lightVal = light.getLightValue();
	    	LCD.drawInt(lightVal, 0, 4);
	    }
	  }

	  public int takeControl() throws InterruptedException
	  {
	    if (lightVal > 50) // WHITE THRESHOLD
	    {	   
	    	if(	light.getLightValue() > 50)
	    	{
	    		return 199;
	    	}
	    }
	    if ( active)
	       return 190;
	    return 0;
	  }

	  public void suppress()
	  {
	    _suppressed = true;// standard practice for suppress methods  
	  }

	  public void action()
	  {
	    _suppressed = false;
	    active = true;
	    Sound.beepSequenceUp();
	    
	    //Motor.A.setSpeed(500);
	    //Motor.C.setSpeed(500);
		
	    // Backward for 1000 msec
	    LCD.drawString("Drive backward",0,3);
	    Motor.A.backward();
	    Motor.C.backward();
	    int now = (int)System.currentTimeMillis();

	    while (!_suppressed && ((int)System.currentTimeMillis()< now + 350) )
	    {
	       Thread.yield(); //don't exit till suppressed
	    }

	    
	    // Turn
	    LCD.drawString("Turn          ",0,3);
	    Motor.A.rotate(2*(-180), true);// start Motor.A rotating backward
	    Motor.C.rotate(2*(180), true);  // rotate C farther to make the turn
	    while (!_suppressed && Motor.C.isMoving())
	    {
	      Thread.yield(); //don't exit till suppressed
	    }
	    Motor.A.stop(); 
	    Motor.C.stop();
	    LCD.drawString("Stopped       ",0,3);
	    Sound.beepSequence();
	    
	    active = false;
	    
	  }
	  private LightSensor light;
	}

	class Exit implements Behavior
	{
	  private boolean _suppressed = false;

	  public int takeControl()
	  {
	    if ( Button.ESCAPE.isPressed() )
	    	return 500;
	    return 0;
	  }

	  public void suppress()
	  {
	    _suppressed = true;// standard practice for suppress methods  
	  }

	  public void action()
	  {
	    System.exit(0);
	  }
}
