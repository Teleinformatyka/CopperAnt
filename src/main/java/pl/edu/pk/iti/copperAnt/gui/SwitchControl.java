package pl.edu.pk.iti.copperAnt.gui;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pk.iti.copperAnt.network.Switch;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

public class SwitchControl extends MultiportDeviceControl {

	private Switch switch_;

	public SwitchControl(List<PortControl> portList, Switch switch_) {
		super(portList);
		this.switch_ = switch_;
		prepareContextMenu();
	}

	@Override
	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/switch.png")
				.toExternalForm(), size, size, true, false);
	}

	public static SwitchControl prepareSwitchWithPorts(int numberOfPorts,
			Switch switch_) {
		List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
		for (int i = 0; i < numberOfPorts; i++) {
			list.add(new PortControl());
		}
		return new SwitchControl(list, switch_);
	}

	@Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Akcja switcha");
		addComputerItem.setOnAction(e -> {
			this.switch_.acceptPackage(null, null);
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

}
