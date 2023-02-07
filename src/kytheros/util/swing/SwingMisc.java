package kytheros.util.swing;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

public class SwingMisc {

	/**
	 * Will center the window on the screen.
	 * 
	 * @param window Window that will be centered.
	 * @param screen Optional. Enter screen index, or if nothing is entered the
	 *               first screen is used.
	 * @return <code>false</code> if the target screen was <code>null</code>. Otherwise, <code>true</code>.
	 */
	public static boolean centerWindow(Window window, int... screen) {
		GraphicsDevice device=null;
		try {
			device = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getScreenDevices()[screen.length > 0 ? screen[0] : 0];
		} catch (NullPointerException e) {
			throw new RuntimeException("Screen was null. Couldn't center window;\n" + e.getMessage());
		}
		int topLeftX, topLeftY, screenX, screenY, windowPosX, windowPosY;

		topLeftX = device.getDefaultConfiguration().getBounds().x;
		topLeftY = device.getDefaultConfiguration().getBounds().y;
		screenX = device.getDefaultConfiguration().getBounds().width;
		screenY = device.getDefaultConfiguration().getBounds().height;

		windowPosX = ((screenX - window.getWidth()) / 2) + topLeftX;
		windowPosY = ((screenY - window.getHeight()) / 2) + topLeftY;

		window.setLocation(windowPosX, windowPosY);
		return true;
	}
}
