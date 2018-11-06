package org.openalpr.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Bind;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    Context CTX = this;
    String MAIL = "";
    String firstname="";
    String usermail, userpass;

    @Bind(R.id.input_email) EditText USERMAIL;
    @Bind(R.id.input_password) EditText USERPASS;

    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        USERMAIL = (EditText) findViewById(R.id.input_email);
        USERPASS = (EditText) findViewById(R.id.input_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        // TODO: Implement your own authentication logic here.

        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        usermail = USERMAIL.getText().toString();
        userpass = USERPASS.getText().toString();

        DatabaseOperations DOP = new DatabaseOperations(CTX);
        Cursor CR = DOP.getInfo(DOP);

        boolean login_status = false;

        if(CR.moveToFirst()){

        do{
            if(usermail.equals(CR.getString(0)) && userpass.equals(CR.getString(1)) ){
                login_status = true;
                firstname = CR.getString(2);
            }
        }while(CR.moveToNext());

            if (login_status) {
                onLoginSuccess();

            }else{
                onLoginFailed();
            }

        }
        progressDialog.dismiss();

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                    }
                }, 3000);*/
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                if (!login_status) {
                    onLoginFailed();
                    return;
                }else{
                    onLoginSuccess();
                }

                this.finish();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

            Toast.makeText(getBaseContext(), "Login successful !\nWelcome "+firstname, Toast.LENGTH_LONG).show();
            _loginButton.setEnabled(true);
            final Intent mainIntent = new Intent(this, MainActivity.class);
            LoginActivity.this.startActivity(mainIntent);
            finish();

    }

    public void onLoginFailed() {

        Toast.makeText(getBaseContext(), "Login failed... Try again!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String email = USERMAIL.getText().toString();
        String password = USERPASS.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            USERMAIL.setError("enter a valid email address");
            valid = false;
        } else {
            USERMAIL.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            USERPASS.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            USERPASS.setError(null);
        }

        return valid;
    }
}
