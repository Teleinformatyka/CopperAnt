package pl.edu.pk.iti.copperAnt.logging;

import javafx.scene.control.TextArea;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaLogAppender extends AppenderSkeleton {
	private TextArea deviceTextAreaRef;
	
	public TextAreaLogAppender(TextArea deviceTextArea, Layout layout) {
	    deviceTextAreaRef = deviceTextArea;
	    this.setLayout(layout);
	}
	
	@Override
	protected void append(LoggingEvent le) {
	    String message = this.getLayout().format(le);
	    deviceTextAreaRef.appendText(message);
	}
	
	@Override
	public void close() {
	    deviceTextAreaRef = null;
	}
	
	@Override
	public boolean requiresLayout() {
	    return true;
	}
}