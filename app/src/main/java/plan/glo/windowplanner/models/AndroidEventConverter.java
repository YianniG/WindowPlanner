package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by yianni on 11/08/17.
 */

public class AndroidEventConverter {

    private static final int IMPORTED_TASK_ID = -1; //Negative task id means event from user calendar

    private Date startTime, endTime;
    private int taskId = IMPORTED_TASK_ID;
    private String title;

    public AndroidEventConverter startTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public AndroidEventConverter endTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public AndroidEventConverter taskId(int importedTaskId) {
        this.taskId = importedTaskId;
        return this;
    }

    public AndroidEventConverter title(String title) {
        this.title = title;
        return this;
    }

    public EventI build() {
        return new Event(startTime, endTime, taskId);
    }
}
