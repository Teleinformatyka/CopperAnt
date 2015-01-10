package pl.edu.pk.iti.copperAnt.simulation.events;

import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public class ComplexMockEvent extends Event {

	private int delay1;
	private int delay2;

	public ComplexMockEvent(long time) {
		this(time, 10, 100);
	}

	public ComplexMockEvent(long time, int delay1, int delay2) {
		super(time);
		this.delay1 = delay1;
		this.delay2 = delay2;
	}

	public ComplexMockEvent withTime(long time) {
		this.time = time;
		return this;
	}

	@Override
	public void run() {
		Clock.getInstance().addEvent(new SimpleMockEvent(this.time + delay1));
		Clock.getInstance().addEvent(new SimpleMockEvent(this.time + delay2));
		System.out.println(this);

	}

	@Override
	public String toString() {
		return "ComplexMockEvent [time=" + time + "]";
	}

	public Package getPackage() {
		return null;
	}

}
