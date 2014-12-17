package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.CableState;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public class CableSendsEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(CableSendsEvent.class);
	private Port port;
	private Cable cable;
	private Package pack;

	public CableSendsEvent(long time, Port port, Package pack) {
		super(time);
		this.port = port;
		this.pack = pack;
		this.cable = port.getCable();

	}

	@Override
	public void run(Clock clock) {
		if (!cable.getState().equals(CableState.COLISION)) {
			clock.addEvent(new PortReceivesEvent(time, port, pack));
		}
		if (cable.getBusyUntilTime() <= time) {
			cable.setState(CableState.IDLE);
		}
		log.info(toString());

	}

	@Override
	public String toString() {
		return super.toString() + "CableSendsEvent [port=" + port + ", cable="
				+ cable + "]";
	}
	public Package getPackage() {
		return this.pack;
	}

}
