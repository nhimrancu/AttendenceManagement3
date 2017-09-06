package arms.attendancemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import arms.attendancemanagement.api.Attendance;
import arms.attendancemanagement.api.Manager;
import arms.attendancemanagement.api.Student;

public class ActivityTakeAttendances extends AppCompatActivity {
    private static Attendance attendance, submittable;
    private static ArrayAdapter<Student> adapter;
    ArrayList<Student> students;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static String attendanceID = "0";
    private static int std_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        attendance = new Attendance();
        attendance.lecture_date = intent.getStringExtra("lecture_date");
        attendance.lecture_count = intent.getIntExtra("lecture_count", 0);
        attendance.course = Manager.getCourse(this, intent.getStringExtra("course"));
        attendance.semester = intent.getIntExtra("semester", 0);

        setContentView(R.layout.activity_take_attendances);

        students = Manager.getStudentsBySemester(this, attendance.semester);
        final ListView listView = (ListView) findViewById(R.id.student_id_list);
        adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, students);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Student student = students.get(position);

                PassInputDialog dialog = new PassInputDialog();

                dialog.listener = new PassInputDialog.PassListener() {
                    @Override
                    public void onSubmit(DialogFragment dialogFragment, String password) {
                        if (!student.password.equals(password)) {
                            new AlertDialog.Builder(ActivityTakeAttendances.this).setMessage("Password Incorrect").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        } else {
                            dialogFragment.dismiss();

                            submittable = new Attendance();
                            submittable.course = attendance.course;
                            submittable.semester = attendance.semester;
                            submittable.lecture_count = attendance.lecture_count;
                            submittable.lecture_date = attendance.lecture_date;
                            submittable.id = System.currentTimeMillis();
                            attendanceID = submittable.id + "";
                            submittable.student = student;

                            std_position = position;

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }
                };

                dialog.show(getSupportFragmentManager(), "password");
                dialog.resetField();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            new AlertDialog.Builder(this).setMessage("Are you sure to submit attendance for ID : " + submittable.student.id)
                    .setNeutralButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(getExternalFilesDir("AttendencePhotos") + File.separator + attendanceID + ".png");
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Manager.createAttendance(ActivityTakeAttendances.this, submittable);
                        students.remove(std_position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ActivityTakeAttendances.this, "Attendance Submitted Successfully", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ActivityTakeAttendances.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            }).create().show();
        }
    }
}
