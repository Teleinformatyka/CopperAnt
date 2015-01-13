package pl.edu.pk.iti.copperAnt.gui;

import java.io.File;
import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pl.edu.pk.iti.copperAnt.network.Switch;

public class MenuController {
	Stage stage;
	ScrollPane scrollPane;
	VBox vbox;
	final PopUp popUp = new PopUp();
	SimulationCanvas simulationCanvas;

	private static final Logger log = LoggerFactory
			.getLogger(MenuController.class);

	public MenuController(Stage stage, ScrollPane scrollPane, VBox vbox,
			SimulationCanvas simulationCanvas) {
		this.vbox = vbox;
		this.scrollPane = scrollPane;
		this.stage = stage;
		this.simulationCanvas = simulationCanvas;
		createMenu();
	}

	private void createMenu() {
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("Plik");
		Menu menuSimulation = new Menu("Symulacja");
		Menu menuHelp = new Menu("Pomoc");

		menuBar.getMenus().add(menuFile);

		MenuItem fileNew = new MenuItem("Nowy");
		fileNew.setOnAction(e -> defaultAction());
		menuFile.getItems().add(fileNew);

		MenuItem fileLoad = new MenuItem("Wczytaj");
		fileLoad.setOnAction(e -> fileLoad());
		menuFile.getItems().add(fileLoad);

		MenuItem fileSave = new MenuItem("Zapisz");
		fileSave.setOnAction(e -> fileSave());
		menuFile.getItems().add(fileSave);

		MenuItem fileClose = new MenuItem("Zakończ");
		fileClose.setOnAction(e -> System.exit(0));
		menuFile.getItems().add(fileClose);

		menuBar.getMenus().add(menuSimulation);
		MenuItem simulationRun = new MenuItem("Start");
		simulationRun.setOnAction(e -> startSimulation());
		menuSimulation.getItems().add(simulationRun);

		MenuItem simulationPause = new MenuItem("Pauza");
		simulationPause.setOnAction(e -> defaultAction());
		menuSimulation.getItems().add(simulationPause);

		MenuItem simulationStop = new MenuItem("Stop");
		simulationStop.setOnAction(e -> defaultAction());
		menuSimulation.getItems().add(simulationStop);

		menuBar.getMenus().add(menuHelp);
		MenuItem helpAbout = new MenuItem("O programie");
		helpAbout.setOnAction(e -> helpAbout());
		menuHelp.getItems().add(helpAbout);

		MenuItem helpAuthors = new MenuItem("O autorach");
		helpAuthors.setOnAction(e -> helpAuthors());
		menuHelp.getItems().add(helpAuthors);

		vbox.getChildren().add(menuBar);
	}

	private Object startSimulation() {

		return null;
	}

	private void fileLoad() {
		try {
			simulationCanvas.clearScreen();
			String xmlFile = "devices.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(xmlFile));
			NodeList nodeList = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				org.w3c.dom.Node node = nodeList.item(i);

				if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					Element elem = (Element) node;

					String controlName = elem.getNodeName();
					String xpos = elem.getElementsByTagName("xPos").item(0)
							.getChildNodes().item(0).getNodeValue();
					String ypos = elem.getElementsByTagName("yPos").item(0)
							.getChildNodes().item(0).getNodeValue();

					System.out.println(controlName + "  X: " + xpos + " Y: "
							+ ypos);
					switch (controlName) {
					case "ComputerControl":
						simulationCanvas.addControl(new ComputerControl(
								new PortControl()), Double.parseDouble(xpos),
								Double.parseDouble(ypos));
						break;

					case "RouterControl":
						simulationCanvas.addControl(
								RouterControl.prepareRouterWithPorts(4),
								Double.parseDouble(xpos),
								Double.parseDouble(ypos));
						break;

					case "SwitchControl":
						simulationCanvas.addControl(SwitchControl
								.prepareSwitchWithPorts(4, new Switch(4)),
								Double.parseDouble(xpos), Double
										.parseDouble(ypos));
						break;
					default:
						System.out.println("urządzenie nie rozpoznane");
						break;

					}
				}
			}

			popUp.showPopupMessage(stage, "Plik xml został wczytany");
		} catch (ParserConfigurationException ex) {
			log.info("ParserConfigurationException ex " + ex.getMessage());
		} catch (SAXException ex) {
			log.info("SAXException ex " + ex.getMessage());
		} catch (IOException ex) {
			log.info("IOException ex " + ex.getMessage());
		}
	}

	// save
	private void fileSave() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("devices");
			doc.appendChild(rootElement);

			for (Node node : simulationCanvas.getControls()) {
				String nodeName = node
						.getClass()
						.toString()
						.substring(
								node.getClass().toString().lastIndexOf('.') + 1);

				Element elNode = doc.createElement(nodeName);
				rootElement.appendChild(elNode);

				Element elXPos = doc.createElement("xPos");
				elXPos.appendChild(doc.createTextNode(node.getLayoutX() + ""));
				elNode.appendChild(elXPos);

				Element elYPos = doc.createElement("yPos");
				elYPos.appendChild(doc.createTextNode(node.getLayoutY() + ""));
				elNode.appendChild(elYPos);
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("devices.xml"));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

		} catch (ParserConfigurationException ex) {
			log.info("ParserConfigurationException ex " + ex.getMessage());
		} catch (TransformerException ex) {
			log.info("TransformerException ex" + ex.getMessage());
		}

		popUp.showPopupMessage(stage, "Plik xml został zapisany");
	}

	private void defaultAction() {
		popUp.showPopupMessage(stage, "default action");
	}

	private void helpAuthors() {
		popUp.showPopupMessage(stage,
				"Autorzy aplikacji: Teleinformatyka CopperAnt's Team");
	}

	private void helpAbout() {
		popUp.showPopupMessage(stage, "O programie CopperAnt:\nsome text");
	}
}