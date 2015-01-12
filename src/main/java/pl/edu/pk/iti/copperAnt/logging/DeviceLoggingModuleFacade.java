package pl.edu.pk.iti.copperAnt.logging;

import javafx.scene.Node;
import javafx.scene.control.TextArea;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

/**
 * Facade to log-on-tabs module.
 * Implemented as singleton, provides interface to retrieve log pane as Node
 * and to manage devices to loggers assignment
 * @author toshiba
 *
 */
public class DeviceLoggingModuleFacade {
	private static DeviceLoggingModuleFacade facadeInstance;
	private InstancesTextAreaTabPane logTabPane;
	private LoggingUtils loggingUtils;
	private boolean javaFxPlatformInitialized;
	
	/**
	 * The only supported way of facade retrieval 
	 * @return initialized instance
	 */
	public static DeviceLoggingModuleFacade getInstance() {
		if (facadeInstance==null) {
			facadeInstance = new DeviceLoggingModuleFacade();
			facadeInstance.initialize();
		}
		return facadeInstance;
	}
	
	public void initializeJavaFxPlatform() {
		logTabPane = new InstancesTextAreaTabPane();
		javaFxPlatformInitialized = true;
	}
	
	private void initialize() {
		javaFxPlatformInitialized = false;
		loggingUtils = new LoggingUtils();
	}

	/**
	 * GUI representation of module
	 * @return javafx.scene.Node instance displaying logs
	 */
	public Node getLoggingGuiNode() {
		return logTabPane;
	}

	public Logger getDeviceLogger(Object deviceObj) {
		String deviceId = loggingUtils.getDeviceId(deviceObj);
		String deviceName = loggingUtils.getDeviceNameFromId(deviceId);
		return getDeviceLogger(deviceObj, deviceName);
	}
	
	public Logger getDeviceLogger(Object deviceObj, String deviceName) {
		String deviceId = loggingUtils.getDeviceId(deviceObj);
		Logger logger = Logger.getLogger(deviceId);

		if (javaFxPlatformInitialized) {
			if (!logTabPane.hasTab(deviceId)) {
				logTabPane.createTab(deviceId, deviceName);
			}
			
			TextArea logsTextArea = logTabPane.getTabTextArea(deviceId);
			Appender textAreaAppender = new TextAreaLogAppender(logsTextArea, loggingUtils.getLoggingLayout());
			logger.addAppender(textAreaAppender);
		}
		return logger;
	}
	
	public void updateDeviceName(Object deviceObj, String deviceName) {
		if (javaFxPlatformInitialized) {
			String deviceId = loggingUtils.getDeviceId(deviceObj);
			logTabPane.renameTab(deviceId, deviceName);
		}
	}
	
	/**
	 * 
	 * @param deviceObj
	 */
	public void removeDeviceReference(Object deviceObj) {
		//TODO
	}
	
	private DeviceLoggingModuleFacade() {}
}
