package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public interface EventI {

    Date getEventStartTime();
    Date getEventEndTime();
    int getTaskId();
    int getId();
}
