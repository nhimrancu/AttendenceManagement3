package arms.attendancemanagement.api;


import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Student {
    public int id, semester;
    public String name,password;


    @Override
    public String toString() {
        return id+ " - " + name;
    }

}
