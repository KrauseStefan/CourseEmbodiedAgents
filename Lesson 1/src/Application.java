import lejos.nxt.*;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LightSensor light = new LightSensor(SensorPort.S1);
		light.setFloodlight(true);
		LCD.drawString("Light %:", 0, 0);
		
		while(! Button.LEFT.isDown()) {
			LCD.drawInt(light.readValue(), 3, 9, 0);
		}
	}

}
