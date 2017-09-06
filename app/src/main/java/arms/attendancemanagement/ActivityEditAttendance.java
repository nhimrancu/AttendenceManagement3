package arms.attendancemanagement;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import arms.attendancemanagement.api.Attendance;
import arms.attendancemanagement.api.Course;
import arms.attendancemanagement.api.Manager;

public class ActivityEditAttendance extends AppCompatActivity {
    private static String date;
    private EditText semesterField, lectureField, idField;
    private TextView lectureCount;
    private Spinner courseField;
    //private static Button lec_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance);
        setTitle("Edit Attendance");
        courseField = (Spinner) findViewById(R.id.courseCodeInputToConfirm);
        semesterField = (EditText) findViewById(R.id.semesterInputToConfirm);
        idField = (EditText) findViewById(R.id.student_id);
        lectureField = (EditText) findViewById(R.id.lecture_count);

        lectureCount = (TextView) findViewById(R.id.token_lectures_count);

        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, Manager.getCourses(this));
        courseField.setAdapter(adapter);
        courseField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showLectureCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        semesterField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showLectureCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void takeAttendances(View view) {
        // TODO:Password Check
        String course_code = courseField.getSelectedItem().toString();

        if (course_code == null || date == null || semesterField.getText().toString().matches("^\\s*$") ||
                idField.getText().toString().matches("^\\s*$") ||
                lectureField.getText().toString().matches("^\\s*$")) {
            Utility.showMessage(this, "Please enter all fields.");
            return;
        }
        Attendance attendance = new Attendance();
        attendance.student = Manager.getStudentById(this, Integer.parseInt(idField.getText().toString()));
        attendance.semester = Integer.parseInt(semesterField.getText().toString());
        attendance.lecture_count = Integer.parseInt(lectureField.getText().toString());
        attendance.lecture_date = date;
        attendance.id = System.currentTimeMillis();
        attendance.course = (Course) courseField.getSelectedItem();
        Manager.createOrUpdateAttendance(this, attendance);
        Toast.makeText(this, "Operation Successful.", Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.dateShower = new DateShower() {
            @Override
            public void showDate(String date) {
                ActivityEditAttendance.date = date;
                ((Button) findViewById(R.id.input_lecture_date)).setText(date);
                showLectureCount();
            }
        };

        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public DateShower dateShower;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            if (date != null) {
                String[] field = date.split("-");
                day = Integer.parseInt(field[2]);
                month = Integer.parseInt(field[1]) - 1;
                year = Integer.parseInt(field[0]);
            }
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            date = year + "-" + (month + 1) + "-" + day;
            if (dateShower != null) dateShower.showDate(date);
        }
    }

    public interface DateShower {
        void showDate(String date);
    }

    private void showLectureCount() {
        try {
            int count = Manager.getMaxAttendance(ActivityEditAttendance.this,
                    semesterField.getText().toString(), courseField.getSelectedItem().toString(), date);
            lectureCount.setText(String.valueOf("Lec : " + count));
        } catch (Exception e) {
            //NOoooooooooooooooooo
        }
    }
}
