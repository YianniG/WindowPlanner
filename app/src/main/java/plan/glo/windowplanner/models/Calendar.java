package plan.glo.windowplanner.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Calendar implements CalendarI {

    private List<Event> calendarEvents;

    public Calendar() {
        this.calendarEvents = new ArrayList<>();
    }

    @Override
    public List<Event> getEvents() {
        return calendarEvents;
    }

    @Override
    public void addEvent() {
        //todo: add events

    }

    @Override
    public void removeEvent(int id) {

    }

    @Override
    public List<Event> findEvent(Event searchObject) {
        return null;
    }

}
