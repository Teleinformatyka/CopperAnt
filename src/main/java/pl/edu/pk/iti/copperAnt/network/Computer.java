package pl.edu.pk.iti.copperAnt.network;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.gui.ComputerControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.ComputerInitializeTrafficEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Computer extends Device implements WithControl {

	public static final IPAddress DEFAULT_IP_ADDRESS = new IPAddress(
			"192.168.0.1");

	private final Logger deviceLog = DeviceLoggingModuleFacade.getInstance()
			.getDeviceLogger(this);
	public static int COMPUTER_COUNT = 0;

	private static final Logger computer_log = Logger
			.getLogger("computer_logs");

	private int number;
	private Port port;
	private IPAddress ip;
	private IPAddress defaultGateway;
	private ComputerControl control;
	private HashMap<String, String> arpTable = new HashMap<String, String>();
	private Multimap<String, Package> packageQueue = HashMultimap.create(); // Ip
																			// package
																			// to
																			// send;

	public Computer() {
		this(new IPAddress(DEFAULT_IP_ADDRESS));

	}

	public Computer(IPAddress ip) {
		this(ip, false);
		deviceLog.info("New computer created without GUI");

	}

	public Computer(boolean withGui) {
		this(new IPAddress(DEFAULT_IP_ADDRESS), withGui);
	}

	public Computer(IPAddress ip, boolean withGui) {
		this.port = new Port(this, withGui);
		port.setControlDestinationMacOfPackages(true);
		this.ip = ip;
		if (withGui) {
			this.setControl(new ComputerControl(this));
		}
		deviceLog.info("New computer created with GUI");
		DEFAULT_IP_ADDRESS.increment();
		number = COMPUTER_COUNT++;

	}

	public int getNumber() {

		return number;
	}

	public void addKnownHost(String ip, String mac) {
		if (ipIsInTheSameNetworkAsComputer(ip)) {
			this.arpTable.put(ip, mac);
		} else {
			this.arpTable.put(this.defaultGateway.toString(), mac);
		}
	}

	public boolean hostIsKnown(String ip) {
		if (ipIsInTheSameNetworkAsComputer(ip)) {
			return arpTable.containsKey(ip);
		} else {
			return arpTable.containsKey(this.defaultGateway.toString());
		}
	}

	public String getKnownHostMac(String ip) {
		if (ipIsInTheSameNetworkAsComputer(ip)) {
			return arpTable.get(ip);
		} else {
			return arpTable.get(this.defaultGateway.toString());
		}

	}

	public Port getPort() {
		return port;
	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		deviceLog.info("Computer received package " + pack);
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
		deviceLog.info("Sending package: " + pack);
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

	public void setControl(ComputerControl computerControl) {
		this.control = computerControl;
		DeviceLoggingModuleFacade.getInstance().updateDeviceLoggerWithControl(
				this);
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
				if (ipIsInTheSameNetworkAsComputer(pack.getDestinationIP())) {
					packageQueue.put(pack.getDestinationIP(), pack);
					sendArpRqFor(pack.getDestinationIP());
				} else if (defaultGateway != null) {
					String gatewayMac = getKnownHostMac(this.defaultGateway
							.toString());
					if (gatewayMac == null) {
						packageQueue.put(pack.getDestinationIP(), pack);
						sendArpRqFor(this.defaultGateway.toString());
					} else {
						pack.setDestinationMAC(gatewayMac);
						addPortSendsEvent(pack);
					}
				}
			}
		}

	}

	private boolean ipIsInTheSameNetworkAsComputer(String destinationIp) {
		IPAddress computerIp = new IPAddress(this.getIP());
		return IPAddress.isInSubnet(destinationIp, computerIp.getNetwork(),
				IPAddress.NETMASK);

	}

	private void sendArpRqFor(String destinationIP) {
		Package pack = new Package(PackageType.ARP_REQ, destinationIP);
		pack.setDestinationMAC(Package.MAC_BROADCAST);
		pack.setDestinationIP(destinationIP);
		sendPackage(pack);
	}

	public void setPort(Port port) {
		this.port = port;

	}

	public IPAddress getDefaultGateway() {
		return defaultGateway;
	}

	public void setDefaultGateway(IPAddress defaultGateway) {
		this.defaultGateway = defaultGateway;
	}

	public IPAddress getIp() {
		return ip;
	}

	public void setIp(IPAddress ip) {
		this.ip = ip;
	}

}
