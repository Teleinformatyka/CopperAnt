package pl.edu.pk.iti.copperAnt.simulation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.simulation.events.SimpleMockEvent;

public class EmptyListFinishConditionTest {
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void testIsSatisfied() {
		// given
		Clock clock = Clock.getInstance();
		clock.setFinishCondition(new EmptyListFinishCondition());
		clock.addEvent(new SimpleMockEvent(10));
		// when
		clock.run();
		// then
		Assert.assertTrue(clock.events.isEmpty());
	}

}
