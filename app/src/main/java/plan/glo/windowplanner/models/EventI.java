package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public interface EventI {

    void modifyEvent(Event event);
    Date getEventStartTime();
    String getEventName();
    Date getEventEndTime();
    int getTaskId();
}
