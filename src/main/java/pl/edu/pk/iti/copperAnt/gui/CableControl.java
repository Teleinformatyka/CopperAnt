package pl.edu.pk.iti.copperAnt.gui;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import pl.edu.pk.iti.copperAnt.network.CableState;

public class CableControl extends Control {

	public Line line;

	public CableControl() {
		line = new Line();
	}

	public void bindWithPort(PortControl portControl, Side side) {
		DoubleProperty xPropertyToBind;
		DoubleProperty yPropertyToBind;
		if (side == Side.START) {
			xPropertyToBind = line.startXProperty();
			yPropertyToBind = line.startYProperty();
		} else {
			xPropertyToBind = line.endXProperty();
			yPropertyToBind = line.endYProperty();
			SimulationCanvas.getInstance().addControl(line, 0, 0);

		}

		xPropertyToBind.bind(new DoubleBinding() {
			{
				super.bind(portControl.getParent().layoutXProperty());
			}

			@Override
			protected double computeValue() {
				return portControl.getParent().getLayoutX()
						+ portControl.getLayoutX() + portControl.getWidth() / 2;

			}
		});

		yPropertyToBind.bind(new DoubleBinding() {
			{
				super.bind(portControl.getParent().layoutYProperty());
			}

			@Override
			protected double computeValue() {
				return portControl.getParent().getLayoutY()
						+ portControl.getLayoutY() + portControl.getHeight()
						/ 2;
			}
		});
	}

	public enum Side {
		START, END;
	}

	public void setState(CableState state) {
		switch (state) {
		case IDLE:
			changeColor(Color.BLACK);
			break;
		case BUSY:
			changeColor(Color.YELLOW);
			break;
		case COLISION:
			changeColor(Color.RED);
			break;

		default:
			changeColor(Color.BLACK);
			break;
		}

	}

	private void changeColor(Paint color) {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				line.setStroke(color);
				return null;
			}
		};
		Platform.runLater(task);
	}

}
