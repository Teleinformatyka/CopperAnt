package pl.edu.pk.iti.copperAnt;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.MenuController;
import pl.edu.pk.iti.copperAnt.gui.SimulationCanvas;
import pl.edu.pk.iti.copperAnt.logging.DeviceLoggingModuleFacade;

public class MainApp extends Application {
	private ScrollPane sp;
	private VBox vb;

	private SimulationCanvas simulationCanvas;

	private static final Logger dev_log = LoggerFactory.getLogger("dev_logs");

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		sp = new ScrollPane();
		vb = new VBox();
		simulationCanvas = new SimulationCanvas(sp);

		VBox box = new VBox();
		Scene scene = new Scene(box, 500, 500);
		stage.setScene(scene);
		stage.setTitle("CopperAnt");

		// adding menu to box
		new MenuController(stage, sp, box, simulationCanvas);

		SplitPane centralSplitPane = new SplitPane();
		centralSplitPane.setOrientation(Orientation.VERTICAL);
		centralSplitPane.getItems().addAll(sp,
				DeviceLoggingModuleFacade.getInstance().getLoggingGuiNode());
		centralSplitPane.setDividerPositions(0.9f);

		box.getChildren().add(centralSplitPane);
		VBox.setVgrow(centralSplitPane, Priority.ALWAYS);
		box.getChildren().add(sp);
		VBox.setVgrow(sp, Priority.ALWAYS);

		vb.getChildren().add(simulationCanvas);
		sp.setContent(vb);

		stage.show();
	}
}
