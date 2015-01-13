package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.RouterControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Router extends Device implements WithControl {

	private static final Logger router_log = LoggerFactory
			.getLogger("router_logs");

	private List<Triplet<Port, IPAddress, IPAddress>> portIP; // Port ip dhcpip

	private HashMap<String, Port> routingTable; // <IP, Port>

	private RouterControl control;
	private static final Logger log = LoggerFactory.getLogger(Router.class);
	private HashMap<String, String> arpTable = new HashMap<String, String>();
	private Multimap<String, Package> packageQueue = HashMultimap.create(); // Ip
																			// --->
																			// package
																			// to
																			// send;

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

		if (withGui) {
			control = new RouterControl(this);
		}
		router_log.info("New router created with GUI");
	}

	public Router(Properties config) {
		this(Integer.parseInt(config.getProperty("numbersOfPorts")), config
				.getProperty("withGui", "false").equals("true"));

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
		log.debug("Accept pacakge from " + receivedPack.getSourceIP() + " to "
				+ receivedPack.getSourceIP());
		String destinationIP = receivedPack.getDestinationIP();
		String sourceIP = receivedPack.getSourceIP();

		if (StringUtils.isBlank(destinationIP) || StringUtils.isBlank(sourceIP)) {
			log.debug("Pacakge from does not contains IP address. Droped.");
			return;
		}

		Port outPort = null;
		Package response = receivedPack.copy();
		response.setDestinationMAC("");
		response.setSourceMAC("");
		if (!receivedPack.validTTL()) {
			log.debug("Pack has not valid ttl!");
			response = new Package(PackageType.DESTINATION_UNREACHABLE, "TTL<0");
			response.setDestinationIP(sourceIP);
			response.setDestinationMAC(receivedPack.getSourceMAC());
			outPort = inPort;
			addPortSendsEvent(outPort, response);
			return;
		}

		if (receivedPack.getType() == PackageType.DHCP) {
			// request to this router to get IP, DHCP only local network
			if (receivedPack.getContent() == null) {
				log.debug("Request to router for IP");
				sourceIP = generateIP(inPort);
				response = new Package(PackageType.DHCP, sourceIP);
				response.setDestinationMAC(receivedPack.getDestinationMAC());
				outPort = inPort;
			} else {
				// response from wan router
				// FIXME: what happen when we connect two routers in DHCP mode?
				log.debug("Get WAN ip");

				return;
			}

		} else if (receivedPack.getType() == PackageType.ECHO_REQUEST
				&& this.isMyIP(destinationIP)) {
			log.debug("Response for ECHO_REQUEST");
			response = new Package(PackageType.ECHO_REPLY,
					receivedPack.getContent());
			response.setDestinationMAC(receivedPack.getSourceMAC());
			response.setDestinationIP(sourceIP);
			response.setSourceIP(destinationIP);
			outPort = inPort;
		} else if (receivedPack.getType() == PackageType.ARP_REQ) {
			int portNumber = getPortNumber(inPort);
			String ipStringOfPort = portIP.get(portNumber).getValue1()
					.toString();
			if (receivedPack.getHeader().equals(ipStringOfPort)) {
				Package outPack = new Package(PackageType.ARP_REP,
						inPort.getMAC());
				outPack.setDestinationIP(receivedPack.getSourceIP());
				outPack.setDestinationMAC(receivedPack.getSourceMAC());
				outPack.setSourceIP(ipStringOfPort);
				outPack.setSourceMAC(inPort.getMAC());
				outPort = inPort;
				addPortSendsEvent(outPort, outPack);
				return;
			}
		} else if (receivedPack.getType() == PackageType.ARP_REP) {
			String receivedIp = receivedPack.getSourceIP();
			String receivedContent = receivedPack.getContent();
			addToArpTable(receivedIp, receivedContent);
			tryToSendPackagesFromQueue(inPort);
			return;
		}
		String sourceNetwork = new IPAddress(sourceIP).getNetwork();
		if (!routingTable.containsKey(sourceNetwork)) {
			log.debug("Adding source ip " + sourceNetwork + " to routingTable");
			routingTable.put(sourceNetwork, inPort);
		}

		String destinationNetwork = new IPAddress(destinationIP).getNetwork();
		if (routingTable.containsKey(destinationNetwork)
				&& !this.isMyIP(destinationIP)) {
			// IP in table
			log.debug("Know IP, send to LAN port");

			outPort = routingTable.get(destinationNetwork);

		}
		if (outPort != null) {
			addPortSendsEvent(outPort, response);
		}

	}

	public void addToArpTable(String ip, String mac) {
		arpTable.put(ip, mac);
	}

	private int getPortNumber(Port inPort) {
		for (int i = 0; i < portIP.size(); i++) {
			Triplet<Port, IPAddress, IPAddress> triplet = portIP.get(i);
			if (triplet.getValue0().equals(inPort)) {
				return i;
			}
		}
		return -1;
	}

	private void addPortSendsEvent(Port port, Package pack) {
		long time = Clock.getInstance().getCurrentTime() + getDelay();
		pack.setSourceMAC(port.getMAC());
		if (StringUtils.isNotBlank(pack.getDestinationMAC())) {
			Clock.getInstance().addEvent(new PortSendsEvent(time, port, pack));
		} else {
			String mac;
			if (itIsNetworkConnetedDirectlyToRouter(pack.getDestinationIP(),
					port)) {
				mac = this.arpTable.get(pack.getDestinationIP());
			} else {
				mac = Package.MAC_BROADCAST;
			}

			if (StringUtils.isNotBlank(mac)) {
				pack.setDestinationMAC(mac);
				Clock.getInstance().addEvent(
						new PortSendsEvent(time, port, pack));
			} else {
				packageQueue.put(pack.getDestinationIP(), pack);
				sendArpRqFor(port, pack.getDestinationIP());
			}
		}
	}

	private boolean itIsNetworkConnetedDirectlyToRouter(String destinationIP,
			Port port) {
		String destinationNetwork = new IPAddress(destinationIP).getNetwork();
		String portNetwork = new IPAddress(getIP(port)).getNetwork();

		return destinationNetwork.equals(portNetwork);
	}

	private void sendArpRqFor(Port port, String destinationIP) {
		Package pack = new Package(PackageType.ARP_REQ, destinationIP);
		pack.setSourceMAC(port.getMAC());
		pack.setDestinationMAC(Package.MAC_BROADCAST);
		pack.setDestinationIP(destinationIP);
		pack.setSourceIP(getIP(port));
		addPortSendsEvent(port, pack);
	}

	@Override
	public int getDelay() {
		return 1;
	}

	public RouterControl getControl() {
		return control;
	}

	public void setControl(RouterControl control) {
		this.control = control;
	}

	public void setIpForPort(int portNumber, IPAddress ip) {
		routingTable.remove(portIP.get(portNumber).getValue1().getNetwork());
		routingTable.put(ip.getNetwork(), portIP.get(portNumber).getValue0());

		Triplet<Port, IPAddress, IPAddress> newValue = portIP.get(portNumber)
				.setAt1(ip);
		newValue.setAt2(ip.copy());
		portIP.remove(portNumber);
		portIP.add(portNumber, newValue);

	}

	private void tryToSendPackagesFromQueue(Port port) {
		for (String toSendIP : packageQueue.keySet()) {
			if (arpTable.containsKey(toSendIP)) {
				for (Iterator<Package> iterator = packageQueue.get(toSendIP)
						.iterator(); iterator.hasNext();) {
					Package toSend = iterator.next();
					packageQueue.remove(toSendIP, toSend);
					toSend.setDestinationMAC(arpTable.get(toSendIP));
					addPortSendsEvent(port, toSend);
				}
			}
		}
	}

	public List<Port> getPortList() {
		List<Port> result = new LinkedList<Port>();
		for (Triplet<Port, IPAddress, IPAddress> triplet : portIP) {
			result.add(triplet.getValue0());
		}
		return result;
	}
}
