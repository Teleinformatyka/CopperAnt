package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.pk.iti.copperAnt.network.TestHelper.portIsConnectedToOneOfCableEnds;

import org.junit.Test;

import pl.edu.pk.iti.copperAnt.simulation.MockDevice;

public class CableTest {

	@Test
	public void insertIntoImpactsPortFieldsTest() throws IOException{
		// given
		Port port1 = new Port(new MockDevice());
		Port port2 = new Port(new MockDevice());
		Cable cable = new Cable();
		// when
		cable.insertInto(port1);
		cable.insertInto(port2);
		// then
		assertEquals(cable, port1.getCable());
		assertTrue("One of cable ends is conncted to port",
				portIsConnectedToOneOfCableEnds(port1, cable));
		assertEquals(cable, port2.getCable());
		assertTrue("One of cable ends is conncted to port",
				portIsConnectedToOneOfCableEnds(port2, cable));
	}

	@Test
	public void ejectFromPortTest() throws IOException{
		Port port = new Port(new MockDevice());
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
	public void cannotConnectMoreThanTwoPortsTest() throws IOException{
		Port port1 = new Port(new MockDevice());
		Port port2 = new Port(new MockDevice());
		Port port3 = new Port(new MockDevice());
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
