package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PortControl extends Control {
	private static final int defaultIconHeight = 14;
	private static final int defaultIconWidth = 14;
	private int width;
	private int height;
	private DiodeControl redDiode;
	private DiodeControl greenDiode;
	private boolean isOn;

	public PortControl() {
		this(defaultIconWidth, defaultIconHeight);
	}

	public PortControl(int width, int height) {
		this.width = width;
		this.height = height;
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
		//greenDiode.turnOn();

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

}
