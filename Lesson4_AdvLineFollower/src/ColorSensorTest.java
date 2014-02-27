
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;

/**
 * Test program for the LEGO Color Sensor in Full mode.
 * A modified version of the program ColorSensorTest in samples.zip
 */

public class ColorSensorTest
{
    static void displayColor(String name, int raw, int calibrated, int line)
    {
        LCD.drawString(name, 0, line);
        LCD.drawInt(raw, 5, 6, line);
        LCD.drawInt(calibrated, 5, 11, line);
    }

    public static void main(String [] args) throws Exception
    {
        ColorSensor cs = new ColorSensor(SensorPort.S1);
        String colorNames[] = {"None", "Red", "Green", "Blue", "Yellow",
                                "Megenta", "Orange", "White", "Black", "Pink",
                                "Grey", "Light Grey", "Dark Grey", "Cyan"};

        cs.setFloodlight(Color.WHITE);

        while (!Button.ESCAPE.isDown())
        {
        	
            LCD.drawString("Mode: " + "Full", 0, 0);
            LCD.drawString("Color   Raw  Cal", 0, 1);

            ColorSensor.Color vals = cs.getColor();
            ColorSensor.Color rawVals = cs.getRawColor();
            displayColor("Red", rawVals.getRed(), vals.getRed(), 2);
            displayColor("Green", rawVals.getGreen(), vals.getGreen(), 3);
            displayColor("Blue", rawVals.getBlue(), vals.getBlue(), 4);
            displayColor("Light", cs.getRawLightValue(), cs.getLightValue(), 5);
            LCD.drawString("Color:          ", 0, 6);
            LCD.drawString(colorNames[vals.getColor() + 1], 7, 6);
            LCD.drawString("Color val:          ", 0, 7);
            LCD.drawInt(vals.getColor(), 3, 11, 7);
        }
    }
}