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
* If you want to execute this program from your IDE simply add any program run argument, otherwise it will fail.
* 
* @author lartsch
* 
*/


public class Main {
	public static String systemName = "";
	public static void main(String[] args) throws Exception {
		// check if an argument was passed on jar execution
	    if (args.length == 0) {
	    	// get the current system name
	    	systemName = System.getProperty("os.name").toLowerCase();
	    	// get the path of the currently running jar
	    	final String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	    	final String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
	    	// Setting for the terminal window title (Linux/Windows)
	    	final String windowTitle = "Die böse 2";
	    	// Settings for the OS dependent commands
	    	final String[] windowsCommands = new String[] {"cmd", "/k", "start", windowTitle, "java", "-jar", decodedPath.substring(1), "run"};
	    	final String[] macCommands = new String[] {"/bin/bash", "-c", "java", "-jar", decodedPath, "run"};
	    	final String[] linuxCommands = new String[] {"xfce4-terminal", "--title="+windowTitle, "--hold", "-x", "java", "-jar", decodedPath, "run"};
	    	// Check the current platform...
	    	if(systemName.contains("windows")) {
	    		// then start the new process
	    		new ProcessBuilder(windowsCommands).start();
	    	} else if(systemName.contains("mac")) {
	    		new ProcessBuilder(macCommands).start();
	    	} else if(systemName.contains("linux")) {
	    		new ProcessBuilder(linuxCommands).start();
	    	} else {
	    		// If no OS could be detected, the program should shut down
	    		System.err.println("OS could not be detected.");
	    		System.exit(0);
	    	}
	    } else {
	    	// get the current system name
	    	systemName = System.getProperty("os.name").toLowerCase();
	    	// Actual program to execute
	    	GameSetup.runGame();
	    } 
	}
}
