package pl.edu.pk.iti.copperAnt.simulation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.PackageType;

public class TwoComputersNonUnitTestScenarios {

	@Test
	public void secndComputerReceivesWhatFirstSends() {
		// given
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		computer1.setPort(spy(computer1.getPort()));
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"));
		computer2.setPort(spy(computer2.getPort()));
		Cable cable = spy(new Cable());
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());
		Package pack = new Package();
		pack.setDestinationMAC(computer2.getPort().getMAC());

		// when
		computer1.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(computer1.getPort()).sendPackage(pack);
		verify(cable).receivePackage(pack, computer1.getPort());
		verify(cable).sendPackage(pack, computer2.getPort());
		verify(computer2.getPort()).receivePackage(pack);
	}

	@Test
	public void computersSendsCorrectArpRqWhenNeeded() {
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
		verify(computer1.getPort(), atLeastOnce()).sendPackage(
				packageCaptor.capture());

		Package arpRQ = packageCaptor.getAllValues().get(0);
		assertEquals(PackageType.ARP_REQ, arpRQ.getType());
		assertEquals(Package.MAC_BROADCAST, arpRQ.getDestinationMAC());
		assertEquals("", arpRQ.getDestinationIP());
		assertEquals(computer1.getPort().getMAC(), arpRQ.getSourceMAC());
		assertEquals(computer1.getIP(), arpRQ.getSourceIP());
		assertEquals(computer2.getIP(), arpRQ.getContent());

	}

	@Test
	public void computersSendsCorrectArpRs() {
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
		verify(computer2.getPort(), atLeastOnce()).sendPackage(
				packageCaptor.capture());

		Package arpRQ = packageCaptor.getAllValues().get(0);
		assertEquals(PackageType.ARP_REP, arpRQ.getType());
		assertEquals(computer1.getPort().getMAC(), arpRQ.getDestinationMAC());
		assertEquals(computer1.getIP(), arpRQ.getDestinationIP());
		assertEquals(computer2.getPort().getMAC(), arpRQ.getSourceMAC());
		assertEquals(computer2.getIP(), arpRQ.getSourceIP());
		assertEquals(computer2.getPort().getMAC(), arpRQ.getContent());

	}

	@Test
	public void computersSendsCorrectEchoRqAfterArpResolution() {
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
		verify(computer1.getPort(), atLeastOnce()).sendPackage(
				packageCaptor.capture());

		Package echoRq = packageCaptor.getAllValues().get(1);

		assertEquals(PackageType.ECHO_REQUEST, echoRq.getType());
		assertEquals(computer2.getPort().getMAC(), echoRq.getDestinationMAC());
		assertEquals(computer2.getIP(), echoRq.getDestinationIP());
		assertEquals(computer1.getPort().getMAC(), echoRq.getSourceMAC());
		assertEquals(computer1.getIP(), echoRq.getSourceIP());

	}

	@Test
	public void computersSendsCorrectEchoRsAfterArpResolution() {
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
		verify(computer2.getPort(), atLeastOnce()).sendPackage(
				packageCaptor.capture());

		Package echoRq = packageCaptor.getAllValues().get(1);

		assertEquals(PackageType.ECHO_REPLY, echoRq.getType());
		assertEquals(computer1.getPort().getMAC(), echoRq.getDestinationMAC());
		assertEquals(computer1.getIP(), echoRq.getDestinationIP());
		assertEquals(computer2.getPort().getMAC(), echoRq.getSourceMAC());
		assertEquals(computer2.getIP(), echoRq.getSourceIP());

	}
}
