package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.Control;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Port;

public class CableConnector {

	public static final CableConnector singletonInstance = new CableConnector();

	Port port1;
	Port port2;

	private Cable cable = new Cable(true);

	private CableConnector() {

	}

	public static CableConnector getInstance() {
		return singletonInstance;
	}

	public Control connectPort(Port port) {
		if (port1 == null) {
			port1 = port;
			port1.conntectCalble(cable);
			return null;
		} else if (port != port1 && port2 == null) {
			port2 = port;
			port2.conntectCalble(cable);
			CableControl control = (CableControl) cable.getControl();

			port1 = null;
			port2 = null;
			cable = new Cable(true);

			return control;
		}
		return null;
	}
}