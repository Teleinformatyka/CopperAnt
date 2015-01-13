package pl.edu.pk.iti.copperAnt.network;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.CableReceivesEvent;

public class PortSendingStrategyWithoutCSMACD implements PortSendingStrategy {
	@Override
	public void sendPackage(Package pack, Port port) {
		if (port.getCable() != null) {
			Clock.getInstance().addEvent(
					new CableReceivesEvent(
							Clock.getInstance().getCurrentTime() + 1, port,
							pack));
		} else {
			Port.log.debug("Dropping package, cable not inserted!");
		}

	}

	@Override
	public boolean isCollisionDetection() {
		return false;
	}
}
