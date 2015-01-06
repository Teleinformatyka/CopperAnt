package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;

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
		this.cable = port != null ? port.getCable() : null;

	}

	@Override
	public void run() {
		cable.sendPackage(pack, port);
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
