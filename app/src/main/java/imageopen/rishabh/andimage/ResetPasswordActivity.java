package imageopen.rishabh.andimage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    public static final String TAG=ResetPasswordActivity.class.getSimpleName();

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Log.i(TAG, "onCreate() method fired ");

        getSupportActionBar().setTitle("Reset Password");

        // init() method for Initialization
        init();


    }

    private void init() {
        Log.i(TAG, "init() method fired ");

        inputEmail = (EditText) findViewById(R.id.email_reset);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick() method fired, on Reset Button ");

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG, "onComplete() method fired, authenticate Data sending");
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete() method fired, Authentication is Successful, Email sent please follow the instructions to reset password.");
                    Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onComplete() method fired, login error");
                    Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email! Please try again", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }


} // class ends here
