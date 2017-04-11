package trainedge.zapdiet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class acc_details extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Account Details";
    private TextView uname;
    private ImageView profile_pic;
    private TextView umail;
    private Button verify;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

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
        ImageView editn = (ImageView) findViewById(R.id.edit_name);
        verify = (Button) findViewById(R.id.verifyacc);

        updateui(FirebaseAuth.getInstance().getCurrentUser());
        verify.setOnClickListener(this);
        editn.setOnClickListener(this);
        profile_pic.setOnClickListener(this);
        verify.setVisibility(View.GONE);
    }

    private void updateui(FirebaseUser user) {
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        String providerId = user.getProviderId();
        uname.setText(name);
        umail.setText(email);
        Picasso.with(this)
                .load(photoUrl)
                .transform(new CircleTransform())
                .into(profile_pic);

        if(providerId.contains("email")) {
            boolean emailVerified = user.isEmailVerified();
            if (!emailVerified) {
                verify.setVisibility(View.VISIBLE);
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
            case R.id.user_profile_photo:
                showFileChooser();
                break;
            case R.id.edit_name:
                forName();
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

    private void updateProfile(String name,Uri photourl){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photourl)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(acc_details.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                            updateui(FirebaseAuth.getInstance().getCurrentUser());
                        }
                    }
                });
    }

    private void showFileChooser(){
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(acc_details.this);
        builder.setTitle("Add Profile Pic");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    /*Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void forName(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        et.setLayoutParams(lp);
        alertDialogBuilder.setView(et);
        alertDialogBuilder.setTitle("Enter your name.");
        alertDialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String username = et.getText().toString();
                Uri url= FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                updateProfile(username, url);
                updateui(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    updateProfile(username, selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    updateProfile(username, selectedImage);
                }
                break;
        }
        updateui(FirebaseAuth.getInstance().getCurrentUser());
    }
}
