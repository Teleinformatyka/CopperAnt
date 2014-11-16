package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public class PortSendsEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(PortSendsEvent.class);

	private Port port;

	private Package pack;

	public PortSendsEvent(long time, Port port, Package pack) {
		super(time);
		this.port = port;
		this.pack = pack;

	}

	@Override
	public void run(Clock clock) {
		clock.addEvent(new CableReceivesEvent(this.time, port, pack));
		log.info(this.toString());

	}

	@Override
	public String toString() {
		return super.toString() + "PortSendsEvent [port=" + port + "]";
	}

}
