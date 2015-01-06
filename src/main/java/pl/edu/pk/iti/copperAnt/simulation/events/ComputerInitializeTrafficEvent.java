package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.ConstantTimeIntervalGenerator;
import pl.edu.pk.iti.copperAnt.simulation.TimeIntervalGenerator;

public class ComputerInitializeTrafficEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(ComputerInitializeTrafficEvent.class);
	protected Computer computer;
	protected Package pack;
	// TODO tu bedzie zastosowana inna implementacja
	TimeIntervalGenerator intervalGenerator = new ConstantTimeIntervalGenerator(
			100);

	public ComputerInitializeTrafficEvent(long time, Computer computer,
			Package pack) {
		super(time);
		this.computer = computer;
		this.pack = pack;
	}

	@Override
	public void run() {
		long timeToNextEvent = intervalGenerator.getTimeInterval();
		computer.sendPackage(pack);
		ComputerInitializeTrafficEvent nextComputerInitializeTrafficEvent = new ComputerInitializeTrafficEvent(
				time + timeToNextEvent, computer, pack)
				.withIntervalGenerator(this.intervalGenerator);
		Clock.getInstance().addEvent(nextComputerInitializeTrafficEvent);
		log.info(this.toString());
	}

	public TimeIntervalGenerator getIntervalGenerator() {
		return intervalGenerator;
	}

	public void setIntervalGenerator(TimeIntervalGenerator intervalGenerator) {
		this.intervalGenerator = intervalGenerator;
	}

	public ComputerInitializeTrafficEvent withIntervalGenerator(
			TimeIntervalGenerator generator) {
		setIntervalGenerator(generator);
		return this;
	}

	public Package getPackage() {
		return this.pack;
	}

}
