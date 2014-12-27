package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pk.iti.copperAnt.Configuration;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.SwitchControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Switch  extends Device implements  WithControl {
    
        private static final Logger log = Logger.getLogger("switch_logs");

	private final List<Port> ports;
	private HashMap<String, Port> macTable; // <MAC, Port>
	private Clock clock;
	private String managementIP; // management IP
	private SwitchControl control;
        private static Integer nrOfLogFile = 1;
        private Logger switch_logs = Logger.getLogger("switch_logs"+nrOfLogFile);

	public Switch(int numberOfPorts, Clock clock) throws IOException {
		this(numberOfPorts, clock, false);
	}

	public Switch(int numberOfPorts, Clock clock, boolean withGui) throws IOException {
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
                switch_logs.addAppender(Configuration.generateAppender("/switch/switch", nrOfLogFile++));
                switch_logs.setLevel(Level.INFO);
                log("New switch created with GUI"+(nrOfLogFile-1),3);
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
        
        private void log(String msg, int type){
            switch (type){
                case 1:
                    log.trace(msg);
                    switch_logs.trace(msg);
                    break;
                case 2:
                    log.debug(msg);
                    switch_logs.debug(msg);
                    break;
                case 3:
                    log.info(msg);
                    switch_logs.info(msg);
                    break;
                case 4:
                    log.warn(msg);
                    switch_logs.warn(msg);
                    break;
                case 5:
                    log.error(msg);
                    switch_logs.error(msg);
                    break;
                case 6:
                    switch_logs.fatal(msg);
                    break;
            }
        }
}
