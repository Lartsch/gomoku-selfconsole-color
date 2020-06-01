package setup;
import java.lang.System;
import java.net.URLDecoder;

/*
* Double-clicking a jar file to open it is commonly associated with the "javaw" command,
* which does not show a console upon start. In some cases, you don't want the end user to have to
* open the jar directly through a console. In some cases, it might also not be possible to implement a
* custom console / GUI using Swing, JavaFX etc. Shipping bridge starting files (like bat, sh, etc.) can suck.
* This code snippet is exactly for those cases, where you need to make sure that double clicking the jar
* does actually open a console window where the program runs. It basically works by getting the path of
* the executed jar during run and then creating a new process by opening itself with OS dependent
* commands / terminals. This usually works nicely across Windows (cmd) and Mac (terminal). For Linux,
* as there are many distributions containing different terminals, there is some terminal detection work to do.

* Small but nice extra: Setting the terminal title (Windows and Mac only as of now)

* Note: When shipping, take care of the compiler compatibility level / compiler version
* If you want to execute your program from an IDE simply add any program run argument, otherwise it will fail to launch.
* 
* @author lartsch
* 
*/

public class Main {
	// get the platform string
	public static String systemName = System.getProperty("os.name").toLowerCase();
	public static void main(String[] args) throws Exception {
		// check if an argument was passed on jar execution
	    if (args.length == 0) {
	    	// get the path of the currently running jar
	    	final String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	    	final String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
	    	// Setting for the terminal window title (Linux/Windows)
	    	final String windowTitle = "Gomoku";
	    	// Check the current platform...
	    	if(systemName.contains("windows")) {
	    		// then start the new process with the OS or terminal dependent commands
	    		new ProcessBuilder(new String[] {"cmd", "/k", "start", windowTitle, "java", "-jar", decodedPath.substring(1), "run"}).start();
	    	} else if(systemName.contains("mac")) {
	    		new ProcessBuilder(new String[] {"/bin/bash", "-c", "java", "-jar", decodedPath, "run"}).start();
	    	} else if(systemName.contains("linux")) {
	    		// TODO: add support for other Linux terminals
	    		new ProcessBuilder(new String[] {"xfce4-terminal", "--title="+windowTitle, "--hold", "-x", "java", "-jar", decodedPath, "run"}).start();
	    	} else {
	    		// No OS could be detected
	    		System.err.println("OS could not be detected.");
	    	}
	    	// destroy the original process
	    	System.exit(0);
	    } else {
	    	// ACTUAL PROGRAM TO EXECUTE COMES HERE
	    	GameSetup.runGame();
	    } 
	}
}
