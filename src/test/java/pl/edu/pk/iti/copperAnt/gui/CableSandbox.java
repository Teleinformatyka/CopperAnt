package pl.edu.pk.iti.copperAnt.gui;

import pl.edu.pk.iti.copperAnt.network.Computer;
import javafx.scene.layout.Pane;

public class CableSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		CableControl cableControl = new CableControl();
		root.getChildren().add(cableControl);
		ComputerControl computer = new Computer(true).getControl();
		ComputerControl computer2 = new Computer(true).getControl();
		cableControl.bindWithPort(
				computer.getComputer().getPort().getControl(),
				CableControl.Side.START);
		cableControl.bindWithPort(computer2.getComputer().getPort()
				.getControl(), CableControl.Side.END);
		root.getChildren().add(computer);
		root.getChildren().add(computer2);

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
