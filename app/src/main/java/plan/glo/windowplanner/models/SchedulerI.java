package plan.glo.windowplanner.models;

import java.util.List;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public interface SchedulerI {
    CalendarI schedule(CalendarI calendarI, List<TaskI> tasks);
    void addTask(TaskI task);
    void removeTask(TaskI task);
}
