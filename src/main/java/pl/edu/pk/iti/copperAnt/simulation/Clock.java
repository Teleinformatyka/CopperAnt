package pl.edu.pk.iti.copperAnt.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pl.edu.pk.iti.copperAnt.simulation.events.Event;

public class Clock {
	long currentTime;
	List<Event> events;

	public Clock() {
		this.currentTime = 0;
		events = new ArrayList<Event>();
	}

	public Event getEventFromList(int index) {
		return this.events.get(index);
	}

	public void addEvent(Event eventToAdd) {
		if (eventToAdd.getTime() > currentTime) {
			events.add(getCorrectIndex(eventToAdd), eventToAdd);
		}
	}

	public int getNumberOfWaitingEvent() {
		return this.events.size();
	}

	public void run() {
		while (events.size() > 0) {
			tick();
		}

	}

	public void tick() {
		if (events.size() > 0) {
			this.currentTime = events.get(0).getTime();
			List<Event> eventsWithCurrentTime = popAllEventsWithCurrentTime();
			for (Event event : eventsWithCurrentTime) {
				event.run(this);
			}
		}
	}

	private List<Event> popAllEventsWithCurrentTime() {
		List<Event> result = new LinkedList<Event>();
		while (!events.isEmpty() && events.get(0).getTime() == currentTime) {
			result.add(events.get(0));
			events.remove(0);
		}
		return result;
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

}
