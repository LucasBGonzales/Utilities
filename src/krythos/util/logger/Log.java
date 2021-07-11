package krythos.util.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class Log {

	public static final int LEVEL_DISABLED = 0;
	public static final int LEVEL_ERROR = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_DEBUG = 4;
	private static int m_LogLevel = LEVEL_DISABLED;

	private static File m_outputFile = null;
	private static String m_outputString = "";
	private static boolean m_validOutput = false;


	public static void debug(Object source, String message) {
		message = "[DEBUG] [" + source.toString().trim() + "] " + message.trim();
		addToLog(message);
		if (m_LogLevel >= LEVEL_DEBUG)
			println(message);
	}


	public static void error(Object source, String message) {
		message = "[ERROR] [" + source.toString().trim() + "] " + message.trim();
		addToLog(message);
		if (m_LogLevel >= LEVEL_ERROR)
			println(message);
	}


	public static void info(Object source, String message) {
		message = "[INFO] [" + source.toString().trim() + "] " + message.trim();
		addToLog(message);
		if (m_LogLevel >= LEVEL_INFO)
			println(message);
	}


	public static void logFile() {
		if (m_validOutput == false)
			return;

		String name = "log_" + DateTimeFormatter.ofPattern("yyMMdd_HH_mm_ss").format(LocalDateTime.now()) + ".txt";
		File file = new File(m_outputFile.getAbsolutePath() + "\\" + name);

		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(m_outputString);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void print(String message) {
		addToLog(message);
		System.out.print(message);
	}


	public static void printDialog(String message) {
		JOptionPane.showMessageDialog(null, message);
	}


	public static void println(String message) {
		addToLog(message);
		System.out.println(message);
	}


	public static void setLevel(int level) {
		m_LogLevel = level;
	}


	/**
	 * 
	 * @param location
	 * @return
	 */
	public static boolean setOutputLocation(File location) {
		if (location.getName().contains("."))
			m_outputFile = null;
		else
			m_outputFile = location.mkdirs() || location.exists() ? location : null;

		// Determine if output is valid (non-null). Return validity as boolean.
		m_validOutput = (m_outputFile != null);
		
		if (m_validOutput)
			info("Log", "Successfully Set Output Location");
		else
			error("Log", "Failed to Set Output Location");
		return m_validOutput;
	}


	public static void throwRuntimeException(Object source, RuntimeException rte) {
		error(source, rte.getMessage());
		throw rte;
	}


	public static void throwRuntimeException(Object source, String message) {
		error(source, message);
		throw new RuntimeException("[" + source.toString() + "] " + message);
	}


	public static void warn(Object source, String message) {
		message = "[WARNING] [" + source.toString().trim() + "] " + message.trim();
		addToLog(message);
		if (m_LogLevel >= LEVEL_WARNING)
			println(message);
	}


	private static void addToLog(String message) {
		m_outputString += "\n" + message;
	}


	public Log() {
		this(LEVEL_INFO);
	}


	public Log(int level) {
		m_LogLevel = level;
		try {
			setOutputLocation(new File(new File(".").getCanonicalPath() + "\\logs"));
		} catch (IOException e) {
			error(this, "Failed to set output location for Log file.");
			e.printStackTrace();
		}
	}

}
