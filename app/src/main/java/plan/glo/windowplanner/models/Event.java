package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Event implements EventI{
    private Date startDate;
    private Date endDate;
    private int taskId;
    private static int EVENT_COUNTER = 0;
    private int eventId;

    public Event(Date startDate, Date endDate, int taskId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskId = taskId;
        this.eventId = EVENT_COUNTER++;
    }

    @Override
    public Date getEventStartTime() {
        return startDate;
    }

    @Override
    public Date getEventEndTime() {
        return endDate;
    }

    @Override
    public int getTaskId() {
        return taskId;
    }

    @Override
    public int getId() {
        return eventId;
    }
}
