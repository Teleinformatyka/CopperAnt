package pl.edu.pk.iti.copperAnt.simulation;

import pl.edu.pk.iti.copperAnt.network.Device;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;

public class MockDevice  extends Device {

	@Override
	public void acceptPackage(Package pack, Port inPort) {

	}

	@Override
	public int getDelay() {
		return 0;
	}

	public String getIP() {
		return null;
	}
}
