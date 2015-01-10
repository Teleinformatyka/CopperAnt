package pl.edu.pk.iti.copperAnt.simulation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
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
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void computersCanPingEachOtherViaRouter() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setPort(spy(computer1.getPort()));
		computer1.setDefaultGateway(new IPAddress("192.168.1.100"));
		Computer computer2 = new Computer(new IPAddress("192.168.2.1"));
		computer2.setDefaultGateway(new IPAddress("192.168.2.100"));
		computer2.setPort(spy(computer2.getPort()));
		Router router = new Router(2);
		router.setIpForPort(0, new IPAddress("192.168.1.100"));
		router.setIpForPort(1, new IPAddress("192.168.2.100"));
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
		packageCaptor//
				.getAllValues()//
				.stream().forEach(p -> System.out.println(p));
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
		boolean echoReplyWasReceived = packageCaptor//
				.getAllValues()//
				.stream()
				//
				.anyMatch(
						p -> (TestUtils.checkExpectedParametersOfPackage(p,
								computer1.getPort().getMAC(),//
								computer1.getIP(), //
								Package.MAC_BROADCAST,//
								"192.168.1.100",//
								PackageType.ARP_REQ)));
		assertTrue(echoReplyWasReceived);
	}

	@Test
	public void routerRepliesWithArpResponse() {
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
		verify(computer1.getPort(), atLeastOnce()).receivePackage(
				packageCaptor.capture());
		boolean echoReplyWasReceived = packageCaptor//
				.getAllValues()//
				.stream()
				//
				.anyMatch(
						p -> (TestUtils.checkExpectedParametersOfPackage(p,
								router.getPort(0).getMAC(), router.getIP(0),
								computer1.getPort().getMAC(),//
								computer1.getIP(), //
								PackageType.ARP_REP)));

		assertTrue(echoReplyWasReceived);
	}

	@Test
	public void computersSendsCorrectEchoReqArferRouterArpRS() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setPort(spy(computer1.getPort()));
		computer1.setDefaultGateway(new IPAddress("192.168.1.100"));
		Computer computer2 = new Computer(new IPAddress("192.168.2.1"));
		computer2.setDefaultGateway(new IPAddress("192.168.2.100"));
		computer2.setPort(spy(computer2.getPort()));
		Router router = new Router(2);
		router.setIpForPort(0, new IPAddress("192.168.1.100"));
		router.setIpForPort(1, new IPAddress("192.168.2.100"));
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
		verify(computer1.getPort(), atLeastOnce()).sendPackage(
				packageCaptor.capture());
		packageCaptor//
				.getAllValues()//
				.stream().forEach(p -> System.out.println(p));
		boolean echoReplyWasReceived = packageCaptor//
				.getAllValues()//
				.stream()
				//
				.anyMatch(p -> (TestUtils.checkExpectedParametersOfPackage(p,//
						computer1.getPort().getMAC(),//
						computer1.getIP(),//
						router.getPort(0).getMAC(),//
						computer2.getIP(),//
						PackageType.ECHO_REQUEST)));
		assertTrue(echoReplyWasReceived);
	}

}
