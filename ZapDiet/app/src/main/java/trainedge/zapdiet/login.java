package trainedge.zapdiet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;


public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static final int RC_SIGN_IN = 7283;
    public static final String TAG = "login";
    private Button GoogleSignIn;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private EditText email;
    private EditText pass;
    static int flag = 1;
    private ImageView show_hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        View scroll =  findViewById(R.id.scrollview);
        ImageView noconn = (ImageView) findViewById(R.id.noConnection);
        Button retr = (Button) findViewById(R.id.retry);

        CheckConnection c = new CheckConnection(scroll, noconn, retr);
        c.checkconn();

        GoogleSignIn = (Button) findViewById(R.id.GoogleSignIn);
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent homeIntent = new Intent(login.this, home.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        GoogleSignIn.setOnClickListener(this);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.FacebookSignIn);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(login.this, "Login canceled through facebook", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
        TextView signup = (TextView) findViewById(R.id.SignUp);
        show_hide = (ImageView) findViewById(R.id.show_password);
        Button sigin = (Button) findViewById(R.id.SignIn);
        email = (EditText) findViewById(R.id.Email1);
        pass = (EditText) findViewById(R.id.Password);
        TextView fpass = (TextView) findViewById(R.id.forgot);
        signup.setOnClickListener(this);
        sigin.setOnClickListener(this);
        fpass.setOnClickListener(this);
        show_hide.setOnClickListener(this);
        email.addTextChangedListener(GenericTextWatcher);
        pass.addTextChangedListener(GenericTextWatcher);


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(login.this, "Signin through google failed", Toast.LENGTH_LONG).show();
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(GoogleSignIn, "Not Connected To Internet", BaseTransientBottomBar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.GoogleSignIn:
                signIn();
                break;
            case R.id.SignUp:
                Intent signupintent = new Intent(login.this, SignUp.class);
                startActivity(signupintent);
                break;
            case R.id.SignIn:
                mysignin();
                break;
            case R.id.forgot:
                forgot();
                break;
            case R.id.show_password:
                switch (flag) {
                    case 1:
                        pass.setTransformationMethod(null);
                        show_hide.setImageResource(R.drawable.show_password);
                        flag = 2;
                        break;

                    case 2:
                        pass.setTransformationMethod(new PasswordTransformationMethod());
                        show_hide.setImageResource(R.drawable.hide_password);
                        flag = 1;
                        break;
                }
                break;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private TextWatcher GenericTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            if (email.getText().hashCode() == s.hashCode()) {
                email_onTextChanged(s);
            } else if (pass.getText().hashCode() == s.hashCode()) {
                pass_onTextChanged(s);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    private void email_onTextChanged(CharSequence s) {
        String string = s.toString();
        if (string.length() < 10) {
            email.setError("Recquired (10 characters minimum)");
        }
    }

    private void pass_onTextChanged(CharSequence s) {
        String string = s.toString();
        if (string.length() < 8) {
            pass.setError("Recquired (8 characters minimum.)");
        }
    }

    private void forgot() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        et.setLayoutParams(lp);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(et);
        alertDialogBuilder.setIcon(R.drawable.ic_vpn_key_black_24dp);
        alertDialogBuilder.setTitle("Forgot Password!");
        alertDialogBuilder.setMessage("Please enter your authorized email address.");
        // set dialog message
        alertDialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String emailAddress = et.getText().toString();
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(login.this, "Email sent please check.", Toast.LENGTH_LONG).show();
                                }
                                if (!task.isSuccessful()) {
                                    Log.d(TAG, "Error Unauthorized email.");
                                    Toast.makeText(login.this, "Invalid Email Address.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void mysignin() {
        String emai = email.getText().toString();
        String pasw = pass.getText().toString();
        if (emai.length() < 10 || !emai.contains("@") || emai.isEmpty()) {
            email.setError("Enter a valid email");
            return;
        }
        if (pasw.length() < 8) {
            pass.setError("Password must be at least 8 characters");
            return;
        }
        Log.d(TAG, "signIn:" + emai);
        final ProgressDialog progressDialog = new ProgressDialog(login.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(emai, pasw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(login.this, "Authentication Failed...",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent homeint = new Intent(login.this, home.class);
                            startActivity(homeint);
                        }

                    }
                });
    }

}
