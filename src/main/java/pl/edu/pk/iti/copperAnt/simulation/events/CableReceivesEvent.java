package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;

public class CableReceivesEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(CableReceivesEvent.class);

	private Cable cable;
	private Port port;

	private Package pack;

	public CableReceivesEvent(long time, Port fromPort, Package pack) {
		super(time);
		this.pack = pack;
		this.cable = fromPort.getCable();
		this.port = fromPort;

	}

	public Package getPackage() {
		return this.pack;
	}

	@Override
	public void run() {
		cable.receivePackage(pack, port);
		log.info(this.toString());
	}

	@Override
	public String toString() {
		return super.toString() + "CableReceivesEvent [cable=" + cable
				+ ", port=" + port + "]";
	}
}
