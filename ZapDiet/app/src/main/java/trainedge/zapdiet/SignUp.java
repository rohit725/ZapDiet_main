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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignUp extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "SignUp";
    private EditText password;
    private EditText email;
    private EditText name;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        Button btn = (Button) findViewById(R.id.create_account);
        TextView txtview = (TextView) findViewById(R.id.link_login);

        btn.setOnClickListener(this);
        txtview.setOnClickListener(this);
        name.addTextChangedListener(GenericTextWatcher);
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
            else if(name.getText().hashCode() == s.hashCode()){
                name_onTextChanged(s);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}

    };

    private void name_onTextChanged(CharSequence s) {
            String names = s.toString();
            if (names.isEmpty() && names.length() < 3) {
                name.setError("Recquired (Minimum 3 characters)");
            }
    }

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
        final String names = name.getText().toString();
        final String emails = email.getText().toString();
        String passwords = password.getText().toString();
        Log.d(TAG, "Create Account:" + emails);
        if(names.isEmpty()){
            name.setError("Please enter your name.");
            return;
        }
        if(!emails.contains("@") || emails.length() <10 || emails.isEmpty()){
            email.setError("Enter a valid email address.");
            return;
        }
        if(passwords.length() < 8){
            password.setError("Password must be at least 8 characters");
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
                        String uid = task.getResult().getUser().getUid();
                        HashMap<String, String> userMap=new HashMap<>();
                        userMap.put("email",emails);
                        userMap.put("name",names);
                        FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(userMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError==null){
                                    progressDialog.dismiss();
                                }
                            }
                        });

                    }
                });
        finish();
    }
}
