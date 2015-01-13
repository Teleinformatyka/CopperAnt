package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.layout.Pane;
import pl.edu.pk.iti.copperAnt.network.Switch;

public class SwitchControlSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		for (int j = 1; j < 5; j++) {
			SwitchControl switchControl = new Switch(4).getControl();
			switchControl.setLayoutX(0);
			switchControl.setLayoutY(j * 200);
			root.getChildren().add(switchControl);
		}

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
