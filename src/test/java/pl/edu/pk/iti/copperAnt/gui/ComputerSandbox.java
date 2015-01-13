package pl.edu.pk.iti.copperAnt.gui;

import pl.edu.pk.iti.copperAnt.network.Computer;
import javafx.scene.layout.Pane;

public class ComputerSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		ComputerControl computer = new Computer(true).getControl();
		computer.setLayoutX(0);
		computer.setLayoutY(200);
		root.getChildren().add(computer);

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
