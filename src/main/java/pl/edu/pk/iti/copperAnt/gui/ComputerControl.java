package pl.edu.pk.iti.copperAnt.gui;

import java.util.List;

import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Port;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jfxtras.labs.util.event.MouseControlUtil;
import jfxtras.scene.control.window.Window;

public class ComputerControl extends DeviceControl {
	private final static int defaultSize = 100;
	private final PortControl portControl;
	private Computer computer;

	public ComputerControl(Computer computer) {
		this.computer = computer;
		this.portControl = computer.getPort().getControl();
		MouseControlUtil.makeDraggable(this);
		setWidth(defaultSize);
		setHeight(defaultSize);
		drawIcon();
		drawPort();
	}

	private void drawPort() {
		// TODO change size
		portControl.setLayoutX(getWidth() - getWidth() / 3);
		portControl.setLayoutY(getHeight() - getHeight() / 3);
		getChildren().add(portControl);
		prepareContextMenu();

	}

	private void drawIcon() {
		Image image = getIconImage(getHeight());

		ImageView imageView = new ImageView();
		imageView.setImage(image);
		getChildren().add(imageView);
	}

	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/pc.png")
				.toExternalForm(), size, size, true, true);
	}

	// @Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem changeIp = new MenuItem("Zmień IP");
		changeIp.setOnAction(e -> sampleAction());
		contextMenu.getItems().add(changeIp);

		MenuItem showComputerState = new MenuItem("Pokaż stan komputera");
		showComputerState.setOnAction(e -> {
			showComputerStateWindow();
		});
		contextMenu.getItems().add(showComputerState);

		setContextMenu(contextMenu);
	}

	private void showComputerStateWindow() {
		Window window = createDefaultWindow("Komputer " + computer.getIP(),
				getWidth());
		VBox windowContent = new VBox();
		Port port = computer.getPort();
		Label label = new Label();
		label.setFont(new Font(10));
		label.setText("Port "
				+ 0
				+ ": "//
				+ "\n\tip: "
				+ computer.getIP()//
				+ "\n\tmac: "
				+ port.getMAC()//
				+ "\n\tWykrywanie kolizji: "
				+ (port.isColisionDetection() ? "tak" : "nie")
				+ "\n\tPodpięty kabel: "
				+ (port.getCable() == null ? "nie" : "tak"));
		windowContent.getChildren().add(label);
		window.getContentPane().getChildren().add(label);
	}

	private void sampleAction() {
	}

	public Computer getComputer() {
		return computer;
	}

	public void setComputer(Computer computer) {
		this.computer = computer;
	}

}
