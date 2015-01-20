package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.gui.HubControl;
import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Hub extends Device implements WithControl {

	private final Logger deviceLog = DeviceLoggingModuleFacade.getInstance()
			.getDeviceLogger(this);

	private final List<Port> ports;
	private HubControl control;

	public Hub(int numberOfPorts) {
		this(numberOfPorts, false);
		deviceLog.info("New hub created without GUI");

	}

	public Hub(int numberOfPorts, boolean withGui) {
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
		deviceLog.info("New computer created with GUI");
	}

	public Port getPort(int portNumber) {
		return ports.get(portNumber);
	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		long time = Clock.getInstance().getCurrentTime() + getDelay();
		for (Port port : ports) {
			Clock.getInstance().addEvent(
					new PortSendsEvent(time, port, pack.copy()));
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

	@Override
	public Logger getLogger() {
		return deviceLog;
	}

}
