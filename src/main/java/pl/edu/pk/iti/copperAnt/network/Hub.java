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

	private Logger deviceLog = null;

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
			control = new HubControl(this);
		}
		deviceLog = DeviceLoggingModuleFacade.getInstance().getDeviceLogger(
				this);
		deviceLog.info("New hub created with GUI");
	}

	public Port getPort(int portNumber) {
		return ports.get(portNumber);
	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		deviceLog.info("Hub accepted package " + pack + "from port"
				+ this.ports.indexOf(inPort));
		long time = Clock.getInstance().getCurrentTime() + getDelay();
		for (Port port : ports) {
			deviceLog.info("Hub sends package " + pack + "to port"
					+ this.ports.indexOf(port));
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
		DeviceLoggingModuleFacade.getInstance().updateDeviceLoggerWithControl(
				this);
	}

	@Override
	public Logger getLogger() {
		return deviceLog;
	}

	public List<Port> getPortList() {
		return this.ports;
	}

}
