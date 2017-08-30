package plan.glo.windowplanner;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import plan.glo.windowplanner.models.AndroidEventConverter;
import plan.glo.windowplanner.models.Calendar;
import plan.glo.windowplanner.models.Event;
import plan.glo.windowplanner.models.EventI;
import plan.glo.windowplanner.models.Job;
import plan.glo.windowplanner.models.JobI;
import plan.glo.windowplanner.models.Scheduler;
import plan.glo.windowplanner.models.Store;
import plan.glo.windowplanner.models.Task;
import plan.glo.windowplanner.models.TaskI;

/**
 * Created by yianni on 17/08/17.
 */

public class Controller extends Observable {

    //Currently I think there only needs to be one controller
    private static Controller instance = new Controller();

    private Calendar            calendar;
    private List<EventI>        scheduledEvents;
    private Map<Integer, TaskI> tasks;

    private List<EventI>        importedEvents;
    private List<Integer>       userCalendars;


    private Controller() {
        this.calendar         = new Calendar();
        this.scheduledEvents  = new ArrayList<>();
        this.tasks            = new HashMap<>();
        this.importedEvents   = new ArrayList<>();
        this.userCalendars    = new ArrayList<>();
    }

    public void loadSavedState(Store store, Activity mainActivity) {
        try {
            List<TaskI> savedTasks      = store.readTasks();
            List<Integer> userCalendars = store.readCalendars();
            Log.i("Controller", "Loading save file");

            //Load saved tasks
            for (TaskI task : savedTasks) {
                tasks.put(task.getId(), task);
            }
            this.userCalendars = userCalendars;

            //Re-import calendar events
            this.importedEvents = CalendarController.getEvents(userCalendars, mainActivity);

            //Schedule tasks to generate task events
            this.scheduledEvents = schedule();
        } catch (IOException e) {
            // Unable to load saved state
            e.printStackTrace();
            Log.e("Controller", "Couldn't load save file");
        }
    }

    public void importEvent(EventI event) {
        this.importedEvents.add(event);
        notifyObservers();
    }

    public void clearEvents() {
        this.importedEvents.clear();
        notifyObservers();
    }

    public void trackCalendar(int calendarId) {
        userCalendars.add(calendarId);
    }

    public List<EventI> getEvents() {
        return this.importedEvents;
    }

    public void stopTrackingCalendars() {
        userCalendars.clear();
    }

    public List<Integer> getTrackedCalendars() {
        return userCalendars;
    }

    public void addTask(int numberOfJobs, Date start, Date end, String title) {
        List<JobI> jobs = new ArrayList<>();
        for (int i = 0; i < numberOfJobs; i++) {
            jobs.add(new Job());
        }

        TaskI newTask = new Task(jobs, start, end, title);
        this.tasks.put(newTask.getId(), newTask);
        notifyObservers();
    }

    public void editTask(int taskId, TaskI changes) {
        TaskI task = this.tasks.get(taskId);
        task.modifyTask(changes);
        notifyObservers();
    }

    public TaskI getTask(int i) {
        return this.tasks.get(i);
    }

    public List<TaskI> allTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public List<Integer> allCalendars() {
        return userCalendars;
    }

    public List<EventI> getScheduledEvents() {
        return this.scheduledEvents;
    }

    public List<EventI> schedule() {
        //Need to check if tasks are empty. Cannot schedule 0 tasks
        if (allTasks().isEmpty()) return this.scheduledEvents;

        calendar = new Calendar();
        //Add the imported events to the calendar
        for (EventI event : getEvents()) {
            calendar.addEvent(event);
        }

        this.scheduledEvents = new Scheduler().schedule(calendar, allTasks());
        notifyObservers();
        return this.scheduledEvents;
    }

    //Whenever we notifyObservers, a change has occurred
    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }

    public static Controller getInstance() {
        return instance;
    }
}
