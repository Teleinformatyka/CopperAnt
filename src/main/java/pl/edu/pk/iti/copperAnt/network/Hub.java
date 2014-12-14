package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pk.iti.copperAnt.Configuration;

import pl.edu.pk.iti.copperAnt.gui.HubControl;
import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Hub   extends Device implements  WithControl {
    
    private static final Logger log = Logger.getLogger("hub_logs");
    
    
    
	private final List<Port> ports;
	private Clock clock;
	private HubControl control;
        private static Integer nrOfLogFile = 1;
        private Logger hub_log = Logger.getLogger("hub_logs"+nrOfLogFile);

	public Hub(int numberOfPorts, Clock clock) throws IOException {
		this(numberOfPorts, clock, false);
	}

	public Hub(int numberOfPorts, Clock clock, boolean withGui) throws IOException {
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
                
                hub_log.addAppender(Configuration.generateAppender("/hub/hub", nrOfLogFile++));
                hub_log.setLevel(Level.INFO);
                log("New hub created with GUI"+(nrOfLogFile-1),3);
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
        
        private void log(String msg, int type){
            switch (type){
                case 1:
                    log.trace(msg);
                    hub_log.trace(msg);
                    break;
                case 2:
                    log.debug(msg);
                    hub_log.debug(msg);
                    break;
                case 3:
                    log.info(msg);
                    hub_log.info(msg);
                    break;
                case 4:
                    log.warn(msg);
                    hub_log.warn(msg);
                    break;
                case 5:
                    log.error(msg);
                    hub_log.error(msg);
                    break;
                case 6:
                    hub_log.fatal(msg);
                    break;
            }
        }
}
