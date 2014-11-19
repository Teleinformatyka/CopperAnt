package pl.edu.pk.iti.copperAnt.simulation;

import org.junit.Ignore;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Hub;

public class RealTimeSimulationSandbox {

	@Test
	@Ignore
	public void sandbox2() {
		Clock clock = new Clock()
				.withFinishCondition(new MaxTimeFinishCondition(100));
		clock.setRealTime(true);
		clock.setTimeScale(500);
		Computer computer1 = new Computer();
		Computer computer2 = new Computer();
		Cable cable = new Cable();
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());

		computer1.initTrafic(clock);
		clock.run();
	}

	@Test
	@Ignore
	public void sandbox3() {
		Clock clock = new Clock()
				.withFinishCondition(new MaxTimeFinishCondition(100));
		clock.setRealTime(true);
		clock.setTimeScale(500);
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

	private void connectComputerToHub(Computer computer, Hub hub, int portNr) {
		Cable cable = new Cable();
		cable.insertInto(computer.getPort());
		cable.insertInto(hub.getPort(portNr));
	}

}