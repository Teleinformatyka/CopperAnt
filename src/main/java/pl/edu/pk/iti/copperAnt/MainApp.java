package pl.edu.pk.iti.copperAnt;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.window.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.gui.MenuController;
import pl.edu.pk.iti.copperAnt.gui.SimulationCanvas;

public class MainApp extends Application {
	final ScrollPane sp = new ScrollPane();
    final VBox vb = new VBox();
    final SimulationCanvas simulationCanvas = new SimulationCanvas();
    
	private static final Logger dev_log = LoggerFactory.getLogger("dev_logs");

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		VBox box = new VBox();
        Scene scene = new Scene(box, 500, 500);
        stage.setScene(scene);
        stage.setTitle("CopperAnt");
		new MenuController(stage,sp,box, simulationCanvas);
        box.getChildren().add(sp);
        VBox.setVgrow(sp, Priority.ALWAYS);
        
        vb.getChildren().add(simulationCanvas);

        sp.setVmax(440);
        sp.setPrefSize(115, 150);
        sp.setContent(vb);
        
		stage.show();
		
		isWorkingProperty = new SimpleBooleanProperty(true);

		window = new Window("Router");
		window.setMinSize(200, 200);
		windowIsVisible = true;
		HBox statusHBox = new HBox();
		Label isOnLabel = new Label();
		isOnLabel.textProperty().bind(this.isWorkingProperty.asString());

		statusHBox.getChildren().add(isOnLabel);
		window.getContentPane().getChildren().add(statusHBox);
		window.setVisible(windowIsVisible);

	}
	
	private Window window;
	private boolean windowIsVisible;
	private SimpleBooleanProperty isWorkingProperty;

}
