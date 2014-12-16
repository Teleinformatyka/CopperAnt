package pl.edu.pk.iti.copperAnt.network;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.PortControl;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.events.PortSendsEvent;

public class Port {
	private static final Logger log = LoggerFactory.getLogger("computer_logs");
	Cable cable;
	final Device device;
	final String MAC;
	PortControl portControl;
	private boolean controlDestinationMacOfPackages = false;

	public Device getDevice() {
		return device;
	}

	public Port(Device device) {
		this(device, false);
	}

	public Port(Device device, boolean withGui) {
		super();
		this.device = device;
		this.MAC = setMAC();
		if (withGui) {
			this.portControl = new PortControl();
		}
	}

	public Cable getCable() {
		return cable;
	}

	public String getMAC() {
		return MAC;
	}

	private String setMAC() {
		Random rand = new Random();
		byte[] macAddr = new byte[6];
		rand.nextBytes(macAddr);

		// zero last 2 bytes to make it unicast and locally adminstrated
		macAddr[0] = (byte) (macAddr[0] & (byte) 254);

		StringBuilder sb = new StringBuilder(18);
		for (byte b : macAddr) {

			if (sb.length() > 0) {
				sb.append(":");
			}

			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	public void conntectCalble(Cable cable) {
		if (this.cable == null) {
			this.cable = cable;
			cable.insertInto(this);

		}

	}

	public void disconnectCable() {
		if (this.cable != null) {
			Cable cableToDisconnect = this.cable;
			this.cable = null;
			cableToDisconnect.ejectFromPort(this);
		}
	}

	public PortControl getControl() {
		return portControl;
	}

	@Override
	public String toString() {
		return "[" + Integer.toHexString(hashCode()) + "]";
	}

	public void sendPackage(Package pack) {
		Clock clock = Clock.getInstance();

		clock.addEvent(new PortSendsEvent(clock.getCurrentTime()
				+ device.getDelay(), this, pack));
	}

	public void receivePackage(Package pack) {
		if (controlDestinationMacOfPackages) {
			if (!pack.getDestinationMAC().equals(this.MAC)) {
				log.info("Dropping package! Wrong MAC! " + pack + " my MAC "
						+ this.MAC);
				return;
			}
		}
		device.acceptPackage(pack, this);
	}

	public boolean isControlDestinationMacOfPackages() {
		return controlDestinationMacOfPackages;
	}

	public void setControlDestinationMacOfPackages(
			boolean controlDestinationMacOfPackages) {
		this.controlDestinationMacOfPackages = controlDestinationMacOfPackages;
	}

}
