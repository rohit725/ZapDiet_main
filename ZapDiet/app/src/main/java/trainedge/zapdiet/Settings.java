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

public class Settings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {

    private Button acc_detail;
    private Button priv_pol;
    private Button share;
    private Button about;
    private Button logout;
    private Switch notif;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getSharedPreferences("setting_pref", MODE_PRIVATE);

        acc_detail = (Button) findViewById(R.id.acc_detail);
        priv_pol = (Button) findViewById(R.id.priv_pol);
        share = (Button) findViewById(R.id.share);
        about = (Button) findViewById(R.id.about);
        logout = (Button) findViewById(R.id.logout);
        notif = (Switch) findViewById(R.id.notif);

        notif.setOnCheckedChangeListener(this);
        updateUI();


        /*SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("is_logged_in",true);
        editor.apply();*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.about) {
            Intent about = new Intent(Settings.this, About.class);
            startActivity(about);
        }
        if (v.getId() == R.id.acc_detail) {
            Intent acc_detail = new Intent(Settings.this, acc_details.class);
            startActivity(acc_detail);
        }
        if (v.getId() == R.id.priv_pol) {
            Intent priv_pol = new Intent(Settings.this,priv_pol.class);
            startActivity(priv_pol);
        }
        /*if (v.getId() == R.id.share) {

        }
        if (v.getId() == R.id.logout) {

        }*/
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
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


