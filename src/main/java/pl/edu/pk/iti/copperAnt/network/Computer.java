package pl.edu.pk.iti.copperAnt.network;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.ComputerControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.ComputerInitializeTrafficEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Computer extends Device implements WithControl {

	private static final Logger computer_log = LoggerFactory
			.getLogger("computer_logs");

	private Port port;
	private IPAddress ip;
	private ComputerControl control;
	private HashMap<String, String> arpTable = new HashMap<String, String>();
	private static final Logger log = LoggerFactory.getLogger(Computer.class);
	private Multimap<String, Package> packageQueue = HashMultimap.create(); // Ip
																			// package
																			// to
																			// send;

	public Computer() {
		this(null);
	}

	public Computer(IPAddress ip) {
		this(ip, false);
		computer_log.info("New computer created without GUI");

	}

	public void addKnownHost(String ip, String mac) {
		this.arpTable.put(ip, mac);
	}

	public boolean hostIsKnown(String ip) {
		return arpTable.containsKey(ip);
	}

	public String getKnownHostMac(String ip) {
		return arpTable.get(ip);

	}

	public Computer(IPAddress ip, boolean withGui) {
		this.port = new Port(this, withGui);
		port.setControlDestinationMacOfPackages(true);
		this.ip = ip;
		if (withGui) {
			this.control = new ComputerControl(port.getControl());
		}
		computer_log.info("New computer created with GUI");
	}

	public Port getPort() {
		return port;
	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		log.info("Computer received package " + pack);
		acceptPackegesWhichDoesNotRequireIP(pack);
		if (this.ip == null
				|| !pack.getDestinationIP().equals(this.ip.toString())) {
			return;
		}
		acceptPackegesWhichRequireIP(pack);

	}

	private void acceptPackegesWhichRequireIP(Package pack) {
		switch (pack.getType()) {
		case ECHO_REQUEST:
			Package outPack = new Package(PackageType.ECHO_REPLY);
			outPack.setDestinationIP(pack.getSourceIP());
			outPack.setDestinationMAC(pack.getSourceMAC());
			outPack.setSourceIP(this.ip.toString());
			outPack.setSourceMAC(this.port.getMAC());
			addPortSendsEvent(outPack);
			break;
		default:
			break;

		}
	}

	private void acceptPackegesWhichDoesNotRequireIP(Package pack) {
		switch (pack.getType()) {
		case DHCP:
			if (this.ip == null) {
				this.ip = new IPAddress(pack.getContent());
			}
			break;
		case ARP_REQ:
			if (pack.getHeader().equals(this.ip.toString())) {
				Package outPack = new Package(PackageType.ARP_REP,
						this.port.getMAC());
				outPack.setDestinationIP(pack.getSourceIP());
				outPack.setDestinationMAC(pack.getSourceMAC());
				outPack.setSourceIP(this.ip.toString());
				outPack.setSourceMAC(this.port.getMAC());
				addPortSendsEvent(outPack);

			}
			break;
		case ARP_REP:
			arpTable.put(pack.getSourceIP(), pack.getContent());
			tryToSendPackagesFromQueue();
			break;
		default:
			break;

		}
	}

	private void tryToSendPackagesFromQueue() {
		for (String toSendIP : packageQueue.keySet()) {
			if (this.hostIsKnown(toSendIP)) {
				for (Package toSend : packageQueue.get(toSendIP)) {
					toSend.setDestinationMAC(this.getKnownHostMac(toSendIP));
					addPortSendsEvent(toSend);
					packageQueue.remove(toSendIP, toSend);
				}
			}
		}
	}

	private void addPortSendsEvent(Package pack) {
		PortSendsEvent event = new PortSendsEvent(Clock.getInstance()
				.getCurrentTime() + this.getDelay(), port, pack);
		Clock.getInstance().addEvent(event);
	}

	public void initTrafic(IPAddress destinationIp) {
		if (this.ip == null) {
			return;
		}
		Clock clock = Clock.getInstance();
		long time = clock.getCurrentTime() + this.getDelay();

		Package pack = new Package(PackageType.ECHO_REQUEST, UUID.randomUUID()
				.toString());
		pack.setDestinationIP(destinationIp.toString());
		ComputerInitializeTrafficEvent event = new ComputerInitializeTrafficEvent(
				time, this, pack);

		clock.addEvent(event);
	}

	public void init() {

		Package pack = new Package(PackageType.DHCP, null);
		if (ip != null) {
			pack.setDestinationIP(ip.getBrodcast());
		} else {
			pack.setDestinationIP("255.255.255.255");
		}

		pack.setDestinationMAC(Package.MAC_BROADCAST);
		addPortSendsEvent(pack);
	}

	@Override
	public ComputerControl getControl() {
		return control;
	}

	public String getIP() {
		if (this.ip != null)
			return this.ip.toString();
		return null;
	}

	public void sendPackage(Package pack) {
		pack.setSourceIP(getIP());
		pack.setSourceMAC(this.port.getMAC());
		if (isNotBlank(pack.getDestinationMAC())) {
			addPortSendsEvent(pack);
		} else {
			if (isNotBlank(pack.getDestinationIP())) {
				packageQueue.put(pack.getDestinationIP(), pack);
				sendArpRqFor(pack.getDestinationIP());
			}
		}

	}

	private void sendArpRqFor(String destinationIP) {
		Package pack = new Package(PackageType.ARP_REQ, destinationIP);
		pack.setDestinationMAC(Package.MAC_BROADCAST);
		sendPackage(pack);
	}

	public void setPort(Port port) {
		this.port = port;

	}

}
