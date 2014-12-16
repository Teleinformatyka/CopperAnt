package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.Event;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class RouterTest {
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void testEmtpyRoutingTable() {
		// given
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		final int numberOfPorts = 4;
		Router router = new Router(numberOfPorts);
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		pack.setSourceMAC("96:66:d5:8d:3b:cb");
		// when
		router.acceptPackage(pack, router.getPort(0));
		// then
		List<PortSendsEvent> capturedEvents = eventCaptor.getAllValues();
		assertEquals(3, capturedEvents.size());
		for (int i = 1; i < numberOfPorts; i++) {
			assertTrue(capturedEvents.stream().anyMatch(
					e -> e.getPort().equals(router.getPort(1))));
		}

	}

	@Test
	public void testNotEmtpyRoutingTable() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Router router = new Router(4);
		router.addRouting("testowy", router.getPort(2));
		Package pack = new Package();
		pack.setDestinationIP("testowy");
		PortSendsEvent expected = new PortSendsEvent(clock.getCurrentTime()
				+ router.getDelay(), router.getPort(2), pack);
		router.acceptPackage(pack, router.getPort(0));
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 1);
		assertEquals(capturedEvent.get(0).toString(), expected.toString());

	}

	@Test
	public void testRequestForIp() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);

		Router router = new Router(4);
		Package pack = new Package(PackageType.DHCP, null);
		pack.setSourceMAC("aaaaaa");
		router.acceptPackage(pack, router.getPort(0));
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 1);
		Event event = ((Event) capturedEvent.get(0));
		assertTrue(event.getPackage().getContent() != null);
		assertEquals(PackageType.DHCP, event.getPackage().getType());
	}

	@Test
	public void testRequestForIp2() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Properties config = new Properties();
		config.setProperty("numbersOfPorts", "4");

		Router router = new Router(config);
		Package pack = new Package(PackageType.DHCP, null);
		pack.setSourceMAC("aaaaaa");
		router.acceptPackage(pack, router.getPort(0));
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 1);
		Event event = ((Event) capturedEvent.get(0));
		assertEquals(event.getPackage().getContent(),
				new IPAddress(router.getIP(0)).increment());
		assertEquals(PackageType.DHCP, event.getPackage().getType());
	}

	@Test
	public void testTTl0() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Properties config = new Properties();
		config.setProperty("numbersOfPorts", "4");
		config.setProperty("DHCPstartIP", "192.168.0.1");

		Router router = new Router(config);
		Package pack = new Package(PackageType.ECHO_REQUEST, "wiadomosc");
		pack.setSourceMAC("aaaaaa");
		pack.setDestinationIP("192.168.222.222");
		for (int i = 0; i < 101; ++i)
			pack.validTTL();
		router.acceptPackage(pack, router.getPort(0));
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 1);
		Event event = ((Event) capturedEvent.get(0));
		assertEquals("TTL<0", event.getPackage().getContent());
		assertEquals(PackageType.DESTINATION_UNREACHABLE, event.getPackage()
				.getType());
	}

	@Test
	public void testRouting() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Properties config = new Properties();
		config.setProperty("numbersOfPorts", "4");

		Router router = new Router(config);
		Package pack = new Package(PackageType.ECHO_REQUEST, "wiadomosc");
		pack.setSourceMAC("aaaaaa");
		pack.setDestinationIP(new IPAddress(router.getIP(2)).increment());

		router.acceptPackage(pack, router.getPort(0));
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(1, capturedEvent.size());
		PortSendsEvent event = ((PortSendsEvent) capturedEvent.get(0));

		assertEquals(1, capturedEvent.size());
		assertEquals(event.getPort(), router.getPort(2));
	}

	@Test
	public void setAndGetIpTest() {
		// given
		Router router = new Router(3);
		// when
		router.setIpForPort(2, new IPAddress("192.168.1.12"));
		String ip = router.getIP(2);
		// then
		assertEquals("192.168.1.12", ip);
	}

	@Test
	public void setAndGetIp2Test() {
		// given
		Router router = new Router(3);
		// when
		router.setIpForPort(2, new IPAddress("192.168.1.12"));
		String ip = router.getIP(router.getPort(2));
		// then
		assertEquals("192.168.1.12", ip);
	}

	@Test
	public void sendPackageIsNotTheSameAsReceived() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Router router = new Router(4);
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		router.acceptPackage(pack, router.getPort(0));
		List<PortSendsEvent> capturedEvents = eventCaptor.getAllValues();
		for (PortSendsEvent event : capturedEvents) {
			assertNotSame(pack, event.getPackage());
		}
	}

	@Test
	public void routerCanRespnseForPingTest() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Router router = new Router(1);
		router.setIpForPort(0, new IPAddress("192.158.2.55"));
		Package pack = new Package();
		pack.setSourceIP("192.158.2.1");
		pack.setDestinationIP("192.158.2.55");
		pack.setType(PackageType.ECHO_REQUEST);
		router.acceptPackage(pack, router.getPort(0));
		PortSendsEvent capturedEvent = eventCaptor.getValue();
		assertEquals(capturedEvent.getPort(), router.getPort(0));
		assertEquals(capturedEvent.getPackage().getType(),
				PackageType.ECHO_REPLY);
		assertEquals(capturedEvent.getPackage().getSourceIP().toString(),
				"192.158.2.55");
		assertEquals(capturedEvent.getPackage().getDestinationIP().toString(),
				"192.158.2.1");
	}

}
