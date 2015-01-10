package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IPAddressTest {

	@Test
	public void testCreate() {
		IPAddress ip = new IPAddress("192.168.0.1");
		assertEquals(ip.toString(), "192.168.0.1");
	}

	@Test
	public void testCreate2() {
		IPAddress ip = new IPAddress("192.168.0.1");
		assertEquals(ip.toString(), "192.168.0.1");
	}

	@Test
	public void testincrement() {
		IPAddress ip = new IPAddress("192.168.0.1");
		ip.increment();
		assertEquals(ip.toString(), "192.168.0.2");
	}

	@Test
	public void testincrement2() {
		IPAddress ip = new IPAddress("192.168.0.1");
		ip.increment(3);
		assertEquals(ip.toString(), "192.168.1.1");
	}

	@Test
	public void testNetwork() {
		IPAddress ip = new IPAddress("192.168.0.1");
		assertEquals(ip.getNetwork(), "192.168.0.0");
	}

	@Test
	public void testNetwork2() {
		IPAddress ip = new IPAddress("192.168.0.155");
		assertEquals(ip.getNetwork(), "192.168.0.0");
	}

	@Test
	public void testSet() {
		IPAddress ip = new IPAddress("192.168.0.1");
		ip.set(3, 127);
		assertEquals(ip.toString(), "192.168.127.1");
	}

	@Test
	public void testIsInSubnet() {
		IPAddress ip = new IPAddress("192.168.0.155");

		assertTrue(IPAddress.isInSubnet(ip.toString(), ip.getNetwork(),
				"255.255.255.0"));
	}

	@Test
	public void testIsInRange() {
		IPAddress ip = new IPAddress("192.168.0.155");

		assertTrue(ip.isInRange("192.168.0.15"));
	}

	@Test
	public void testIsInRange2() {
		IPAddress ip = new IPAddress("192.168.0.155");

		assertTrue(ip.isInRange("192.168.0.135"));
	}

	@Test
	public void testIsNotInRange() {
		IPAddress ip = new IPAddress("192.168.0.155");

		assertFalse(ip.isInRange("192.163.0.15"));
	}

	@Test
	public void testIsNotInSubnet() {
		assertFalse(IPAddress.isInSubnet("192.168.12.1", "192.168.0.0",
				"255.255.255.128"));
	}

	@Test
	public void decrementTest() {
		final IPAddress ipAddress = new IPAddress("192.168.1.3");
		assertEquals(ipAddress.decrement().toString(), "192.168.1.2");
	}

	@Test
	public void decrementTest2() {
		final IPAddress ipAddress = new IPAddress("192.168.1.0");
		assertEquals(ipAddress.decrement().toString(), "192.168.1.0");
	}

	@Test
	public void getBrodcastTest() {
		final IPAddress ipAddress = new IPAddress("192.168.1.0");
		assertEquals(ipAddress.getBrodcast().toString(), "192.168.1.255");
	}
}
