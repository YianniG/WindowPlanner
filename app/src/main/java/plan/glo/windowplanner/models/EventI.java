package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public interface EventI {

    void modifyEvent(Event event);
    Date getEventStartTime(Event event);
    String getEventName(Event event);
    Date getEventEndTime(Event event);
    int getEventTaskId(Event event);
    int getTaskId(Event event);
}
