package plan.glo.windowplanner.models;

import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class StoreJob {

    void writeJobArray(List<JobI> jobs, JsonWriter writer) throws IOException {
        writer.beginArray();
        for (JobI job : jobs) {
            writeJob(job, writer);
        }
        writer.endArray();
    }

    List<JobI> readJobArray(JsonReader reader) throws IOException {
        List<JobI> jobArray = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            jobArray.add(readJob(reader));
        }
        reader.endArray();

        return jobArray;
    }

    private void writeJob(JobI job, JsonWriter writer) throws IOException {
        writer.beginObject();
        if (job.hasEvent()) {
            writer.name("event");
            new StoreEvent().writeEvent(job.getEvent(), writer);
        }
        writer.endObject();

    }

    private JobI readJob(JsonReader reader) throws IOException {
        EventI event = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "event": {
                    event = new StoreEvent().readEvent(reader);
                    break;
                }

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        JobI job = new Job();
        if (event != null) {
            job.assignEvent(event);
        }

        return job;
    }
}