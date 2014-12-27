package pl.edu.pk.iti.copperAnt.network;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.pk.iti.copperAnt.network.TestHelper.portIsConnectedToOneOfCableEnds;

import org.junit.Test;

import pl.edu.pk.iti.copperAnt.simulation.MockDevice;

public class PortTest {

	@Test
	public void conntectCalbleImpactsCableFieldsTest() throws IOException {
		// given
		Port port = new Port(new MockDevice());
		Cable cable = new Cable();
		// when
		port.conntectCalble(cable);
		// then
		assertEquals(cable, port.getCable());
		assertTrue("One of cable ends is conncted to port",
				portIsConnectedToOneOfCableEnds(port, cable));
	}

	@Test
	public void disconnectCableTest() throws IOException {
		Port port = new Port(new MockDevice());
		Cable cable = new Cable();
		port.conntectCalble(cable);
		// when
		port.disconnectCable();
		// then
		assertNull(port.getCable());
		assertFalse("None of cable ends is conncted to port",
				portIsConnectedToOneOfCableEnds(port, cable));
	}

	@Test
	public void cannotConnectWhenCableExistsTest() throws IOException {
		Port port = new Port(new MockDevice());
		Cable cable1 = new Cable();
		Cable cable2 = new Cable();
		port.conntectCalble(cable1);
		// when
		port.conntectCalble(cable2);
		// then
		assertFalse(port.getCable().equals(cable2));
		assertFalse(portIsConnectedToOneOfCableEnds(port, cable2));
	}

}
