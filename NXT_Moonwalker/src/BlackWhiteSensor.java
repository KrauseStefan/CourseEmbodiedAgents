import lejos.nxt.*;

/**
 * A sensor that is able to distinguish a black/dark surface from a white/bright
 * surface.
 * 
 * Light percent values from an active light sensor and a threshold value
 * calculated based on a reading over a black/dark surface and a reading over a
 * light/bright surface is used to make the distinction between the two types of
 * surfaces.
 * 
 * @author Ole Caprani
 * @version 20.02.13
 */

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
		while (!Button.ENTER.isPressed()) {
			lightValue = light();
			LCD.drawInt(lightValue, 4, 10, 2);
			LCD.refresh();
		}
		return lightValue;
	}

	public void calibrate() {
		whiteLightValue = read("white");
		blackLightValue = read("black");
		// The threshold is calculated as the median between
		// the two readings over the two types of surfaces
		blackWhiteThreshold = (blackLightValue + whiteLightValue) / 2;
	}

	public void calibrate(int blackLightValue, int whiteLightValue) {
		if(blackLightValue > 200) //blackLightValue != -1 && 
			this.blackLightValue = blackLightValue;
		if(whiteLightValue != -1)
			this.whiteLightValue = whiteLightValue;
		// The threshold is calculated as the median between
		// the two readings over the two types of surfaces
		blackWhiteThreshold = (this.blackLightValue + this.whiteLightValue) / 2;
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
			if(lastValue > whiteLightValue){
				calibrate(-1, lastValue);
			}else if(lastValue < blackLightValue){
				calibrate(lastValue, -1);
			}

			return lastValue;			
		}		
	}
	
	public int getChachedLightValue(){
		synchronized (this) {
			return lastValue;
		}
	}

}
