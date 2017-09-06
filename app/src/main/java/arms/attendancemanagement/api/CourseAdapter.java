package arms.attendancemanagement.api;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import arms.attendancemanagement.R;

public class CourseAdapter extends ArrayAdapter<Course>{

    public CourseAdapter(Context context, int textviewResourceId){

        super(context, textviewResourceId);
    }

    public CourseAdapter(Context context, int resource, ArrayList<Course> courses){

        super(context, resource, courses);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null){
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.student_sample, null);
        }

        Course course = getItem(position);

        if(course != null){

            TextView courseCode = (TextView) v.findViewById(R.id.studentIdSampleText);
            TextView courseTitle = (TextView) v.findViewById(R.id.studentNameSample);

            if(courseCode != null){
                courseCode.setText(course.code);
            }

            if(courseTitle != null){
                courseTitle.setText(course.title);
            }
        }

        return v;
    }
}
