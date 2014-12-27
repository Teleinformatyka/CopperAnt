package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.javatuples.Triplet;
import pl.edu.pk.iti.copperAnt.Configuration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.RouterControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Router extends Device implements  WithControl  {
    
        private static final Logger log = Logger.getLogger("router_logs");
	
	private List<Triplet<Port, IPAddress, IPAddress>> portIP; // Port ip dhcpip
	private Clock clock;
	private HashMap<String, Port> routingTable; // <IP, Port>

	private Properties config;
	private RouterControl control;
        private static Integer nrOfLogFile = 1;
        private Logger router_log = Logger.getLogger("router_logs"+nrOfLogFile);

	public Router(int numberOfPorts, Clock clock) throws IOException {
		this(numberOfPorts, clock, false);
	}

	public Router(int numberOfPorts, Clock clock, boolean withGui) throws IOException {
		this.clock = clock;
		
		Random generator = new Random();
		portIP = new ArrayList<Triplet<Port, IPAddress, IPAddress>>();
		
		for (int i = 0; i < numberOfPorts; i++) {
			IPAddress tmp = new IPAddress("192.168.0.1");
			tmp.set(3, generator.nextInt(254) + 1);
			portIP.add(new Triplet<Port, IPAddress, IPAddress>(new Port(this, withGui), tmp, new IPAddress(tmp)));
			
		}
		routingTable = new HashMap<String, Port>();
		config = new Properties();
		
		if (withGui) {
			List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
			for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {
				list.add(trip.getValue0().getControl());
			}
			control = new RouterControl(list);
		}
                router_log.addAppender(Configuration.generateAppender("/router/router", nrOfLogFile++));
                router_log.setLevel(Level.INFO);
                log("New router created with GUI"+(nrOfLogFile-1),3);
	}

	public Router(Properties config, Clock clock) throws IOException {
		this(Integer.parseInt(config.getProperty("numbersOfPorts")),clock, config.getProperty("withGui", "false").equals("true"));
	
	}

	

	private String generateIP(int index) {
		return portIP.get(index).getValue2().increment();

	}
	private String generateIP(Port inPort) {
		for (Triplet<Port, IPAddress, IPAddress> trip: portIP) {
			if (trip.getValue0() == inPort) {
				return trip.getValue2().increment();
			}
		}
		return null;

	}

	public Port getPort(int portNumber) {
		return portIP.get(portNumber).getValue0();
	}
	public String getIP(int portNumber) {
		return portIP.get(portNumber).getValue1().toString();
	}
	public String getIP(Port port) {
		for (Triplet<Port, IPAddress, IPAddress> trip: portIP) {
			if (trip.getValue0() == port) {
				return trip.getValue1().toString();
			}
		}
		return null;
	}

	private boolean isMyIP(String addr) {
		
		for (Triplet<Port, IPAddress, IPAddress> trip: portIP) {
			if (trip.getValue1().toString().equals(addr)) {
				return true;
			}
		}
		return false;
	}

	// to facilitate unit testing
	public void addRouting(String ip, Port port) {
		routingTable.put(ip, port);

	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		log.debug("Accept pacakge from " + pack.getSourceIP() + " to "
				+ pack.getSourceIP());
		String destinationIP = pack.getDestinationIP();
		String sourceIP = pack.getSourceIP();
		Port outPort = null;
		Package response = pack;
		if (!pack.validTTL()) {
			log.debug("Pack has not valid ttl!");
			response = new Package(PackageType.DESTINATION_UNREACHABLE, "TTL<0");
			response.setDestinationIP(sourceIP);
			response.setDestinationMAC(pack.getDestinationMAC());
			outPort = inPort;

		}
		if (pack.getType() == PackageType.DHCP) {
			// request to this router to get IP, DHCP only local network
			if (pack.getContent() == null) {
				log.debug("Request to router for IP");
				sourceIP = generateIP(inPort);
				response = new Package(PackageType.DHCP, sourceIP);
				response.setDestinationMAC(pack.getDestinationMAC());
				outPort = inPort;
			} else {
				// response from wan router
				// FIXME: what happen when we connect two routers in DHCP mode?
				log.debug("Get WAN ip");

				return;
			}

		} else if (pack.getType() == PackageType.ECHO_REQUEST
				&& this.isMyIP(destinationIP)) {
			log.debug("Response for ECHO_REQUEST");
			response = new Package(PackageType.ECHO_REPLY, pack.getContent());
			response.setDestinationMAC(pack.getSourceMAC());
			response.setDestinationIP(sourceIP);
			outPort = inPort;
		}
		if (!routingTable.containsKey(sourceIP)) {
			log.debug("Adding source ip " + sourceIP + " to routingTable");
			routingTable.put(sourceIP, inPort);
		}

		if (routingTable.containsKey(destinationIP)
				&& !this.isMyIP(destinationIP)) {
			// IP in table
			log.debug("Know IP, send to LAN port");

			outPort = routingTable.get(destinationIP);

		} else if (outPort == null) {
			// routing
			boolean isInSubnet = false;
			for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {
				if (trip.getValue1().isInRange(destinationIP)) {
					outPort = trip.getValue0();
					isInSubnet = true;
					break;
				}
			}
			if (!isInSubnet) {
				for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {

					if (trip.getValue0() != inPort) {						
						clock.addEvent(new PortSendsEvent(clock.getCurrentTime()				
							+ getDelay(), trip.getValue0(), pack));
					}
				}
				return;
			}
			
			
		}
		

		clock.addEvent(new PortSendsEvent(clock.getCurrentTime() + getDelay(),
				outPort, response));

	}

	
	public String getMac() {
		return this.portIP.get(0).getValue0().getMAC();
	}

	@Override
	public int getDelay() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	public RouterControl getControl() {
		return control;
	}

	public void setControl(RouterControl control) {
		this.control = control;
	}

        private void log(String msg, int type){
            switch (type){
                case 1:
                    log.trace(msg);
                    router_log.trace(msg);
                    break;
                case 2:
                    log.debug(msg);
                    router_log.debug(msg);
                    break;
                case 3:
                    log.info(msg);
                    router_log.info(msg);
                    break;
                case 4:
                    log.warn(msg);
                    router_log.warn(msg);
                    break;
                case 5:
                    log.error(msg);
                    router_log.error(msg);
                    break;
                case 6:
                    router_log.fatal(msg);
                    break;
            }
        }
}
