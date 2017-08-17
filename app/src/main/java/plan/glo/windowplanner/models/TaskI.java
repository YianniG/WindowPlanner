package plan.glo.windowplanner.models;

import java.util.Date;
import java.util.List;

public interface TaskI {
    int getId();
    String getTaskName();
    Date getTaskEndDate();
    Date getTaskStartDate();
    List<JobI> getJobs();
    void modifyTask(TaskI configObject);
    JobI getFreeJob();
}
