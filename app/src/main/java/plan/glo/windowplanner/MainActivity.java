package plan.glo.windowplanner;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = ( Toolbar ) findViewById( R.id.main_toolbar );
        calendarView = (MaterialCalendarView) findViewById( R.id.main_calendarView );
        floatingActionButton = ( FloatingActionButton ) findViewById( R.id.main_fab );

        setSupportActionBar( toolbar );


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent( MainActivity.this, TaskFormActivity.class );
            startActivity( intent );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        return super.onCreateOptionsMenu(menu);
    }


}
