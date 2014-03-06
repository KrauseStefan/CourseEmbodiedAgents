import lejos.nxt.SensorPort;

public class App {


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		ThreeColorSensor b = new ThreeColorSensor(SensorPort.S3);

		b.calibrate();

	}

}
