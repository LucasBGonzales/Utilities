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
}
