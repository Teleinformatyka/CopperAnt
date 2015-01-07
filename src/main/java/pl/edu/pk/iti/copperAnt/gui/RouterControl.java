package pl.edu.pk.iti.copperAnt.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import jfxtras.labs.util.event.MouseControlUtil;
import jfxtras.scene.control.window.Window;
import javafx.event.EventHandler;

public class RouterControl extends MultiportDeviceControl {
	
	private Window window;
	private boolean windowIsVisible;
	public RouterControl(List<PortControl> portList) {
		super(portList);
		prepareContextMenu();
		
		setListeners();
		window = new Window("Router");
		window.setMinSize(200, 200);
		windowIsVisible = false;
		window.getContentPane().getChildren().add(new HBox());
		window.setVisible(windowIsVisible);
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
	private void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Akcja 3");
		addComputerItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja 4");
		addRouterItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addRouterItem);


		// TODO
		// MenuItem addHubItem = new MenuItem("add hub");
		// addHubItem.setOnAction(e -> add(new ComputerControl()));
		// contextMenu.getItems().add(addHubItem);

		setContextMenu(contextMenu);
	}
	private void sampleAction(){}

	private void setListeners() {
		MouseControlUtil.makeDraggable(this);
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				windowIsVisible = !windowIsVisible;
				window.setVisible(true);
				System.out.println("click");
				if (event.getClickCount() > 1) {
				} else {
//					isWorkingProperty.set(!isWorkingProperty.get());
				}

			}
		});

	}
	
}
