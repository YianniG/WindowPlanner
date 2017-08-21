package plan.glo.windowplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import plan.glo.windowplanner.models.Store;
import plan.glo.windowplanner.util.CustomViewPager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String FIRST_TIME_KEY = "first_time_key";
    public static final String OVERRIDE_EXTRA = "override";

    private BottomNavigationView bottomNavigationView;
    private CustomViewPager viewPager;
    private FloatingActionButton floatingActionButton;

    private ScreensAdapter adapter;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = ( CustomViewPager ) findViewById( R.id.main_viewpager );
        adapter = new ScreensAdapter( getSupportFragmentManager() );

        viewPager.setAdapter( adapter );
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch ( position ){
                    case 0:
                        viewPager.setPagingEnabled(true);
                        bottomNavigationView.setSelectedItemId( R.id.main_bottom_dayview );
                        break;
                    case 1:
                        viewPager.setPagingEnabled(true);
                        bottomNavigationView.setSelectedItemId( R.id.main_bottom_taskview );
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        bottomNavigationView = ( BottomNavigationView ) findViewById( R.id.bottom_navigation );
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Intent intent = getIntent();

        if ( intent != null && intent.getExtras() != null && intent.getExtras().containsKey( OVERRIDE_EXTRA ) ){
            //This is a first run explore app override
        }else {
            firstRunLogic();
        }

        floatingActionButton = (FloatingActionButton) findViewById(R.id.main_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, TaskFormActivity.class );
                startActivity( intent );
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
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_bottom_dayview:
                viewPager.setCurrentItem( 0 );
                return true;
            case R.id.main_bottom_taskview:
                viewPager.setCurrentItem( 1 );
                return true;
        }
        return false;
    }

    private class ScreensAdapter extends FragmentPagerAdapter {

        public ScreensAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public Fragment getItem(int position) {

            switch( position ){
                case 0:
                    return DayViewFragment.newInstance();
                case 1:
                    return TaskViewFragment.newInstance();
            }
            return null;
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
