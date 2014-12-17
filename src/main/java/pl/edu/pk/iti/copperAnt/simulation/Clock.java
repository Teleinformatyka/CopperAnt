package pl.edu.pk.iti.copperAnt.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pk.iti.copperAnt.simulation.events.Event;
import pl.edu.pk.iti.copperAnt.simulation.StatisticData;
import pl.edu.pk.iti.copperAnt.network.Device;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.PackageType;
import pl.edu.pk.iti.copperAnt.network.Port;


public class Clock {
	private static final Logger log = LoggerFactory.getLogger(Clock.class);
	private FinishCondition finishCondition = new EmptyListFinishCondition();

	long currentTime;
	long lastEventTime;
	boolean realTime = false;
	
	private HashMap<Port,List<StatisticData>> statsParams;

	List<Event> events;
	private long timeScale = 10;

	public Clock() {
		this.currentTime = -1;
		this.lastEventTime = -1;
		events = new ArrayList<Event>();
		statsParams = new HashMap<Port,List<StatisticData>>();
	}

	public Event getEventFromList(int index) {
		return this.events.get(index);
	}

	public void addEvent(Event eventToAdd) {
		log.debug("Added event: " + eventToAdd);
		if (eventToAdd.getTime() >= currentTime) {
			events.add(getCorrectIndex(eventToAdd), eventToAdd);
		}
	}

	public int getNumberOfWaitingEvent() {
		return this.events.size();
	}

	public void run() {
		while (!finishCondition.isSatisfied(this)) {
			tick();
		}

	}

	public void tick() {
		if (!finishCondition.isSatisfied(this)) {
			this.lastEventTime = this.currentTime;
			this.currentTime = events.get(0).getTime();
			if (realTime) {
				try {
					Thread.sleep((currentTime - lastEventTime) * timeScale);
				} catch (InterruptedException e) {
				}
			}
			while (!events.isEmpty() && events.get(0).getTime() == currentTime) {
				Event eventToRun = events.get(0);
				events.remove(eventToRun);
				eventToRun.run(this);
			}

		}
		log.debug("Tick. Size of list:" + events.size());
	}

	private int getCorrectIndex(Event eventToAdd) {
		int i = 0;
		Iterator<Event> iterator = events.iterator();
		while (iterator.hasNext()) {
			Event nextEvent = iterator.next();
			if (nextEvent.getTime() >= eventToAdd.getTime()) {
				break;
			}
			i++;
		}
		return i;
	}

	public void setFinishCondition(FinishCondition finishCondition) {
		this.finishCondition = finishCondition;
	}

	public Clock withFinishCondition(FinishCondition finishCondition) {
		setFinishCondition(finishCondition);
		return this;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public long getTimeScale() {
		return timeScale;
	}

	public void setTimeScale(long timeScale) {
		this.timeScale = timeScale;
	}

	public boolean isRealTime() {
		return realTime;
	}

	public void setRealTime(boolean realTime) {
		this.realTime = realTime;
	}
	
	public void updateStatistics(long time, Port triggeredPort,Package triggeredPackage){
		if(triggeredPackage.getType()== null && (triggeredPackage.getType() == PackageType.DESTINATION_UNREACHABLE 
				|| triggeredPackage.getType() == PackageType.DHCP)){
			return;
		}
		List<StatisticData> tempList = new ArrayList<StatisticData>();
		if(triggeredPackage.getSourceMAC()!= null && triggeredPackage.getSourceMAC().length() > 0){
			if(statsParams.containsKey(triggeredPort)){
				statsParams.get(triggeredPort).add(new StatisticData(time, triggeredPackage));
			} else {
				statsParams.put(triggeredPort,tempList);
				statsParams.get(triggeredPort).add(new StatisticData(time, triggeredPackage));				
			}
			log.debug("Statistics updated");
		}
		if(triggeredPackage.getDestinationMAC()!= null && triggeredPackage.getDestinationMAC().length() > 0){
			if(statsParams.containsKey(triggeredPort)){
				statsParams.get(triggeredPort).add(new StatisticData(time, triggeredPackage));
			} else {
				statsParams.put(triggeredPort,tempList);
				statsParams.get(triggeredPort).add(new StatisticData(time, triggeredPackage));
			}
			log.debug("Statistics updated");
		}
		
	}

}
