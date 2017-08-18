package plan.glo.windowplanner.models;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Scheduler implements SchedulerI {
    private int[] timeArray;
    private static int BLOCK_SIZE = 30; //30 min block size
    private Date scheduleStartTime;
    private SparseArray<TaskI> taskHash = new SparseArray<>();

    @Override
    public List<EventI> schedule(CalendarI calendarI, List<TaskI> tasks) {

        //Populate hash map for search later on
        for (TaskI t : tasks) {
           taskHash.append(t.getId(), t);
        }

        Date endTime = findFurthestEndTime(tasks);
        this.scheduleStartTime = new Date(System.currentTimeMillis());
        int blocksUntilEndTime = blocksInInterval(scheduleStartTime, endTime);
        timeArray = new int[blocksUntilEndTime];

        //Populated time array, with events from users calendar
        List<EventI> calendarEvents = calendarI.getEvents();
        for (EventI e : calendarEvents) {
            //skip events that have ended before scheduleStartTime
            if (e.getEventEndTime().compareTo(scheduleStartTime) == -1) continue;
            //skip events that start after last task
            if (e.getEventStartTime().compareTo(endTime) == 1) continue;

            //If the event started before scheduled start time, we pretend it started at
            // scheduledStartTime
            Date eventStart = e.getEventStartTime();
            if (eventStart.compareTo(scheduleStartTime) == -1) eventStart = scheduleStartTime;

            Date eventEnd = e.getEventEndTime();
            //If the event starts before endTime and finishes after, we pretend it finishes at endTime
            if (eventEnd.compareTo(endTime) == 1) eventEnd = endTime;

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

            //Mark block as full.
            fillInBlock(i, eligibleTask.getId());
        }
        //we can do any shuffling before we move on
        return makeEvents();
    }

    private void shuffleBlock() {

    }

    private List<EventI> makeEvents() {
        List<EventI> events = new ArrayList<>();

        // Track contiguous blocks of blocks with same taskId
        int startOfContiguousBlock = 0;
        for (int i = 1; i < timeArray.length; i++) {
            if (timeArray[i] < 0) {
                startOfContiguousBlock = i;
                continue;
            }

            if (timeArray[i-1] < 0) {
                // Exiting block of imported events
                startOfContiguousBlock = i;
            } else if (timeArray[i] != timeArray[i-1]) {
                // End of block of contiguous id's
                // Make event for previous block
                TaskI t = searchForTask(timeArray[i-1]);

                Date startTime = new Date(scheduleStartTime.getTime()
                        + startOfContiguousBlock * BLOCK_SIZE);
                events.add(constructEvent(startTime, i-startOfContiguousBlock, t.getId()));

                startOfContiguousBlock = i;
            }

            // Account for last block
            if (i == timeArray.length - 1) {

                int eventLengthInBlocks;
                if (i == startOfContiguousBlock) {
                    // Create event for final block
                    eventLengthInBlocks = 1;
                } else {
                    // Create event for contiguous block, finishing on final block
                    eventLengthInBlocks = i - startOfContiguousBlock + 1;
                }

                TaskI t = searchForTask(timeArray[i]);
                Date startTime = new Date(scheduleStartTime.getTime()
                        + startOfContiguousBlock * BLOCK_SIZE);
                events.add(constructEvent(startTime, eventLengthInBlocks, t.getId()));
            }
        }

        return events;
    }

    private TaskI searchForTask(int i) {
        return taskHash.get(i);
    }

    //Might want to create builder
    private EventI constructEvent(Date startTime, int lengthInBlocks, int taskId) {
        //Event is length in blocks long
        Date endTime = new Date(startTime.getTime() + BLOCK_SIZE * lengthInBlocks * 60 * 1000);

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
        if (startTime.compareTo(endTime) == 1) {
            throw new ScheduleException();
        }

        long endTimeMs = endTime.getTime();
        long startTimeMs = startTime.getTime();
        long difference = endTimeMs - startTimeMs;

        double minute = difference / 1000 / 60;

        int numberOfBlocks = (int) Math.ceil(minute / BLOCK_SIZE);
        return numberOfBlocks;
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

}
