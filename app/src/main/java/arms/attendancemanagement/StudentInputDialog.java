package arms.attendancemanagement;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import arms.attendancemanagement.api.Student;

import static android.app.Activity.RESULT_OK;

public class StudentInputDialog extends DialogFragment {

    public StudentSubmitListener studentSubmitListener;

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap image;
    public String title;
    public Student editing;
    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_student_dialog_layout, container, false);

        populateStudent();

// ----------------------- capture photo --------------------------
        view.findViewById(R.id.photoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
//------------------------- capture photo --------------------------------


        view.findViewById(R.id.addStudentsubmitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //-----------------  take student's info -----------------
                EditText nameField = (EditText) view.findViewById(R.id.studentInputName),
                        idField = (EditText) view.findViewById(R.id.studentInputId),
                        passField = (EditText) view.findViewById(R.id.studentInputpass),
                        semesterField = (EditText) view.findViewById(R.id.studentInputsemester);
            // ----------------  take student's info --------------------

                // validate std info
                if (image == null || nameField.getText().toString().matches("^\\s*$") || passField.getText().toString().matches("^\\s*$") || idField.getText().toString().matches("^\\s*$") || semesterField.getText().toString().matches("^\\s*$")) {
                    Utility.showMessage(getActivity(), "Please fill up all the fields.");
                    return;
                }

                Student student = new Student();
                student.id = Integer.parseInt(idField.getText().toString());
                student.name = nameField.getText().toString();
                student.semester = Integer.parseInt(semesterField.getText().toString());
                student.password = passField.getText().toString();

                if (studentSubmitListener != null) studentSubmitListener.studentSubmit(student, image);

                // Reset fields
                populateStudent();
                getDialog().dismiss();
            }
        });

        view.findViewById(R.id.addStudentcancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        setCancelable(false);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            try {
                ((ImageView) view.findViewById(R.id.image)).setImageBitmap(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void populateStudent() {
        //if editing
        if (editing != null) {
            System.out.print(getActivity());
            image = BitmapFactory.decodeFile(getActivity().getExternalFilesDir("Avatars") + File.separator +
                    editing.id + ".png");
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setImageBitmap(image);
            ((EditText) view.findViewById(R.id.studentInputName)).setText(editing.name);
            ((EditText) view.findViewById(R.id.studentInputsemester)).setText(String.valueOf(editing.semester));
            ((EditText) view.findViewById(R.id.studentInputpass)).setText(editing.password);
            ((EditText) view.findViewById(R.id.studentInputId)).setText(String.valueOf(editing.id));
        } else {
            image = null;
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setImageBitmap(null);
            ((EditText) view.findViewById(R.id.studentInputName)).setText(null);
            ((EditText) view.findViewById(R.id.studentInputsemester)).setText(null);
            ((EditText) view.findViewById(R.id.studentInputpass)).setText(null);
            ((EditText) view.findViewById(R.id.studentInputId)).setText(null);
        }
        getDialog().setTitle(title);
    }

    public interface StudentSubmitListener {
        void studentSubmit(Student student, Bitmap image);
    }
}
