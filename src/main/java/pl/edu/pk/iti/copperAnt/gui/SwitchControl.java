package pl.edu.pk.iti.copperAnt.gui;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jfxtras.scene.control.window.Window;
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

		MenuItem switchState = new MenuItem("Pokaż stan switcha");
		switchState.setOnAction(e -> showSwtichState());
		contextMenu.getItems().add(switchState);

		// TODO
		// MenuItem addHubItem = new MenuItem("add hub");
		// addHubItem.setOnAction(e -> add(new ComputerControl()));
		// contextMenu.getItems().add(addHubItem);

		setContextMenu(contextMenu);
	}

	private void showSwtichState() {
		Window window = createDefaultWindow("Switch", getWidth());
		VBox windowContent = new VBox();
		List<Port> portList = switch_.getPortList();
		for (int i = 0; i < portList.size(); i++) {
			Port port = portList.get(i);
			Label label = new Label("Port " + i + ": ");
			label.setFont(new Font(10));
			label.setText("Port "
					+ i
					+ ": "//
					+ "\n\tmac: "
					+ port.getMAC()//
					+ "\n\tWykrywanie kolizji: "
					+ (port.isColisionDetection() ? "tak" : "nie")
					+ "\n\tPodpięty kabel: "
					+ (port.getCable() == null ? "nie" : "tak"));
			windowContent.getChildren().add(label);

		}
		window.getContentPane().getChildren().add(windowContent);
		window.setPrefHeight(200);
		window.setPrefWidth(200);

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
