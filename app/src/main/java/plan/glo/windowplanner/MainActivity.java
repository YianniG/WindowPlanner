package plan.glo.windowplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;

    public static final int CALENDAR_PERMISSION_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = ( Toolbar ) findViewById( R.id.main_toolbar );

        //TODO: This breaks, because of the hacky way I'm trying to get and notify the user we need permission - ig
        //setSupportActionBar( toolbar );

        if (checkForPermission()) {
            loadApp();
        } else {
            loadNeedPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        return super.onCreateOptionsMenu(menu);
    }

    private boolean checkForPermission(){
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED ){
            if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_CALENDAR )){
                //showExplanation( getString( R.string.import_permission_title ), getString( R.string.import_permission_desc ) );
                ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_CALENDAR }, CALENDAR_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_CALENDAR }, CALENDAR_PERMISSION_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALENDAR_PERMISSION_REQUEST:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadApp();
                }
            }
        }
    }

//  private void showExplanation(String title, String message) {
//      AlertDialog.Builder builder = new AlertDialog.Builder( this );
//      builder.setTitle(title)
//              .setMessage(message)
//              .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                  public void onClick(DialogInterface dialog, int id) {
//                      ActivityCompat.requestPermissions(super. , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CALENDAR_PERMISSION_REQUEST);
//                  }
//              });
//      builder.create().show();
//  }

    private void loadApp() {
        setContentView(R.layout.activity_main);
        calendarView = (MaterialCalendarView) findViewById( R.id.main_calendarView );
        floatingActionButton = ( FloatingActionButton ) findViewById( R.id.main_fab );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, ImportCalendarActivity.class );
                startActivity( intent );
            }
        });
    }

    private void loadNeedPermissions() {
        setContentView(R.layout.activity_main_need_permissions);
    }
}
