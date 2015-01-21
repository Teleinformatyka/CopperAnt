package pl.edu.pk.iti.copperAnt.gui;

import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Hub;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.MaxTimeFinishCondition;

public class HubSimulationWithGuiSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		SimulationCanvas simulationCanvas = new SimulationCanvas(
				new ScrollPane(), null);
		root.getChildren().add(simulationCanvas);

		Clock.getInstance().setFinishCondition(new MaxTimeFinishCondition(100));
		Clock.getInstance().setRealTime(true);
		Clock.getInstance().setTimeScale(100);
		Computer computer1 = new Computer(new IPAddress("192.168.1.1"), true);
		Computer computer2 = new Computer(new IPAddress("192.168.1.2"), true);
		Computer computer3 = new Computer(new IPAddress("192.168.1.3"), true);
		Hub hub = new Hub(3, true);
		Cable cable = new Cable(true);
		cable.insertInto(computer1.getPort());
		cable.insertInto(hub.getPort(0));
		Cable cable2 = new Cable(true);
		cable2.insertInto(computer2.getPort());
		cable2.insertInto(hub.getPort(1));
		Cable cable3 = new Cable(true);
		cable3.insertInto(computer3.getPort());
		cable3.insertInto(hub.getPort(2));
		simulationCanvas.addControlOf(cable, 0, 0);
		simulationCanvas.addControlOf(cable2, 0, 0);
		simulationCanvas.addControlOf(cable3, 0, 0);
		simulationCanvas.addControlOf(hub, 100, 0);
		simulationCanvas.addControlOf(computer1, 0, 200);
		simulationCanvas.addControlOf(computer2, 100, 200);
		simulationCanvas.addControlOf(computer3, 200, 200);

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
