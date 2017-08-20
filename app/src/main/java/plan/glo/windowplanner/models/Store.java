package plan.glo.windowplanner.models;

import android.app.Activity;
import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import plan.glo.windowplanner.Controller;

/**
 * Created by yianni on 19/08/17.
 */

public class Store implements Observer {

    // Store and read app data from phone
    // Store data in internal storage

    // Store tasks
    // TODO: Store settings - preferences

    private Activity mainActivity;
    private String TASK_FILE = "TASK-DATA";

    public Store(Activity activity) {
        mainActivity = activity;
    }

    public void storeTasks(List<TaskI> tasks) throws IOException {
        FileOutputStream fileOutputStream
                = mainActivity.openFileOutput(TASK_FILE, Context.MODE_PRIVATE);

        // Write tasks as json
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
        writer.setIndent("    ");
        new StoreTask().writeTaskArray(tasks, writer);
        writer.close();
        fileOutputStream.close();
    }

    public List<TaskI> readTasks() throws IOException {
        List<TaskI> tasks;
        try {
            FileInputStream fileInputStream
                    = mainActivity.openFileInput(TASK_FILE);

            JsonReader reader = new JsonReader(new InputStreamReader(fileInputStream, "UTF-8"));
            tasks = new StoreTask().readTaskArray(reader);
            reader.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            // File not found. Return empty array.
            tasks = new ArrayList<>();
        }

        return tasks;
    }

    // Anytime the controller gets updated, store saves its state
    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof Controller) {
            Controller controller = (Controller) observable;

            // Save tasks
            try {
                storeTasks(controller.allTasks());
                Log.i("Store", "saving tasks");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Store", "couldn't save tasks");
            }
        }
    }
}
