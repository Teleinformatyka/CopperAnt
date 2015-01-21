package pl.edu.pk.iti.copperAnt.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import jfxtras.scene.control.window.Window;
import jfxtras.scene.control.window.WindowIcon;

public abstract class DeviceControl extends Control {
	protected abstract void prepareContextMenu();

	protected Window createDefaultWindow(String name, double deviceWidth) {
		Window window = new Window(name);
		window.setMinSize(250, 200);
		window.setVisible(true);

		SimulationCanvas simulationCanvas = (SimulationCanvas) this.getParent();
		simulationCanvas.addControl(window, getLayoutX() + deviceWidth,
				getLayoutY());

		WindowIcon closeIcon = new WindowIcon();

		closeIcon.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				simulationCanvas.getControls().remove(window);
			}
		});

		closeIcon.getStyleClass().setAll("window-close-icon");
		window.setMaxWidth(1000);
		window.getRightIcons().add(closeIcon);

		return window;
	}
}