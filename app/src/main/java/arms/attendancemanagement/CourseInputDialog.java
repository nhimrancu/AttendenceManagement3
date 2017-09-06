package arms.attendancemanagement;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import arms.attendancemanagement.api.Course;

public class CourseInputDialog extends DialogFragment {

    public CourseSubmitListener courseSubmitListener;
    public String courseInputDialogTitle;

    public View courseView;
    EditText courseCodeEditText, courseTitleEditText, courseCreditEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater courseLayoutInflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        courseView = courseLayoutInflater.inflate(R.layout.add_course_layout, container, false);
        populateCourse();

        courseCodeEditText = (EditText) courseView.findViewById(R.id.courseCodeInputToConfirm);
        courseTitleEditText = (EditText) courseView.findViewById(R.id.input_course_title);
        courseCreditEditText = (EditText) courseView.findViewById(R.id.input_course_credit);

        //----------------- action of the submit button of course input dialog ---------------------------------
        courseView.findViewById(R.id.addCourseSubmitButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //----------   check whether the fields are empty
                if(courseCodeEditText.getText().toString().matches("^\\s*$")
                        || courseTitleEditText.getText().toString().matches("^\\s*$")
                        || courseCreditEditText.getText().toString().matches("^\\s*$")) {

                    Utility.showMessage(getActivity(), "Please fill up all the fields");
                    return;
                }
                //----------   check whether the fields are empty

                //-------- Create a new Course object -------------
                Course newCourse = new Course();
                newCourse.code = courseCodeEditText.getText().toString();
                newCourse.title = courseTitleEditText.getText().toString();
                newCourse.credits = Integer.parseInt(courseCreditEditText.getText().toString());
                //-------- Create a new Course object -------------

                if(courseSubmitListener != null){
                    courseSubmitListener.courseSubmit(newCourse);
                }

                populateCourse();   // reset fields
                getDialog().dismiss();
            }
        });
        //----------------- action of the submit button of course input dialog ---------------------------------


        //------------- action of cancel button -----------------
        courseView.findViewById(R.id.addCourseCancelButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getDialog().dismiss();
            }
        });
        //------------- action of cancel button -----------------

        setCancelable(false);

        return courseView;

    }// end of onCreateView method

// populateCourse method
    public void populateCourse(){

        ((EditText)courseView.findViewById(R.id.courseCodeInputToConfirm)).setText(null);
        ((EditText)courseView.findViewById(R.id.input_course_title)).setText(null);
        ((EditText)courseView.findViewById(R.id.input_course_credit)).setText(null);

        getDialog().setTitle(courseInputDialogTitle);

    }


    // submitlistener interface
    public interface CourseSubmitListener {
        void courseSubmit(Course course);
    }
}
