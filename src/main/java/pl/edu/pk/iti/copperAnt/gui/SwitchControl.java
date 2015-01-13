package pl.edu.pk.iti.copperAnt.gui;

import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import pl.edu.pk.iti.copperAnt.network.Switch;

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

}
