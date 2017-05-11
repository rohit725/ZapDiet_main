package trainedge.zapdiet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import trainedge.zapdiet.fragment.ChartFragment;
import trainedge.zapdiet.fragment.HomeFragment;
import trainedge.zapdiet.fragment.NutritionalFragment;
import trainedge.zapdiet.fragment.SearchFragment;
import trainedge.zapdiet.fragment.itemfragment;
import trainedge.zapdiet.fragment.userinput;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NutritionalFragment.OnFragmentInterActionListener, ChartFragment.OnFragmentInterActionListener {

    private static final String TAG = "Home Activity";
    private View headerView;
    private FragmentManager manager;
    private DatabaseReference mDatabase;
    private boolean found;
    private itemfragment fragiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ZapDiet");
        manager = getSupportFragmentManager();

        ImageView img = (ImageView) findViewById(R.id.noconn1);
        Button bt = (Button) findViewById(R.id.retry1);
        View v = findViewById(R.id.view1);

        found = false;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);  //set
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        CheckConnection c = new CheckConnection(v, img, bt);
        boolean check = c.checkconn();
        if (!check) {
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(userId)) {
                        found = true;
                    }
                }
                dialog.dismiss();
                if (found) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, new HomeFragment());
                    transaction.commit();
                    //Menu menuview = navigationView.getMenu();
                    //menuview.getItem(0).setChecked(true);
                } else {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, new userinput());
                    transaction.commit();
                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
                dialog.dismiss();
            }
        };
        mDatabase.addValueEventListener(postListener);
        headerView = navigationView.getHeaderView(0);
        updateui(headerView);
        LinearLayout ll = (LinearLayout) headerView.findViewById(R.id.llout);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accont = new Intent(home.this, acc_details.class);
                startActivity(accont);
            }
        });

    }

    private void updateui(View headerView) {
        ImageView profile = (ImageView) headerView.findViewById(R.id.profile_pi);
        TextView nam = (TextView) headerView.findViewById(R.id.nameu);
        TextView mil = (TextView) headerView.findViewById(R.id.mailu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String strname = user.getDisplayName();
        String strmail = user.getEmail();
        Uri picurl = user.getPhotoUrl();
        nam.setText(strname);
        mil.setText(strmail);
        Picasso.with(this)
                .load(picurl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .transform(new CircleTransform())
                .into(profile);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                makeSearchFragmentVisible(s);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchView.setIconified(true);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, new HomeFragment());
                    transaction.commit();
                }
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return true;
            }
        });
        return true;
    }


    private void makeSearchFragmentVisible(String s) {
        SearchFragment myFragment = SearchFragment.newInstance(s);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, myFragment, "searchFragment");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingintent = new Intent(home.this, Settings.class);
            startActivity(settingintent);
            return true;
        }
        if (id == R.id.feedback) {
            Intent feedback = new Intent(home.this, Feedback.class);
            startActivity(feedback);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, new HomeFragment());
            transaction.commit();
        } else if (id == R.id.diet) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, new ChartFragment());
            transaction.commit();
        } else if (id == R.id.info) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, new NutritionalFragment());
            transaction.commit();

        }

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
