import lejos.nxt.SensorPort;

public class App {

	public static void main(String[] args) {
		ThreeColorSensor b = new ThreeColorSensor(SensorPort.S3);

		b.calibrate();

	}

}
