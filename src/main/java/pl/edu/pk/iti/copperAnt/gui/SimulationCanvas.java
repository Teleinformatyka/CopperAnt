package pl.edu.pk.iti.copperAnt.gui;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SimulationCanvas extends Control {
	private double nextDeviceX;
	private double nextDeviceY;

	public SimulationCanvas() {
		setWidth(1900);
		setHeight(1000);

		// TODO ten prostokat to brzydki hack którego trzeba sie pozbyc
		Rectangle rectangle = new Rectangle(getWidth(), getHeight());
		rectangle.setFill(Color.WHITE);
		rectangle.setStroke(Color.BLACK);
		getChildren().add(rectangle);

		prepareContextMenu();

		setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {
				nextDeviceX = event.getSceneX();
				nextDeviceY = event.getSceneY();
			}
		});
	}

	private void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("add computer");
		addComputerItem.setOnAction(e -> addControl(//
				new ComputerControl(new PortControl())));
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("add router");
		addRouterItem.setOnAction(e -> addControl(RouterControl
				.prepareRouterWithPorts(4)));
		contextMenu.getItems().add(addRouterItem);

		MenuItem addSwitchItem = new MenuItem("add switch");
		addSwitchItem.setOnAction(e -> addControl(SwitchControl
				.prepareSwithcWithPorts(4)));
		contextMenu.getItems().add(addSwitchItem);

		// TODO
		// MenuItem addHubItem = new MenuItem("add hub");
		// addHubItem.setOnAction(e -> add(new ComputerControl()));
		// contextMenu.getItems().add(addHubItem);

		setContextMenu(contextMenu);
	}

	private void addControl(Control control) {
		addControl(control, nextDeviceX, nextDeviceY);

	}

	public void addControl(Control control, double x, double y) {
		getChildren().add(control);
		control.setLayoutX(x);
		control.setLayoutY(y);
	}

	public void addControlOf(WithControl withControl, double x, double y) {
		Control control = withControl.getControl();
		addControl(control, x, y);
	}

	public ObservableList<Node> getControls() {
		return getChildren();
	}
}
