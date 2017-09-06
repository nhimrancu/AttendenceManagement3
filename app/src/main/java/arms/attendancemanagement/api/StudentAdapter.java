package arms.attendancemanagement.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import arms.attendancemanagement.R;

public class StudentAdapter extends ArrayAdapter<Student>{

    public StudentAdapter(Context context,int textviewResourceId){

        super(context, textviewResourceId);
    }

    public StudentAdapter(Context context, int resource, ArrayList<Student> students){

        super(context, resource, students);
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null){
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.student_sample, null);
        }

        Student stdnt = getItem(position);

        if(stdnt != null){

            TextView stdntId = (TextView) v.findViewById(R.id.studentIdSampleText);
            TextView stdntName = (TextView) v.findViewById(R.id.studentNameSample);

            if(stdntId != null){
                stdntId.setText(stdnt.id+"");
            }

            if(stdntName != null){
                stdntName.setText(stdnt.name);
            }
        }
        return v;
    }
}

