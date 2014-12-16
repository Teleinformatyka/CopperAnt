package pl.edu.pk.iti.copperAnt.gui;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.Hub;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.simulation.Clock;
import pl.edu.pk.iti.copperAnt.simulation.MaxTimeFinishCondition;
import pl.edu.pk.iti.copperAnt.simulation.events.CableReceivesEvent;
import pl.edu.pk.iti.copperAnt.simulation.events.Event;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class ListViewSample extends Application {
    
    public static final ObservableList names = 
        FXCollections.observableArrayList();
    public static final ObservableList data = 
        FXCollections.observableArrayList();
    ObservableList<String> items = null;
    
    protected void addElements(Pane root) {
		SimulationCanvas simulationCanvas = new SimulationCanvas();
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
		
		

		computer1.initTrafic();
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				Clock.getInstance().run();
				return null;
			}
		};
		new Thread(task).start();

		
		for(Event e : Clock.getInstance().getEvents()){
        	data.add(e.toString());
        	System.out.println(e.toString());
       }
		Clock.getInstance().setFlag(false);
	}

    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("List View Sample");  
        ListViewSample a = new ListViewSample();
        a.addElements(new Pane());
        
        
      
        
        
//        for(Event e : c.getEvents()){
//        	items.add(e.toString());
//        }
//        	
     
        
        
        final ListView listView = new ListView(data);
        listView.setPrefSize(200, 250);
        listView.setEditable(true);
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.setItems(data);             
               
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        primaryStage.setScene(new Scene(root, 200, 250));
        primaryStage.show();
    
        Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				for (int i = 0; i < 100; i++) {
					 if(Clock.getInstance().isFlag()){
						 data.add(Clock.getInstance().getEvents().get(Clock.getInstance().getEvents().size()-1));
						 Clock.getInstance().setFlag(false);
					 }
					Thread.sleep(1000);
				}
				return null;
			}
		};
		new Thread(task).start();
    
    
    }
       
    public static void main(String[] args) {
    	
        launch(args);
   
    }

}