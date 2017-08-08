package plan.glo.windowplanner;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class ImportCalendarActivity extends AppCompatActivity {

    private ListView mListView;
    private ImportCalendarAdapter mAdapter;
    private Button mImportBtn;

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
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

    public static final int MY_PERMISSION_REQUEST = 101;

    private List<String> mCalendars = new ArrayList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_import );

        mListView = ( ListView ) findViewById( R.id.import_listview );
        mImportBtn = ( Button ) findViewById( R.id.import_button );

        checkForPermission();

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

    }

    public void checkForPermission(){
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED ){
            if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_CALENDAR )){
                showExplanation( getString( R.string.import_permission_title ), getString( R.string.import_permission_desc ) );
            } else {
                ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_CALENDAR }, MY_PERMISSION_REQUEST );
            }
        }
    }

    private void showExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions( ImportCalendarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
                    }
                });
        builder.create().show();
    }


}
