package trainedge.zapdiet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {

    private Button accdet;
    private Button ppol;
    private Button shr;
    private Button abt;
    private Button lgt;
    private Switch notif;
    private SharedPreferences pref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getSharedPreferences("setting_pref", MODE_PRIVATE);

        accdet = (Button) findViewById(R.id.acc_detail);
        ppol = (Button) findViewById(R.id.priv_pol);
        shr = (Button) findViewById(R.id.share);
        abt = (Button) findViewById(R.id.about);
        lgt = (Button) findViewById(R.id.logout);
        notif = (Switch) findViewById(R.id.notif);
        mAuth = FirebaseAuth.getInstance();
        accdet.setOnClickListener(this);
        abt.setOnClickListener(this);
        ppol.setOnClickListener(this);
        notif.setOnCheckedChangeListener(this);

        updateUI();


        /*SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("is_logged_in",true);
        editor.apply();*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.about) {
            Intent aboutintent = new Intent(Settings.this, About.class);
            startActivity(aboutintent);
        }
        if (v.getId() == R.id.acc_detail) {
            Intent accintent = new Intent(Settings.this, acc_details.class);
            startActivity(accintent);
        }
        if (v.getId() == R.id.priv_pol) {
            Intent privintent = new Intent(Settings.this,priv_pol.class);
            startActivity(privintent);
        }
        /*if (v.getId() == R.id.share) {

        }*/
        if (v.getId() == R.id.logout) {
            mAuth.signOut();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = pref.edit();
        if(buttonView.getId() == R.id.notif){
            editor.putBoolean("notif_option",isChecked);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }
    private void updateUI() {
        boolean notifi = pref.getBoolean("notif_option",false);
        notif.setChecked(notifi);
    }

}


