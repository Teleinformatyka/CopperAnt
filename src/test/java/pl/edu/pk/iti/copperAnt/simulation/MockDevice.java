package pl.edu.pk.iti.copperAnt.simulation;

import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.network.Device;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.Port;

public class MockDevice extends Device {

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

	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}
}
