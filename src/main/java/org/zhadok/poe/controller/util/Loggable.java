package org.zhadok.poe.controller.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.joda.time.LocalDateTime;

public interface Loggable {

	int getVerbosity();
	
	/**
	 * Write logs to system.out as well as to a file
	 * @param fileLog
	 */
	static void writeLogsToFile(Path fileLog) {
		try {
			TeeOutputStream out = new TeeOutputStream(System.out, 
					new PrintStream(new BufferedOutputStream(new FileOutputStream(fileLog.toString(), true)), true));
			System.setOut(new PrintStream(out));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	default void println(String message) {
		System.out.println(message);
	}
	
	default void print(String message) {
		System.out.print(message);
	}
	
	default void printMessage(String message, boolean insertNewLine) {
		message = new StringBuilder(LocalDateTime.now().toString()).append(" ").append(message).toString(); 
		if (insertNewLine == true) {
			println(message);
		}
		else {
			print(message);
		}
	}
	
	default void printWithClassName(String message, boolean insertNewLine) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String className = stackTraceElements[3].getClassName();
		
		className = className.substring(className.lastIndexOf(".")+1);
		printMessage(className + "::" + stackTraceElements[3].getMethodName() + " " + message, insertNewLine);
	}
	
	/**
	 * Default behaviour: If getVerbosity() >= minVerbosity, print the message (complete line)
	 * @param minVerbosity
	 * @param message
	 */
	default void log(int minVerbosity, String message) {
		if (getVerbosity() >= minVerbosity) {
			printWithClassName(message, true);
		}
	}
	
	default void log(int minVerbosity, String message, boolean showClassName) {
		if (getVerbosity() >= minVerbosity) {
			if (showClassName==false) {
				printMessage(message, true);
			}
			else if (showClassName==true) {
				printWithClassName(message, true);
			}
		}
	}
	
	default void log(int minVerbosity, String message, boolean showClassName, boolean insertNewLine) {
		if (getVerbosity() >= minVerbosity) {
			if (showClassName==false) {
				printMessage(message, insertNewLine);
			}
			else if (showClassName==true) {
				printWithClassName(message, insertNewLine);
			}
		}
	}
	
	default void log(int minVerbosity, Object message) {
		log(minVerbosity, message.toString());
	}

	
}