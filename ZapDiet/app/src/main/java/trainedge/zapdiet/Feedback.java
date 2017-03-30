package trainedge.zapdiet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class Feedback extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ImageButton rateobj1,rateobj2,rateobj3,rateobj4,rateobj5;
    private EditText usermess,uremail;
    private Button subm;
    int rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rateobj1 = (ImageButton) findViewById(R.id.rate1);
        rateobj2 = (ImageButton) findViewById(R.id.rate2);
        rateobj3 = (ImageButton) findViewById(R.id.rate3);
        rateobj4 = (ImageButton) findViewById(R.id.rate4);
        rateobj5 = (ImageButton) findViewById(R.id.rate5);
        subm = (Button) findViewById(R.id.submit);
        usermess = (EditText) findViewById(R.id.user_mess);
        uremail = (EditText) findViewById(R.id.user_email);

        rateobj1.setOnClickListener(this);
        rateobj2.setOnClickListener(this);
        rateobj3.setOnClickListener(this);
        rateobj4.setOnClickListener(this);
        rateobj5.setOnClickListener(this);
        subm.setOnClickListener(this);
        uremail.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rate1){
            rating = 1;
            Toast.makeText(this, "Strongly Hated", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.rate2){
            rating = 2;
            Toast.makeText(this, "Hated it", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.rate3){
            rating = 3;
            Toast.makeText(this, "Normal", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.rate4){
            rating = 4;
            Toast.makeText(this, "Liked it", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.rate5){
            rating = 5;
            Toast.makeText(this, "Loved it", Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.submit){
            Intent emailint = new Intent(Intent.ACTION_SEND);
            emailint.setType("text/html");
            emailint.putExtra(Intent.EXTRA_EMAIL,new String[]{"rpandey516@gmail.com"});
            emailint.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
            emailint.putExtra(Intent.EXTRA_TEXT,"Hi,\n \t You have got a feedback e-mail.Your app has been rated "+rating+" star by the user. And user's thought about the app are - \""+usermess.getText().toString()+"\". To write him back please use the email \""+uremail.getText().toString()+"\". \n\tHave a nice day. \n\tThank You.");
            startActivity(Intent.createChooser(emailint,"Send Feedback"));
            Toast.makeText(this, "Thanks for the Feedback", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String email = s.toString();
        if (email.isEmpty() && email.length() < 10 && !email.contains("@")) {
            uremail.setError("Please give a correct email address.");
        }

    }
}
