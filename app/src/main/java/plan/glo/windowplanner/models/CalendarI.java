package plan.glo.windowplanner.models;

import java.util.List;

public interface CalendarI {
    List<Event> getEvents();
    void addEvent();
    void removeEvent(int id);
    List<Event> findEvent(Event searchObject);
}
