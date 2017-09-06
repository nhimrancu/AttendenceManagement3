package arms.attendancemanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import arms.attendancemanagement.api.Attendance;
import arms.attendancemanagement.api.Manager;


public class ActivityConfirmAttendance extends AppCompatActivity {
    private String semester, course, date;
    private ArrayList<Attendance> attendances;
    private boolean[] selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_attendance);

        semester = getIntent().getStringExtra("semester");
        course = getIntent().getStringExtra("course");
        date = getIntent().getStringExtra("date");

        attendances = Manager.getAttendancesByInfo(this, semester, course, date);
        setTitle("Total : " + attendances.size());
        selected = new boolean[attendances.size()];

        ImageAdapter adapter = new ImageAdapter();
        ((ListView) findViewById(R.id.imageList)).setAdapter(adapter);
    }

    public void confirmUnselected(View view) {
        for (int i = 0; i < attendances.size(); i++) {
            if (!selected[i])
                Manager.confirmAttendance(this, attendances.get(i).id);
            else Manager.deleteAttendance(this, attendances.get(i).id);
            new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + attendances.get(i).id + ".png").delete();
        }
        finish();
    }


    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return attendances.size();
        }

        public Object getItem(int position) {
            return attendances.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.sample_image, null);
                holder.image1 = (ImageView) convertView.findViewById(R.id.first_image);
                holder.image2 = (ImageView) convertView.findViewById(R.id.second_image);
                holder.textView = (TextView) convertView.findViewById(R.id.student_id);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image2.setId(position);
            holder.image1.setId(position);

            convertView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (selected[position]) {
                        selected[position] = false;
                        v.setBackgroundColor(Color.WHITE);
                    } else {
                        selected[position] = true;
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                }
            });

            holder.image1.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir("Avatars") +
                    File.separator + attendances.get(position).student.id + ".png"));

            Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir("AttendencePhotos") +
                    File.separator + attendances.get(position).id + ".png");

            holder.image2.setImageBitmap(bitmap);

            holder.textView.setText(String.valueOf(attendances.get(position).student.id));
            holder.id = position;
            return convertView;
        }
    }


    class ViewHolder {
        ImageView image1, image2;
        TextView textView;
        int id;
    }
}
