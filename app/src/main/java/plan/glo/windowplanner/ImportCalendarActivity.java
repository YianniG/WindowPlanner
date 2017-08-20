package plan.glo.windowplanner;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImportCalendarActivity extends AppCompatActivity {

    private Controller controller = Controller.getInstance();

    private ListView mListView;
    private ImportCalendarAdapter mAdapter;
    private Button mImportBtn;

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,           // 0
            CalendarContract.Events.DTSTART,       // 1
            CalendarContract.Events.DTEND,         // 2
            CalendarContract.Events.TITLE          // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_EVENT_ID_INDEX = 0;
    private static final int PROJECTION_EVENT_DT_START = 1;
    private static final int PROJECTION_EVENT_DT_END = 2;
    private static final int PROJECTION_EVENT_TITLE = 3;


    private List<String> mCalendars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        mListView = (ListView) findViewById(R.id.import_listview);
        mImportBtn = (Button) findViewById(R.id.import_button);

        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

        while ( cur.moveToNext() ){
            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            mCalendars.add( displayName );
            Log.d("ImportCalendayActivity", displayName);
        }

        mAdapter = new ImportCalendarAdapter( this, mCalendars );
        mListView.setAdapter( mAdapter );

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Import events from all the calendars
                importEvents();

                //Finished importing, go back to Main Activity
                finish();
            }
        });
    }

    private void importEvents() {
        //Make sure to remove any old events
        controller.clearEvents();

        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_EVENT_PROJECTION, null, null, null);

        while ( cur.moveToNext() ){
            long rawDTStart = cur.getLong(PROJECTION_EVENT_DT_START);
            long rawDTEnd   = cur.getLong(PROJECTION_EVENT_DT_END);
            String title    = cur.getString(PROJECTION_EVENT_TITLE);
            if (rawDTStart == 0 || rawDTEnd == 0) continue;
            if (rawDTEnd - rawDTStart == 0) continue;

            //If there's an event that's longer than a day long, don't include it.
            // A really long event probably doesn't mean you're busy (until it does) Gonna leave for the moment
            long milliSecondsInADay = 1000 * 60 * 60 * 24;
            if (rawDTEnd - rawDTStart >= milliSecondsInADay) continue;

            Date dtstart = new Date(rawDTStart);
            Date dtend = new Date(rawDTEnd);
            //Create event
            controller.importEvent(dtstart, dtend, title);
        }
    }
}
