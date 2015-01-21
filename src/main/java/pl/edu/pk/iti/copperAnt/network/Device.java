package pl.edu.pk.iti.copperAnt.network;

import org.apache.log4j.Logger;

public abstract class Device extends WithDelay {

	public abstract void acceptPackage(Package pack, Port inPort);

	public abstract Logger getLogger();

}
