package plan.glo.windowplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by zubairchowdhury on 02/08/2017.
 */

public class TaskFormActivity extends AppCompatActivity {

    private Controller controller = Controller.getInstance();
    private String name;
    private Date startDate;
    private Date endDate;
    private int numberOfJobs = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskform);

        final EditText taskName = (EditText) findViewById(R.id.taskform_task_title);

        //TODO: Date button's need to select date
        Button startDateButton = (Button) findViewById(R.id.taskform_task_start);
        Button endDateButton = (Button) findViewById(R.id.taskform_task_end);

        SeekBar length = (SeekBar) findViewById(R.id.taskform_seekbar);
        length.setMax(100); // arbitrary max of 100 jobs

        length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numberOfJobs = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do nothing rn
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Do nothing rn
            }
        });
        Button addTask = (Button) findViewById(R.id.taskform_task_create);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = taskName.getText().toString();

                //Temp start and end times
                startDate = new Date(System.currentTimeMillis() + 10000);
                endDate = new Date(System.currentTimeMillis() + 50000);

                boolean[] inputValidation = validateInputs();
                if (allTrue(inputValidation)) {
                    controller.addTask(numberOfJobs, startDate, endDate, name);
                    finish();
                    // startActivity(new Intent(TaskFormActivity.this, MainActivity.class));
                } else {
                    // Error inputs
                    TextView title_error_view = (TextView) findViewById(R.id.task_name_error_view);
                    TextView start_error_view = (TextView) findViewById(R.id.task_start_date_error_view);
                    TextView end_error_view = (TextView) findViewById(R.id.task_end_date_error_view);
                    TextView length_error_view = (TextView) findViewById(R.id.task_number_of_jobs_error_view);

                    title_error_view.setText("");
                    start_error_view.setText("");
                    end_error_view.setText("");
                    length_error_view.setText("");

                    if (!inputValidation[0]) {
                        //Bad title
                        title_error_view.setText("Task name can't be empty");
                    }

                    if (!inputValidation[1]) {
                        //Bad start time
                        start_error_view.setText("Pick a valid start date");
                    }

                    if (!inputValidation[2]) {
                        //Bad end time
                        end_error_view.setText("Pick a valid end date");
                    }

                    if (!inputValidation[3]) {
                        //Bad number of jobs
                        length_error_view.setText("Size of tasks must be greater than 0");
                    }
                }
            }
        });
    }

    private boolean[] validateInputs() {
        return new boolean[]{
            !name.isEmpty(),

            startDate != null
                && startDate.compareTo(new Date(System.currentTimeMillis())) >= 0,

            endDate != null
                && endDate.compareTo(new Date(System.currentTimeMillis())) >= 0
                && (startDate == null || endDate.after(startDate)),

            numberOfJobs > 0
        };
    }

    private boolean allTrue(boolean[] bools) {
        for (boolean b : bools) {
            if (!b) return false;
        }
        return true;
    }
}
