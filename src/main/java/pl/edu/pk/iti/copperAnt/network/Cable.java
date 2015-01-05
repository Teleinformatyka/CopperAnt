package pl.edu.pk.iti.copperAnt.network;

import javafx.scene.control.Control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.CableControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.CableSendsEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.PortReceivesEvent;

public class Cable implements WithControl {

	private static final Logger cable_log = LoggerFactory
			.getLogger("cable_logs");

	Port a;
	Port b;

	CableState state;
	private long busyUntilTime;
	private CableControl control;

	public Cable() {
		this(false);
		cable_log.info("New cable created without GUI");
	}

	public Cable(boolean withGui) {
		state = CableState.IDLE;
		if (withGui) {
			this.control = new CableControl();
		}
		cable_log.info("New cable created with GUI");
	}

	public CableState getState() {
		return state;
	}

	public void setState(CableState state) {
		this.state = state;
		if (this.control != null) {
			control.setState(state);
		}
	}

	public Port getOtherEnd(Port port) {
		if (port.equals(a)) {
			return b;
		} else {
			return a;
		}

	}

	public void ejectFromPort(Port port) {
		if (port == a) {
			a = null;
		} else if (port == b) {
			b = null;
		}
		port.disconnectCable();

	}

	public Port getA() {
		return a;
	}

	public void setA(Port a) {
		this.a = a;
	}

	public Port getB() {
		return b;
	}

	public void setB(Port b) {
		this.b = b;
	}

	public void insertInto(Port port) {
		if (port != a && port != b) {
			if (a == null) {
				a = port;
				port.conntectCalble(this);
				if (getControl() != null && port.getControl() != null) {
					control.bindWithPort(port.getControl(),
							CableControl.Side.START);
				}
			} else if (b == null) {
				b = port;
				port.conntectCalble(this);
				if (getControl() != null && port.getControl() != null) {
					control.bindWithPort(port.getControl(),
							CableControl.Side.END);
				}
			}
		}
	}

	@Override
	public String toString() {
		return Integer.toHexString(hashCode()) + "[state=" + state + "]"
				+ "[busyUntilTime=" + busyUntilTime + "]";
	}

	public long getDelay() {
		// TODO uzaleznić to od długości kabla
		return 0;
	}

	public void setBusyUntil(long time) {
		this.busyUntilTime = time;
	}

	public long getBusyUntilTime() {
		return busyUntilTime;
	}

	@Override
	public Control getControl() {
		return control;
	}

	public void setControl(CableControl control) {
		this.control = control;
	}

	public void receivePackage(Package pack, Port fromPort) {
		long currentTime = Clock.getInstance().getCurrentTime();
		setBusyUntil(currentTime + getDelay());
		if (getState() == CableState.IDLE) {
			setState(CableState.BUSY);
			Clock.getInstance().addEvent(
					new CableSendsEvent(currentTime + getDelay(), this
							.getOtherEnd(fromPort), pack));
		} else {
			setState(CableState.COLISION);
			cable_log.debug("There was collision. Package was lost.");
		}

	}

	public void sendPackage(Package pack, Port port) {
		long time = Clock.getInstance().getCurrentTime();
		if (!getState().equals(CableState.COLISION)) {
			Clock.getInstance().addEvent(
					new PortReceivesEvent(time, port, pack));
		}
		if (getBusyUntilTime() <= time) {
			setState(CableState.IDLE);
		}

	}
}
