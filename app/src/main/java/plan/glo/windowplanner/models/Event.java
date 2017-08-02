package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Event implements EventI{
    private int id;
    private Date startDate;
    private Date endDate;
    private String name;
    private int taskId;

    @Override
    public void modifyEvent(Event event) {

    }

    @Override
    public Date getEventStartTime(Event event) {
        return null;
    }

    @Override
    public String getEventName(Event event) {
        return null;
    }

    @Override
    public Date getEventEndTime(Event event) {
        return null;
    }

    @Override
    public int getEventTaskId(Event event) {
        return 0;
    }

    @Override
    public int getTaskId(Event event) {
        return 0;
    }
}
