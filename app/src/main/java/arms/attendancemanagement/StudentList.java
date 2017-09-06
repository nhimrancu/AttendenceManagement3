package arms.attendancemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
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
import arms.attendancemanagement.api.StudentAdapter;


public class StudentList extends AppCompatActivity {

    ListView listView;
    ArrayList<Student> students;

    StudentAdapter customAdapter;

    private StudentInputDialog studentInputDialog; // input neyar jonno fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list_layout);
        setTitle("Students");

        listView = (ListView) findViewById(R.id.studentList); // student er list dekhabe
        students = Manager.getAllStudents(this);    // database theke list nicche

        customAdapter = new StudentAdapter(this, R.layout.student_sample, students);
        listView.setAdapter(customAdapter);

        // for context menu
        registerForContextMenu(listView);


        studentInputDialog = new StudentInputDialog();

        studentInputDialog.studentSubmitListener = new StudentInputDialog.StudentSubmitListener() {
            @Override
            public void studentSubmit(Student student, Bitmap image) {
                try {
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(getExternalFilesDir("Avatars") + File.separator +
                                student.id + ".png");
                        image.compress(Bitmap.CompressFormat.PNG, 100, out);
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

                    takeAction(student);

                } catch (SQLException e) {
                    Utility.showMessage(StudentList.this, e.getMessage());
                }
            }
        };
        //-------------


        findViewById(R.id.addStudentBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentInputDialog.title = "Add new student";
                studentInputDialog.show(getFragmentManager(), "add");
            }
        });

    }// ---------- end of onCreate

    private void takeAction(Student student) {
        String msg;

        Manager.createStudent(this, student);
        students.add(student);

        msg = "Addition Successful";
        customAdapter.notifyDataSetChanged();

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        return super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = info.position;

        SharedPreferences sp = getSharedPreferences("signup", MODE_PRIVATE);
        final String realpass = sp.getString("password", null);

        if(item.getTitle() == "Edit"){
////            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
////            password check
//            PassInputDialog dialog = new PassInputDialog();
//
//            dialog.listener = new PassInputDialog.PassListener() {
//                @Override
//                public void onSubmit(DialogFragment dialogFragment, String password) {
//                    if (!realpass.equals(password)) {
////                        new AlertDialog.Builder(StudentList.this).setMessage("Password Incorrect").setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                dialog.dismiss();
////                            }
////                        }).create().show();
//                        Utility.showMessage(StudentList.this, "Incorrect Password");
//                    } else {
//                        dialogFragment.dismiss();
//                    }
//                }
//            };
//
//            dialog.show(getSupportFragmentManager(), "password");
//            dialog.resetField();
////            password check

        }
        else if(item.getTitle() == "Delete"){
//            Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
            PassInputDialog dialog = new PassInputDialog();

            dialog.listener = new PassInputDialog.PassListener() {
                @Override
                public void onSubmit(DialogFragment dialogFragment, String password) {
                    if (!realpass.equals(password)) {
                        Utility.showMessage(StudentList.this, "Incorrect Password");
                    } else {
                        // delete
                        Student stdnt = customAdapter.getItem(pos);
                        Manager.deleteStudent(StudentList.this, stdnt.id);
                        students.remove(pos);
                        customAdapter.notifyDataSetChanged();

                        dialogFragment.dismiss();
                        Toast.makeText(StudentList.this, "Deleted Successfully!",Toast.LENGTH_SHORT).show();
                    }
                }
            };

            dialog.show(getSupportFragmentManager(), "password");
            dialog.resetField();
        }
        else{
            return false;
        }

        return  true;

    }
}
