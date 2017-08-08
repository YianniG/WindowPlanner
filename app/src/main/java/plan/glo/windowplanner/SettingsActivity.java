package plan.glo.windowplanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by david on 07/08/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private static final int MIN_TIME_BLOCK_SIZE = 30;
    private static final int DEFAULT_TIME_BLOCK = 60;
    public static final String SETTINGS_TIME_BLOCK_KEY = "time-block";

    private SeekBar mTimeBar;
    private TextView mTimeNum;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mToolbar = ( Toolbar ) findViewById( R.id.settings_toolbar );
        mTimeBar = ( SeekBar ) findViewById( R.id.settings_job_seekbar );
        mTimeNum = ( TextView ) findViewById( R.id.settings_job_seekbar_num );

        setSupportActionBar( mToolbar );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleSeekbar();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void handleSeekbar(){
        final String minutes = getString( R.string.settings_job_minutes );
        int currentTime = sharedPreferences.getInt( SETTINGS_TIME_BLOCK_KEY, DEFAULT_TIME_BLOCK );
        mTimeNum.setText( currentTime + minutes );
        mTimeBar.setProgress( (currentTime / MIN_TIME_BLOCK_SIZE) - 1 );
        mTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTimeNum.setText( ( (progress + 1) *  MIN_TIME_BLOCK_SIZE ) + minutes );
                sharedPreferences.edit().putInt( SETTINGS_TIME_BLOCK_KEY, (progress * 30) ).apply();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

}
