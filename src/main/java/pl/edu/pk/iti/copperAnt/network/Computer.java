package pl.edu.pk.iti.copperAnt.network;

import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.ComputerControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.DistributionTimeIntervalGenerator;
import pl.edu.pk.iti.copperAnt.simulation.events.ComputerSendsEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Computer extends Device implements WithControl {
    
    private static final Logger computer_log = LoggerFactory.getLogger("computer_logs");
    
	private Port port;
	private IPAddress ip;
	private ComputerControl control;

	public Computer() {
		this(null);
	}

	public Computer(IPAddress ip) {
		this(ip, false);
                computer_log.info("New computer created without GUI");
	}

	public Computer(IPAddress ip, boolean withGui) {
		this.port = new Port(this, withGui);
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
		// assume is response for arp package
		if (pack.getType() == PackageType.DHCP && this.ip == null) {
			this.ip = new IPAddress(pack.getContent());
		} else if (pack.getType() == PackageType.ECHO_REQUEST
				&& pack.getDestinationIP() == this.ip.toString()) {
			// TODO: add event to pong
		}

		System.out.println("Computer received package");

	}

	public void initTrafic(Clock clock) {
		if (this.ip == null) {
			return;
		}
		long time = clock.getCurrentTime() + this.getDelay();
		Package pack = new Package(PackageType.ECHO_REQUEST, UUID.randomUUID()
				.toString());
		pack.setSourceIP(this.ip.toString());
		IPAddress dest = this.ip;
		Random generator = new Random();
		dest.set(generator.nextInt(4) + 1, generator.nextInt(254) + 1);
		pack.setDestinationIP(dest.toString());
		ComputerSendsEvent event = new ComputerSendsEvent(time, this, pack);
		event.setIntervalGenerator(new DistributionTimeIntervalGenerator());
		clock.addEvent(event);
	}

	public void init(Clock clock) {
		long time = clock.getCurrentTime() + this.getDelay();
		Package pack = new Package(PackageType.DHCP, null);
		clock.addEvent(new PortSendsEvent(time, this.port, pack));
	}

	@Override
	public ComputerControl getControl() {
		return control;
	}

}
