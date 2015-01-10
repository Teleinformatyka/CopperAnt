package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class HubTest {
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void acceptPackageForwardsPachagesToAllPorts() {
		Hub hub = new Hub(4);
		Cable cable0 = new Cable();
		Cable cable1 = new Cable();
		Cable cable2 = new Cable();
		Cable cable3 = new Cable();

		hub.getPort(0).conntectCalble(cable0);
		hub.getPort(1).conntectCalble(cable1);
		hub.getPort(2).conntectCalble(cable2);
		hub.getPort(3).conntectCalble(cable3);

		Device mockDevice = mock(Device.class);
		Port mockPort0 = spy(new Port(mockDevice));
		Port mockPort1 = spy(new Port(mockDevice));
		Port mockPort2 = spy(new Port(mockDevice));
		Port mockPort3 = spy(new Port(mockDevice));

		cable0.insertInto(mockPort0);
		cable1.insertInto(mockPort1);
		cable2.insertInto(mockPort2);
		cable3.insertInto(mockPort3);

		hub.acceptPackage(new Package(), hub.getPort(0));
		Clock.getInstance().run();
		verify(mockPort0).receivePackage(any(Package.class));
		verify(mockPort1).receivePackage(any(Package.class));
		verify(mockPort2).receivePackage(any(Package.class));
		verify(mockPort3).receivePackage(any(Package.class));

	}

	@Test
	public void sendPackageIsNotTheSameAsReceived() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Hub hub = new Hub(4);
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		hub.acceptPackage(pack, hub.getPort(0));
		List<PortSendsEvent> capturedEvents = eventCaptor.getAllValues();
		for (PortSendsEvent event : capturedEvents) {
			assertNotSame(pack, event.getPackage());
		}

	}

	@Test
	public void hubDoesNotChangePackages() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Hub hub = new Hub(4);
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		pack.setSourceMAC("96:66:d5:8d:3b:cb");
		hub.acceptPackage(pack, hub.getPort(0));
		List<PortSendsEvent> capturedEvents = eventCaptor.getAllValues();
		for (PortSendsEvent event : capturedEvents) {
			assertEquals(pack, event.getPackage());
		}

	}
}
