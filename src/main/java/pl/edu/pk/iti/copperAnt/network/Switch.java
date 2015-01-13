package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.SwitchControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;

public class Switch extends Device implements WithControl {
	private final Logger deviceLog = DeviceLoggingModuleFacade.getInstance().getDeviceLogger(this);
	private static final Logger switch_log = Logger
			.getLogger("switch_logs");

	private final List<Port> ports;
	private HashMap<String, Port> macTable; // <MAC, Port>
	private SwitchControl control;

	public Switch(int numberOfPorts) {
		this(numberOfPorts, false);
		switch_log.info("New switch created without GUI");
	}

	public Switch(int numberOfPorts, boolean withGui) {
		ports = new ArrayList<>(numberOfPorts);
		for (int i = 0; i < numberOfPorts; i++) {
			ports.add(new Port(this, withGui));
		}
		macTable = new HashMap<>();
		if (withGui) {
			List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
			for (Port port : ports) {
				list.add(port.getControl());
			}
			this.setControl(new SwitchControl(list));
		}
		switch_log.info("New computer created with GUI");
	}

	public Port getPort(int portNumber) {
		return ports.get(portNumber);
	}

	public void setPort(int portNumber, Port port) {
		ports.remove(portNumber);
		ports.add(portNumber, port);
	}

	/**
	 * Process incoming Package on receiving port and forward it
	 *
	 * @param pack
	 *            - package
	 * @param inPort
	 *            - receiving port
	 */

	@Override
	public void acceptPackage(Package receivedPack, Port inPort) {
		Package pack = receivedPack.copy();
		String destinationMAC = pack.getDestinationMAC();
		String sourceMAC = pack.getSourceMAC();
		deviceLog.info("AcceptPackage from " + sourceMAC + " to " + destinationMAC);
		if (!macTable.containsKey(sourceMAC)) {
			deviceLog.info("Added new mac to macTable: " + sourceMAC + "port: "
					+ inPort);
			macTable.put(sourceMAC, inPort);
		}
		Port outPort = macTable.get(destinationMAC);

		if (outPort != null) {
			deviceLog.info("Known MAC address. Send to port");
			outPort.sendPackage(pack);
		} else {
			deviceLog.info("Unknown MAC " + destinationMAC
					+ " address. Send to all ports");
			for (Port port : ports) {
				if (port != inPort) {
					port.sendPackage(pack);
				}
			}
		}
	}

	@Override
	public SwitchControl getControl() {
		return control;
	}

	public void setControl(SwitchControl control) {
		this.control = control;
		DeviceLoggingModuleFacade.getInstance().updateDeviceLoggerWithControl(this);
	}

	public HashMap<String, Port> getMacTable() {
		return macTable;
	}
}
