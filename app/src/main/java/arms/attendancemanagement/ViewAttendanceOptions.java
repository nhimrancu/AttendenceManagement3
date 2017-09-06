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


public class ViewAttendanceOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendance_options);
        setTitle(" View Options ");

        ArrayList<Course> courses = Manager.getCourses(this);
        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, courses);
        ((Spinner) findViewById(R.id.courseCodeInputToConfirm)).setAdapter(adapter);
    }

//     display course wise attendances list
    public void showCourseWiseAttendances(View view) {
        String semester = ((EditText) findViewById(R.id.semesterInputToConfirm)).getText().toString();
        String course = ((Spinner) findViewById(R.id.courseCodeInputToConfirm)).getSelectedItem().toString();

        // check if semester input is empty
        if(semester.matches("^\\s*$")){
            Utility.showMessage(this, "Please enter all fields"); return;
        }

        goToPrint(semester,course);
    }

//    display entire semester's attendaces
    public void showSemesterAttendances(View view){
        String semester = ((EditText) findViewById(R.id.semesterInputToConfirm)).getText().toString();

        // check if semester input is empty
        if(semester.matches("^\\s*$")){
            Utility.showMessage(this, "Please enter all fields"); return;
        }
        goToPrint(semester,null);
    }

    public void goToPrint(String semester, String course){
        Intent intent = new Intent(this, Print.class);
        intent.putExtra("semester", semester);
        intent.putExtra("course", course);
        startActivity(intent);
    }
}
