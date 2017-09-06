package arms.attendancemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
    }

    public void LoginSubmit(View v) {
        String username = ((EditText) findViewById(R.id.LoginUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.LoginPassword)).getText().toString();

        SharedPreferences sp = getSharedPreferences("signup", MODE_PRIVATE);

        String realuser = sp.getString("username", null);
        String realpass = sp.getString("password", null);

        if (username.equals(realuser) && password.equals(realpass)) {
            // login ok
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Username or Password Incorrect", Toast.LENGTH_LONG).show();
        }
    }
}
