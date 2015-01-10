package pl.edu.pk.iti.copperAnt.simulation.generators;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.simulation.generators.factory.ContinousDistributionFactory;
import pl.edu.pk.iti.copperAnt.simulation.generators.factory.DistributionFactory;
import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import cern.jet.random.StudentT;

public class ContinousDistributionFactoryTest {
	@Test
	public void correctCreateTest() {
		DistributionFactory factory = new ContinousDistributionFactory();
		Assert.assertEquals(Exponential.class, factory.create("noname", 100)
				.getClass());
		Assert.assertEquals(Normal.class, factory.create("Normal", 100)
				.getClass());
		Assert.assertEquals(StudentT.class, factory.create("StudentT", 1000)
				.getClass());

	}
}
