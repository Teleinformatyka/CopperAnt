package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class SimulationCanvasSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		root.getChildren().add(new SimulationCanvas(new ScrollPane()));

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
