package pl.edu.pk.iti.copperAnt.simulation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.PackageType;

public class ComputersWithHubNonUnitScenarios {

	@Test
	public void computersCanPingEachOtherViaHub() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setPort(spy(computer1.getPort()));
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"));
		computer2.setPort(spy(computer2.getPort()));
		Cable cable = spy(new Cable());
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());
		Package pack = new Package();
		pack.setDestinationIP(computer2.getIP());

		// when
		computer1.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(computer1.getPort(), atLeastOnce()).receivePackage(
				packageCaptor.capture());

		Package echoRq = packageCaptor.getAllValues().get(1);

		assertEquals(PackageType.ECHO_REPLY, echoRq.getType());
		assertEquals(computer1.getPort().getMAC(), echoRq.getDestinationMAC());
		assertEquals(computer1.getIP(), echoRq.getDestinationIP());
		assertEquals(computer2.getPort().getMAC(), echoRq.getSourceMAC());
		assertEquals(computer2.getIP(), echoRq.getSourceIP());

	}

}
