package pl.edu.pk.iti.copperAnt.gui;

import java.util.List;

import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

public class HubControl extends MultiportDeviceControl {
	public HubControl(List<PortControl> portList) {
		super(portList);
		DeviceLoggingModuleFacade.getInstance().assignLoggingTab(this);
	}

	@Override
	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/hub.png")
				.toExternalForm(), size, size, true, false);
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

	private void sampleAction(){}

}
