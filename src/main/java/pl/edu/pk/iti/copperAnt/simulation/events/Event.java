package pl.edu.pk.iti.copperAnt.simulation.events;

import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public abstract class Event {

	protected long time;

	public Event(long time) {
		this.time = time;
	}

	public long getTime() {
		return this.time;
	}

	abstract public void run(Clock clock);

	@Override
	public String toString() {
		return time + ": ";
	}
	abstract public  Package getPackage();
	
	//for notifying stats updateing in Clock
	public void notifyAboutUsage(Clock clock, Port port) { 
		clock.updateStatistics(System.currentTimeMillis(), port, this.getPackage());	
	}

}