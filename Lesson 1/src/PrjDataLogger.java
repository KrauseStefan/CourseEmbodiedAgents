import java.io.IOException;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.util.LogColumn;
import lejos.util.NXTDataLogger;

public class PrjDataLogger {
	NXTDataLogger dlog = null;
	NXTConnection conn = null;

	PrjDataLogger() {
		dlog = new NXTDataLogger();

	}

	void connect() throws IOException {
		conn = Bluetooth.waitForConnection(0, NXTConnection.PACKET);
		dlog.startRealtimeLog(conn);

		dlog.setColumns(new LogColumn[] { new LogColumn("sensor", LogColumn.DT_INTEGER, 2) });

		// code example below assumes gyro object has the illustrated methods
		// gyro.setAccScale2G();
		// float gx, gy, pitch;
		//
		// while (Button.ESCAPE.isUp()) {
		// gx = gyro.getLastAccel(0);
		// gy = gyro.getLastAccel(1);
		// pitch = gyro.getLastPitch();
		// dlog.writeLog(gx);
		// dlog.writeLog(gy);
		// dlog.writeLog(pitch);
		// dlog.writeLog(gyro.getAngularVelocity());
		// dlog.writeLog(gyro.getHeading());
		// dlog.finishLine();
		// Delay.msDelay(50);
		// gyro.readAllData();
		// }
		// dlog.stopLogging();

	}

	void log(int sensorVal) throws InterruptedException {
		dlog.writeLog(sensorVal);
		dlog.finishLine();
	}

	protected void finalize() {
		dlog.stopLogging();
	}
}
