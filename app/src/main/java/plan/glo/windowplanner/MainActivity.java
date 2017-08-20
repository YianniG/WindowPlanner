package plan.glo.windowplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {

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

        Intent intent = getIntent();

        if ( intent != null && intent.getExtras() != null && intent.getExtras().containsKey( OVERRIDE_EXTRA ) ){
            //This is a first run explore app override
        }else {
            firstRunLogic();
        }

        toolbar = ( Toolbar ) findViewById( R.id.main_toolbar );
        calendarView = (MaterialCalendarView) findViewById( R.id.main_calendarView );
        floatingActionButton = ( FloatingActionButton ) findViewById( R.id.main_fab );



        setSupportActionBar( toolbar );


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Change to added tasks/events whatever
            }
        });
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
