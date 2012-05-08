/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;


/**
 * Helper methods of the Framework.
 * 
 * @author f_m
 */
public final class FrameworkUtils {
	/**
	 * purely static, do not instantiate!
	 */
	private FrameworkUtils() { }
	
	/**
	 * Method to retrieve a File object representing a file in the distribution jar. <br><br>
	 * 
	 * WARNING: It may be a good idea to save static files like images or configuration
	 * files to the distribution jar, but you shouldn't edit files in the jar. So for files 
	 * that have to be changed like the highscore storage you should use a different location
	 * like the home folder of the users os.
	 * 
	 * @param pathToFile the path to the file, starting at the beginning of the classpath, e.g.
	 * if you have an file called win.png in the pakage resources.images, the right parameter
	 * would be "resources/images/win.png"
	 * @return a File object representing the file. WARNING: If you mistyped the name or the 
	 * path to the file, the file may not exist. You can check this with the .exists() method.
	 * @deprecated Please use {@link de.tu_darmstadt.gdi1.framework.utils.FrameworkUtils#loadFile(String)} (or {@link #loadFile(java.io.File, String)}).
	 */
	public static File getFileFromJar(final String pathToFile) /*throws FileNotFoundException*/ {
		URL url = ClassLoader.getSystemClassLoader().getResource(pathToFile);
//		if (url == null) {
//			throw new FileNotFoundException();
//		}
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			// ClassLoader.getSystemResource should never return invalid URLs
			System.err.println("This should never happen.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * a simple redirect to {@link de.tu_darmstadt.gdi1.framework.utils.FrameworkUtils#loadFile(String)}.
	 * the given arguments will be concatenate to a correct path and passed to this method.<br>
	 * <p>Please see {@link de.tu_darmstadt.gdi1.framework.utils.FrameworkUtils#loadFile(String)} for important additional informations!
	 * </p>
	 * 
	 * @param parent
	 * 		the directory, as {@link java.io.File}
	 * @param child
	 * 		the name of the file you wish, as string.
	 * @return
	 * 		the file you want
	 * @throws java.io.FileNotFoundException
	 * 		if the wanted file was not found. 
	 */
	public static File loadFile(final File parent, final String child) throws FileNotFoundException {
		//avoid ugly checking for correct set separators, let the file object do this:
		String path = (new File(parent, child)).getPath();
		return loadFile(path);
		
	}

	
	/**
	 * Method to retrieve a File object representing a file from the classpath (i.e. the distribution
	 * jar) or from the file system. <br><br>
	 * 
	 * The method will check first whether there is a file in the classpath at the specified path. If
	 * not, it will check for the file in the systems file system.
	 * 
	 * WARNING: You won't be able to change files that are stored in an jar file, so it may be a good idea 
	 * to save static files like images or configuration files to the distribution jar. Files that have to 
	 * be changed like the highscore storage should use a different location like the home folder of the users 
	 * os.
	 * 
	 * @param pathToFile the path to the file. If the file is on the classpath: Starting at the beginning 
	 * of the classpath, e.g. if you have an file called win.png in the package resources.images, the right 
	 * parameter would be "resources/images/win.png" <br> 
	 * If the file is in the file system: the path to the file starting from the games folder (then start 
	 * without an /) or from the systems root (then start with an /).
	 * 
	 * @return the file
	 * 
	 * @throws java.io.FileNotFoundException if the file couldn't be found.
	 */
	public static File loadFile(final String pathToFile) throws FileNotFoundException {
		String cleanedPath = pathToFile;
		URL url = null;
		
		//der classloader will einen pfad ohne führendes /
		if (pathToFile.startsWith("/")) {
			cleanedPath = pathToFile.substring(1);
		}
		url = ClassLoader.getSystemResource(cleanedPath);
		if (url != null) {
			try {
				return new File(url.toURI());
			} catch (URISyntaxException e) {
				// ClassLoader.getSystemResource should never return invalid URLs
				System.err.println("This should never happen.");
				e.printStackTrace();
			}
		}
		//nicht gefunden - wir versuchen den path ohne führendes /  
		File file = new File(cleanedPath);
		if (file.exists()) {
			return file;
		}
		//immer noch nichts - als letztes versuchen wirs mit dem pfad 
		//inklusive führendem /
		file = new File(pathToFile);
		if (file.exists()) {
			return file;
		} else {
			// ich denke an dieser Stelle ist es ok eine nicht framework exception zu werfen.
			throw new FileNotFoundException("The File " + pathToFile + " wasn't found on the ClassPath or in the file system.");
		}
	}
	
	/**
	 * Returns a File object representing the home folder of the currently logged in user, e.g.
	 * to save highscore data.
	 * 
	 * @return a File representing the home folder
	 */
	public static File getHomeFolder() {
		return new File(System.getProperty("user.home"));
	}
	
	/**
	 * Returns the user name of the currently logged in user of the operating system.
	 * 
	 * @return the name of the currently logged in user
	 */
	public static String getUserAccountName() {
		return System.getProperty("user.name");
	}
	
	/**
	 * 
	 * Will create a new {@link java.util.ArrayDeque} of the same size than your input toCopy.<br>
	 * Everty element contained in your input will be cloned (using {@link IGameBoard#clone()}, its up to you to implement this correctly).<br>
	 * The element order will not be changed.<br>
	 * 
	 * <p>
	 * Obviously we know that you will hand in a Dequeue containing some IGameBoard - and we know (because of the interface) that 
	 * {@link IGameBoard#clone()} will return also an {@link IGameBoard}.
	 * Its up to you to make sure that the concrete implementation of {@link IGameBoard} you hand in 
	 * implements clone() correctly.
	 * </p>
	 * 
	 * @param <E>
	 * 		the generic type of your {@link IGameBoard}
	 * @param toCopy
	 * 		the dequeue you want to deep-clone
	 * @return
	 * 		a {@link java.util.ArrayDeque} containing clones of all elemnts of the input in the same order.
	 * @author jonas
	 */
	public static <E extends IBoardElement> Deque<IGameBoard<E>> deepDequeClone(final Deque<IGameBoard<E>> toCopy) {
		Deque<IGameBoard<E>> copy = new ArrayDeque<IGameBoard<E>>(toCopy.size());
		for (IGameBoard<E> ele : toCopy) {
			copy.add(ele.clone());
		}
		return copy;
	}
}
