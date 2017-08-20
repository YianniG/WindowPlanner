package plan.glo.windowplanner.models;

import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class StoreTask {

    void writeTaskArray(List<TaskI> tasks, JsonWriter writer) throws IOException {
        writer.beginArray();
        for (TaskI task : tasks) {
            writeTask(task, writer);
        }
        writer.endArray();
    }

    List<TaskI> readTaskArray(JsonReader reader) throws IOException {
        List<TaskI> taskArray = new ArrayList<TaskI>();

        reader.beginArray();
        while (reader.hasNext()) {
            taskArray.add(readTask(reader));
        }
        reader.endArray();

        return taskArray;
    }

    private void writeTask(TaskI task, JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("taskId").value(task.getId());
        writer.name("name").value(task.getTaskName());
        writer.name("startDate").value(task.getTaskStartDate().getTime());
        writer.name("endDate").value(task.getTaskEndDate().getTime());
        writer.name("jobs");
        new StoreJob().writeJobArray(task.getJobs(), writer);
        writer.endObject();
    }

    private TaskI readTask(JsonReader reader) throws IOException {
        int taskId = -1;
        Date startDate = null, endDate = null;
        String name = null;
        List<JobI> jobs = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "taskId": {
                    taskId = reader.nextInt();
                    break;
                }

                case "name": {
                    name = reader.nextString();
                    break;
                }

                case "startDate": {
                    startDate = new Date(reader.nextLong());
                    break;
                }

                case "endDate": {
                    endDate = new Date(reader.nextLong());
                    break;
                }

                case "jobs": {
                    jobs = new StoreJob().readJobArray(reader);
                    break;
                }

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        if (startDate == null || endDate == null || taskId == -1 || name == null || jobs == null) {
            throw new IOException("Error reading task from json file. Malformed input.");
            //TODO: Error handling should be more descriptive. eg which of startDate, endDate and taskId failed?
        }

        return new Task(jobs, startDate, endDate, name, taskId);
    }
}