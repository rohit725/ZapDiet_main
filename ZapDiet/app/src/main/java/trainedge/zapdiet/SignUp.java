package trainedge.zapdiet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "SignUp";
    private EditText password;
    private EditText email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirm_pass = (EditText) findViewById(R.id.confirm);
        Button btn = (Button) findViewById(R.id.create_account);
        TextView txtview = (TextView) findViewById(R.id.link_login);

        btn.setOnClickListener(this);
        txtview.setOnClickListener(this);
        email.addTextChangedListener(GenericTextWatcher);
        password.addTextChangedListener(GenericTextWatcher);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        }; 
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private TextWatcher GenericTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            if (email.getText().hashCode() == s.hashCode())
            {
                email_onTextChanged(s);
            }
            else if (password.getText().hashCode() == s.hashCode())
            {
                pass_onTextChanged(s);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}

    };

    private void email_onTextChanged(CharSequence s) {
        String emails = s.toString();
        if (emails.length() < 10) {
            email.setError("Recquired (Minimum 10 characters)");
        }
    }

    private void pass_onTextChanged(CharSequence s){
        String passwords = s.toString();
        if (passwords.length() < 8) {
            password.setError("Recquired (Minimum 8 characters)");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.link_login){
            finish();
        }
        if(v.getId() == R.id.create_account){
            createacc();
        }

    }

    private void createacc(){
        String conf = confirm_pass.getText().toString();
        String emails = email.getText().toString();
        String passwords = password.getText().toString();
        Log.d(TAG, "Create Account:" + emails);
        if(!emails.contains("@") || emails.length() <10 || emails.isEmpty()){
            email.setError("Enter a valid email address.");
            return;
        }
        if(passwords.length() < 8){
            password.setError("Password must be at least 8 characters");
            return;
        }
        if(!passwords.equals(conf)){
            confirm_pass.setError("Password didn't match");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Account Creation Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
        finish();
    }

}
