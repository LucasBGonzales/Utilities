package krythos.util.logger;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class Log {
	// Determines whether logs are written to file.
	private boolean mfWriteLogs = false;
	// Determines which logs are printed to console.
	private int mLogLevel = LEVEL_DISABLED;

	public static final int LEVEL_DISABLED = 0;
	public static final int LEVEL_ERROR = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_DEBUG = 4;


	public Log() {
		this(true);
	}


	/**
	 * Specify whether to write logs to file.
	 * 
	 * @param mfWriteLogs
	 */
	public Log(boolean pWriteLogs) {
		this(LEVEL_INFO, pWriteLogs);
	}


	/**
	 * Specify level of logs to print and whether to write logs to file.
	 * 
	 * @param pLevel
	 * @param mfWriteLogs
	 */
	public Log(int pLevel, boolean pWriteLogs) {
		this.mLogLevel = pLevel;
		this.mfWriteLogs = pWriteLogs;
		clearLogFolder();
	}


	/**
	 * Tries to determine the class and line number within that class where this log
	 * was made. Assumes the stack is 3 calls deep. This should be accurate, I'm
	 * going to test this for a while to see if there are any unforeseen problems. I
	 * may make this an automatic sourcing later.
	 * 
	 * @return {@link String} representing the ClassName and LineNumber of the
	 *         log-call.
	 */
	private static String autoSource() {
		return (new Throwable()).getStackTrace()[4].getClassName() + "."
				+ (new Throwable()).getStackTrace()[4].getLineNumber();
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

		if (files != null)
			for (File f : files)
				f.delete();
	}


	/**
	 * Constructs the formatted message from the information provided.
	 * 
	 * @param source  Source of the message. If <code>null</code>, then a source
	 *                will be automatically determined.
	 * @param message Information to present; description of the message.
	 * @return {@link String} of the formatted message.
	 */
	private static String contructMessage(Object source, Object message) {
		source = source == null ? autoSource() : source;
		message = message == null ? "Unspecified Message (Why?!)" : message;
		return "[" + getTimeStamp() + "] [" + source.toString().trim() + "] " + message.toString().trim();
	}


	/**
	 * Writes (appends) a message to the specified filename, saved to a logs folder
	 * in the current working directory. If the file doesn't exist, it will be
	 * created.
	 * 
	 * @param filename Name of the file.
	 * @param message  Message to write to the file.
	 */
	private void writeLog(String filename, Object message) {
		if (!mfWriteLogs)
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
			mfWriteLogs = false;
			error("Log", "Failed to write log file: Could not access: " + file.getAbsolutePath());
		}
	}


	/**
	 * Variable arguments function. If there is only one {@link Object} passed, then
	 * that object will be treated as the message, and the source tag will be
	 * determined automatically. If there are two {@link Object}s passed, the first
	 * is treated as the source tag, the second the message. If three
	 * {@link Object}s are passed, then a {@link Dialog} will be shown displaying
	 * the message and the third {@link Object} will be the {@link Frame}. Null may
	 * be passed to use a default {@link Frame}. Any {@link Object}s beyond 3 will
	 * be ignored.
	 * 
	 * @param args {@link Object} array of options. ([Source], Message,
	 *             [OwnerFrame])
	 */
	public void debug(Object... args) {
		if (args.length == 1)
			debug(null, args[0]);
		else if (args.length > 1) {
			debug(args[0], args[1]);
			if (args.length > 2)
				showMessageDialog(args[2], args[1]);
		}
	}


	/**
	 * Outputs a debug message.
	 * 
	 * @param source  Source tag of message. If <code>null</code>, a source will be
	 *                automatically determined.
	 * @param message Message to print.
	 */
	public void debug(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("debug", message);
		if (mLogLevel >= LEVEL_DEBUG)
			println("[DEBUG] " + message);
	}


	/**
	 * Variable arguments function. If there is only one {@link Object} passed, then
	 * that object will be treated as the message, and the source tag will be
	 * determined automatically. If there are two {@link Object}s passed, the first
	 * is treated as the source tag, the second the message. If three
	 * {@link Object}s are passed, then a {@link Dialog} will be shown displaying
	 * the message and the third {@link Object} will be the {@link Frame}. Null may
	 * be passed to use a default {@link Frame}. Any {@link Object}s beyond 3 will
	 * be ignored.
	 * 
	 * @param args {@link Object} array of options. ([Source], Message,
	 *             [OwnerFrame])
	 */
	public void error(Object... args) {
		if (args.length == 1)
			error(null, args[0]);
		else if (args.length > 1) {
			error(args[0], args[1]);
			if (args.length > 2)
				showMessageDialog(args[2], args[1]);
		}
	}


	/**
	 * Outputs an error message.
	 * 
	 * @param source  Source tag of message. If <code>null</code>, a source will be
	 *                automatically determined.
	 * @param message Message to print.
	 */
	public void error(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("error", message);
		if (mLogLevel >= LEVEL_ERROR)
			println("[ERROR] " + message);
	}


	/**
	 * @return Formatted {@link String} representing the current date. (yyMMdd)
	 */
	public static String getDateStamp() {
		return DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now());
	}


	/**
	 * @return {@link Integer} representing the line number of the code that calls
	 *         this function.
	 */
	public static int getLineNumber() {
		return (new Throwable()).getStackTrace()[1].getLineNumber();

	}


	/**
	 * @return Formatted {@link String} representing the current time. (HH_mm_ss)
	 */
	public static String getTimeStamp() {
		return DateTimeFormatter.ofPattern("HH_mm_ss").format(LocalDateTime.now());
	}


	/**
	 * Variable arguments function. If there is only one {@link Object} passed, then
	 * that object will be treated as the message, and the source tag will be
	 * determined automatically. If there are two {@link Object}s passed, the first
	 * is treated as the source tag, the second the message. If three
	 * {@link Object}s are passed, then a {@link Dialog} will be shown displaying
	 * the message and the third {@link Object} will be the {@link Frame}. Null may
	 * be passed to use a default {@link Frame}. Any {@link Object}s beyond 3 will
	 * be ignored.
	 * 
	 * @param args {@link Object} array of options. ([Source], Message,
	 *             [OwnerFrame])
	 */
	public void info(Object... args) {
		if (args.length == 1)
			info(null, args[0]);
		else if (args.length > 1) {
			info(args[0], args[1]);
			if (args.length > 2)
				showMessageDialog(args[2], args[1]);
		}
	}


	/**
	 * Outputs an info message.
	 * 
	 * @param source  Source tag of message. If <code>null</code>, a source will be
	 *                automatically determined.
	 * @param message Message to print.
	 */
	public void info(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("info", message);
		if (mLogLevel >= LEVEL_INFO)
			println("[INFO] " + message);
	}


	/**
	 * Prints to <code>System.out.print()</code>.
	 * 
	 * @param message Message to print.
	 */
	public static void print(Object message) {
		System.out.print(message);
	}


	/**
	 * Prints to <code>System.out.println()</code>.
	 * 
	 * @param message Message to print.
	 */
	public static void println(Object message) {
		System.out.println(message);
	}


	/**
	 * Set the logging level. Logs that are greater than this level will not be
	 * printed to console, but will still be written to file if writing is enabled.
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		mLogLevel = level;
	}


	/**
	 * Creates a Message Dialog with a default Frame. Effectively the same as
	 * <code>showMesssageDialog(<b>null</b>, message)</code>.
	 * 
	 * @param message Message to display in the dialog.
	 */
	public void showMessageDialog(Object message) {
		showMessageDialog(null, message);
	}


	/**
	 * Creates a Message Dialog wth the given parent and message.
	 * 
	 * @param parentComponent determines the Frame in which the dialog is displayed;
	 *                        if null, or if the parentComponent has no Frame, a
	 *                        default Frame is used.
	 * @param message         Message to display in the dialog.
	 */
	public void showMessageDialog(Object parentComponent, Object message) {
		if (parentComponent instanceof Component || parentComponent == null)
			JOptionPane.showMessageDialog((Component) parentComponent, message);
		else
			throwRuntimeException("Log.showMessageDialog(parent,message)", "Invalid Parent");
	}


	/**
	 * Will log an error and throw a {@link RuntimeException}.
	 * 
	 * @param source  Source tag of the exception.
	 * @param message Message of the exception.
	 */
	public void throwRuntimeException(Object source, Object message) {
		error(source, message);
		throw new RuntimeException("[" + source.toString() + "] " + message);
	}


	/**
	 * Will log an error and throw the given {@link RuntimeException}.
	 * 
	 * @param source Source tag of the exception.
	 * @param rte    {@link RuntimeException} to throw.
	 */
	public void throwRuntimeException(Object source, RuntimeException rte) {
		error(source, rte.getMessage());
		throw rte;
	}


	/**
	 * Variable arguments function. If there is only one {@link Object} passed, then
	 * that object will be treated as the message, and the source tag will be
	 * determined automatically. If there are two {@link Object}s passed, the first
	 * is treated as the source tag, the second the message. If three
	 * {@link Object}s are passed, then a {@link Dialog} will be shown displaying
	 * the message and the third {@link Object} will be the {@link Frame}. Null may
	 * be passed to use a default {@link Frame}. Any {@link Object}s beyond 3 will
	 * be ignored.
	 * 
	 * @param args {@link Object} array of options. ([Source], Message,
	 *             [OwnerFrame])
	 */
	public void warn(Object... args) {
		if (args.length == 1)
			warn(null, args[0]);
		else if (args.length > 1) {
			warn(args[0], args[1]);
			if (args.length > 2)
				showMessageDialog(args[2], args[1]);
		}
	}


	/**
	 * Outputs a warning message.
	 * 
	 * @param source  Source tag of message. If <code>null</code>, a source will be
	 *                automatically determined.
	 * @param message Message to print.
	 */
	public void warn(Object source, Object message) {
		message = contructMessage(source, message);
		writeLog("warning", message);
		if (mLogLevel >= LEVEL_WARNING)
			println("[WARNING] " + message);
	}

}
