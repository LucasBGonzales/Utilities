package krythos.util.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class Log {
	public static final int LEVEL_DISABLED = 0;
	public static final int LEVEL_ERROR = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_DEBUG = 4;

	/**
	 * Determines which logs are printed to console.
	 */
	private static int m_LogLevel = LEVEL_DISABLED;

	/**
	 * Determines whether logs are written to file.
	 */
	private static boolean fWriteLogs = false;


	public static int getLineNumber() {
		return (new Throwable()).getStackTrace()[1].getLineNumber();
	}


	public static void debug(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("debug", message);
		if (m_LogLevel >= LEVEL_DEBUG)
			println("[DEBUG] " + message);
	}


	public static void error(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("error", message);
		if (m_LogLevel >= LEVEL_ERROR)
			println("[ERROR] " + message);
	}


	public static void info(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("info", message);
		if (m_LogLevel >= LEVEL_INFO)
			println("[INFO] " + message);
	}


	public static String getTimeStamp() {
		return DateTimeFormatter.ofPattern("HH_mm_ss").format(LocalDateTime.now());
	}


	public static String getDateStamp() {
		return DateTimeFormatter.ofPattern("yyMMdd_HH_mm_ss").format(LocalDateTime.now());
	}


	public static void print(Object message) {
		System.out.print(message);
	}


	public static void printDialog(Object message) {
		JOptionPane.showMessageDialog(null, message);
	}


	public static void println(Object message) {
		System.out.println(message);
	}


	public static void setLevel(int level) {
		m_LogLevel = level;
	}


	public static void throwRuntimeException(Object source, RuntimeException rte) {
		error(source, rte.getMessage());
		throw rte;
	}


	public static void throwRuntimeException(Object source, Object message) {
		error(source, message);
		throw new RuntimeException("[" + source.toString() + "] " + message);
	}


	public static void warn(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("warning", message);
		if (m_LogLevel >= LEVEL_WARNING)
			println("[WARNING] " + message);
	}


	private static String contructMessage(Object source, Object message) {
		source = source == null ? autoSource() : source;
		message = message == null ? "Unspecified Message (Why?!)" : message;
		return "[" + getTimeStamp() + "] [" + source.toString().trim() + "] " + message.toString().trim();
	}


	/**
	 * Tries to determine the class and line number within that class
	 * where this log was made. Assumes the stack is 3 calls deep.
	 * This should be accurate, I'm going to test this for a while to see
	 * if there are any unforeseen problems. I may make this an automatic
	 * sourcing later.
	 * 
	 * @return {@link String} representing the ClassName and LineNumber of the log-call.
	 */
	private static String autoSource() {
		return (new Throwable()).getStackTrace()[3].getClassName() + "."
				+ (new Throwable()).getStackTrace()[3].getLineNumber();
	}


	private static void writeLog(String filename, Object message) {
		if (!fWriteLogs)
			return;

		// Create File Name
		filename = filename.trim() + ".txt";
		File file;
		try {
			file = new File(new File(".").getCanonicalPath() + "\\logs\\" + filename);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		// Determine file exists, if not then make try to make it exist. If
		// both of these fail, validOutput is false.

		try {
			Files.write(Paths.get(file.getAbsolutePath()), ("\n" + message.toString()).getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			fWriteLogs = false;
			error("Log", "Failed to write log file: Could not access: " + file.getAbsolutePath());
		}
	}


	/**
	 * Clears all files in the log folder.
	 */
	private static void clearLogFolder() {
		File[] files = null;
		try {
			files = (new File(new File(".").getCanonicalPath() + "\\logs")).listFiles();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (File f : files)
			f.delete();
	}


	public Log(boolean f_writelogs) {
		this(LEVEL_INFO, f_writelogs);
	}


	public Log(int level, boolean f_writelogs) {
		m_LogLevel = level;
		fWriteLogs = f_writelogs;
		clearLogFolder();
	}

}
