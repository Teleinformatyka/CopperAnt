package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jfxtras.labs.util.event.MouseControlUtil;
import jfxtras.scene.control.window.Window;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Package;

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
		Label label = new Label();
		Font font = new Font(10);
		label.setFont(font);
		label.setText(extractComputerStateToString());
		windowContent.getChildren().add(label);
		HBox ipHbox1 = new HBox(5);
		windowContent.getChildren().add(new Label("Operacje:"));

		TextField textFieldIp = new TextField();
		textFieldIp.setFont(font);
		ipHbox1.getChildren().add(textFieldIp);
		Button buttonIp = new Button("Zmień IP");
		buttonIp.setOnMouseClicked(e -> {
			computer.setIp(new IPAddress(textFieldIp.getText()));
			textFieldIp.setText("");
			label.setText(extractComputerStateToString());
		});
		buttonIp.setFont(font);
		ipHbox1.getChildren().add(buttonIp);

		HBox ipHbox2 = new HBox(5);
		TextField textFieldGatewayIp = new TextField();
		textFieldGatewayIp.setFont(font);
		ipHbox2.getChildren().add(textFieldGatewayIp);
		Button buttonGateway = new Button("Zmień brame domyślną");
		buttonGateway.setOnMouseClicked(e -> {
			computer.setDefaultGateway(new IPAddress(textFieldGatewayIp
					.getText()));
			textFieldGatewayIp.setText("");
			label.setText(extractComputerStateToString());
		});
		buttonGateway.setFont(font);
		ipHbox2.getChildren().add(buttonGateway);

		HBox ipHbox3 = new HBox(5);
		TextField textFieldPingIp = new TextField();
		textFieldPingIp.setFont(font);
		ipHbox3.getChildren().add(textFieldPingIp);
		Button buttonPing = new Button("Ping");
		buttonPing.setOnMouseClicked(e -> {
			Package pack = new Package();
			pack.setDestinationIP(textFieldPingIp.getText());
			System.out.println("Ping: " + textFieldPingIp.getText());
			computer.sendPackage(pack);
		});
		buttonPing.setFont(font);
		ipHbox3.getChildren().add(buttonPing);

		windowContent.getChildren().add(ipHbox1);
		windowContent.getChildren().add(ipHbox2);
		windowContent.getChildren().add(ipHbox3);
		window.getContentPane().getChildren().add(windowContent);
	}

	private String extractComputerStateToString() {
		return "Port "
				+ 0
				+ ": "//
				+ "\n\tip: "
				+ computer.getIP()//
				+ "\n\tbrama domyślna: "
				+ computer.getDefaultGateway()//
				+ "\n\tmac: "
				+ computer.getPort().getMAC()//
				+ "\n\tWykrywanie kolizji: "
				+ (computer.getPort().isColisionDetection() ? "tak" : "nie")
				+ "\n\tPodpięty kabel: "
				+ (computer.getPort().getCable() == null ? "nie" : "tak");
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
