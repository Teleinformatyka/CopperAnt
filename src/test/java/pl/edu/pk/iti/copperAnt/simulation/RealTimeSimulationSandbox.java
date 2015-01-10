package pl.edu.pk.iti.copperAnt.simulation;

import org.junit.Ignore;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Hub;
import pl.edu.pk.iti.copperAnt.network.IPAddress;

public class RealTimeSimulationSandbox {

	@Test
	@Ignore
	public void sandbox2() {
		Clock clock = Clock.getInstance().withFinishCondition(
				new MaxTimeFinishCondition(100));
		clock.setRealTime(true);
		clock.setTimeScale(500);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"));
		Cable cable = new Cable();
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());

		computer1.initTrafic(new IPAddress("192.168.1.2"));
		clock.run();
	}

	@Test
	@Ignore
	public void sandbox3() {
		Clock clock = Clock.getInstance().withFinishCondition(
				new MaxTimeFinishCondition(100));
		clock.setRealTime(true);
		clock.setTimeScale(500);
		Hub hub = new Hub(3);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"));
		Computer computer3 = new Computer(new IPAddress("192.168.1.3"));
		connectComputerToHub(computer1, hub, 0);
		connectComputerToHub(computer2, hub, 1);
		connectComputerToHub(computer3, hub, 2);

		computer1.initTrafic(new IPAddress("192.168.1.2"));
		clock.run();
	}

	private void connectComputerToHub(Computer computer, Hub hub, int portNr) {
		Cable cable = new Cable();
		cable.insertInto(computer.getPort());
		cable.insertInto(hub.getPort(portNr));
	}

}
