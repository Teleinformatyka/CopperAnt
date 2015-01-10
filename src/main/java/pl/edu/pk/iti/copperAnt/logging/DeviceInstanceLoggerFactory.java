package pl.edu.pk.iti.copperAnt.logging;

import pl.edu.pk.iti.copperAnt.gui.TabbedLogPane;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public class DeviceInstanceLoggerFactory {
	private static TabbedLogPane deviceLogOutputPane;
	
	public static void initializeInstanceOutputPane(TabbedLogPane pane) {
		deviceLogOutputPane = pane;
	}
	
	public static Logger getInstanceLogger(String id) {
		Logger log = Logger.getLogger(id);
		if (deviceLogOutputPane!=null) {
			Appender outputAppender = deviceLogOutputPane.getTabLogAppenderForInstance(id);
			log.addAppender(outputAppender);
		}
		
		System.out.println("test");
		
		return log;
	}
}
