package plan.glo.windowplanner.models;

import java.util.List;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Scheduler implements SchedulerI {
    private CalendarI calendar;
    private List<Task> tasks;
    private int[] timeArray;

    @Override
    public CalendarI schedule(CalendarI calendarI, List<TaskI> tasks) {
        return null;
    }

    @Override
    public void addTask(TaskI task) {

    }

    @Override
    public void removeTask(TaskI task) {

    }
}
