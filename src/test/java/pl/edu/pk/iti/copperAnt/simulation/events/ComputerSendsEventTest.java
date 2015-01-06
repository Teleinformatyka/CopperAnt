package pl.edu.pk.iti.copperAnt.simulation.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.ConstantTimeIntervalGenerator;

public class ComputerSendsEventTest {

	@Test
	public void computerSendsEventGeneratesNewEvent() {
		// given
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<Event> eventCaptor = ArgumentCaptor
				.forClass(Event.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		// when
		Computer computer = new Computer();
		new ComputerInitializeTrafficEvent(1L, computer, new Package())
				.withIntervalGenerator(new ConstantTimeIntervalGenerator(10))
				.run();

		// then
		Event capturedEvent = eventCaptor.getValue();
		assertEquals(capturedEvent.getTime(), 1L + 10);

	}
}
