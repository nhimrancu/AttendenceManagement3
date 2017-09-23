package arms.attendancemanagement;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.FragmentManager;
import android.widget.Toast;

import java.util.ArrayList;

import arms.attendancemanagement.api.Course;
import arms.attendancemanagement.api.CourseAdapter;
import arms.attendancemanagement.api.Manager;
import arms.attendancemanagement.api.Student;

public class CourseList extends AppCompatActivity {

    CourseAdapter courseAdapter;
    ListView courseListView;


    ArrayList<Course> courses;

    private CourseInputDialog courseInputDialog; // input deyar jonno

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list_layout);
        setTitle("Courses");

        courseListView = (ListView) findViewById(R.id.courseList);

        courses = Manager.getCourses(this);

        courseAdapter = new CourseAdapter(this, R.layout.student_sample, courses);
        courseListView.setAdapter(courseAdapter);


        registerForContextMenu(courseListView); // for context menu

        courseInputDialog = new CourseInputDialog();

        courseInputDialog.courseSubmitListener = new CourseInputDialog.CourseSubmitListener(){
            @Override
            public void courseSubmit(Course course){
                 courseTakeAction(course);
            }
        };
        //

        //-----------------------------------------------
        findViewById(R.id.courseAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseInputDialog.courseInputDialogTitle = "Add new Course";
                //courseInputDialog.show(getFragmentManager(), "addCourse");
                courseInputDialog.show(getFragmentManager(), "add");
            }
        });
        //----------------------------------------------

    } // end of onCreate method


    public void courseTakeAction(Course course){
        String msg;

        Manager.createCourse(this, course);
        courses.add(course);
        msg = "Addition Successful";
        courseAdapter.notifyDataSetChanged();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // works when context menu is created
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    // works when context menu item selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        return super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = info.position; // position in the list

        // getting user password
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
                        Utility.showMessage(CourseList.this, "Incorrect Password");
                    } else {
                        // delete
                        Course course = courseAdapter.getItem(pos);
                        Manager.deleteCourse(CourseList.this, course.code);
                        courses.remove(pos);
                        courseAdapter.notifyDataSetChanged();
                        dialogFragment.dismiss();
                        Toast.makeText(CourseList.this, "Deleted Successfully!",Toast.LENGTH_SHORT).show();
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
