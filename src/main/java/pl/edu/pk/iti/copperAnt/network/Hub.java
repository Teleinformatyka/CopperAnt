package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.HubControl;
import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Hub   extends Device implements  WithControl {
    
    private static final Logger hub_log = LoggerFactory.getLogger("hub_logs");
    
	private final List<Port> ports;
	private Clock clock;
	private HubControl control;

	public Hub(int numberOfPorts, Clock clock) {
		this(numberOfPorts, clock, false);
                hub_log.info("New hub created without GUI");
	}

	public Hub(int numberOfPorts, Clock clock, boolean withGui) {
		this.clock = clock;
		ports = new ArrayList<Port>(numberOfPorts);
		for (int i = 0; i < numberOfPorts; i++) {
			ports.add(new Port(this, withGui));
		}
		if (withGui) {
			List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
			for (Port port : ports) {
				list.add(port.getControl());
			}
			control = new HubControl(list);
		}
                hub_log.info("New computer created with GUI");
	}

	public Port getPort(int portNumber) {
		return ports.get(portNumber);
	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		for (Port port : ports) {
			clock.addEvent(new PortSendsEvent(clock.getCurrentTime()
					+ getDelay(), port, pack));
		}
	}

	@Override
	public int getDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HubControl getControl() {
		return control;
	}

	public void setControl(HubControl control) {
		this.control = control;
	}
}
