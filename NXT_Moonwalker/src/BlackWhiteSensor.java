import lejos.nxt.LightSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;


public class BlackWhiteSensor {

	private LightSensor ls;
	private int blackLightValue;
	private int whiteLightValue;
	public int blackWhiteThreshold;
	private int lastValue = 1000;

	public BlackWhiteSensor(LightSensor ls, int blackLightValue, int whiteLightValue) {
		this(ls);
		calibrate(blackLightValue, whiteLightValue);
	}

	public BlackWhiteSensor(LightSensor ls) {
		this.ls = ls;
		// Use the light sensor as a reflection sensor
		ls.setFloodlight(true);
	}

	private int read(String color) {

		int lightValue = 0;

		while (Button.ENTER.isDown())
			;

		LCD.clear();
		LCD.drawString("Press ENTER", 0, 0);
		LCD.drawString("to callibrate", 0, 1);
		LCD.drawString(color, 0, 2);
		while (!Button.ENTER.isUp()) {
			lightValue = light();
			LCD.drawInt(lightValue, 4, 10, 2);
			LCD.refresh();
		}
		return lightValue;
	}

	/**
	 * Manually calibrate Black and White Sensor
	 */
	public void calibrate() {
		whiteLightValue = read("white");
		blackLightValue = read("black");
		// The threshold is calculated as the median between
		// the two readings over the two types of surfaces
		blackWhiteThreshold = (blackLightValue + whiteLightValue) / 2;
	}
	
	/**
	 * Automatically calibrate Black and White Sensor
	 * 
	 * @param blackLightValue ignored if -1, previous value is used instead
	 * @param whiteLightValue ignored if -1
	 */
	public void calibrate(int blackLightValue, int whiteLightValue) {
		if(blackLightValue != -1)
			this.blackLightValue = blackLightValue;
		if(whiteLightValue != -1)
			this.whiteLightValue = whiteLightValue;
		// The threshold is calculated as the median between
		// the two readings over the two types of surfaces
		blackWhiteThreshold = (blackLightValue + whiteLightValue) / 2;
	}

	
	public void setBlackWhiteThreshold(int threshold){
		blackWhiteThreshold = threshold;
	}
	
	public int getBlackWhiteThreshold(){
		return blackWhiteThreshold;
	}
	
	public boolean wasBlack() {
		synchronized (this) {
			return (lastValue < getBlackWhiteThreshold());
		}
	}

	public boolean wasWhite() {
		synchronized (this) {
			return (lastValue > getBlackWhiteThreshold());
		}
	}

	public boolean isBlack() {
		return (light() < getBlackWhiteThreshold());
	}

	public boolean isWhite() {
		return !isBlack();
	}
	
	public int light() {
		synchronized (this) {
			lastValue = ls.readNormalizedValue();   //readValue();
			return lastValue;			
		}		
	}
	
	public int getChachedLightValue(){
		synchronized (this) {
			return lastValue;
		}
	}

}
