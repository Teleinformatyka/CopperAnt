package pl.edu.pk.iti.copperAnt.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jfxtras.scene.control.window.Window;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.network.Router;

public class RouterControl extends MultiportDeviceControl {

	private Router router;

	public RouterControl(Router router) {
		super(extractPortControlList(router));
		this.router = router;
		prepareContextMenu();
	}

	@Override
	protected Image getIconImage(double size) {
		return new Image(PortControl.class.getResource("/images/router.png")
				.toExternalForm(), size, size, true, false);
	}

	@Override
	protected void prepareContextMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem addComputerItem = new MenuItem("Zmień ip");
		addComputerItem.setOnAction(e -> changeIpPopup());
		contextMenu.getItems().add(addComputerItem);

		MenuItem addRouting = new MenuItem("Dodaj routing");
		addRouting.setOnAction(e -> addRoutingPopup());
		contextMenu.getItems().add(addRouting);

		MenuItem showRouterState = new MenuItem("Pokaż stan routera");
		showRouterState.setOnAction(e -> showRouterStatePopup());
		contextMenu.getItems().add(showRouterState);

		setContextMenu(contextMenu);
	}

	private void addRoutingPopup() {
		Window window = createDefaultWindow("Router - dodaj routing",
				placeForIconHeight);

		Font font = new Font(10);
		VBox windowContent = new VBox();
		Label networkAddress = new Label("adres sieci:");
		networkAddress.setFont(font);
		windowContent.getChildren().add(networkAddress);
		TextField netowrkTextField = new TextField();
		netowrkTextField.setFont(font);
		windowContent.getChildren().add(netowrkTextField);
		Label portNumberLabel = new Label("number portu:");
		portNumberLabel.setFont(font);
		windowContent.getChildren().add(portNumberLabel);
		TextField portNumberTextField = new TextField();
		portNumberTextField.setFont(font);
		windowContent.getChildren().add(portNumberTextField);
		Button changeIpButton = new Button("dodaj");
		changeIpButton.setFont(font);
		changeIpButton.setOnMouseClicked(e -> {
			router.addRouting(netowrkTextField.getText(), router
					.getPort(Integer.parseInt(portNumberTextField.getText())));
			window.close();
		});
		windowContent.getChildren().add(changeIpButton);
		window.getContentPane().getChildren().add(windowContent);
	}

	private void showRouterStatePopup() {
		Window window = createDefaultWindow("Router", getWidth());
		VBox windowContent = new VBox();
		List<Port> portList = router.getPortList();
		for (int i = 0; i < portList.size(); i++) {
			Port port = portList.get(i);
			Label label = new Label("Port " + i + ": ");
			label.setFont(new Font(10));
			label.setText("Port "
					+ i
					+ ": "//
					+ "\n\tip: "
					+ router.getIP(i)//
					+ "\n\tmac: "
					+ port.getMAC()//
					+ "\n\tWykrywanie kolizji: "
					+ (port.isColisionDetection() ? "tak" : "nie")
					+ "\n\tPodpięty kabel: "
					+ (port.getCable() == null ? "nie" : "tak"));
			windowContent.getChildren().add(label);

		}
		String routingTableString = "";
		HashMap<String, Port> routingTable = router.getRoutingTable();
		for (String ip : routingTable.keySet()) {
			routingTableString += ip + " --> port "
					+ router.getPortNumber(routingTable.get(ip)) + "\n";
		}
		Label routingLabel = new Label(routingTableString);
		routingLabel.setFont(new Font(10));
		windowContent.getChildren().add(routingLabel);

		window.getContentPane().getChildren().add(windowContent);
		window.setMinHeight(200);
		window.setMinWidth(200);
	}

	private void changeIpPopup() {
		Window window = createDefaultWindow("Router - zmień IP",
				placeForIconHeight);

		Font font = new Font(10);
		VBox windowContent = new VBox();
		Label portLabel = new Label("port:");
		portLabel.setFont(font);
		windowContent.getChildren().add(portLabel);
		TextField portNumber = new TextField();
		portNumber.setFont(font);
		windowContent.getChildren().add(portNumber);
		Label ipLabel = new Label("ip:");
		ipLabel.setFont(font);
		windowContent.getChildren().add(ipLabel);
		TextField newIp = new TextField();
		newIp.setFont(font);
		windowContent.getChildren().add(newIp);
		Button changeIpButton = new Button("zmień");
		changeIpButton.setFont(font);
		changeIpButton.setOnMouseClicked(e -> {
			router.setIpForPort(Integer.parseInt(portNumber.getText()),
					new IPAddress(newIp.getText()));
			window.close();
		});
		windowContent.getChildren().add(changeIpButton);
		window.getContentPane().getChildren().add(windowContent);
	}

	private static List<PortControl> extractPortControlList(Router input) {
		List<Port> portList = input.getPortList();
		List<PortControl> result = new LinkedList<PortControl>();
		for (Port port : portList) {
			result.add(port.getControl());
		}
		return result;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

}