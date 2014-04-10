import lejos.nxt.MotorPort;
import lejos.nxt.Sound;


public class catchLineSecond { 
	
	public static void run(int initialState, int power, BlackWhiteSensor sensorLeft, BlackWhiteSensor sensorRight, int upDown) throws InterruptedException
	{
		
		//initialState: 0 = {car|}, 1 = {|car}
		Car.forward(power, power);
		
		/*DataLogger dataLogger = new DataLogger("dpLine.txt");
		int leftSensorVal = 0;
		int rightSensorVal = 0;
		long start = System.currentTimeMillis() + 1000;
		while(System.currentTimeMillis() < start)
		{
			rightSensorVal = sensorRight.light();
			leftSensorVal = sensorLeft.light();
			dataLogger.writeLine("r:"+rightSensorVal + " l:"+leftSensorVal + "time:" + System.currentTimeMillis());
		}
		*/
		int maxError = sensorLeft.whiteLightValue - sensorLeft.blackLightValue;
		
		
		int leftSensorVal = 0;
		int rightSensorVal = 0;
		
		if(upDown == 0)
		{
		if (initialState == 0)
		{
			Car.forward(power, power-5);
			while(true)
			{
				rightSensorVal = sensorRight.light();
				
				if(rightSensorVal < sensorRight.whiteLightValue - 22)
				{
					Thread.sleep(30);
					int tempRightLight = sensorRight.light();
					
					if(rightSensorVal - tempRightLight > 90)
					{
						Car.forward(0, power);
					
						Thread.sleep((long) (((rightSensorVal - tempRightLight)+100)*0.7));
					}
					else if(rightSensorVal - tempRightLight > 30)
					{
						Car.forward(0, power);
						Thread.sleep((long) ((rightSensorVal - tempRightLight)*3.5));
					}
					else
					{
						Thread.sleep((65 - (rightSensorVal - tempRightLight))*3);
					}
					/*
					while(sensorLeft.light() > sensorLeft.whiteLightValue-14*scale)
					{}*/
					break;
				}
				
			}
		}
		if(initialState == 1)
		{
			Car.forward(power-3, power);
			while(true)
			{
				leftSensorVal = sensorLeft.light();
				
				if(leftSensorVal < sensorLeft.whiteLightValue - 22)
				{
					Thread.sleep(30);
					int tempLeftLight = sensorLeft.light();
					
					if(leftSensorVal - tempLeftLight > 90)
					{
						Car.forward(power, 0);
						Thread.sleep((long) (((leftSensorVal - tempLeftLight)+100)*1.3));
					}
					else if(leftSensorVal - tempLeftLight > 30)
					{
						Car.forward(power, 0);
						Thread.sleep((long) (((leftSensorVal - tempLeftLight))*4.1));
					}
					else
					{
						Thread.sleep((65 - (leftSensorVal - tempLeftLight))*5);
					}
					/*while(sensorLeft.light() > sensorRight.whiteLightValue-14)
					{}*/
					break;
				}
				
			}
		}
		
		}
		else
		{
			if (initialState == 0)
			{
				Car.forward(power, power-1);
				while(true)
				{
					rightSensorVal = sensorRight.light();
					
					if(rightSensorVal < sensorRight.whiteLightValue - 30)
					{
						Thread.sleep(30);
						int tempRightLight = sensorRight.light();
						
						if(rightSensorVal - tempRightLight > 90)
						{
							Car.forward(0, 100);
						
							Thread.sleep((long) (((rightSensorVal - tempRightLight)+100)*0.8));
						}
						else if(rightSensorVal - tempRightLight > 30)
						{
							Car.forward(0, 100);
							Thread.sleep((long) ((rightSensorVal - tempRightLight)*1));
						}
						else
						{
							Thread.sleep((65 - (rightSensorVal - tempRightLight))*5);
						}
						/*
						while(sensorLeft.light() > sensorLeft.whiteLightValue-14*scale)
						{}*/
						Car.stop();
						break;
					}
					
				}
			}
			if(initialState == 1)
			{
				Car.forward(power-3, power);
				while(true)
				{
					leftSensorVal = sensorLeft.light();
					
					if(leftSensorVal < sensorLeft.whiteLightValue - 45)
					{
						Thread.sleep(30);
						int tempLeftLight = sensorLeft.light();
						
						if(leftSensorVal - tempLeftLight > 90)
						{
							Car.forward(100, 0);
							Thread.sleep((long) (((leftSensorVal - tempLeftLight)+100)*0.8));
						}
						else if(leftSensorVal - tempLeftLight > 30)
						{
							Car.forward(100, 0);
							Thread.sleep((long) (((leftSensorVal - tempLeftLight))*1));
						}
						else
						{
							Thread.sleep((65 - (leftSensorVal - tempLeftLight))*5);
						}
						/*while(sensorLeft.light() > sensorRight.whiteLightValue-14)
						{}*/
						break;
					}
					
				}
			}
		}
		//dataLogger.close();
	}

}
