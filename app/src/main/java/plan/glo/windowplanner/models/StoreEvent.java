package plan.glo.windowplanner.models;

import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.Date;

public class StoreEvent {
    void writeEvent(EventI event, JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("taskId").value(event.getTaskId());
        writer.name("startTime").value(event.getEventStartTime().getTime());
        writer.name("endTime").value(event.getEventEndTime().getTime());
        writer.endObject();
    }

    EventI readEvent(JsonReader reader) throws IOException {
        Date startDate = null, endDate = null;
        int taskId = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "taskId": {
                    taskId = reader.nextInt();
                    break;
                }

                case "startTime": {
                    startDate = new Date(reader.nextInt());
                    break;
                }

                case "endTime": {
                    endDate = new Date(reader.nextInt());
                    break;
                }

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        if (startDate == null || endDate == null || taskId == -1) {
            throw new IOException("Error reading event from json file. Malformed input.");
            //TODO: Error handling should be more descriptive. eg which of startDate, endDate and taskId failed?
        }

        return new Event(startDate, endDate, taskId);
    }
}