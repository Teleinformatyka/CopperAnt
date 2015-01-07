package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.Control;

public class DeviceControl extends Control{

	private static final int defaultIconHeight = 14;
	private static final int defaultIconWidth = 14;
	protected int width;
	protected int height;
	
	public DeviceControl(){
		this(defaultIconWidth, defaultIconHeight);
	}
	public DeviceControl(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
