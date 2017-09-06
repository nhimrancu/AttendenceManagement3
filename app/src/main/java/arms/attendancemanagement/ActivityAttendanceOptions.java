package arms.attendancemanagement;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Calendar;

import arms.attendancemanagement.api.Manager;

public class ActivityAttendanceOptions extends AppCompatActivity {
    private static String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.take_attendance_options_layout);

        setTitle("Take Attendance");
        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, Manager.getCourses(this));

        ((Spinner) findViewById(R.id.courseCodeInputToConfirm)).setAdapter(adapter);
    }

    public void takeAttendances(View view) {
        // TODO:Password Check
        String course_code = ((Spinner) findViewById(R.id.courseCodeInputToConfirm)).getSelectedItem().toString();
        EditText semesterField = (EditText) findViewById(R.id.semesterInputToConfirm);
        EditText lectureCountField = (EditText) findViewById(R.id.lecture_count);

        if (course_code == null || date == null || semesterField.getText().toString().matches("^\\s*$") ||
                lectureCountField.getText().toString().matches("^\\s*$")) {
            Utility.showMessage(this, "Please enter all fields.");
            return;
        }

        Intent intent = new Intent(this, ActivityTakeAttendances.class);
        intent.putExtra("semester", Integer.parseInt(semesterField.getText().toString()));
        intent.putExtra("lecture_count", Integer.parseInt(lectureCountField.getText().toString()));
        intent.putExtra("lecture_date", date);
        intent.putExtra("course", course_code);

        startActivity(intent);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.dateShower = new DateShower() {
            @Override
            public void showDate(String date) {
                ((Button) findViewById(R.id.input_lecture_date)).setText(date);
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
}
