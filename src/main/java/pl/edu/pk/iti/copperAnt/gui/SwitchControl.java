package pl.edu.pk.iti.copperAnt.gui;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.network.Router;
import pl.edu.pk.iti.copperAnt.network.Switch;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;

public class SwitchControl extends MultiportDeviceControl {

	private Switch switch_;

	public SwitchControl(Switch switch_) {
		super(extractPortControlList(switch_));
		this.switch_ = switch_;
		prepareContextMenu();
		DeviceLoggingModuleFacade.getInstance().assignLoggingTab(this);
		
	}

	@Override
	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/switch.png")
				.toExternalForm(), size, size, true, false);
	}

	@Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Akcja switcha");
		addComputerItem.setOnAction(e -> {
			this.switch_.testMethod();
			sampleAction();
		});
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Akcja switcha 2");
		addRouterItem.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(addRouterItem);

		// TODO
		// MenuItem addHubItem = new MenuItem("add hub");
		// addHubItem.setOnAction(e -> add(new ComputerControl()));
		// contextMenu.getItems().add(addHubItem);

		setContextMenu(contextMenu);
	}

	private void sampleAction() {
	}

	public Switch getSwitch_() {
		return switch_;
	}

	public void setSwitch_(Switch switch_) {
		this.switch_ = switch_;
	}

	private static List<PortControl> extractPortControlList(Switch input) {
		List<Port> portList = input.getPortList();
		List<PortControl> result = new LinkedList<PortControl>();
		for (Port port : portList) {
			result.add(port.getControl());
		}
		return result;
	}

}
