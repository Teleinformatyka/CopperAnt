package pl.edu.pk.iti.copperAnt.gui;

import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Router;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.MaxTimeFinishCondition;

public class RouterSimulationWithGuiSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {

		SimulationCanvas simulationCanvas = new SimulationCanvas(
				new ScrollPane(), null);
		root.getChildren().add(simulationCanvas);

		Clock.getInstance().setFinishCondition(
				new MaxTimeFinishCondition(10000));
		Clock.getInstance().setRealTime(true);
		Clock.getInstance().setTimeScale(50);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"), true);
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"), true);
		Router router = new Router(2, true);
		Cable cable = new Cable(true);
		cable.insertInto(computer1.getPort());
		cable.insertInto(router.getPort(0));
		Cable cable2 = new Cable(true);
		cable2.insertInto(computer2.getPort());
		cable2.insertInto(router.getPort(1));
		simulationCanvas.addControlOf(cable, 0, 0);
		simulationCanvas.addControlOf(cable2, 0, 0);
		simulationCanvas.addControlOf(computer1, 0, 200);
		simulationCanvas.addControlOf(router, 100, 0);
		simulationCanvas.addControlOf(computer2, 200, 200);

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
