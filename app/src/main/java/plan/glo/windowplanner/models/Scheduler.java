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
    private static int BLOCK_SIZE = 30; //30 min block size

    @Override
    public CalendarI schedule(CalendarI calendarI, List<TaskI> tasks) {
        Date endTime = findFurthestEndTime(tasks);
        Date scheduleStartTime = new Date(System.currentTimeMillis());
        int blocksUntilEndTime = blocksInInterval(scheduleStartTime, endTime);
        timeArray = new int[blocksUntilEndTime];

        //Populated time array, with events from users calendar
        List<EventI> calendarEvents = calendarI.getEvents();
        for (EventI e : calendarEvents) {
            Date eventStart = e.getEventStartTime();
            Date eventEnd = e.getEventEndTime();
            int blocksInEvent = blocksInInterval(eventStart, eventEnd);
            int startIndex = blocksInInterval(scheduleStartTime, eventStart);
            int endIndex = startIndex + blocksInEvent;
            fillInBlock(startIndex, endIndex, e.getTaskId());
        }

        //Find free blocks and fill with tasks
        for (int i = 0; i < timeArray.length; i++) {
            //Skip blocks that aren't empty
            if (timeArray[i] != 0) { continue; }

            //Found all eligible tasks for this time block
            Date startTime = convertIndexToTime(i, scheduleStartTime);
            List<TaskI> ts = taskWithStartTimeEarlier(startTime, tasks);
            Collections.sort(ts, new Comparator<TaskI>() {
                @Override
                public int compare(TaskI taskI, TaskI t1) {
                    return taskI.getTaskEndDate().compareTo(t1.getTaskEndDate());
                }
            });

            JobI unassignedJob = null;
            TaskI eligibleTask = null;
            for (TaskI t : ts) {
                //Get unassigned job
                unassignedJob = t.getFreeJob();
                eligibleTask = t;
                if (unassignedJob != null) {
                    break;
                }
            }

            //If unable to find task for this time slot, skip
            if (unassignedJob == null) { continue; }

            //Mark block as full
            fillInBlock(i, eligibleTask.getId());
        }



//        Assign job
//        EventI event = constructEvent(startTime, eligibleTask.getId());
//        unassignedJob.assignEvent(event);
//        calendarI.addEvent(event);
//        return null;
    }

    //Might want to create builder
    private EventI constructEvent(Date startTime, int taskId) {
        //Event is 30mins long
        Date endTime = new Date(startTime.getTime() + BLOCK_SIZE * 60 * 1000);

        return new Event(startTime, endTime, taskId);
    }

    private Date convertIndexToTime(int i, Date scheduleStartTime) {
        long indexTimeMilli = scheduleStartTime.getTime() + i * BLOCK_SIZE * 60 * 1000;
        return new Date(indexTimeMilli);
    }

    private List<TaskI> taskWithStartTimeEarlier(Date startTime, List<TaskI> tasks) {
        List<TaskI> earlierStartTimes = new ArrayList<>();
        for(TaskI t : tasks) {
            if (t.getTaskStartDate().compareTo(startTime) <= 0) {
                earlierStartTimes.add(t);
            }
        }
        return earlierStartTimes;
    }

    private void fillInBlock(int index, int taskId) {
        timeArray[index] = taskId;
    }

    private void fillInBlock(int startIdx, int endIdx, int taskId) {
        for (int i = startIdx; i <= endIdx; i++) {
            fillInBlock(i, taskId);
        }
    }

    //Assume 30 min blocks
    private int blocksInInterval(Date startTime, Date endTime) {
        if (endTime.after(startTime)) {
            throw new ScheduleException();
        }

        long endTimeMs = endTime.getTime();
        long startTimeMs = startTime.getTime();
        long difference = endTimeMs - startTimeMs;

        long minute = difference / 1000 / 60;

        long numberOfBlocks = minute * BLOCK_SIZE;

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
