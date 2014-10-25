package pl.edu.pk.iti.copperAnt.simulation;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class ClockTest {

	@Test
	public void addEventTest() {
		// given
		Clock clock = new Clock();
		// when
		clock.addEvent(new SimpleMockEvent().withTime(6));
		clock.addEvent(new SimpleMockEvent().withTime(3));
		clock.addEvent(new SimpleMockEvent().withTime(1));
		clock.addEvent(new SimpleMockEvent().withTime(2));
		clock.addEvent(new SimpleMockEvent().withTime(2));
		clock.addEvent(new SimpleMockEvent().withTime(4));
		// then
		assertEquals(clock.getNumberOfWaitingEvent(), 6);
		assertEquals(clock.getEventFromList(0).getTime(), 1);
		assertEquals(clock.getEventFromList(1).getTime(), 2);
		assertEquals(clock.getEventFromList(2).getTime(), 2);
		assertEquals(clock.getEventFromList(3).getTime(), 3);
		assertEquals(clock.getEventFromList(4).getTime(), 4);
		assertEquals(clock.getEventFromList(5).getTime(), 6);

	}

	@Test
	public void runSimpleEventsTest() throws InterruptedException {
		// given
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		Clock clock = new Clock();
		// when
		clock.addEvent(new SimpleMockEvent().withTime(6));
		clock.addEvent(new SimpleMockEvent().withTime(3));
		clock.addEvent(new SimpleMockEvent().withTime(1));
		clock.addEvent(new SimpleMockEvent().withTime(2));
		clock.addEvent(new SimpleMockEvent().withTime(2));
		clock.addEvent(new SimpleMockEvent().withTime(4));
		// then
		Thread thread = new Thread(clock);
		thread.start();
		thread.join();
		System.setOut(null);
		assertEquals("SimpleMockEvent [time=1]\n"
				+ "SimpleMockEvent [time=2]\n" + "SimpleMockEvent [time=2]\n"
				+ "SimpleMockEvent [time=3]\n" + "SimpleMockEvent [time=4]\n"
				+ "SimpleMockEvent [time=6]\n", outContent.toString());

	}

	@Test
	public void runEventsWithConsequencesTest() throws InterruptedException {
		// given
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		Clock clock = new Clock();
		// when
		clock.addEvent(new ComplexMockEvent().withTime(6));
		clock.addEvent(new ComplexMockEvent().withTime(3));
		clock.addEvent(new ComplexMockEvent().withTime(4));
		// then
		Thread thread = new Thread(clock);
		thread.start();
		thread.join();
		System.setOut(null);
		assertEquals("ComplexMockEvent [time=3]\n"
				+ "ComplexMockEvent [time=4]\n" + "ComplexMockEvent [time=6]\n"
				+ "SimpleMockEvent [time=13]\n" + "SimpleMockEvent [time=14]\n"
				+ "SimpleMockEvent [time=16]\n"
				+ "SimpleMockEvent [time=103]\n"
				+ "SimpleMockEvent [time=104]\n"
				+ "SimpleMockEvent [time=106]\n", outContent.toString());

	}

}
