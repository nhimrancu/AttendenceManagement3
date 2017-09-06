package arms.attendancemanagement.api;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import arms.attendancemanagement.R;

public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private ListView list;

    public MyGestureDetector(ListView list) {
        this.list = list;
    }

    //CONDITIONS ARE TYPICALLY VELOCITY OR DISTANCE
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (INSERT_CONDITIONS_HERE)
            if (showDeleteButton(e1))
                return true;
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    private boolean showDeleteButton(MotionEvent e1) {
        int pos = list.pointToPosition((int)e1.getX(), (int)e1.getY());
        return showDeleteButton(pos);
    }

    private boolean showDeleteButton(int pos) {
        View child = list.getChildAt(pos);
        if (child != null){
            Button delete = (Button) child.findViewById(R.id.delete_button);
            if (delete != null)
                if (delete.getVisibility() == View.INVISIBLE)
                    delete.setVisibility(View.VISIBLE);
                else
                    delete.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }
}