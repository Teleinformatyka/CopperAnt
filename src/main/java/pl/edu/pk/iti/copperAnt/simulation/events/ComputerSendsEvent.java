package pl.edu.pk.iti.copperAnt.simulation.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.ConstantTimeIntervalGenerator;
import pl.edu.pk.iti.copperAnt.simulation.TimeIntervalGenerator;

public class ComputerSendsEvent extends Event {
	private static final Logger log = LoggerFactory
			.getLogger(ComputerSendsEvent.class);
	protected Computer computer;
	protected Package pack;
	// TODO tu bedzie zastosowana inna implementacja
	TimeIntervalGenerator intervalGenerator = new ConstantTimeIntervalGenerator(
			100);

	public ComputerSendsEvent(long time, Computer computer, Package pack) {
		super(time);
		this.computer = computer;
		this.pack = pack;
	}

	@Override
	public void run() {
		long timeToNextEvent = intervalGenerator.getTimeInterval();
		PortSendsEvent event = new PortSendsEvent(time, computer.getPort(),
				pack);

		ComputerSendsEvent nextComputerSendsEvent = new ComputerSendsEvent(time
				+ timeToNextEvent, computer, pack)
				.withIntervalGenerator(this.intervalGenerator);
		Clock.getInstance().addEvent(event);
		Clock.getInstance().addEvent(nextComputerSendsEvent);
		log.info(this.toString());
	}

	public TimeIntervalGenerator getIntervalGenerator() {
		return intervalGenerator;
	}

	public void setIntervalGenerator(TimeIntervalGenerator intervalGenerator) {
		this.intervalGenerator = intervalGenerator;
	}

	public ComputerSendsEvent withIntervalGenerator(
			TimeIntervalGenerator generator) {
		setIntervalGenerator(generator);
		return this;
	}

	public Package getPackage() {
		return this.pack;
	}

}
