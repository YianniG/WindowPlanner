package plan.glo.windowplanner.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class Scheduler implements SchedulerI {
    private int[] timeArray;

    @Override
    public CalendarI schedule(CalendarI calendarI, List<TaskI> tasks) {
        Date endTime = findFurthestEndTime(tasks);
        Date scheduleStartTime = new Date(System.currentTimeMillis());
        int blocksUntilEndTime = blocksInInterval(scheduleStartTime, endTime);
        timeArray = new int[blocksUntilEndTime];

        List<EventI> calendarEvents = calendarI.getEvents();
        for (EventI e : calendarEvents) {
            Date eventStart = e.getEventStartTime();
            Date eventEnd = e.getEventEndTime();
            int blocksInEvent = blocksInInterval(eventStart, eventEnd);
            int startIndex = blocksInInterval(scheduleStartTime, eventStart);
            int endIndex = startIndex + blocksInEvent;
            fillInBlock(startIndex, endIndex, e);
        }

        Collections.sort(tasks, new Comparator<TaskI>() {
            @Override
            public int compare(TaskI t1, TaskI t2) {
                return t1.getTaskEndDate().compareTo(t2.getTaskEndDate());
            }
        });

        List<JobI> allJobs = new ArrayList<>();

        for (TaskI t: tasks) {
            List<JobI> taskJobs = t.getJobs();
        }



        for (int i = 0; i < timeArray.length; i++) {
            //Skip blocks that aren't empty
            if (timeArray[i] != 0) continue;

            //Find tasks that include the block
            List<TaskI> includesTheBlock = getTasksThatIncludeTheBlock(i, tasks);

            //Of those tasks, choose the one with the earliest deadline
            TaskI taskWithEarliestDealine = getEarliestDeadline(includesTheBlock);

            //Assign job to this block

        }
        return null;
    }

    private void fillInBlock(int startIdx, int endIdx, EventI e) {
        for (int i = startIdx; i <= endIdx; i++) {
           timeArray[i] = e.getTaskId();
        }
    }

    private int blocksInInterval(Date startTime, Date endTime) {
        if (endTime.after(startTime)) {
            throw new ScheduleException();
        }

        long endTimeMs = endTime.getTime();
        long startTimeMs = startTime.getTime();
        long difference = endTimeMs - startTimeMs;

        long hours = difference / 1000 / 60 / 60;

        long numberOfBlocks = hours * 2;

        return (int) numberOfBlocks;

    }

    private Date findFurthestEndTime(List<TaskI> tasks) {
        if (tasks.isEmpty()) throw new ScheduleException();
        Date maxTime = tasks.get(0).getTaskEndDate();

        for (TaskI t : tasks) {

            Date currEndTime = t.getTaskEndDate();
            if (currEndTime.compareTo(maxTime) == 1) {
                maxTime = currEndTime;
            }
        }

        return maxTime;
    }

    @Override
    public void addTask(TaskI task) {

    }

    @Override
    public void removeTask(TaskI task) {

    }
}
