package arms.attendancemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(" Home ");
    }


// to display students list
    public void showStudentsList(View view) {
        startActivity(new Intent(this, StudentList.class));
    }

// to display courses list
    public void showCoursesList(View view) {
        startActivity(new Intent(this, CourseList.class));
    }


    public void openAttendanceForm(View view) {

        if(Utility.emptyCheck(this))return;     // empty check student or course table
        startActivity(new Intent(this, ActivityAttendanceOptions.class));
    }


    public void openConfirmAttendanceOptions(View view) {
        if(Utility.emptyCheck(this))return;
        startActivity(new Intent(this, ActivityConfirmOptions.class));
    }

//    view attendance info
    public void showInfo(View view) {
        if(Utility.emptyCheck(this))return;
        startActivity(new Intent(this, ViewAttendanceOptions.class));
    }

    public void openEditForm(View view) {
        if(Utility.emptyCheck(this))return;
        startActivity(new Intent(this, ActivityEditAttendance.class));
    }
}
