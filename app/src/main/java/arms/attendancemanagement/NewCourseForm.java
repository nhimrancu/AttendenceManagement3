package arms.attendancemanagement;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import arms.attendancemanagement.api.Course;
import arms.attendancemanagement.api.Manager;

public class NewCourseForm extends AppCompatActivity {

    EditText codeField, titleField, creditField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_layout);
        setTitle("Add New Course");

        codeField = (EditText) findViewById(R.id.courseCodeInputToConfirm);
        titleField = (EditText) findViewById(R.id.input_course_title);
        creditField = (EditText) findViewById(R.id.input_course_credit);
    }

    public void addNewCourse(View view) {

        String code = codeField.getText().toString();
        String title = titleField.getText().toString();

        //empty check course table data

        if (code.matches("^\\s*$") || title.matches("^\\s*$") || creditField.getText().toString().matches("^\\s*$")) {

            Utility.showMessage(this, "Please fill up all the fields.");

            return;
        }

        int credits = Integer.parseInt(creditField.getText().toString());

        Course course = new Course();
        course.code = code;
        course.title = title;
        course.credits = credits;

        try {
            Manager.createCourse(this, course);

            Toast.makeText(this, "Course Added Successfully", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {

            Utility.showMessage(this, "This Course already exists !!");
        }
    }
}
