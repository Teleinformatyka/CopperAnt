package pl.edu.pk.iti.copperAnt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
	}	
}
