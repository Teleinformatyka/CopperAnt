package pl.edu.pk.iti.copperAnt.simulation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.simulation.events.SimpleMockEvent;

public class MaxTimeFinishConditionTest {
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void testIsSatisfied() {
		// given
		Clock clock = Clock.getInstance();
		clock.setFinishCondition(new MaxTimeFinishCondition(15));
		clock.addEvent(new SimpleMockEvent(10));
		clock.addEvent(new SimpleMockEvent(20));
		// when
		clock.run();
		// then
		Assert.assertFalse(clock.events.isEmpty());
		Assert.assertTrue(clock.currentTime <= 15);
	}
}
