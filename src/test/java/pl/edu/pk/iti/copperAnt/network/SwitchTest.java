package pl.edu.pk.iti.copperAnt.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class SwitchTest {

	@Before
	public void setUp() throws Exception {
		Clock.resetInstance();
	}

	@Test
	public void acceptPackageWithEmptyMacTableTest() throws Exception {
		// given
		Switch switch_ = new Switch(4);
		Cable cable0 = new Cable();
		Cable cable1 = new Cable();
		Cable cable2 = new Cable();
		Cable cable3 = new Cable();

		switch_.getPort(0).conntectCalble(cable0);
		switch_.getPort(1).conntectCalble(cable1);
		switch_.getPort(2).conntectCalble(cable2);
		switch_.getPort(3).conntectCalble(cable3);

		Device mockDevice = mock(Device.class);
		Port mockPort0 = spy(new Port(mockDevice));
		Port mockPort1 = spy(new Port(mockDevice));
		Port mockPort2 = spy(new Port(mockDevice));
		Port mockPort3 = spy(new Port(mockDevice));

		cable0.insertInto(mockPort0);
		cable1.insertInto(mockPort1);
		cable2.insertInto(mockPort2);
		cable3.insertInto(mockPort3);

		// when
		Package pack = new Package();
		pack.setDestinationMAC("dest");
		mockPort0.sendPackage(pack);
		Clock.getInstance().run();

		// then
		verify(mockPort0, never()).receivePackage(any(Package.class));
		verify(mockPort1).receivePackage(any(Package.class));
		verify(mockPort2).receivePackage(any(Package.class));
		verify(mockPort3).receivePackage(any(Package.class));
	}

	@Test
	public void acceptPackageWithNotEmptyMacTableTest() throws Exception {
		// given
		Switch switch_ = new Switch(4);

		Cable cable0 = new Cable();
		Cable cable1 = new Cable();
		Cable cable2 = new Cable();
		Cable cable3 = new Cable();

		switch_.getPort(0).conntectCalble(cable0);
		switch_.getPort(1).conntectCalble(cable1);
		switch_.getPort(2).conntectCalble(cable2);
		switch_.getPort(3).conntectCalble(cable3);

		Device mockDevice = mock(Device.class);
		Port mockPort0 = spy(new Port(mockDevice));
		Port mockPort1 = spy(new Port(mockDevice));
		Port mockPort2 = spy(new Port(mockDevice));
		Port mockPort3 = spy(new Port(mockDevice));

		switch_.getMacTable().put(mockPort0.getMAC(), switch_.getPort(0));

		cable0.insertInto(mockPort0);
		cable1.insertInto(mockPort1);
		cable2.insertInto(mockPort2);
		cable3.insertInto(mockPort3);

		// when
		Package pack = new Package();
		pack.setDestinationMAC(mockPort0.getMAC());
		pack.setSourceMAC(mockPort1.getMAC());
		switch_.getPort(1).receivePackage(pack);
		Clock.getInstance().run();
		// then
		verify(mockPort0).receivePackage(any(Package.class));
		verify(mockPort1, never()).receivePackage(any(Package.class));
		verify(mockPort2, never()).receivePackage(any(Package.class));
		verify(mockPort3, never()).receivePackage(any(Package.class));
	}

	@Test
	public void switchLearnsFromTraffic() throws Exception {
		// given
		Switch switch_ = new Switch(4);

		Cable cable0 = new Cable();
		Cable cable1 = new Cable();

		switch_.getPort(0).conntectCalble(cable0);
		switch_.getPort(1).conntectCalble(cable1);

		Device mockDevice = mock(Device.class);
		Port mockPort0 = spy(new Port(mockDevice));
		Port mockPort1 = spy(new Port(mockDevice));

		cable0.insertInto(mockPort0);
		cable1.insertInto(mockPort1);

		// when
		Package pack = new Package();
		pack.setDestinationMAC(mockPort0.getMAC());
		pack.setSourceMAC(mockPort1.getMAC());
		switch_.getPort(1).receivePackage(pack);
		// then
		assertTrue(switch_.getMacTable().containsKey(mockPort1.getMAC()));
		assertEquals(switch_.getPort(1),
				switch_.getMacTable().get(mockPort1.getMAC()));
		assertFalse(switch_.getMacTable().containsKey(mockPort0.getMAC()));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void switchConstructorTest() {
		// when
		Switch switch_ = new Switch(4);
		// then
		assertNotNull(switch_.getPort(0));
		assertNotNull(switch_.getPort(1));
		assertNotNull(switch_.getPort(2));
		assertNotNull(switch_.getPort(3));
		// generate exception
		switch_.getPort(4);
	}

	@Test
	public void conructorCreatesManyPortObjects() {
		// when
		Switch switch_ = new Switch(2);
		// then
		assertNotSame(switch_.getPort(0), switch_.getPort(1));
	}

	@Test
	public void sendPackageIsNotTheSameAsReceived() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Switch switch_ = new Switch(4);
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		switch_.acceptPackage(pack, switch_.getPort(0));
		List<PortSendsEvent> capturedEvents = eventCaptor.getAllValues();
		for (PortSendsEvent event : capturedEvents) {
			assertNotSame(pack, event.getPackage());
		}

	}

	@Test
	public void switchDoesNotChangePackages() {
		Clock clock = mock(Clock.class);
		Clock.setInstance(clock);
		ArgumentCaptor<PortSendsEvent> eventCaptor = ArgumentCaptor
				.forClass(PortSendsEvent.class);
		doNothing().when(clock).addEvent(eventCaptor.capture());
		when(clock.getCurrentTime()).thenReturn(11L);
		Switch switch_ = new Switch(4);
		Package pack = new Package();
		pack.setDestinationIP("192.158.2.55");
		pack.setSourceMAC("96:66:d5:8d:3b:cb");
		switch_.acceptPackage(pack, switch_.getPort(0));
		List<PortSendsEvent> capturedEvents = eventCaptor.getAllValues();
		for (PortSendsEvent event : capturedEvents) {
			assertEquals(pack, event.getPackage());
		}

	}

}
