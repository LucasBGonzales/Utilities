package krythos.util.logger;

import javax.swing.JOptionPane;

public class Log {

	public static final int LEVEL_DISABLED = 0;
	public static final int LEVEL_ERROR = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_DEBUG = 3;

	private static int m_LogLevel;


	public Log() {
		this(LEVEL_INFO);
	}


	public Log(int level) {
		m_LogLevel = level;
	}


	public static void setLevel(int level) {
		m_LogLevel = level;
	}


	public static void print(String message) {
		System.out.print(message);
	}


	public static void println(String message) {
		System.out.println(message);
	}


	public static void printDialog(String message) {
		JOptionPane.showMessageDialog(null, message);
	}


	public static void debug(String tag, String message) {
		if (m_LogLevel >= LEVEL_DEBUG)
			println("[DEBUG] [" + tag.trim() + "] " + message.trim());
	}


	public static void warn(String tag, String message) {
		if (m_LogLevel >= LEVEL_WARNING)
			println("[WARNING] [" + tag.trim() + "] " + message.trim());
	}


	public static void info(String tag, String message) {
		if (m_LogLevel >= LEVEL_INFO)
			println("[INFO] [" + tag.trim() + "] " + message.trim());
	}


	public static void error(String tag, String message) {
		if (m_LogLevel >= LEVEL_ERROR)
			println("[ERROR] [" + tag.trim() + "] " + message.trim());
	}

}
