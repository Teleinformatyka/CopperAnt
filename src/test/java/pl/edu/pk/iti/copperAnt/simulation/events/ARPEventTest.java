package pl.edu.pk.iti.copperAnt.simulation.events;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.PackageType;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.simulation.Clock;

public class ARPEventTest {

	@Test
	public void testRun() {
		Computer comp = new Computer(new IPAddress("192.168.2.1"));
		comp.addKnownHost("192.168.1.1", "a");
		Clock clock = mock(Clock.class);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Package pack = new Package(PackageType.ARP_REQ, "192.168.1.1");

		ARPEvent test = new ARPEvent(0, comp, pack);
		test.run();
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 0);

	}
}
