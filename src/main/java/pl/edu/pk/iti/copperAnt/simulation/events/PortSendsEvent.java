package pl.edu.pk.iti.copperAnt.simulation.events;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.CableState;
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
		Cable cable = this.port.getCable();
		if (cable != null) {
			if (cable.getState() == CableState.IDLE) {
				port.removePackFromBuffor(pack);
				Clock.getInstance().addEvent(
						new CableReceivesEvent(this.time, port, pack));
			} else {
				Random random = new Random();
				port.addPackToBuffor(pack);
				Clock.getInstance().addEvent(
						new PortSendsEvent(this.time + random.nextInt(100),
								port, pack));
			}

		} else {
			log.debug("Dropping package, cable not inserted!");
		}

		log.info(this.toString());

	}

	@Override
	public String toString() {
		return super.toString() + "PortSendsEvent [port=" + port + "]";
	}

}
