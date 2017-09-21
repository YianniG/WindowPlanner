package plan.glo.windowplanner;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plan.glo.windowplanner.models.EventI;

public class ImportCalendarActivity extends AppCompatActivity {

    private Controller controller = Controller.getInstance();

    private static final int CALENDAR_PERMISSION_REQUEST = 101;

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

    private boolean firstRun = false;

    private Map<Integer, String> mCalendars = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        mListView = (ListView) findViewById(R.id.import_listview);
        mImportBtn = (Button) findViewById(R.id.import_button);
        Intent intent = getIntent();

        if ( intent != null && intent.getExtras() != null && intent.getExtras().containsKey( MainActivity.FIRST_TIME_KEY ) ){
            //This is a first run
            firstRun = true;
        }else {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkForPermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ( firstRun ){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate( R.menu.import_calendar, menu );
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch( item.getItemId() ){
            case android.R.id.home:
                finish();
                return true;
            case R.id.main_menu_import_calendar:
                Intent intent = new Intent( ImportCalendarActivity.this, MainActivity.class );
                intent.putExtra( MainActivity.OVERRIDE_EXTRA, true );
                finish();
                startActivity( intent );
                return true;
        }
        return false;
    }

    private void setupScreen(){
        // Run query
        Cursor cur;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

        while ( cur.moveToNext() ){
            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            int calendarID     = cur.getInt(PROJECTION_ID_INDEX);
            mCalendars.put(calendarID, displayName);
        }

        mAdapter = new ImportCalendarAdapter(this, mCalendars);
        mListView.setAdapter( mAdapter );

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Import events from all the calendars

                //Make sure to remove any old events
                controller.clearEvents();
                controller.stopTrackingCalendars();

                Log.d("ImportCalendayActivity", "Importing calendars");

                // Mark the calendars to import and track
                Map<Integer, Boolean> isChecked = mAdapter.getCheckedCalendars();
                for (Integer calID : isChecked.keySet()) {
                    if (isChecked.get(calID)) {
                        controller.trackCalendar(calID);
                    }
                }

                List<EventI> importedEvents = CalendarController.getEvents(
                        controller.getTrackedCalendars(), ImportCalendarActivity.this);
                for (EventI e : importedEvents) {
                    controller.importEvent(e);
                }

                if ( firstRun ){
                    PreferenceManager.getDefaultSharedPreferences( ImportCalendarActivity.this )
                            .edit().putBoolean( MainActivity.FIRST_TIME_KEY, true ).apply();

                    Intent intent = new Intent( ImportCalendarActivity.this, MainActivity.class );
                    finish();
                    startActivity( intent );
                }else {
                    finish();
                }
            }
        });
    }

    /**
     * All the permission code
     */

    private void checkForPermission(){
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED ){
            if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_CALENDAR )){
                showExplanation( getString( R.string.import_permission_title ), getString( R.string.import_permission_desc ) );
            } else {
                ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_CALENDAR }, CALENDAR_PERMISSION_REQUEST );
            }
        }else {
            setupScreen();
        }
    }

    private void showExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions( ImportCalendarActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_REQUEST );
                    }
                });

        if ( firstRun ){
            builder.setNegativeButton(R.string.import_permission_explore, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent( ImportCalendarActivity.this, MainActivity.class );
                    intent.putExtra( MainActivity.OVERRIDE_EXTRA, true );
                    finish();
                    startActivity( intent );
                }
            });
        }

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALENDAR_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted, yes fam
                    setupScreen();
                } else {
                    //Didn't grant permission :(, start again lol
                    checkForPermission();
                }
                return;
            }

        }
    }
}
