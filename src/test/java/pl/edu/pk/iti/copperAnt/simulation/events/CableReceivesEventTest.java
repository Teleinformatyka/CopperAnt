package pl.edu.pk.iti.copperAnt.simulation.events;

import static org.junit.Assert.assertEquals;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.CableState;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.MockDevice;

@RunWith(JUnitParamsRunner.class)
public class CableReceivesEventTest {

	@Test
	@Parameters({ "IDLE, BUSY", "BUSY, COLISION", "COLISION, COLISION" })
	public void eventChangesCableStateTest(CableState expectedStateBeforeEvent,
			CableState expectedStateAfterEvent) throws Exception {
		// given
		Port port = new Port(new MockDevice());
		Cable cable = new Cable();
		port.conntectCalble(cable);
		cable.insertInto(new Port(new MockDevice()));
		cable.setA(port);
		cable.setState(expectedStateBeforeEvent);
		CableReceivesEvent event = new CableReceivesEvent(0, port,
				new Package());
		// when
		event.run();
		// then
		CableState stateAfterEvent = cable.getState();
		assertEquals(expectedStateAfterEvent, stateAfterEvent);

	}

}
