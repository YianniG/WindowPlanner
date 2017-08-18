package plan.glo.windowplanner;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class TaskFormActivity extends PreferenceActivity {

    private String name;
    private Date startDate;
    private Date endDate;
    private int numberOfJobs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskform);

        EditText taskName = (EditText) findViewById(R.id.taskform_task_title);

        //TODO: Date button's need to select date
        Button startDateButton = (Button) findViewById(R.id.taskform_task_start);
        Button endDateButton = (Button) findViewById(R.id.taskform_task_end);

        SeekBar length = (SeekBar) findViewById(R.id.taskform_seekbar);
        Button addTask = (Button) findViewById(R.id.taskform_task_create);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Verify all inputs are given, else give error - highlight missing info

                //TODO: Take inputs and contruct a task and add to controller
            }
        });
    }
}
