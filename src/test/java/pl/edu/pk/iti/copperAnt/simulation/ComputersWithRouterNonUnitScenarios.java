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
import pl.edu.pk.iti.copperAnt.network.Router;

public class ComputersWithRouterNonUnitScenarios {
	@Test
	public void computersCanPingEachOtherViaRouter() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setPort(spy(computer1.getPort()));
		Computer computer2 = new Computer(new IPAddress("192.168.2.1"));
		computer2.setPort(spy(computer2.getPort()));
		Router router = new Router(2);
		Cable cable1 = new Cable();
		cable1.insertInto(router.getPort(0));
		cable1.insertInto(computer1.getPort());
		Cable cable2 = new Cable();
		cable2.insertInto(router.getPort(1));
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
						computer2.getIP(),//
						computer1.getIP(),//
						PackageType.ECHO_REPLY)));
		assertTrue(echoReplyWasReceived);
	}

	@Test
	public void computerSendsToRouterCorrectArpRequest() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setDefaultGateway(new IPAddress("192.168.1.100"));
		computer1.setPort(spy(computer1.getPort()));
		Router router = new Router(1);
		router.setIpForPort(0, new IPAddress("192.168.1.100"));
		Cable cable1 = new Cable();
		cable1.insertInto(router.getPort(0));
		cable1.insertInto(computer1.getPort());
		Package pack = new Package();
		pack.setDestinationIP("192.168.2.1"); // any address outside 192.168.1.0
												// network

		// when
		computer1.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(computer1.getPort(), atLeastOnce()).sendPackage(
				packageCaptor.capture());
		packageCaptor//
				.getAllValues()//
				.stream().forEach(p -> System.out.println(p));
		boolean echoReplyWasReceived = packageCaptor//
				.getAllValues()//
				.stream()
				//
				.anyMatch(
						p -> (TestUtils.checkExpectedParametersOfPackage(p,
								computer1.getPort().getMAC(),//
								computer1.getIP(), //
								Package.MAC_BROADCAST,//
								"",//
								PackageType.ARP_REQ)));

		assertTrue(echoReplyWasReceived);
	}
}
