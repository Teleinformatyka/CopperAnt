package pl.edu.pk.iti.copperAnt.simulation.events;

import pl.edu.pk.iti.copperAnt.network.Package;

public class SimpleMockEvent extends Event {

	public SimpleMockEvent(long time) {
		super(time);
	}

	@Override
	public void run() {
		System.out.println(this);

	}

	@Override
	public String toString() {
		return "SimpleMockEvent [time=" + time + "]";
	}

	public Package getPackage() {
		return null;
	}

}
