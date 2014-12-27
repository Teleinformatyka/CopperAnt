package pl.edu.pk.iti.copperAnt.simulation;

import java.io.IOException;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Hub;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class SimulationSandbox {

	@Test
	public void sandbox1() throws IOException{
		Clock clock = new Clock();
		clock.setFinishCondition(new MaxTimeFinishCondition(100));
		Port port1 = new Port(new MockDevice());
		Port port2 = new Port(new MockDevice());
		Cable cable = new Cable();
		cable.insertInto(port1);
		cable.insertInto(port2);

		clock.addEvent(new PortSendsEvent(0, port1, new Package()));
		clock.addEvent(new PortSendsEvent(0, port2, new Package()));
		clock.run();
	}

	@Test
	public void sandbox2() throws IOException{
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

	@Test
	public void sandbox3() throws IOException{
		Clock clock = new Clock()
				.withFinishCondition(new MaxTimeFinishCondition(100));
		Hub hub = new Hub(3, clock);
		Computer computer1 = new Computer();
		Computer computer2 = new Computer();
		Computer computer3 = new Computer();
		connectComputerToHub(computer1, hub, 0);
		connectComputerToHub(computer2, hub, 1);
		connectComputerToHub(computer3, hub, 2);

		computer1.initTrafic(clock);
		clock.run();
	}

	private void connectComputerToHub(Computer computer, Hub hub, int portNr) throws IOException{
		Cable cable = new Cable();
		cable.insertInto(computer.getPort());
		cable.insertInto(hub.getPort(portNr));
	}

}
