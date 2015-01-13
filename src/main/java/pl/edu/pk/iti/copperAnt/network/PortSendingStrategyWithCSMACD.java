package pl.edu.pk.iti.copperAnt.network;

import java.util.Random;

import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.CableReceivesEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class PortSendingStrategyWithCSMACD implements PortSendingStrategy {

	@Override
	public void sendPackage(Package pack, Port port) {
		Clock clock = Clock.getInstance();
		if (port.getCable().getState() == CableState.IDLE) {
			port.removePackFromBuffor(pack);
			if (port.getCable() != null) {
				Clock.getInstance().addEvent(
						new CableReceivesEvent(clock.getCurrentTime() + 1,
								port, pack));
			} else {
				Port.log.debug("Dropping package, cable not inserted!");
			}
		} else {
			if (port.thereIsEnoughSpaceForPackageInBuffor(pack)) {
				port.addPackToBuffor(pack);
				clock.addEvent(new PortSendsEvent(clock.getCurrentTime()
						+ new Random().nextInt(100), port, pack));
			}
		}
	}

	@Override
	public boolean isCollisionDetection() {
		return true;

	}

}
