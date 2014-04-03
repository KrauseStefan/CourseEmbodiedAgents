import lejos.nxt.*;
import lejos.util.Delay;

/*
 * Avoid behavior
 */

class Escape extends Thread {
	private SharedCar car = new SharedCar();

	private int power = 70, ms = 500;

	TouchSensor bmpLeft = new TouchSensor(SensorPort.S2);
	TouchSensor bmpRight = new TouchSensor(SensorPort.S3);

	public Escape(SharedCar car) {
		this.car = car;
	}

	public void run() {
		while (true) {
			if (bmpLeft.isPressed()) {
				car.backspin(ms*2, power, "right");
			} else if (bmpRight.isPressed()) {
				car.backspin(ms*2, power, "left");
			} else {
				car.noCommand();
			}
		}
	}
}
