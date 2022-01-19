package krythos.util.system_utils;

public class SystemUtils {

	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	public static String getUserAppdata() {
		return getUserHome() + "\\AppData";
	}

	public static String getUserAppdataLocal() {
		return getUserHome() + "\\AppData\\Local";
	}

	public static String getUserAppdataLocalLow() {
		return getUserHome() + "\\AppData\\LocalLow";
	}

	public static String getUserAppdataRoaming() {
		return getUserHome() + "\\AppData\\Roaming";
	}

	public static String getCWD() {
		return System.getProperty("user.dir");
	}

	public static String getOS() {
		return System.getProperty("os.name");
	}

	public static boolean isWindows() {
		return getOS().contains("Windows");
	}

	public static boolean isLinux() {
		return getOS().contains("Linux");
	}
}
