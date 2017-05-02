package trainedge.zapdiet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import trainedge.zapdiet.R;



public class HomeFragment extends Fragment {


    private FirebaseDatabase dbinstance;
    private DatabaseReference dbref;
    private double bmi;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        String bmistr = FirebaseDatabase.getInstance().getReference("users").child(uid).child("BMI").toString();
        try {
            bmi = Double.parseDouble(bmistr);
        }
        catch(NumberFormatException n){
            Toast.makeText(getContext(), n.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String val = null;
        final TextView brea = (TextView) view.findViewById(R.id.breakfast1);
        final TextView morn = (TextView) view.findViewById(R.id.mornsnacks1);
        final TextView lunc = (TextView) view.findViewById(R.id.lunch2);
        final TextView even = (TextView) view.findViewById(R.id.evensnacks2);
        final TextView din = (TextView) view.findViewById(R.id.dinner2);
        if (bmi < 18.5) {
            val = "Weight_Gain";
        } else if (bmi >= 18.5 && bmi < 25) {

        } else if (bmi >= 25 && bmi < 30) {
            val = "Weight_Loss";
        } else if (bmi >= 30) {
            val = "Obesity";
        }
        dbinstance = FirebaseDatabase.getInstance();
        if (val != null) {
            dbref = dbinstance.getReference("Diet_Chart").child(val);
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        String breakfast = dataSnapshot.child("Breakfast").getValue().toString();
                        String morn_snack = dataSnapshot.child("Morning_Snack").getValue().toString();
                        String lunch = dataSnapshot.child("Lunch").getValue().toString();
                        String dinner = dataSnapshot.child("Dinner").getValue().toString();
                        String evening_snack = dataSnapshot.child("Evening_Snack").getValue().toString();
                        brea.setText(breakfast);
                        morn.setText(morn_snack);
                        lunc.setText(lunch);
                        even.setText(evening_snack);
                        din.setText(dinner);
                        Toast.makeText(getContext(), "Data Loaded", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }
}

