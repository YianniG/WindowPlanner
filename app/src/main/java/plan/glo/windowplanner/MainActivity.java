package plan.glo.windowplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

import plan.glo.windowplanner.models.EventI;
import plan.glo.windowplanner.models.Store;


public class MainActivity extends AppCompatActivity {

    private Controller controller = Controller.getInstance();
    private Store store = new Store(this);

    private static final int CALENDAR_PERMISSION_REQUEST = 101;
    public static final String FIRST_TIME_KEY = "first_time_key";
    public static final String OVERRIDE_EXTRA = "override";

    private MaterialCalendarView calendarView;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar( toolbar );

        controller.addObserver(store);
        controller.loadSavedState(store);

        MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.main_calendarView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.main_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, ImportCalendarActivity.class );
                startActivity( intent );
            }
        });

        Button newTaskButton = (Button) findViewById(R.id.add_task);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, TaskFormActivity.class );
                startActivity( intent );
            }
        });

        Button scheduleButton = (Button) findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<EventI> newEvents = controller.schedule();
                Log.d("Schedule", "Schedule!");
            }
        });


        Intent intent = getIntent();

        if ( intent != null && intent.getExtras() != null && intent.getExtras().containsKey( OVERRIDE_EXTRA ) ){
            //This is a first run explore app override
        }else {
            firstRunLogic();
        }

    }

    private void firstRunLogic(){
        preferences = PreferenceManager.getDefaultSharedPreferences( this );
        if ( !preferences.contains( FIRST_TIME_KEY ) ){
            //Need to ask them to import calendar
            Intent intent = new Intent( this, ImportCalendarActivity.class );
            //Cheekily will reuse the first time key
            intent.putExtra( FIRST_TIME_KEY,  true );

            finish();
            startActivity( intent );

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch( item.getItemId() ){
            case R.id.main_menu_import_calendar:
                Intent intent = new Intent( MainActivity.this, ImportCalendarActivity.class );
                startActivity( intent );
                return true;
        }
        return false;
    }
}
