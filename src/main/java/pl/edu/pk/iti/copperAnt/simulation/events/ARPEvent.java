package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public class ARPEvent extends ComputerSendsEvent {
	private static final Logger log = LoggerFactory.getLogger(ARPEvent.class);

	private Event eventAfter;

	public ARPEvent(long time, Computer device, Package pack) {
		super(time, device, pack);

	}

	public Package getPackage() {
		return this.pack;
	}

	@Override
	public void run() {
		if (!this.computer.knownHost(this.pack.getContent())) {
			Clock.getInstance().addEvent(
					new PortSendsEvent(time, this.computer.getPort(), pack));
		}
		log.info(this.toString());

	}

	@Override
	public String toString() {
		return super.toString() + "ARPResolveEvent [computer=" + this.computer
				+ "]";
	}

}
