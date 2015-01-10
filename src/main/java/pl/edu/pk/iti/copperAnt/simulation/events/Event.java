package pl.edu.pk.iti.copperAnt.simulation.events;

import pl.edu.pk.iti.copperAnt.network.Package;

public abstract class Event {

	protected long time;

	public Event(long time) {
		this.time = time;
	}

	public long getTime() {
		return this.time;
	}

	abstract public void run();

	@Override
	public String toString() {
		return time + ": ";
	}

	abstract public Package getPackage();

}