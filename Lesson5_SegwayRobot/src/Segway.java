import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * A controller for a self-balancing Lego robot with a light sensor on port 2.
 * The two motors should be connected to port B and C.
 * 
 * Building instructions in Brian Bagnall: Maximum Lego NXTBuilding Robots with
 * Java Brains</a>, Chapter 11, 243 - 284
 * 
 * @author Brian Bagnall
 * @version 26-2-13 by Ole Caprani for leJOS version 0.9.1
 */

public class Segway implements Runnable {
	private String connected = "Connected";
	private String started = "Started...From the bottom";
	private String waiting = "Waiting...";
	private String closing = "Closing...";

	private BTConnection btc;
	private DataInputStream dis;
	private DataOutputStream dos;

	// PID constants
	int KP = 28;
	int KI = 4;
	int KD = 33;
	int SCALE = 18;

	// Global vars:
	int offset;
	int prev_error;
	float int_error;

	private Thread t1;
	
	LightSensor ls;

	public Segway() {
		ls = new LightSensor(SensorPort.S2, true);
		
		LCD.drawString(started, 0, 0);
	}

	public void getBalancePos() {
		LCD.clear();
		LCD.drawString(waiting, 0, 0);

		offset = ls.readNormalizedValue();
	}

	public void sendInitData() {
		btc = Bluetooth.waitForConnection();

		LCD.clear();
		LCD.drawString(connected, 0, 0);

		dis = btc.openDataInputStream();
		dos = btc.openDataOutputStream();
		LCD.clear();
		LCD.drawInt(offset, 2, 4);
		try {
			Thread.sleep(2000);
			dos.writeInt(offset);
			dos.flush();
			dos.writeInt(KP);
			dos.flush();
			dos.writeInt(KI);
			dos.flush();
			dos.writeInt(KD);
			dos.flush();
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString("ERROR_SendInit", 2, 1);

		}
	}

	public void readPidData() {
		try {
			LCD.drawString("Thread started.", 0, 0);
			offset = dis.readInt();
			LCD.drawInt(offset, 0, 1);
			KP = dis.readInt();
			LCD.drawInt(KP, 0, 2);
			KI = dis.readInt();
			LCD.drawInt(KI, 0, 3);
			KD = dis.readInt();
			LCD.drawInt(KD, 0, 4);
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString("Thread ERROR.", 0, 0);
		}
	}

	public void pidControl() {
		t1 = new Thread(this);
		LCD.clear();
		t1.start();
		
		while (!Button.ESCAPE.isDown()) {
			int normVal = ls.readNormalizedValue();

			// Proportional Error:
			int error = normVal - offset;
			// Adjust far and near light readings:
			if (error < 0)
				error = (int) (error * 1.8F);

			// Integral Error:
			int_error = ((int_error + error) * 2) / 3;

			// Derivative Error:
			int deriv_error = error - prev_error;
			prev_error = error;

			int pid_val = (int) (KP * error + KI * int_error + KD * deriv_error)
					/ SCALE;

			if (pid_val > 100)
				pid_val = 100;
			if (pid_val < -100)
				pid_val = -100;

			// Power derived from PID value:
			int power = Math.abs(pid_val);
			power = 55 + (power * 45) / 100; // NORMALIZE POWER

			if (pid_val > 0) {
				MotorPort.B.controlMotor(power, BasicMotorPort.FORWARD);
				MotorPort.C.controlMotor(power, BasicMotorPort.FORWARD);
			} else {
				MotorPort.B.controlMotor(power, BasicMotorPort.BACKWARD);
				MotorPort.C.controlMotor(power, BasicMotorPort.BACKWARD);
			}
		}
	}

	public void shutDown() {
		// Shut down light sensor, motors
		Motor.B.flt();
		Motor.C.flt();
		ls.setFloodlight(false);

		LCD.clear();
		LCD.drawString(closing, 0, 0);
		try {
			dis.close();
			dos.close();
			Thread.sleep(100); // wait for data to drain
			btc.close();
		} catch (Exception e) {
		}
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		Segway sej = new Segway();
		sej.getBalancePos();
		sej.sendInitData();
		sej.pidControl();
		sej.shutDown();
	}

	@Override
	public void run() {
		while (true) {
			this.readPidData();
		}
	}
}