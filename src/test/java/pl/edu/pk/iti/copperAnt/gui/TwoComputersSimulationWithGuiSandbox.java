package pl.edu.pk.iti.copperAnt.gui;

import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.MaxTimeFinishCondition;

public class TwoComputersSimulationWithGuiSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		SimulationCanvas simulationCanvas = new SimulationCanvas();
		root.getChildren().add(simulationCanvas);

		Clock clock = new Clock()
				.withFinishCondition(new MaxTimeFinishCondition(100));
		clock.setRealTime(true);
		clock.setTimeScale(100);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"));
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"));
		Cable cable = new Cable();
		cable.insertInto(computer1.getPort());
		cable.insertInto(computer2.getPort());

		simulationCanvas.addControl(computer1.getControl(), 0, 0);
		simulationCanvas.addControl(computer2.getControl(), 20, 0);

		computer1.initTrafic(clock);
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				clock.run();
				return null;
			}
		};
		new Thread(task).start();

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}