package pl.edu.pk.iti.copperAnt.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class AbstractControlSandbox extends Application {

	protected abstract void addElements(Pane root) throws IOException;

	public void start(Stage stage) throws Exception {
		Pane root = new AnchorPane();
		Scene scene = new Scene(root, 400, 200);
		addElements(root);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();

	}

}
