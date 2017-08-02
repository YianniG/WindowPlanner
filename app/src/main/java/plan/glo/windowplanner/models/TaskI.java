package plan.glo.windowplanner.models;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public interface TaskI {
    String getTaskName();
    Date getTaskEndDate();
    Date getTaskStartDate();
    int getJobs();
    void modifyTask(Task configObject);
}
