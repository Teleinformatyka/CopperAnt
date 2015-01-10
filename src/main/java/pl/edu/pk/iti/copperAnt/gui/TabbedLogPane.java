package pl.edu.pk.iti.copperAnt.gui;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.Logger;

import pl.edu.pk.iti.copperAnt.logging.TextAreaLogAppender;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class TabbedLogPane extends TabPane {
	public TabbedLogPane() {
		super();
		builLogTab("All", Logger.getRootLogger());
		builLogTab("Routers", Logger.getLogger("router_logs"));
		builLogTab("Cables", Logger.getLogger("cable_logs"));
		builLogTab("Computers", Logger.getLogger("computer_logs"));
		builLogTab("Hubs", Logger.getLogger("hub_logs"));
		builLogTab("Switches", Logger.getLogger("switch_logs"));
	}

	private void builLogTab(String tabName, Logger log) {
		Tab tab = new Tab();
		tab.setClosable(false);
	    tab.setText(tabName);
	    TextArea logsArea = new TextArea();
	    logsArea.setEditable(false);
	    tab.setContent(logsArea);
	    log.addAppender(new TextAreaLogAppender(logsArea, new TTCCLayout()));
	    this.getTabs().add(tab);
	}
}
