package trainedge.zapdiet;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class acc_details extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Account Details";
    private TextView uname;
    private ImageView profile_pic;
    private TextView umail;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Profile");

        profile_pic = (ImageView) findViewById(R.id.user_profile_photo);
        uname = (TextView) findViewById(R.id.user_profile_name);
        umail = (TextView) findViewById(R.id.user_email);
        verify = (Button) findViewById(R.id.verifyacc);
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        updateui(user1);

        verify.setOnClickListener(this);

    }

    private void updateui(FirebaseUser user) {
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();
                // UID specific to the provider

                //String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                if(providerId.contains("google.com") || providerId.contains("facebook.com")){
                    verify.setVisibility(View.GONE);
                }
                else{
                    boolean emailVerified = user.isEmailVerified();
                    if(emailVerified){
                        verify.setVisibility(View.GONE);
                    }
                }

                    uname.setText(name);
                    umail.setText(email);
                Picasso.with(this)
                        .load(photoUrl)
                        .transform(new CircleTransform())
                        .into(profile_pic);
            }
        }

    }

    private class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }
        @Override
        public String key() {
            return "circle";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verifyacc:
                sendEmailVerification();
                break;
        }

    }

    private void sendEmailVerification() {
        // Disable button
        verify.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        if (task.isSuccessful()) {
                            Toast.makeText(acc_details.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            verify.setEnabled(true);
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(acc_details.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
}
