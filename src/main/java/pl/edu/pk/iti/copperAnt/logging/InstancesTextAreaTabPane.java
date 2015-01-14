package pl.edu.pk.iti.copperAnt.logging;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class InstancesTextAreaTabPane extends TabPane {
	private static final Logger log = Logger.getLogger(InstancesTextAreaTabPane.class);
	private Map<String, Tab> instancesIdTabs;
	
	public InstancesTextAreaTabPane() {
		super();
		instancesIdTabs = new HashMap<String, Tab>();
	}
	
	public void createTab(String instanceId, String name) {
		log.debug("Creating tab '" + instanceId + "', name '" + name);
		
		Tab tab = new Tab();
		instancesIdTabs.put(instanceId, tab);
		
		TextArea contentTextArea = new TextArea();
		contentTextArea.setEditable(false);
		
		tab.setClosable(false);
		tab.setText(name);
		tab.setContent(contentTextArea);
		this.getTabs().add(tab);
	}
	
	boolean hasTab(String instanceId) {
		return instancesIdTabs.containsKey(instanceId);
	}
	
	public void renameTab(String instanceId, String name) {
		log.debug("Renaming tab '" + instanceId + "' to '" + name);
		
		Tab instanceTab = instancesIdTabs.get(instanceId);
		if (instanceTab!=null) {
			instanceTab.setText(name);
		} else {
			log.warn("Cannot rename tab '" + instanceId + "' - doesn't exist");
		}
	}
	
	public TextArea getTabTextArea(String instanceId) {
		TextArea instanceTextArea = null;
		Tab instanceTab = instancesIdTabs.get(instanceId);
		if (instanceTab!=null) {
			instanceTextArea = (TextArea)instanceTab.getContent();
		}
		return instanceTextArea;
	}
}
