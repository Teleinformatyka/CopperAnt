package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.javatuples.Triplet;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.RouterControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;

public class Router extends Device implements WithControl {
	private final Logger deviceLog = DeviceLoggingModuleFacade.getInstance().getDeviceLogger(this);
	private static final Logger router_log = Logger
			.getLogger("router_logs");

	private List<Triplet<Port, IPAddress, IPAddress>> portIP; // Port ip dhcpip

	private HashMap<String, Port> routingTable; // <IP, Port>

	private Properties config;
	private RouterControl control;

	public Router(int numberOfPorts) {
		this(numberOfPorts, false);
		router_log.info("New router created without GUI");
	}

	public Router(int numberOfPorts, boolean withGui) {

		Random generator = new Random();
		portIP = new ArrayList<Triplet<Port, IPAddress, IPAddress>>();

		for (int i = 0; i < numberOfPorts; i++) {
			IPAddress tmp = new IPAddress("192.168.0.1");
			tmp.set(3, generator.nextInt(254) + 1);
			portIP.add(new Triplet<Port, IPAddress, IPAddress>(new Port(this,
					withGui), tmp, new IPAddress(tmp)));

		}
		routingTable = new HashMap<String, Port>();
		config = new Properties();

		if (withGui) {
			List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
			for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {
				list.add(trip.getValue0().getControl());
			}
			this.setControl(new RouterControl(list));
		}
		router_log.info("New router created with GUI");
	}

	public Router(Properties config) {
		this(Integer.parseInt(config.getProperty("numbersOfPorts")), config
				.getProperty("withGui", "false").equals("true"));

	}

	private String generateIP(int index) {
		return portIP.get(index).getValue2().increment();

	}

	private String generateIP(Port inPort) {
		for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {
			if (trip.getValue0() == inPort) {
				return trip.getValue2().increment();
			}
		}
		return null;

	}

	public void setPort(int portNumber, Port port) {
		Triplet<Port, IPAddress, IPAddress> newTriplet = portIP.get(portNumber)
				.setAt0(port);
		portIP.remove(portNumber);
		portIP.add(portNumber, newTriplet);
	}

	public Port getPort(int portNumber) {
		return portIP.get(portNumber).getValue0();
	}

	public String getIP(int portNumber) {
		return portIP.get(portNumber).getValue1().toString();
	}

	public String getIP(Port port) {
		for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {
			if (trip.getValue0() == port) {
				return trip.getValue1().toString();
			}
		}
		return null;
	}

	private boolean isMyIP(String addr) {

		for (Triplet<Port, IPAddress, IPAddress> trip : portIP) {
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
	public void acceptPackage(Package receivedPack, Port inPort) {
		Package pack = receivedPack.copy();
		deviceLog.info("Accept pacakge from " + pack.getSourceIP() + " to "
				+ pack.getSourceIP());
		String destinationIP = pack.getDestinationIP();
		String sourceIP = pack.getSourceIP();
		Port outPort = null;
		Package response = pack;
		if (!pack.validTTL()) {
			deviceLog.info("Pack has not valid ttl!");
			response = new Package(PackageType.DESTINATION_UNREACHABLE, "TTL<0");
			response.setDestinationIP(sourceIP);
			response.setDestinationMAC(pack.getDestinationMAC());
			outPort = inPort;

		}
		if (pack.getType() == PackageType.DHCP) {
			// request to this router to get IP, DHCP only local network
			if (pack.getContent() == null) {
				deviceLog.info("Request to router for IP");
				sourceIP = generateIP(inPort);
				response = new Package(PackageType.DHCP, sourceIP);
				response.setDestinationMAC(pack.getDestinationMAC());
				outPort = inPort;
			} else {
				// response from wan router
				// FIXME: what happen when we connect two routers in DHCP mode?
				deviceLog.info("Get WAN ip");

				return;
			}

		} else if (pack.getType() == PackageType.ECHO_REQUEST
				&& this.isMyIP(destinationIP)) {
			deviceLog.info("Response for ECHO_REQUEST");
			response = new Package(PackageType.ECHO_REPLY, pack.getContent());
			response.setDestinationMAC(pack.getSourceMAC());
			response.setDestinationIP(sourceIP);
			response.setSourceIP(destinationIP);
			outPort = inPort;
		}
		if (!routingTable.containsKey(sourceIP)) {
			deviceLog.info("Adding source ip " + sourceIP + " to routingTable");
			routingTable.put(sourceIP, inPort);
		}

		if (routingTable.containsKey(destinationIP)
				&& !this.isMyIP(destinationIP)) {
			// IP in table
			deviceLog.info("Know IP, send to LAN port");

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

					Port port = trip.getValue0();
					if (port != inPort) {
						port.sendPackage(pack);
					}
				}
				return;
			}

		}

		outPort.sendPackage(response);

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
		DeviceLoggingModuleFacade.getInstance().updateDeviceLoggerWithControl(this);
	}

	public void setIpForPort(int portNumber, IPAddress ip) {
		Triplet<Port, IPAddress, IPAddress> newValue = portIP.get(portNumber)
				.setAt1(ip);
		newValue.setAt2(ip.copy());
		portIP.remove(portNumber);
		portIP.add(portNumber, newValue);
	}

}
