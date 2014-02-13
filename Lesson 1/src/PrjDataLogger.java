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

		dlog.setColumns(new LogColumn[] { new LogColumn("sensor", LogColumn.DT_LONG) });
	}

	void log(long sensorVal) throws InterruptedException {
		dlog.writeLog(sensorVal);
		dlog.finishLine();
	}

	protected void finalize() {
		dlog.stopLogging();
	}
}
