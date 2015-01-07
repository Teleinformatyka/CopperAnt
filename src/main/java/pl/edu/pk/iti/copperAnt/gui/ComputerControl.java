package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.labs.util.event.MouseControlUtil;

public class ComputerControl extends Control {
	private final static int defaultSize = 100;
	private final PortControl portControl;

	public ComputerControl(PortControl portControl) {
		this.portControl = portControl;
		MouseControlUtil.makeDraggable(this);
		setWidth(defaultSize);
		setHeight(defaultSize);
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
	private void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Zmień IP");
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
