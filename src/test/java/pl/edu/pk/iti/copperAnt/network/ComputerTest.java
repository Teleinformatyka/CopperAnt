package pl.edu.pk.iti.copperAnt.network;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.Event;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;
public class ComputerTest {
	@Test
	public void testSendDHCPPackage() throws IOException{
		Clock clock = mock(Clock.class);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		
		
		Computer comp = new Computer();

		comp.init(clock);
		List<Event> capturedEvent = eventCaptor.getAllValues();
		assertEquals(capturedEvent.size(), 1);
		Event event =  ((Event)capturedEvent.get(0));

		assertEquals(event.getPackage().getType(), PackageType.DHCP);
		assertEquals(event.getPackage().getContent(), null);

		
			
		
	}
	

}
