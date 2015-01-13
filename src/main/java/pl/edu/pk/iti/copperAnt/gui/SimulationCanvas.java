package pl.edu.pk.iti.copperAnt.gui;

import pl.edu.pk.iti.copperAnt.network.Switch;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SimulationCanvas extends Region {
	private double nextDeviceX;
	private double nextDeviceY;
	private ContextMenu contextMenu;

	private Rectangle rectangle;

	public SimulationCanvas(ScrollPane sp) {
		setWidth(1900);
		setHeight(1000);

		// TODO ten prostokat to brzydki hack którego trzeba sie pozbyc
		rectangle = new Rectangle(getWidth(), getHeight());
		rectangle.setFill(Color.WHITE);
		rectangle.setStroke(Color.BLACK);
		getChildren().add(rectangle);

		prepareContextMenu();

		setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				nextDeviceX = event.getSceneX()
						+ sp.hvalueProperty().getValue()
						* sp.getContent().getBoundsInLocal().getWidth()
						- sp.hvalueProperty().getValue()
						* (-1 * (int) sp.getViewportBounds().getMinX() + (int) sp
								.getViewportBounds().getMaxX());
				nextDeviceY = event.getSceneY()
						+ sp.vvalueProperty().getValue()
						* sp.getContent().getBoundsInLocal().getHeight()
						- sp.vvalueProperty().getValue()
						* (-1 * (int) sp.getViewportBounds().getMinY() + (int) sp
								.getViewportBounds().getMaxY());
				contextMenu.show(rectangle, Side.TOP, nextDeviceX,
						nextDeviceY + 70);
			}
		});

		rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.PRIMARY)
					contextMenu.hide();
			}
		});
	}

	private void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Dodaj komputer");
		addComputerItem.setOnAction(e -> addControl(//
				new ComputerControl(new PortControl())));
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouterItem = new MenuItem("Dodaj router");
		addRouterItem.setOnAction(e -> addControl(RouterControl
				.prepareRouterWithPorts(4)));
		contextMenu.getItems().add(addRouterItem);

		MenuItem addSwitchItem = new MenuItem("Dodaj switch");
		addSwitchItem.setOnAction(e -> {
			Switch switch_ = new Switch(4, true);
			addControl(switch_.getControl());
		});
		contextMenu.getItems().add(addSwitchItem);

		this.contextMenu = contextMenu;
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

	public void clearScreen() {
		this.getControls().clear();
		this.getChildren().add(rectangle);
	}
}
