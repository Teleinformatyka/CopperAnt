package pl.edu.pk.iti.copperAnt.gui;

import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.MaxTimeFinishCondition;

public class TwoComputersSimulationWithGuiSandbox extends
		AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		SimulationCanvas simulationCanvas = new SimulationCanvas();
		root.getChildren().add(simulationCanvas);

		Clock.getInstance().setFinishCondition(
				new MaxTimeFinishCondition(100000));
		Clock.getInstance().setRealTime(true);
		Clock.getInstance().setTimeScale(100);
		Cable cable = new Cable(true);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"), true);
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"), true);
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());

		simulationCanvas.addControlOf(cable, 0, 0);

		simulationCanvas.addControlOf(computer1, 0, 0);
		simulationCanvas.addControlOf(computer2, 100, 0);

		computer1.initTrafic(new IPAddress("192.168.1.2"));
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				Clock.getInstance().run();
				return null;
			}
		};
		new Thread(task).start();

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}