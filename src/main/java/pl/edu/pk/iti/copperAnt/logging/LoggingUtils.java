package pl.edu.pk.iti.copperAnt.logging;

import org.apache.log4j.Layout;
import org.apache.log4j.TTCCLayout;

public class LoggingUtils {
	private Layout loggingLayout;
	
	public LoggingUtils() {
		loggingLayout = new TTCCLayout();
	}
	
	//package + @hash...
	public String getDeviceId(Object deviceObj) {
		return deviceObj.toString();
	}
	
	public Layout getLoggingLayout() {
		return loggingLayout;
	}
}
