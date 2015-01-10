package pl.edu.pk.iti.copperAnt;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void testRead() {
		// given
		Configuration conf = Configuration.getInstance();
		// FIXME: jak sie mokuje w Javie?
		conf.init("./configuration.xml");
		List<Properties> data = conf.getData();

		assertEquals(1, data.size());
		assertEquals("router", data.get(0).getProperty("type"));
		assertEquals("4", data.get(0).getProperty("portCount"));

	}

}
