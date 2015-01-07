package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PortControl extends DeviceControl {
	private DiodeControl redDiode;
	private DiodeControl greenDiode;
	private boolean isOn;

	public PortControl() {
		super();
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
		redDiode = new DiodeControl(width / 2, height / 10, Color.RED);
		redDiode.setLayoutX(width / 2);
		redDiode.turnOn();

		greenDiode = new DiodeControl(width / 2, height / 10, Color.GREENYELLOW);
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
	
	private void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Akcja 1");
		addComputerItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja 2");
		addRouterItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addRouterItem);


		// TODO
		// MenuItem addHubItem = new MenuItem("add hub");
		// addHubItem.setOnAction(e -> add(new ComputerControl()));
		// contextMenu.getItems().add(addHubItem);

		setContextMenu(contextMenu);
	}
	private void sampleAction(){}


}
