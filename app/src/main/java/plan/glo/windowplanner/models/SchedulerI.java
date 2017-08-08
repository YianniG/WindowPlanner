package plan.glo.windowplanner.models;

import java.util.List;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public interface SchedulerI {
    List<EventI> schedule(CalendarI calendarI, List<TaskI> tasks);
}
