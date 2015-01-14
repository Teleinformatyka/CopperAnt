package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.labs.util.event.MouseControlUtil;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;
import pl.edu.pk.iti.copperAnt.network.Computer;

public class ComputerControl extends Control {
	private final static int defaultSize = 100;
	private final PortControl portControl;
	private Computer computer;

	public ComputerControl(Computer computer) {
		this.computer = computer;
		this.portControl = computer.getPort().getControl();
		MouseControlUtil.makeDraggable(this);
		setWidth(defaultSize);
		setHeight(defaultSize);
		DeviceLoggingModuleFacade.getInstance().assignLoggingTab(this);

		drawIcon();
		drawPort();
	}

	private void drawPort() {
		// TODO change size
		portControl.setLayoutX(getWidth() - getWidth() / 3);
		portControl.setLayoutY(getHeight() - getHeight() / 3);
		getChildren().add(portControl);
		prepareContextMenu();

	}

	private void drawIcon() {
		Image image = getIconImage(getHeight());

		ImageView imageView = new ImageView();
		imageView.setImage(image);
		getChildren().add(imageView);
	}

	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/pc.png")
				.toExternalForm(), size, size, true, true);
	}

	// @Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Zmień IP");
		addComputerItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja 2");
		addRouterItem.setOnAction(e -> {
			this.computer.testMethod();
		});
		contextMenu.getItems().add(addRouterItem);

		setContextMenu(contextMenu);
	}

	private void sampleAction() {
	}

	public Computer getComputer() {
		return computer;
	}

	public void setComputer(Computer computer) {
		this.computer = computer;
	}

}
