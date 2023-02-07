package kytheros.util.misc;

public class SystemUtils {

	/**
	 * Returns the "user.home" variable. For Windows this is usually "C:\Users\User"
	 * and for Linux "/home/user/"
	 * 
	 * @return User home location.
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}


	/**
	 * Windows-specific. Returns the address for the user's AppData folder. If the
	 * OS is Linux, this will throw an error.
	 * 
	 * @return The address for the user's AppData folder.
	 * @throws WrongOSException
	 */
	public static String getUserAppdata() throws WrongOSException {
		if (isWindows() == false) {
			throw new WrongOSException(
					"The function called is for a Windows OS. This OS is not recognized as Windows OS.");
		} else
			return getUserHome() + "\\AppData";
	}


	/**
	 * Windows-specific.
	 * 
	 * @return The address for Local Application Data.
	 * @throws WrongOSException
	 */
	public static String getUserAppdataLocal() throws WrongOSException {
		return getUserAppdata() + "\\Local";
	}


	/**
	 * Windows-specific.
	 * 
	 * @return The address for Locallow Application Data.
	 * @throws WrongOSException
	 */
	public static String getUserAppdataLocalLow() throws WrongOSException {
		return getUserAppdata() + "\\LocalLow";
	}


	/**
	 * Windows-specific.
	 * 
	 * @return The address for Roaming Application Data.
	 * @throws WrongOSException
	 */
	public static String getUserAppdataRoaming() throws WrongOSException {
		return getUserAppdata() + "\\Roaming";
	}


	/**
	 * 
	 * @return The current working directory of this application.
	 */
	public static String getCurrentWorkingDirectory() {
		return System.getProperty("user.dir");
	}


	/**
	 * 
	 * @return "os.name" system property.
	 */
	public static String getOS() {
		return System.getProperty("os.name");
	}


	/**
	 * @return Whether "Windows" is a part of "os.name" system property.
	 */
	public static boolean isWindows() {
		return getOS().contains("Windows");
	}


	/**
	 * @return Whether "Linux" is a part of "os.name" system property.
	 */
	public static boolean isLinux() {
		return getOS().contains("Linux");
	}


	public static class WrongOSException extends Exception {
		private static final long serialVersionUID = 997439750324500168L;


		public WrongOSException(String message) {
			super(message);
		}
	}
}
