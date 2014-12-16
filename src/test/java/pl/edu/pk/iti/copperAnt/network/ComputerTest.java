package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.Event;

public class ComputerTest {
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void testSendDHCPPackage() {
		Clock clock = mock(Clock.class);

		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Computer computer = new Computer();
		computer.init();
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 1);
		Event event = ((Event) capturedEvent.get(0));
		assertEquals(event.getPackage().getType(), PackageType.DHCP);
		assertEquals(event.getPackage().getContent(), null);

	}

	@Test
	public void dhcpResponseTest() {
		// given
		Computer computer = new Computer();
		Package pack = new Package();
		pack.setDestinationMAC(computer.getPort().getMAC());
		pack.setType(PackageType.DHCP);
		pack.setContent("192.168.11.11");
		// when
		computer.getPort().receivePackage(pack);
		// then
		assertEquals(computer.getIP(), "192.168.11.11");

	}

	@Test
	public void arpResponseTest() {
		// given
		Computer computer = new Computer();
		Package pack = new Package();
		pack.setDestinationMAC(computer.getPort().getMAC());
		pack.setType(PackageType.ARP_REP);
		pack.setContent("00:B0:D0:86:BB:F7");
		pack.setDestinationMAC(computer.getPort().getMAC());
		pack.setSourceIP("192.168.11.11");
		// when
		computer.getPort().receivePackage(pack);
		// then
		assertEquals(computer.getKnownHostMac("192.168.11.11"),
				"00:B0:D0:86:BB:F7");

	}

}
