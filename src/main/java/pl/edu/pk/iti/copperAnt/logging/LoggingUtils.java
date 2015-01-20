package pl.edu.pk.iti.copperAnt.logging;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

import pl.edu.pk.iti.copperAnt.gui.ComputerControl;
import pl.edu.pk.iti.copperAnt.gui.HubControl;
import pl.edu.pk.iti.copperAnt.gui.RouterControl;
import pl.edu.pk.iti.copperAnt.gui.SwitchControl;

public class LoggingUtils {
	private Layout loggingLayout;

	// used as primitive naming rule for devices
	private int computerCounter;
	private int routerCounter;
	private int switchCounter;
	private int hubCounter;

	public LoggingUtils() {
		loggingLayout = new PatternLayout("%m%n");
		computerCounter = 0;
		routerCounter = 0;
		switchCounter = 0;
		hubCounter = 0;
	}

	// package + @hash...
	public String getDeviceId(Object deviceObj) {
		return deviceObj.toString();
	}

	public String getDeviceNameFromId(String deviceId) {
		return deviceId.substring(deviceId.lastIndexOf(".") + 1,
				deviceId.length());
	}

	public Layout getLoggingLayout() {
		return loggingLayout;
	}

	public String getTabNamePrimitiveRule(Object controlObj) {
		// TODO change naming rule to read name from
		// ComputerControl/RouterControl/SwitchControl

		String name = null;
		if (controlObj instanceof ComputerControl) {
			name = "Computer " + this.computerCounter++;
		} else if (controlObj instanceof SwitchControl) {
			name = "Switch " + this.switchCounter++;
		} else if (controlObj instanceof RouterControl) {
			name = "Router " + this.routerCounter++;
		} else if (controlObj instanceof HubControl) {
			name = "Hub " + this.hubCounter++;
		}
		return name;
	}
}
