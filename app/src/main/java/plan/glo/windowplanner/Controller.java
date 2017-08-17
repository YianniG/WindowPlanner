package plan.glo.windowplanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plan.glo.windowplanner.models.Calendar;
import plan.glo.windowplanner.models.Event;
import plan.glo.windowplanner.models.EventI;
import plan.glo.windowplanner.models.JobI;
import plan.glo.windowplanner.models.Scheduler;
import plan.glo.windowplanner.models.Task;
import plan.glo.windowplanner.models.TaskI;

/**
 * Created by yianni on 17/08/17.
 */

public class Controller {
    //Currently I think there only needs to be one controller
    private static Controller instance = new Controller();

    private Calendar calendar;
    private List<EventI> importedEvents;
    private List<EventI> scheduledEvents;
    private Map<Integer, TaskI> tasks;

    private static final int IMPORTED_TASK_ID = -1;

    private Controller() {
        this.calendar = new Calendar();
        this.importedEvents = new ArrayList<>();
        this.scheduledEvents = new ArrayList<>();
        this.tasks = new HashMap<>();
    }

    public void importEvent(Date startTime, Date endTime, String title) {
        this.importedEvents.add(new Event(startTime, endTime, IMPORTED_TASK_ID));
    }

    public void clearEvents() {
        this.importedEvents.clear();
    }

    public void addTask(List<JobI> jobs, Date start, Date end, String title) {
        TaskI newTask = new Task(jobs, start, end, title);
        this.tasks.put(newTask.getId(), newTask);
    }

    public void editTask(int taskId, TaskI changes) {
        TaskI task = this.tasks.get(taskId);
        task.modifyTask(changes);
    }

    public TaskI getTask(int i) {
        return this.tasks.get(i);
    }

    public List<TaskI> allTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public List<EventI> getScheduledEvents() {
        return this.scheduledEvents;
    }

    public List<EventI> schedule() {
        //Need to check if tasks are empty. Cannot schedule 0 tasks
        if (allTasks().isEmpty()) return this.scheduledEvents;

        calendar = new Calendar();
        //Add the imported events to the calendar
        for (EventI event : importedEvents) {
            calendar.addEvent(event);
        }

        return this.scheduledEvents = new Scheduler().schedule(calendar, allTasks());
    }

    public static Controller getInstance() {
        return instance;
    }
}
