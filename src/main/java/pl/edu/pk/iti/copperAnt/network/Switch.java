package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.SwitchControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Switch extends Device implements WithControl {
	private static final Logger switch_log = LoggerFactory
			.getLogger("switch_logs");

	private final List<Port> ports;
	private HashMap<String, Port> macTable; // <MAC, Port>
	private SwitchControl control;
	private static final Logger log = LoggerFactory.getLogger(Switch.class);

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
			control = new SwitchControl(list, this);
		}
		switch_log.info("New switch created with GUI");
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
		log.info("AcceptPackage from " + sourceMAC + " to " + destinationMAC);
		if (!macTable.containsKey(sourceMAC)) {
			log.debug("Added new mac to macTable: " + sourceMAC + "port: "
					+ inPort);
			macTable.put(sourceMAC, inPort);
		}
		Port outPort = macTable.get(destinationMAC);

		if (outPort != null) {
			log.debug("Known MAC address. Send to port");
			addPortSendsEvent(outPort, pack);

		} else {
			log.debug("Unknown MAC " + destinationMAC
					+ " address. Send to all ports");
			for (Port port : ports) {
				if (port != inPort) {
					addPortSendsEvent(port, pack);
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
	}

	public HashMap<String, Port> getMacTable() {
		return macTable;
	}

	private void addPortSendsEvent(Port port, Package pack) {
		long time = Clock.getInstance().getCurrentTime() + getDelay();
		Clock.getInstance().addEvent(new PortSendsEvent(time, port, pack));
	}
}
