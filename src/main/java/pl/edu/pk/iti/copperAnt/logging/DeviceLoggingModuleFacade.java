package pl.edu.pk.iti.copperAnt.logging;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.gui.WithControl;

/**
 * Facade to log-on-tabs module. Implemented as singleton, provides interface to
 * retrieve log pane as Node and to manage devices to loggers assignment
 * 
 * @author toshiba
 *
 */
public class DeviceLoggingModuleFacade {
	private final Logger log = Logger
			.getLogger(DeviceLoggingModuleFacade.class);
	private static DeviceLoggingModuleFacade facadeInstance;
	private InstancesTextAreaTabPane logTabPane;
	private LoggingUtils loggingUtils;
	private boolean javaFxPlatformInitialized;

	private Map<String, String> sucessfullyAssignedDeviceToControl;

	/**
	 * The only supported way of facade retrieval
	 * 
	 * @return initialized instance
	 */
	public static DeviceLoggingModuleFacade getInstance() {
		if (facadeInstance == null) {
			facadeInstance = new DeviceLoggingModuleFacade();
			facadeInstance.initializeWithoutGui();
		}
		return facadeInstance;
	}

	public void assignLoggingTab(Object object) {
		initializeJavaFxPlatformWhenRequired();

		String instanceId = loggingUtils.getDeviceId(object);
		String tabName = loggingUtils.getTabNamePrimitiveRule(object);
		logTabPane.createTab(instanceId, tabName);
	}

	private void initializeJavaFxPlatformWhenRequired() {
		if (!javaFxPlatformInitialized) {
			logTabPane = new InstancesTextAreaTabPane();
			javaFxPlatformInitialized = true;
		}
	}

	private void initializeWithoutGui() {
		javaFxPlatformInitialized = false;
		loggingUtils = new LoggingUtils();
		sucessfullyAssignedDeviceToControl = new HashMap<String, String>();
	}

	/**
	 * GUI representation of module
	 * 
	 * @return javafx.scene.Node instance displaying logs
	 */
	public Node getLoggingGuiNode() {
		initializeJavaFxPlatformWhenRequired();
		return logTabPane;
	}

	public Logger getDeviceLogger(WithControl deviceWithControl) {
		String deviceId = loggingUtils.getDeviceId(deviceWithControl); // Computer!=ComputerControl
		Logger logger = Logger.getLogger(deviceId);

		Control deviceControl = deviceWithControl.getControl();
		if (deviceControl != null) {
			String deviceControlId = loggingUtils.getDeviceId(deviceControl);
			if (logTabPane.hasTab(deviceControlId)) {
				TextArea logsTextArea = logTabPane
						.getTabTextArea(deviceControlId);
				Appender textAreaAppender = new TextAreaLogAppender(
						logsTextArea, loggingUtils.getLoggingLayout());
				logger.addAppender(textAreaAppender);

				sucessfullyAssignedDeviceToControl.put(deviceId,
						deviceControlId); // hack
			} else {
				log.error("Tab with id: '" + deviceControlId
						+ "' hasn't been created! Unable to log from: "
						+ deviceId);
			}
		} else {
			log.error("getControl() is null, device: " + deviceId);
		}

		return logger;
	}

	public void updateDeviceLoggerWithControl(WithControl deviceWithControl) { // hack
		String deviceId = loggingUtils.getDeviceId(deviceWithControl);
		Control deviceControl = deviceWithControl.getControl();
		if (deviceControl != null) {
			String deviceControlId = loggingUtils.getDeviceId(deviceControl);

			String oldControl = sucessfullyAssignedDeviceToControl
					.get(deviceControl);
			if (!deviceControlId.equals(oldControl)) {
				TextArea logsTextArea = logTabPane
						.getTabTextArea(deviceControlId);
				Appender textAreaAppender = new TextAreaLogAppender(
						logsTextArea, loggingUtils.getLoggingLayout());

				Logger logger = Logger.getLogger(deviceId);
				logger.addAppender(textAreaAppender);

				sucessfullyAssignedDeviceToControl.put(deviceId,
						deviceControlId); // hack
			}

		}
		// todo przemyslec przypadek gdy juz byl przypisany control, jest tab, a
		// wolane update()
	}

	// public Logger getDeviceLogger(Object deviceObj) {
	// String deviceId = loggingUtils.getDeviceId(deviceObj);
	// String deviceName = loggingUtils.getDeviceNameFromId(deviceId);
	// return getDeviceLogger(deviceObj, deviceName);
	// }
	//
	// public Logger getDeviceLogger(Object deviceObj, String deviceName) {
	// String deviceId = loggingUtils.getDeviceId(deviceObj);
	// Logger logger = Logger.getLogger(deviceId);
	//
	// if (javaFxPlatformInitialized) {
	// if (!logTabPane.hasTab(deviceId)) {
	// logTabPane.createTab(deviceId, deviceName);
	// }
	//
	// TextArea logsTextArea = logTabPane.getTabTextArea(deviceId);
	// Appender textAreaAppender = new TextAreaLogAppender(logsTextArea,
	// loggingUtils.getLoggingLayout());
	// logger.addAppender(textAreaAppender);
	// }
	// return logger;
	// }

	// public void updateDeviceName(Object deviceObj, String deviceName) {
	// if (javaFxPlatformInitialized) {
	// String deviceId = loggingUtils.getDeviceId(deviceObj);
	// logTabPane.renameTab(deviceId, deviceName);
	// }
	// }

	// /**
	// *
	// * @param deviceObj
	// */
	// public void removeDeviceReference(Object deviceObj) {
	// //TODO
	// }

	private DeviceLoggingModuleFacade() {
	}
}
