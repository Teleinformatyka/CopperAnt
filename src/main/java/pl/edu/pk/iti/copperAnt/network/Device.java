package pl.edu.pk.iti.copperAnt.network;

public abstract class Device extends WithDelay {

	public abstract void acceptPackage(Package pack, Port inPort);

}
