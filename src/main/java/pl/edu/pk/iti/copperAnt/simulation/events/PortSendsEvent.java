package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;

public class PortSendsEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(PortSendsEvent.class);

	private Port port;

	private Package pack;

	public PortSendsEvent(long time, Port port, Package pack) {
		super(time);
		this.port = port;
		this.pack = pack;
		if (this.pack.getSourceMAC() == null)
			this.pack.setSourceMAC(port.getMAC());

	}

	public Port getPort() {
		return this.port;
	}

	public Package getPackage() {
		return this.pack;
	}

	@Override
	public void run() {
		PortControl portControl = port.getControl();
		if (portControl != null) {
			portControl.acceptPackage();
		}
		port.sendPackage(pack);
		log.info(this.toString());

	}

	@Override
	public String toString() {
		return super.toString() + "PortSendsEvent [port=" + port + "]";
	}

}
