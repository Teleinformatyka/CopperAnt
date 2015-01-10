package pl.edu.pk.iti.copperAnt.network;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.gui.ComputerControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.logging.DeviceInstanceLoggerFactory;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.DistributionTimeIntervalGenerator;
import pl.edu.pk.iti.copperAnt.simulation.events.ARPEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.ComputerSendsEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Computer extends Device implements WithControl {
	private final Logger computer_log = Logger.getLogger("computer_logs");	//todo package-pattern path with allows to user hierarchical loggers

	private Port port;
	private IPAddress ip;
	private ComputerControl control;
	private HashMap<String, String> arpTable = new HashMap<String, String>();
	private Clock clock;
	private static int TIMEOUT_ADDRESS_RESOLVE = 10;
	//private static final Logger log = LoggerFactory.getLogger(Computer.class);
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

	public boolean knownHost(String ip) {
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
		computer_log.info("Computer received package " + pack);
		acceptPackegesWhichDoesNotRequireIP(pack);
		if (this.ip == null || pack.getDestinationIP() != this.ip.toString()) {
			return;
		}
		acceptPackegesWhichRequireIP(pack);

	}

	private void acceptPackegesWhichRequireIP(Package pack) {
		switch (pack.getType()) {
		case ECHO_REQUEST:
			// TODO: add event to pong
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
			if (pack.getContent() == null
					&& pack.getHeader() == this.ip.toString()) {
				Package outPack = new Package(PackageType.ARP_REP,
						this.port.getMAC());
				// FIXME: ----------------------------------------------
				outPack.setDestinationIP(pack.getSourceIP());
				outPack.setDestinationMAC(pack.getSourceMAC());
				// ---------------------------------------------
				port.sendPackage(outPack);

			} else {
				arpTable.put(pack.getSourceIP(), pack.getContent());
			}
			break;
		case ARP_REP:
			arpTable.put(pack.getSourceIP(), pack.getContent());
			tryToSendPackagesFromQueue();
			break;

		}
	}

	private void tryToSendPackagesFromQueue() {
		for (String toSendIP : packageQueue.keySet()) {
			if (this.knownHost(toSendIP)) {
				for (Package toSend : packageQueue.get(toSendIP)) {
					toSend.setDestinationMAC(this.getKnownHostMac(toSendIP));
					ComputerSendsEvent event = new ComputerSendsEvent(
							clock.getCurrentTime() + this.getDelay(), this,
							toSend);
					event.setIntervalGenerator(new DistributionTimeIntervalGenerator());
					clock.addEvent(event);
					packageQueue.remove(toSendIP, toSend);
				}
			}
		}
	}

	public void initTrafic() {
		if (this.ip == null) {
			return;
		}
		Clock clock = Clock.getInstance();
		long time = clock.getCurrentTime() + this.getDelay();

		ComputerSendsEvent event = null;

		Package pack = new Package(PackageType.ECHO_REQUEST, UUID.randomUUID()
				.toString());
		pack.setSourceIP(this.ip.toString());
		IPAddress dest = this.ip;
		Random generator = new Random();
		// if (generator.nextBoolean()) {
		Set<String> ipAddreses = arpTable.keySet();
		for (String ip : arpTable.keySet()) {
			if (generator.nextInt(100) > 50) {
				dest = new IPAddress(ip);
				break;
			}
		}
		/*
		 * } else { dest.set(generator.nextInt(4) + 1, generator.nextInt(254) +
		 * 1); }
		 */
		pack.setDestinationIP(dest.toString());
		String destMAC = null;
		if (arpTable.containsKey(dest.toString())) {
			destMAC = arpTable.get(dest.toString());
			pack.setDestinationMAC(destMAC);
			event = new ComputerSendsEvent(time, this, pack);
			event.setIntervalGenerator(new DistributionTimeIntervalGenerator());
		} else {
			Package resolvePack = new Package(PackageType.ARP_REQ,
					dest.toString());
			resolvePack.setDestinationMAC(Package.MAC_BROADCAST);
			resolvePack.setDestinationIP(dest.toString());

			event = new ARPEvent(time + getDelay(), this, resolvePack);
			packageQueue.put(dest.toString(), pack);

		}

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
		port.sendPackage(pack);
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
}
