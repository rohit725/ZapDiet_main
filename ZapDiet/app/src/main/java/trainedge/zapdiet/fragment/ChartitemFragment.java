package trainedge.zapdiet.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import trainedge.zapdiet.R;


public class ChartitemFragment extends Fragment {

    private String diet;
    private String val;
    private FirebaseDatabase dbinstance;
    private DatabaseReference dbref;

    public ChartitemFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_chartitem, container, false);

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();
        dbinstance = FirebaseDatabase.getInstance();
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
                    TextView dietfo = (TextView) view.findViewById(R.id.dietfor);
                    TextView breakf = (TextView) view.findViewById(R.id.breakfast);
                    TextView mornsnack = (TextView) view.findViewById(R.id.morn_snacks);
                    TextView lunc = (TextView) view.findViewById(R.id.lunch);
                    TextView evensnack = (TextView) view.findViewById(R.id.evening_snacks);
                    TextView dinn = (TextView) view.findViewById(R.id.dinner);
                    if(diet != null){
                        dietfo.setText(diet);
                    }
                    breakf.setText(breakfast);
                    mornsnack.setText(morn_snack);
                    lunc.setText(lunch);
                    evensnack.setText(evening_snack);
                    dinn.setText(dinner);
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Data Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public  static  ChartitemFragment newInstance(String str) {
        ChartitemFragment fragment= new ChartitemFragment();
        Bundle args = new Bundle();
        args.putString("nutritional", str);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            diet = getArguments().getString("nutritional");
            val = null;
            if(diet.contains("Obesity")){
                val = "Obesity";
            }
            else if(diet.contains("Weight Gain")){
                val = "Weight_Gain";
            }
            else if(diet.contains("Weight Loss")){
                val = "Weight_Loss";
            }
        }
    }

    public interface OnFragmentInterActionListener {
    }
}
