package arms.attendancemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import arms.attendancemanagement.api.Course;
import arms.attendancemanagement.api.Manager;


public class ActivityConfirmOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_options);
        setTitle("Verifying Information");

        ArrayList<Course> courses = Manager.getCourses(this);

        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, courses);

        ((Spinner) findViewById(R.id.courseCodeInputToConfirm)).setAdapter(adapter);


        ArrayList<String> dates = Manager.getUnconfirmedDates(this);
        adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, dates);
        ((Spinner) findViewById(R.id.input_lecture_date)).setAdapter(adapter);
    }

    // display unconfirmed attendance
    public void showUnconfirmed(View view) {
        try {
            String semester = ((EditText) findViewById(R.id.semesterInputToConfirm)).getText().toString();
            String date = ((Spinner) findViewById(R.id.input_lecture_date)).getSelectedItem().toString();
            String course = ((Spinner) findViewById(R.id.courseCodeInputToConfirm)).getSelectedItem().toString();

            if (semester.matches("^\\s*$") || course.matches("^\\s*$") || date.matches("^\\s*$")) {
                throw new Exception("Vool hoise");
            }

            Intent intent = new Intent(this, ActivityConfirmAttendance.class);
            intent.putExtra("semester", semester);
            intent.putExtra("course", course);
            intent.putExtra("date", date);

            startActivity(intent);
        } catch (Exception e) {
            Utility.showMessage(this, "Please submit all fields.");
        }
    }
}
