package pl.edu.pk.iti.copperAnt.gui;

import javafx.scene.layout.Pane;
import pl.edu.pk.iti.copperAnt.network.Router;

public class RouterControlSandbox extends AbstractControlSandbox {

	@Override
	protected void addElements(Pane root) {
		for (int j = 1; j < 5; j++) {
			RouterControl router = new Router(8, true).getControl();
			router.setLayoutX(100);
			router.setLayoutY(j * 200);
			root.getChildren().add(router);
		}

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
