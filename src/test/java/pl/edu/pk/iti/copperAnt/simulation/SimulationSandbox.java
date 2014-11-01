package pl.edu.pk.iti.copperAnt.simulation;

import org.junit.Test;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class SimulationSandbox {

	@Test
	public void sandbox1() {
		Clock clock = new Clock();
		clock.setFinishCondition(new MaxTimeFinishCondition(100));
		Port port1 = new Port();
		Port port2 = new Port();
		Cable cable = new Cable();
		cable.insertInto(port1);
		cable.insertInto(port2);

		clock.addEvent(new PortSendsEvent(0, port1, new Package()));
		clock.addEvent(new PortSendsEvent(0, port2, new Package()));
		clock.run();
	}

	@Test
	public void sandbox2() {
		Clock clock = new Clock()
				.withFinishCondition(new MaxTimeFinishCondition(100));
		Computer computer1 = new Computer();
		Computer computer2 = new Computer();
		Cable cable = new Cable();
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());

		computer1.initTrafic(clock);
		clock.run();
	}

}
