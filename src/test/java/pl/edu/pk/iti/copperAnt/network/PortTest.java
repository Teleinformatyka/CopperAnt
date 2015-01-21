package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static pl.edu.pk.iti.copperAnt.network.TestHelper.portIsConnectedToOneOfCableEnds;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.EmptyListFinishCondition;
import pl.edu.pk.iti.copperAnt.simulation.MockDevice;

public class PortTest {
	@Before
	public void setUp() {
		Clock.resetInstance();
	}

	@Test
	public void conntectCalbleImpactsCableFieldsTest() {
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
	public void disconnectCableTest() {
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
	public void cannotConnectWhenCableExistsTest() {
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

	@Test
	public void byDefaultAcceptFramesWithWrongDestMac() {
		final Device mockDevice = mock(Device.class);
		Port port = new Port(mockDevice);
		// when
		port.receivePackage(new Package());
		// then
		verify(mockDevice).acceptPackage(any(), any());
	}

	@Test
	public void canRejectFramesWithWrongDestMac() {
		final Device mockDevice = mock(Device.class);
		when(mockDevice.getLogger()).thenReturn(Logger.getRootLogger());
		Port port = new Port(mockDevice);
		port.setControlDestinationMacOfPackages(true);
		Package pack = new Package();
		pack.setDestinationMAC("no:tc:or:re:ct");
		// when
		port.receivePackage(pack);
		// then
		verify(mockDevice, never()).acceptPackage(any(), any());
	}

	@Test
	public void canBufferPackagesWhenCableIsBussy() {
		Port port = new Port(mock(Device.class));
		Cable cableSpy = spy(new Cable());
		cableSpy.insertInto(new Port(mock(Device.class)));
		port.conntectCalble(cableSpy);
		// when
		cableSpy.setState(CableState.BUSY);
		port.sendPackage(new Package());
		Clock.getInstance().tick();
		// then
		verify(cableSpy, never()).receivePackage(any(), any());
	}

	@Test
	public void canBufferPackagesWhenCableIsInCollision() {
		Port port = new Port(mock(Device.class));
		Cable cableSpy = spy(new Cable());
		cableSpy.insertInto(new Port(mock(Device.class)));
		port.conntectCalble(cableSpy);
		// when
		cableSpy.setState(CableState.COLISION);
		port.sendPackage(new Package());
		Clock.getInstance().tick();
		// then
		verify(cableSpy, never()).receivePackage(any(), any());
	}

	@Test
	public void doNotBufferPackagesWhenCableIdle() {
		Port port = new Port(mock(Device.class));
		Cable cableSpy = spy(new Cable());
		cableSpy.insertInto(new Port(mock(Device.class)));
		port.conntectCalble(cableSpy);
		// when
		cableSpy.setState(CableState.IDLE);
		port.sendPackage(new Package());
		Clock.getInstance().//
				withFinishCondition(new EmptyListFinishCondition()).//
				run();
		// then
		verify(cableSpy).receivePackage(any(), any());
	}

	@Test
	public void doNotBufferPackagesWhenBufferingIsDisabled() {
		Port port = new Port(mock(Device.class));
		port.enableCSMACD(false);
		Cable cableSpy = spy(new Cable());
		cableSpy.insertInto(new Port(mock(Device.class)));
		port.conntectCalble(cableSpy);
		// when
		cableSpy.setState(CableState.BUSY);
		port.sendPackage(new Package());
		Clock.getInstance().//
				withFinishCondition(new EmptyListFinishCondition()).//
				run();
		// then
		verify(cableSpy).receivePackage(any(), any());
	}

	@Test
	public void canSendBufferedPackagesAfterCableStateChange() {
		Port port = new Port(mock(Device.class));
		Cable cableSpy = spy(new Cable());
		cableSpy.insertInto(new Port(mock(Device.class)));
		port.conntectCalble(cableSpy);
		// when
		cableSpy.setState(CableState.BUSY);
		port.sendPackage(new Package());
		Clock clock = Clock.getInstance()//
				.withFinishCondition(new EmptyListFinishCondition());
		for (int i = 0; i < 10; i++) {
			clock.tick();
		}
		verify(cableSpy, never()).receivePackage(any(), any());
		cableSpy.setState(CableState.IDLE);
		clock.run();
		// then
		verify(cableSpy).receivePackage(any(), any());
	}
}
