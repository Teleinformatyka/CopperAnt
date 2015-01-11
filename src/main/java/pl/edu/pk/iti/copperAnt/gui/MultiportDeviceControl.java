package pl.edu.pk.iti.copperAnt.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.util.event.MouseControlUtil;

public abstract class MultiportDeviceControl extends DeviceControl {
	protected static final double placeForIconHeight = 128;
	private static final double placeForPortsHeight = placeForIconHeight * 1;

	public MultiportDeviceControl(List<PortControl> portList) {
		MouseControlUtil.makeDraggable(this);
		double portWith = portList.isEmpty() ? 0 : portList.get(0).getWidth();
		double portHeight = portList.isEmpty() ? 0 : portList.get(0)
				.getHeight();
		setWidth(placeForIconHeight);
		setHeight(placeForIconHeight);
		drawIcon(placeForIconHeight);
		//drawPortsInBlock(portList, placeForPortsHeight);
		drawPortsWithLines(portList, placeForPortsHeight);

	}

	private void drawIcon(double placeForIconHeight) {
		Image image = getIconImage(placeForIconHeight - 0.1
				* placeForIconHeight);

		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setLayoutX(getWidth() / 2 - image.getWidth() / 2);
		imageView.setLayoutY(placeForIconHeight / 2 - image.getHeight() / 2);
		getChildren().add(imageView);
	}

	private void drawPortsWithLines(List<PortControl> portList,
			double placeForIconHeight) {
		for (int i = 0; i < portList.size(); i++) {
			double portX = i * portList.get(0).getWidth();
			PortControl port = portList.get(i);
			port.setLayoutX(portX);
			port.setLayoutY(placeForIconHeight);
			Line line = new Line(portX + port.getWidth() / 2,
					placeForIconHeight, getWidth() / 2, placeForIconHeight *2 / 3);
			getChildren().add(line);
			getChildren().add(port);
		}
	}

	private void drawPortsInBlock(List<PortControl> portList,
			double placeForIconHeight) {
		double shiftX = 27;
		double portX = shiftX;
		double portY = placeForIconHeight;

		double DeviceHeight = getHeight();
		double DeviceWidth = getWidth() * 0.8 + shiftX;

		for (int i = 0; i < portList.size(); i++) {
			PortControl port = portList.get(i);
			if ((i % 5) == 0) {
				if (i != 0)
					portX += port.getWidth();

				if ((port.getWidth() * 4 + portX) > DeviceWidth) {
					portY += port.getHeight() + 2;
					portX = shiftX;
				}
			}
			port.setLayoutX(portX);
			port.setLayoutY(portY);
			getChildren().add(port);

			portX += portList.get(0).getWidth();
		}
	}

	protected abstract Image getIconImage(double size);

	public static RouterControl prepareRouterWithPorts(int numberOfPorts) {
		List<PortControl> list = new ArrayList<PortControl>(numberOfPorts);
		for (int i = 0; i < numberOfPorts; i++) {
			list.add(new PortControl());
		}
		return new RouterControl(list);
	}
}
