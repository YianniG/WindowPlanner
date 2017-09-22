package plan.glo.windowplanner;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import plan.glo.windowplanner.models.AndroidEventConverter;
import plan.glo.windowplanner.models.EventI;

/**
 * Created by yianni on 21/08/17.
 */

public class CalendarController {

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,           // 0
            CalendarContract.Events.DTSTART,       // 1
            CalendarContract.Events.DTEND,         // 2
            CalendarContract.Events.TITLE,         // 3
            CalendarContract.Events.CALENDAR_ID    // 4
    };

    // The indices for the projection array above.
    private static final int PROJECTION_EVENT_ID_INDEX = 0;
    private static final int PROJECTION_EVENT_DT_START = 1;
    private static final int PROJECTION_EVENT_DT_END   = 2;
    private static final int PROJECTION_EVENT_TITLE    = 3;
    private static final int PROJECTION_EVENT_CAL_ID   = 4;


    public static List<EventI> getEvents(List<Integer> calendarIDs, Activity activity) {
        List<EventI> events = new ArrayList<>();

        // Start importing calendars
        Cursor cur = null;
        ContentResolver cr = activity.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_EVENT_PROJECTION, null, null, null);

        while ( cur.moveToNext() ){
            long rawDTStart = cur.getLong(PROJECTION_EVENT_DT_START);
            long rawDTEnd   = cur.getLong(PROJECTION_EVENT_DT_END);
            String title    = cur.getString(PROJECTION_EVENT_TITLE);
            int calendarId  = cur.getInt(PROJECTION_EVENT_CAL_ID);

            // Only import checked calendars
            if (!calendarIDs.contains(calendarId)) continue;

            if (rawDTStart == 0 || rawDTEnd == 0) continue;
            if (rawDTEnd - rawDTStart == 0) continue;

            // If there's an event that's longer than a day long, don't include it.
            // A really long event probably doesn't mean you're busy (until it does) Gonna leave for the moment
            long milliSecondsInADay = 1000 * 60 * 60 * 24;
            if (rawDTEnd - rawDTStart >= milliSecondsInADay) continue;

            Date dtstart = new Date(rawDTStart);
            Date dtend = new Date(rawDTEnd);

            // Create event
            events.add(new AndroidEventConverter().startTime(dtstart).endTime(dtend).title(title)
                    .build());
        }

        return events;
    }

}
