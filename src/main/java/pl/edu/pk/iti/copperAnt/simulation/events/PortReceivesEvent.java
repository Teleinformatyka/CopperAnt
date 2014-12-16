package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public class PortReceivesEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(PortReceivesEvent.class);

	private Port port;
	private Package pack;

	public PortReceivesEvent(long time, Port port, Package pack) {
		super(time);
		this.port = port;
		this.pack = pack;

	}

	@Override
	public void run() {
		PortControl portControl = port.getControl();
		if (portControl != null) {
			portControl.acceptPackage();
		}
		port.receivePackage(pack);
		log.info(this.toString());

	}

	@Override
	public String toString() {
		return super.toString() + "PortReceivesEvent [port=" + port + "]";
	}

	public Package getPackage() {
		return this.pack;
	}

}
