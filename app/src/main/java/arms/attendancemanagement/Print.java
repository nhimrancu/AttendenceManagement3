package arms.attendancemanagement;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import arms.attendancemanagement.api.Manager;


public class Print extends AppCompatActivity {

    private String html; // to show the info
    String semesterString, courseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendances);
        setTitle(" Attendance Info ");


//        html = Manager.getAllAttendance(this, getIntent().getStringExtra("semester"));

        semesterString = getIntent().getStringExtra("semester");
        courseString = getIntent().getStringExtra("course");

        if(courseString == null){
            html = Manager.getAverageAttendance(this, getIntent().getStringExtra("semester"));
        }
        else{
            html = Manager.getAttendanceCounts(this, getIntent().getStringExtra("semester"), getIntent().getStringExtra("course"));
        }

        ((WebView) findViewById(R.id.attendanceWebView)).loadData(html, "text/html", "UTF-8");

    }

    // exporting data to as html file
    public void exportHtml(View view) {
        try {
            String out = Environment.getExternalStoragePublicDirectory("Exported-Data") + File.separator +
                    getIntent().getStringExtra("semester") + "-" + getIntent().getStringExtra("course") + ".html";

            Environment.getExternalStoragePublicDirectory("Exported-Data").mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            writer.write(html);
            writer.close();
            Utility.showMessage(this, "HTML exported in " + out);
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showMessage(this, "Error Exporting Data" + e.getMessage());
        }
    }

}
