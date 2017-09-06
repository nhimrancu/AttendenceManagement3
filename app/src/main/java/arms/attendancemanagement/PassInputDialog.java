package arms.attendancemanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class PassInputDialog extends DialogFragment {
    private EditText passField;
    public PassListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pass_input, container, false);
        passField = (EditText) view.findViewById(R.id.passwordField);
        getDialog().setTitle("Enter your password");
        resetField();
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onSubmit(PassInputDialog.this, passField.getText().toString());
                else getDialog().dismiss();
            }
        });
        setCancelable(false);
        return view;
    }

    public void resetField() {
        if (passField != null) passField.setText(null);
    }

    public interface PassListener {
        void onSubmit(DialogFragment dialogFragment, String password);
    }
}
