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

public class Switch  extends Device implements  WithControl {
    
    private static final Logger switch_log = LoggerFactory.getLogger("switch_logs");

	private final List<Port> ports;
	private HashMap<String, Port> macTable; // <MAC, Port>
	private Clock clock;
	private String managementIP; // management IP
	private SwitchControl control;

	public Switch(int numberOfPorts, Clock clock) {
		this(numberOfPorts, clock, false);
                switch_log.info("New switch created without GUI");
	}

	public Switch(int numberOfPorts, Clock clock, boolean withGui) {
		this.clock = clock;
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
			control = new SwitchControl(list);
		}
                switch_log.info("New computer created with GUI");
	}

	public Port getPort(int portNumber) {
		return ports.get(portNumber);
	}

	/**
	 * Add MAC & port to macTable
	 *
	 * @param MAC
	 * @param port
	 */
	private void addMACtoTable(String MAC, Port port) {
		macTable.put(MAC, port);
	}

	/**
	 * Search for MAC & port in macTable
	 *
	 * @param MAC
	 * @param Port
	 * @return true or false
	 */
	private boolean macLookup(String MAC, Port port) {
		if (macTable.containsKey(MAC)) {
			port = macTable.get(MAC);
			return true;
		} else {
			return false;
		}
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
	public void acceptPackage(Package pack, Port inPort) {
		String destinationMAC = pack.getDestinationMAC();
		String sourceMAC = pack.getSourceMAC();
		Port outPort = null;

		// Save source MAC & port to macTable, if it doesn't exist
		if (!macLookup(sourceMAC, inPort)) {
			addMACtoTable(sourceMAC, inPort);
		}

		// Search for MAC & port in macTable
		if (macLookup(destinationMAC, outPort)) {
			// Send through desired port
			pack.setSourceMAC(outPort.getMAC());
			clock.addEvent(new PortSendsEvent(clock.getCurrentTime()
					+ getDelay(), outPort, pack));
		} else {
			// Send through all ports
			// TODO: add exception for source port
			for (Port port : ports) {
				// pack.setSourceMAC(outPort.getMAC());
				clock.addEvent(new PortSendsEvent(clock.getCurrentTime()
						+ getDelay(), port, pack));
			}
			// TODO: maybe some ACK that package was/wasn't delivered ?
		}
	}

	

	@Override
	public SwitchControl getControl() {
		return control;
	}

	public void setControl(SwitchControl control) {
		this.control = control;
	}

}
