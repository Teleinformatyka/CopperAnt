package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import javafx.scene.control.Control;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pk.iti.copperAnt.Configuration;
import pl.edu.pk.iti.copperAnt.gui.CableControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;

public class Cable implements WithControl {

    
        private static final org.slf4j.Logger log = LoggerFactory.getLogger("cable_logs");
        
	Port a;
	Port b;

	CableState state;
	private long busyUntilTime;
	private CableControl control;
        private static final String path = "/cable/cable";
        private static Integer nrOfLogFile = 1;
        private Logger cable_log = Logger.getLogger("cable_logs"+nrOfLogFile);

	public Cable() throws IOException {
            this(false); 
            cable_log.addAppender(Configuration.generateAppender(path, nrOfLogFile++));
            cable_log.setLevel(Level.INFO);
            log("New cable created without GUI"+(nrOfLogFile-1),3);
	}

	public Cable(boolean withGui) throws IOException {
		state = CableState.IDLE;
		if (withGui) {
			this.control = new CableControl();
		}
                log("New cable created with GUI"+(nrOfLogFile-1),3);
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
		return 2;
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
        
        private void log(String msg, int type){
            switch (type){
                case 1:
                    log.trace(msg);
                    cable_log.trace(msg);
                    break;
                case 2:
                    log.debug(msg);
                    cable_log.debug(msg);
                    break;
                case 3:
                    log.info(msg);
                    cable_log.info(msg);
                    break;
                case 4:
                    log.warn(msg);
                    cable_log.warn(msg);
                    break;
                case 5:
                    log.error(msg);
                    cable_log.error(msg);
                    break;
                case 6:
                    cable_log.fatal(msg);
                    break;
            }
        }
}
