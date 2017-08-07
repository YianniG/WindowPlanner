package plan.glo.windowplanner.models;

import java.util.ArrayList;
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
    public void addEvent() {
        //todo: add events

    }

    @Override
    public void removeEvent(int id) {

    }

    @Override
    public List<EventI> findEvent(EventI searchObject) {
        return null;
    }


}
