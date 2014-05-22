import lejos.robotics.navigation.Pose;

public class UtilityScenarios {

	static double mesureLineDistance(TrackNavigator navigator) {

		double speed = navigator.getMoveController().getTravelSpeed();
//		navigator.getMoveController().setTravelSpeed(3);

		navigator.getMoveController().forward();
		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop(); // stop at firstLine
		Pose p1 = null;
		try {
			Thread.sleep(200);

			p1 = navigator.getPoseProvider().getPose();

			navigator.getMoveController().forward();
			Thread.sleep(7000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop(); // stop at secondLine

		Pose p2 = navigator.getPoseProvider().getPose();

		navigator.getMoveController().setTravelSpeed(speed);

		double x2 = Math.pow(p1.getX() - p2.getX(), 2);
		double y2 = Math.pow(p1.getY() - p2.getY(), 2);

		double result = Math.sqrt(x2 + y2);

		return result;
	}

	static void calibrationProgram(TrackNavigator navigator) {
		int squareSize = 30;
			
		while (true) {
			navigator.addWaypoint(0, squareSize);
			navigator.addWaypoint(squareSize, squareSize);
			navigator.addWaypoint(squareSize, 0);
			navigator.addWaypoint(0, 0);
			navigator.followPath();
			navigator.waitForStop();
		}
	}

}
