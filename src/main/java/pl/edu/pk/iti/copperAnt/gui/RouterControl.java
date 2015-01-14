package pl.edu.pk.iti.copperAnt.gui;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.window.Window;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.network.Router;

public class RouterControl extends MultiportDeviceControl {

	private Router router;

	public RouterControl(Router router) {
		super(extractPortControlList(router));
		this.router = router;
		prepareContextMenu();
		DeviceLoggingModuleFacade.getInstance().assignLoggingTab(this);
	}

	@Override
	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/router.png")
				.toExternalForm(), size, size, true, false);
	}

	@Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Akcja 3");
		addComputerItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja 4");
		addRouterItem.setOnAction(e -> router.testMethod());
		contextMenu.getItems().add(addRouterItem);

		setContextMenu(contextMenu);
	}

	private void sampleAction() {
		Window window = createDefaultWindow("Router - Akcja 3",
				placeForIconHeight);

		VBox windowContent = new VBox();
		windowContent.getChildren().add(new Button("button"));
		windowContent.getChildren().add(new Button("button"));
		windowContent.getChildren().add(new Label("label"));
		windowContent.getChildren().add(new TextField("textfield"));
		window.getContentPane().getChildren().add(windowContent);
	}

	private static List<PortControl> extractPortControlList(Router input) {
		List<Port> portList = input.getPortList();
		List<PortControl> result = new LinkedList<PortControl>();
		for (Port port : portList) {
			result.add(port.getControl());
		}
		return result;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

}