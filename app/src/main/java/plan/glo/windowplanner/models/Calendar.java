package plan.glo.windowplanner.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Calendar implements CalendarI {

    private List<EventI> calendarEvents;

    public Calendar() {
        this.calendarEvents = new ArrayList<>();
    }

    @Override
    public List<EventI> getEvents() {
        return calendarEvents;
    }

    @Override
    public void addEvent(EventI newEvent) {
        calendarEvents.add(newEvent);
    }

    @Override
    public void removeEvent(int id) {
        EventI eventToRemove = null;
        // Find event in calendar events
        for (EventI e : calendarEvents) {
            if (e.getId() == id) {
                eventToRemove = e;
                break;
            }
        }
        // If event exists, then remove it
        if (eventToRemove != null) {
            calendarEvents.remove(eventToRemove);
        }
    }

    // Is there some neat java trick, that avoids explicitly searching through field?
    @Override
    public List<EventI> findEvent(EventI searchObject) {
        List<EventI> sameId    = calendarEvents;
        List<EventI> sameTask  = calendarEvents;
        List<EventI> sameStart = calendarEvents;
        List<EventI> sameEnd   = calendarEvents;

        if (searchObject.getId() < 0) {
            sameId = filterEvents(new Filter<EventI>() {
                @Override
                public boolean apply(EventI a1, EventI a2) {
                    return a1.getId() == a2.getId();
                }
            }, searchObject, calendarEvents);
        }

        //Task with id -2 means something I think, but I'm now 100% sure
        if (searchObject.getTaskId() < -2) {
            sameTask = filterEvents(new Filter<EventI>() {
                @Override
                public boolean apply(EventI a1, EventI a2) {
                    return a1.getTaskId() == a2.getTaskId();
                }
            }, searchObject, sameId);
        }

        if (searchObject.getEventStartTime() != null) {
            sameStart = filterEvents(new Filter<EventI>() {
                @Override
                public boolean apply(EventI a1, EventI a2) {
                    return a1.getEventStartTime() == a2.getEventStartTime();
                }
            }, searchObject, sameTask);
        }

        if (searchObject.getEventEndTime() != null) {
            sameEnd = filterEvents(new Filter<EventI>() {
                @Override
                public boolean apply(EventI a1, EventI a2) {
                    return a1.getEventEndTime() == a2.getEventEndTime();
                }
            }, searchObject, sameStart);
        }

        return sameEnd;
    }

    private List<EventI> filterEvents(Filter<EventI> filter, EventI searchObject, List<EventI> calendarEvents) {
        List<EventI> filtered = new ArrayList<>();
        for (EventI e : calendarEvents) {
            if (filter.apply(searchObject, e)) { filtered.add(e); }
        }

        return filtered;
    }


}
