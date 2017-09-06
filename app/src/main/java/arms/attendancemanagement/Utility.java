package arms.attendancemanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import arms.attendancemanagement.api.Manager;


public class Utility {
    public static void showMessage(Context context, String msg) {
        (new AlertDialog.Builder(context).setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })).create().show();
    }

    public static boolean emptyCheck(Context context){
        String msg;
        ArrayList arlist;


        arlist = Manager.getAllStudents(context);
        if(arlist.isEmpty()){
            Utility.showMessage(context, "No Student Exists !");
            return true;
        }

        arlist = Manager.getCourses(context);
        if(arlist.isEmpty()){
            Utility.showMessage(context, "No Course Exists !");
            return true;
        }
        return false;
    }
}
