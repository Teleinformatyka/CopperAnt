package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.pk.iti.copperAnt.network.TestHelper.portIsConnectedToOneOfCableEnds;

import org.junit.Test;

public class CableTest {

	@Test
	public void insertIntoImpactsPortFieldsTest() {
		// given
		Port port = new Port();
		Cable cable = new Cable();
		// when
		cable.insertInto(port);
		// then
		assertEquals(cable, port.getCable());
		assertTrue("One of cable ends is conncted to port",
				portIsConnectedToOneOfCableEnds(port, cable));
	}

	@Test
	public void ejectFromPortTest() {
		Port port = new Port();
		Cable cable = new Cable();
		port.conntectCalble(cable);
		// when
		cable.ejectFromPort(port);
		// then
		assertNull(port.getCable());
		assertFalse("None of cable ends is conncted to port",
				portIsConnectedToOneOfCableEnds(port, cable));
	}

	@Test
	public void cannotConnectMoreThanTwoPortsTest() {
		Port port1 = new Port();
		Port port2 = new Port();
		Port port3 = new Port();
		Cable cable = new Cable();
		cable.insertInto(port1);
		cable.insertInto(port2);
		// when
		cable.insertInto(port3);
		// then
		assertFalse(port3.getCable() == null ? false : port3.getCable().equals(
				cable));
		assertFalse(portIsConnectedToOneOfCableEnds(port3, cable));
	}
}