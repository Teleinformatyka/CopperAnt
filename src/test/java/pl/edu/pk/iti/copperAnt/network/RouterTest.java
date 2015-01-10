package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
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
		int numberOfPorts = 4;
		Router router = new Router(numberOfPorts);
		for (int i = 0; i < numberOfPorts; ++i) {
			router.getPort(i).conntectCalble(new Cable());
			router.setPort(i, spy(router.getPort(i)));

		}
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		pack.setSourceMAC("96:66:d5:8d:3b:cb");
		pack.setSourceIP("192.168.1.1");
		// when
		router.acceptPackage(pack, router.getPort(0));
		Clock.getInstance().tick();
		// then
		verify(router.getPort(0), never()).sendPackage(any());
		verify(router.getPort(1)).sendPackage(any());
		verify(router.getPort(2)).sendPackage(any());
		verify(router.getPort(3)).sendPackage(any());

	}

	@Test
	public void testNotEmtpyRoutingTable() {
		// given
		int numberOfPorts = 4;
		Router router = new Router(numberOfPorts);
		for (int i = 0; i < numberOfPorts; ++i) {
			router.getPort(i).conntectCalble(new Cable());
			router.setPort(i, spy(router.getPort(i)));
		}
		router.addRouting("testowy", router.getPort(2));
		Package pack = new Package();
		pack.setDestinationIP("testowy");
		pack.setSourceIP("192.168.1.1");

		// when
		router.acceptPackage(pack, router.getPort(0));
		Clock.getInstance().tick();
		// then
		verify(router.getPort(0), never()).sendPackage(any());
		verify(router.getPort(1), never()).sendPackage(any());
		verify(router.getPort(2)).sendPackage(any());
		verify(router.getPort(3), never()).sendPackage(any());

	}

	@Test
	public void testRequestForIp() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);

		int numberOfPorts = 4;
		Router router = new Router(numberOfPorts);
		for (int i = 0; i < numberOfPorts; ++i) {
			router.getPort(i).conntectCalble(new Cable());
		}
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
		for (int i = 0; i < 4; ++i) {
			router.getPort(i).conntectCalble(new Cable());
		}
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
		// given
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
		for (int i = 0; i < 4; ++i) {
			router.getPort(i).conntectCalble(new Cable());
		}
		Package pack = new Package(PackageType.ECHO_REQUEST, "wiadomosc");
		pack.setSourceMAC("aaaaaa");
		pack.setDestinationIP("192.168.222.222");
		pack.setSourceIP("192.168.1.1");
		for (int i = 0; i < 256; ++i)
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
		// given

		Properties config = new Properties();
		config.setProperty("numbersOfPorts", "4");
		Router router = new Router(config);
		for (int i = 0; i < 4; ++i) {
			router.getPort(i).conntectCalble(new Cable());
			router.setPort(i, spy(router.getPort(i)));
		}
		Package pack = new Package(PackageType.ECHO_REQUEST, "wiadomosc");
		pack.setSourceMAC("aaaaaa");
		pack.setDestinationIP(new IPAddress(router.getIP(2)).increment());
		pack.setSourceIP("192.168.1.1");
		// when
		router.acceptPackage(pack, router.getPort(0));
		Clock.getInstance().tick();
		// then
		verify(router.getPort(0), never()).sendPackage(any());
		verify(router.getPort(1), never()).sendPackage(any());
		verify(router.getPort(2)).sendPackage(any());
		verify(router.getPort(3), never()).sendPackage(any());
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
		// given
		ArgumentCaptor<Package> eventCaptor = ArgumentCaptor
				.forClass(Package.class);
		Router router = new Router(1);
		router.setPort(0, spy(router.getPort(0)));
		doNothing().when(router.getPort(0)).sendPackage(eventCaptor.capture());
		router.setIpForPort(0, new IPAddress("192.158.2.55"));
		router.addToArpTable("192.158.2.1", "so:me:th:in:gg");
		Package pack = new Package();
		pack.setSourceIP("192.158.2.1");
		pack.setDestinationIP("192.158.2.55");
		pack.setType(PackageType.ECHO_REQUEST);

		// when
		router.acceptPackage(pack, router.getPort(0));
		Clock.getInstance().run();
		// then
		List<Package> allValues = eventCaptor.getAllValues();
		Package capturedPackage = allValues.get(allValues.size() - 1);
		assertEquals(capturedPackage.getType(), PackageType.ECHO_REPLY);
		assertEquals(capturedPackage.getSourceIP().toString(), "192.158.2.55");
		assertEquals(capturedPackage.getDestinationIP().toString(),
				"192.158.2.1");
	}

}
