package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.ContextMenu;
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
	
	public PortControl() {
		this(defaultIconWidth, defaultIconHeight);
	}

	public PortControl(int width, int height) {
		Image image = new Image(PortControl.class.getResource(
				"/images/portMini.png").toExternalForm(), width, height, false,
				false);

		ImageView imageView = new ImageView();
		imageView.setImage(image);
		getChildren().add(imageView);
		setWidth(width);
		setHeight(height);

		prepareDiods();
		turnOn();
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
		redDiode = new DiodeControl((int)getWidth() / 2, (int)getHeight() / 10, Color.RED);
		redDiode.setLayoutX((int)getWidth() / 2);
		redDiode.turnOn();

		greenDiode = new DiodeControl((int)getWidth() / 2, (int)getHeight() / 10, Color.GREENYELLOW);
		greenDiode.turnOn();

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

		MenuItem addComputerItem = new MenuItem("Akcja 1");
		addComputerItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja 2");
		addRouterItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addRouterItem);

		setContextMenu(contextMenu);
	}
	
	private void sampleAction(){}
}
