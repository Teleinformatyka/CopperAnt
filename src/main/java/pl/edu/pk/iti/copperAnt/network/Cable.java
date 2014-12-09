package pl.edu.pk.iti.copperAnt.network;

import javafx.scene.control.Control;
import pl.edu.pk.iti.copperAnt.gui.CableControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;

public class Cable implements WithControl {

	Port a;
	Port b;

	CableState state;
	private long busyUntilTime;
	private CableControl control;

	public Cable() {
		this(false);
	}

	public Cable(boolean withGui) {
		state = CableState.IDLE;
		if (withGui) {
			this.control = new CableControl();
		}
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
}
