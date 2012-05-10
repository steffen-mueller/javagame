package de.tu_darmstadt.gdi1.framework.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Utility class for handling files.
 * Allows you to easily read/write Strings to/from a file.
 *
 * @author jonas
 */
public class FileUtility {

	/** hiding the constructor. */
	protected FileUtility() {
		
	}
	/**
	 * This method stores a string in a file.
	 * 
	 * @param f
	 * 				the path where to store the file.
	 * @param content
	 * 				string to store in file
	 * @throws java.io.IOException
	 * 				something went wrong when dealing with the file
	 */
	public static void writeFile(final File f, final String content) throws IOException {		
		FileOutputStream fos;
		fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos);			
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(content, 0, content.length());
		bw.close();		
	}
	
	/**
	 * Reads a complete file. This method actually opens the file and reads
	 * it. 
	 * 
	 * @param path
	 *            the filepath of the file. may not null and should exists.
	 * @return the content of the file at path (as string)
	 * @throws java.io.IOException
	 *             if a file could not be loaded
	 */
	public static String readFile(final File path) throws IOException {
		if (path == null) {
			throw new NullPointerException("null as path is illegal.");
		} 
		if (!path.exists()) {
			throw new FileNotFoundException("Could not load a file which not exists.");
		}
		StringBuffer myBuf;
		BufferedReader bufferedreader = null;
		FileReader filereader = new FileReader(path);
		try {
			bufferedreader = new BufferedReader(filereader);
			String curContent = "";
			myBuf = new StringBuffer(2048);
			while ((curContent = bufferedreader.readLine()) != null) {
				myBuf.append(curContent).append("\n");
			}
		} finally {
			if (bufferedreader != null) {
				bufferedreader.close();
			}
			filereader.close();
		}
		if (myBuf.length() != 0) {
			return myBuf.toString().substring(0, myBuf.toString().length() - 1);
		} else {
			return "";
		}
	}
}

