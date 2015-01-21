package pl.edu.pk.iti.copperAnt.gui;

import pl.edu.pk.iti.copperAnt.network.Port;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PortControl extends DeviceControl {
	private static final int defaultIconHeight = 14;
	private static final int defaultIconWidth = 14;

	private DiodeControl redDiode;
	private DiodeControl greenDiode;
	private boolean isOn;
	private Port port;

	public PortControl(Port port) {
		this(defaultIconWidth, defaultIconHeight, port);
	}

	public PortControl(int width, int height, Port port) {
		this.port = port;
		Image image = new Image(PortControl.class.getResource(
				"/images/portMini.png").toExternalForm(), width, height, false,
				false);

		ImageView imageView = new ImageView();
		imageView.setImage(image);
		getChildren().add(imageView);
		setWidth(width);
		setHeight(height);

		prepareDiods();
		turnOff();
		prepareContextMenu();
	}

	public void turnOn() {
		redDiode.turnOn();
		this.isOn = true;
	}

	public void turnOff() {
		redDiode.turnOff();
		greenDiode.turnOff();
		this.isOn = false;
	}

	private void prepareDiods() {
		redDiode = new DiodeControl((int) getWidth() / 2,
				(int) getHeight() / 4, Color.RED);
		redDiode.setLayoutX((int) getWidth() / 2);
		redDiode.turnOn();

		greenDiode = new DiodeControl((int) getWidth() / 2,
				(int) getHeight() / 4, Color.GREENYELLOW);

		getChildren().add(redDiode);
		getChildren().add(greenDiode);
	}

	public void acceptPackage() {
		if (isOn) {
			greenDiode.blink();
		}
	}

	public boolean isOn() {
		return this.isOn;
	}

	@Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Podłącz kabel");
		addComputerItem.setOnAction(e -> connectCable());
		contextMenu.getItems().add(addComputerItem);

		setContextMenu(contextMenu);
	}

	private void connectCable() {
		Control cableControl = CableConnector.getInstance().connectPort(
				this.port);
		turnOn();
		if (cableControl != null) {
			SimulationCanvas.getInstance().addControl(cableControl);
		}
	}

	public Port getPort() {
		return port;
	}

	public void setPort(Port port) {
		this.port = port;
	}
}
