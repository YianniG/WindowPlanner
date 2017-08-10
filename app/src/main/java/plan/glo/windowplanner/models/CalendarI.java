package plan.glo.windowplanner.models;

import java.util.List;

public interface CalendarI {
    List<EventI> getEvents();
    void addEvent(EventI newEvent);
    void removeEvent(int id);
    List<EventI> findEvent(EventI searchObject);
}
