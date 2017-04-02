package trainedge.zapdiet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignUp extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    private Button btn;
    private TextView txtview;
    private EditText password;
    private EditText email;
    private EditText name;
    private FirebaseDatabase db;
    private DatabaseReference usersref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        btn = (Button) findViewById(R.id.create_account);
        txtview = (TextView) findViewById(R.id.link_login);

        db = FirebaseDatabase.getInstance();
        usersref = db.getReference("Users");

        btn.setOnClickListener(this);
        txtview.setOnClickListener(this);
        name.addTextChangedListener(this);
        email.addTextChangedListener(this);
        //password.addTextChangedListener(this);        implement it later.


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s == name.getEditableText()){
            String names = name.getText().toString();
            if (names.isEmpty() && names.length() < 3) {
                name.setError("please enter your name correctly");
            }
        }
        else if(s == email.getEditableText()){
            String emails = email.getText().toString();
            if (emails.isEmpty() && emails.length() < 10 && !emails.contains("@")) {
                email.setError("please enter a valid email");
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.link_login){
            finish();
        }
        else if(v.getId() == R.id.create_account){

            String names = name.getText().toString();
            String emails = email.getText().toString();
            String passwords = password.getText().toString();
            if(names.isEmpty()){
                Snackbar.make(v, "Please Enter Your Name First!", Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            if(emails.isEmpty()){
                Snackbar.make(v, "Please Enter Your Email First!", Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            if(passwords.length() < 8){
                Snackbar.make(v, "Password must be at least 8 characters.", Snackbar.LENGTH_INDEFINITE).show();
                return;
            }

            HashMap<String,String> map = new HashMap<>();
            map.put("Name",names);
            map.put("Email",emails);
            map.put("Password",passwords);
            usersref.push().setValue(map);

            finish();
        }

    }
}
