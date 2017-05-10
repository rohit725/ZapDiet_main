package trainedge.zapdiet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class Splash extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        pref = getSharedPreferences("setting_pref", MODE_PRIVATE);
        final TextView zap = (TextView) findViewById(R.id.Zap);
        final Animation ai = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.view1);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        ai.setInterpolator(interpolator);
        setSupportActionBar(toolbar);
        Thread mythread =new Thread(){
                        @Override
                        public void run(){
                            try {
                                zap.startAnimation(ai);
                                sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                                Intent intent = new Intent(Splash.this,login.class);
                                startActivity(intent);
                                finish();
                            }
            }
        };
        shownotification();
        mythread.start();
    }
    public void shownotification() {
        if(pref.getBoolean("notif_option",true)){
            Intent notifyIntent = new Intent(Splash.this,MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (Splash.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
                    1000 * 60 * 60 * 24, pendingIntent);
        }
    }
}
