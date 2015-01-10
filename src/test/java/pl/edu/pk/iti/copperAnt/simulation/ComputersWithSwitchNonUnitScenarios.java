package pl.edu.pk.iti.copperAnt.simulation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.TestUtils;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.PackageType;
import pl.edu.pk.iti.copperAnt.network.Switch;

public class ComputersWithSwitchNonUnitScenarios {

	@Test
	public void computersCanPingEachOtherViaSwitch() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setPort(spy(computer1.getPort()));
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"));
		computer2.setPort(spy(computer2.getPort()));
		Switch swotch = new Switch(2);
		Cable cable1 = new Cable();
		cable1.insertInto(swotch.getPort(0));
		cable1.insertInto(computer1.getPort());
		Cable cable2 = new Cable();
		cable2.insertInto(swotch.getPort(1));
		cable2.insertInto(computer2.getPort());
		Package pack = new Package();
		pack.setDestinationIP(computer2.getIP());

		// when
		computer1.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(computer1.getPort(), atLeastOnce()).receivePackage(
				packageCaptor.capture());

		boolean echoReplyWasReceived = packageCaptor//
				.getAllValues()//
				.stream()
				//
				.anyMatch(p -> (TestUtils.checkExpectedParametersOfPackage(p,//
						computer2.getPort().getMAC(),//
						computer2.getIP(),//
						computer1.getPort().getMAC(),//
						computer1.getIP(),//
						PackageType.ECHO_REPLY)));
		assertTrue(echoReplyWasReceived);
	}

}