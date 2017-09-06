package arms.attendancemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharedPreferences = getSharedPreferences("signup", MODE_PRIVATE);

        boolean data_available = sharedPreferences.getBoolean("available", false);

        setTitle(null);

        if (data_available) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

    public void SignupSubmit(View v) {
        String name = ((EditText) findViewById(R.id.SignupName)).getText().toString();
        String title = ((EditText) findViewById(R.id.SignupDesignation)).getText().toString();
        String contact = ((EditText) findViewById(R.id.SignupContact)).getText().toString();
        String user = ((EditText) findViewById(R.id.SignupUsername)).getText().toString();
        String pass = ((EditText) findViewById(R.id.SignupPassword)).getText().toString();

        // TODO:Test validity
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please Enter both username & password", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("designation", title);
        editor.putString("contact", contact);
        editor.putString("username", user);
        editor.putString("password", pass);
        editor.putBoolean("available", true);
        editor.apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
