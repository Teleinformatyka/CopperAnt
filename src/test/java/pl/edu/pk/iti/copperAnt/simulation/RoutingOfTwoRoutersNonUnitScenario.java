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

public class RoutingOfTwoRoutersNonUnitScenario {
	@Test
	public void computersCanPingEachOtherViaTwoRouter() {
		// given
		ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setDefaultGateway(new IPAddress("192.168.1.100"));
		computer1.setPort(spy(computer1.getPort()));

		Router router1 = new Router(2);
		router1.setIpForPort(0, new IPAddress("192.168.1.100"));
		router1.setIpForPort(1, new IPAddress("192.168.2.1"));
		router1.addRouting("192.168.3.0", router1.getPort(1));

		Router router2 = new Router(2);
		router2.setIpForPort(0, new IPAddress("192.168.3.100"));
		router2.setIpForPort(1, new IPAddress("192.168.2.2"));
		router2.addRouting("192.168.1.0", router2.getPort(1));

		Computer computer2 = new Computer(new IPAddress("192.168.3.1"));
		computer2.setDefaultGateway(new IPAddress("192.168.3.100"));
		computer2.setPort(spy(computer2.getPort()));

		Cable cable1 = new Cable();
		cable1.insertInto(computer1.getPort());
		cable1.insertInto(router1.getPort(0));

		Cable cable2 = new Cable();
		cable2.insertInto(router1.getPort(1));
		cable2.insertInto(router2.getPort(1));

		Cable cable3 = new Cable();
		cable3.insertInto(computer2.getPort());
		cable3.insertInto(router2.getPort(0));

		Package pack = new Package();
		pack.setDestinationIP(computer2.getIP());

		// when
		computer1.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(computer1.getPort(), atLeastOnce()).receivePackage(
				packageCaptor.capture());
		packageCaptor.getAllValues().stream()
				.forEach(p -> System.out.println(p));
		boolean expectedPackageWasPresent = packageCaptor//
				.getAllValues()//
				.stream()
				//
				.anyMatch(p -> (TestUtils.checkExpectedParametersOfPackage(p,//
						computer2.getIP(),//
						computer1.getIP(),//
						PackageType.ECHO_REPLY)));
		assertTrue(expectedPackageWasPresent);
	}

	@Test
	public void router1ForwardsEchoRequest() {
		// given
		ArgumentCaptor<Package> router1Port0PackageCaptor = ArgumentCaptor
				.forClass(Package.class);
		ArgumentCaptor<Package> router1Port1PackageCaptor = ArgumentCaptor
				.forClass(Package.class);
		ArgumentCaptor<Package> router2Port0PackageCaptor = ArgumentCaptor
				.forClass(Package.class);
		ArgumentCaptor<Package> router2Port1PackageCaptor = ArgumentCaptor
				.forClass(Package.class);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setDefaultGateway(new IPAddress("192.168.1.100"));

		Router router1 = new Router(2);
		router1.setPort(0, spy(router1.getPort(0)));
		router1.setPort(1, spy(router1.getPort(1)));
		router1.setIpForPort(0, new IPAddress("192.168.1.100"));
		router1.setIpForPort(1, new IPAddress("192.168.2.1"));
		router1.addRouting("192.168.3.0", router1.getPort(1));

		Router router2 = new Router(2);
		router2.setPort(0, spy(router2.getPort(0)));
		router2.setPort(1, spy(router2.getPort(1)));
		router2.setIpForPort(0, new IPAddress("192.168.3.100"));
		router2.setIpForPort(1, new IPAddress("192.168.2.2"));
		router2.addRouting("192.168.1.0", router2.getPort(1));

		Computer computer2 = new Computer(new IPAddress("192.168.3.1"));
		computer2.setDefaultGateway(new IPAddress("192.168.3.100"));
		computer2.setPort(spy(computer2.getPort()));

		Cable cable1 = new Cable();
		cable1.insertInto(computer1.getPort());
		cable1.insertInto(router1.getPort(0));

		Cable cable2 = new Cable();
		cable2.insertInto(router1.getPort(1));
		cable2.insertInto(router2.getPort(1));

		Cable cable3 = new Cable();
		cable3.insertInto(computer2.getPort());
		cable3.insertInto(router2.getPort(0));

		Package pack = new Package();
		pack.setDestinationIP(computer2.getIP());

		// when
		computer1.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(router1.getPort(0), atLeastOnce()).sendPackage(
				router1Port0PackageCaptor.capture());

		verify(router1.getPort(1), atLeastOnce()).sendPackage(
				router1Port1PackageCaptor.capture());

		verify(router2.getPort(0), atLeastOnce()).sendPackage(
				router2Port0PackageCaptor.capture());

		verify(router2.getPort(1), atLeastOnce()).sendPackage(
				router2Port1PackageCaptor.capture());

		boolean expectedPackageWasPresent = router1Port0PackageCaptor//
				.getAllValues()//
				.stream()//
				.anyMatch(p -> (TestUtils.checkExpectedParametersOfPackage(p,//
						router1.getPort(0).getMAC(),//
						computer2.getIP(),//
						computer1.getPort().getMAC(),//
						computer1.getIP(),//
						PackageType.ECHO_REPLY)));

		if (!expectedPackageWasPresent) {
			System.out.println("== router 1 port 0 =================");
			router1Port0PackageCaptor.getAllValues().stream()
					.forEach(p -> System.out.println(p));
			System.out.println("== router 1 port 1 =================");
			router1Port1PackageCaptor.getAllValues().stream()
					.forEach(p -> System.out.println(p));
			System.out.println("== router 2 port 0 =================");
			router2Port0PackageCaptor.getAllValues().stream()
					.forEach(p -> System.out.println(p));
			System.out.println("== router 2 port 1 =================");
			router2Port1PackageCaptor.getAllValues().stream()
					.forEach(p -> System.out.println(p));
		}
		assertTrue(expectedPackageWasPresent);
	}
}
