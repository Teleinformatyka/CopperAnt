package pl.edu.pk.iti.copperAnt.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Router implements Device {
	private static final long DELAY = 1;
	private final List<Port> ports;
	private Clock clock;
    private HashMap<String, Port> routingTable;   // <Network, Port>
	private String MAC;
	private String ip;
	private String network; 

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
	@Override
	public void acceptPackage(Package pack, Port inPort) {
		String destinationIP = pack.getDestinationIP();
	    String sourceIP = pack.getSourceIP();

	  
	    if (!routingTable.containsKey(sourceIP))  {
	    	routingTable.put(sourceIP, inPort);
	    }
		
	    if (routingTable.containsKey(destinationIP)) {
	    	// IP in table
	    	 clock.addEvent(new PortSendsEvent(clock.getCurrentTime() + getDelay(),
	    			routingTable.get(destinationIP), pack));
	    } else {
	    	// Destination Host Unreachable
	   	 clock.addEvent(new PortSendsEvent(clock.getCurrentTime() + getDelay(),
	    			inPort, pack));
	    	
	    }
		

	}
	
	public String getIp() {
		return ip;
	}
	public String getMac() {
		return MAC;
	}
	


	@Override
	public int getDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
