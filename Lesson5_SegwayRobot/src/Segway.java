import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.BaseMotor;

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
	float KP = 18;
	float KI = 3;
	float KD = 9;
	float SCALE = 18;

	// Global vars:
	float offset;
	float prev_error;
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
		getBalancePos();
		LCD.clear();
		LCD.drawString(connected, 0, 0);

		dis = btc.openDataInputStream();
		dos = btc.openDataOutputStream();
		LCD.clear();
		LCD.drawInt((int) offset, 2, 4);
		try {
			Thread.sleep(2000);
			dos.writeFloat(offset);
			dos.flush();
			dos.writeFloat(KP);
			dos.flush();
			dos.writeFloat(KI);
			dos.flush();
			dos.writeFloat(KD);
			dos.flush();
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString("ERROR_SendInit", 2, 1);

		}
	}

	public void readPidData() {
		try {
			LCD.drawString("Thread started.", 0, 0);
			offset = dis.readFloat();
			LCD.drawInt((int) offset, 0, 1);
			KP = dis.readFloat();
			LCD.drawInt((int) KP, 0, 2);
			KI = dis.readFloat();
			LCD.drawInt((int) KI, 0, 3);
			KD = dis.readFloat();
			LCD.drawInt((int) KD, 0, 4);
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString("Thread ERROR.", 0, 0);
		}
	}

	public void pidControl() {
		t1 = new Thread(this);
		LCD.clear();
		t1.start();
		
		float preNormVal = ls.readNormalizedValue();
		while (!Button.ESCAPE.isDown()) {
			float normVal = preNormVal;
			preNormVal = ls.readNormalizedValue();
			
			normVal = (normVal + preNormVal)/2;
			
			// Proportional Error:
			float error = normVal - offset;
			// Adjust far and near light readings:
			if (error < 0)
				error = (error * 1.8F);

			// Integral Error:
			int_error = ((int_error + error) * 2) / 3;

			// Derivative Error:
			float deriv_error = error - prev_error;
			prev_error = error;

			float pid_val = (KP * error + KI * int_error + KD * deriv_error) / SCALE;

			float maxVal = 20;
			if (pid_val > maxVal)
				pid_val = maxVal;
			if (pid_val < -maxVal)
				pid_val = -maxVal;

			// Power derived from PID value:
			int power = (int) Math.abs(pid_val);
			power = 55 + power;

//			if(pid_val > -2 && pid_val < 2 ){
//				MotorPort.B.controlMotor(power, BasicMotorPort.STOP);
//				MotorPort.C.controlMotor(0, BasicMotorPort.STOP);
//			}else 
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
//		sej.getBalancePos();
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