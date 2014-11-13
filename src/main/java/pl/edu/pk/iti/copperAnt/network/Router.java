package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Router implements Device {
	private static final long DELAY = 1;
	private final List<Port> ports;
	private Clock clock;
	private String m_Mac;
	private String m_ip;

	public Router(int numberOfPorts, Clock clock) {
		this.clock = clock;
		ports = new ArrayList<Port>(numberOfPorts);
		for (int i = 0; i < numberOfPorts; i++) {
			ports.add(new Port(this));
		}
	}

	public Port getPort(int portNumber) {
		return ports.get(portNumber);
	}
	//FIXME: port zrodlowy by sie tez przydał
	@Override
	public void acceptPackage(Package pack, Port inPort) {
		// TODO Auto-generated method stub

	}
	
	public String getIp() {
		return m_ip;
	}
	public String getMac() {
		return m_Mac;
	}
	


	@Override
	public int getDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
