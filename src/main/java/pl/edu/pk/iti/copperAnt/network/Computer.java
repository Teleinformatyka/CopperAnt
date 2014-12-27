package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pk.iti.copperAnt.Configuration;

import pl.edu.pk.iti.copperAnt.gui.ComputerControl;
import pl.edu.pk.iti.copperAnt.gui.WithControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.DistributionTimeIntervalGenerator;
import pl.edu.pk.iti.copperAnt.simulation.events.ComputerSendsEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Computer extends Device implements WithControl {
    
    private static final Logger log = Logger.getLogger("computer_logs");
    
	private Port port;
	private IPAddress ip;
	private ComputerControl control;
        private static Integer nrOfLogFile = 1;
        private Logger computer_log = Logger.getLogger("computer_logs"+nrOfLogFile);
        
	public Computer() throws IOException {
		this(null);
	}

	public Computer(IPAddress ip) throws IOException {
		this(ip, false);
	}

	public Computer(IPAddress ip, boolean withGui) throws IOException{
		this.port = new Port(this, withGui);
		this.ip = ip;
		if (withGui) {
			this.control = new ComputerControl(port.getControl());
		}
                computer_log.addAppender(Configuration.generateAppender("/computer/computer", nrOfLogFile++));
                computer_log.setLevel(Level.INFO);
                log("New computer created with GUI"+(nrOfLogFile-1),3);
	}

	public Port getPort() {
		return port;
	}

	@Override
	public void acceptPackage(Package pack, Port inPort) {
		// assume is response for arp package
		if (pack.getType() == PackageType.DHCP && this.ip == null) {
			this.ip = new IPAddress(pack.getContent());
		} else if (pack.getType() == PackageType.ECHO_REQUEST
				&& pack.getDestinationIP() == this.ip.toString()) {
			// TODO: add event to pong
		}

		System.out.println("Computer received package");

	}

	public void initTrafic(Clock clock) {
		if (this.ip == null) {
			return;
		}
		long time = clock.getCurrentTime() + this.getDelay();
		Package pack = new Package(PackageType.ECHO_REQUEST, UUID.randomUUID()
				.toString());
		pack.setSourceIP(this.ip.toString());
		IPAddress dest = this.ip;
		Random generator = new Random();
		dest.set(generator.nextInt(4) + 1, generator.nextInt(254) + 1);
		pack.setDestinationIP(dest.toString());
		ComputerSendsEvent event = new ComputerSendsEvent(time, this, pack);
		event.setIntervalGenerator(new DistributionTimeIntervalGenerator());
		clock.addEvent(event);
	}

	public void init(Clock clock) {
		long time = clock.getCurrentTime() + this.getDelay();
		Package pack = new Package(PackageType.DHCP, null);
		clock.addEvent(new PortSendsEvent(time, this.port, pack));
	}

	@Override
	public ComputerControl getControl() {
		return control;
	}
        
        private void log(String msg, int type){
            switch (type){
                case 1:
                    log.trace(msg);
                    computer_log.trace(msg);
                    break;
                case 2:
                    log.debug(msg);
                    computer_log.debug(msg);
                    break;
                case 3:
                    log.info(msg);
                    computer_log.info(msg);
                    break;
                case 4:
                    log.warn(msg);
                    computer_log.warn(msg);
                    break;
                case 5:
                    log.error(msg);
                    computer_log.error(msg);
                    break;
                case 6:
                    computer_log.fatal(msg);
                    break;
            }
        }
}
