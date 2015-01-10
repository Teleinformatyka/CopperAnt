package pl.edu.pk.iti.copperAnt.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.window.Window;

public class RouterControl extends MultiportDeviceControl {
	
	public RouterControl(List<PortControl> portList) {
		super(portList);
		prepareContextMenu();
	}

	@Override
	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/router.png")
				.toExternalForm(), size, size, true, false);
	}

	public static RouterControl prepareRouterWithPorts(int numberOfPorts) {
		List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
		for (int i = 0; i < numberOfPorts; i++) {
			list.add(new PortControl());
		}
		return new RouterControl(list);
	}
	
	@Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Akcja 3");
		addComputerItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja 4");
		addRouterItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addRouterItem);

		setContextMenu(contextMenu);
	}
	
	private void sampleAction() {
		Window window = createDefaultWindow("Router - Akcja 3", placeForIconHeight);
		
		VBox windowContent = new VBox();
		windowContent.getChildren().add(new Button("button"));
		windowContent.getChildren().add(new Button("button"));
		windowContent.getChildren().add(new Label("label"));
		windowContent.getChildren().add(new TextField("textfield"));
		window.getContentPane().getChildren().add(windowContent);
	}
	
}