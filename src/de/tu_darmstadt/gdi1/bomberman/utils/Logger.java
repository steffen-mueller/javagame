package de.tu_darmstadt.gdi1.bomberman.utils;

/**
 * Simpler Wrapper um System.out, damit das Logging einfacher nachzuvollziehen und zu deaktivieren ist.
 * @author Steffen Müller
 */
public class Logger {
	public static void log (String s) {
		System.out.println(s);
	}
}
