package trainedge.zapdiet.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;

import trainedge.zapdiet.R;

public class itemfragment extends Fragment {
    private ArrayList<InfoModel> InfoList;
    private RecyclerView rvItem;
    private FirebaseDatabase dbinstance;
    private DatabaseReference dbref;
    private String nutri;
    private String val;
    private String searchstr;

    public itemfragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item, container, false);

        InfoList = new ArrayList<>();
        dbinstance = FirebaseDatabase.getInstance();
        if (!(val == null)) {
            dbref = dbinstance.getReference("Nutritional_Info").child(val);
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    InfoList.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            InfoList.add(new InfoModel(snapshot));
                        }
                        Snackbar.make(view, "Data shown here is for 100 gram each.", Snackbar.LENGTH_LONG).show();
                    } else if (InfoList.size() == 0) {
                        Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        rvItem = (RecyclerView) view.findViewById(R.id.rv_itm);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvItem.setLayoutManager(manager);
        InfoAdapter adapter = new InfoAdapter(getActivity(), InfoList);
        rvItem.setAdapter(adapter);

        return view;
    }

    public static itemfragment newInstance(String str) {
        itemfragment fragment = new itemfragment();
        Bundle args = new Bundle();
        args.putString("nutritional", str);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nutri = getArguments().getString("nutritional");
            val = null;
            if (nutri.contains("Cereal")) {
                val = "Cereal_Grain_and_Pasta";
            } else if (nutri.contains("Dairy")) {
                val = "Dairy_and_Egg_Production";
            } else if (nutri.contains("Fats")) {
                val = "Fats_and_Oils";
            } else if (nutri.contains("Fruits")) {
                val = "Fruits_and_Fruits_Juices";
            } else if (nutri.contains("Legume")) {
                val = "Legume_and_Products";
            } else if (nutri.contains("Non-Veg")) {
                val = "Non_Veg_Products";
            } else if (nutri.contains("Nuts")) {
                val = "Nuts_and_Seeds";
            } else if (nutri.contains("Spices")) {
                val = "Spices_and_Herbs";
            } else if (nutri.contains("Vegetables")) {
                val = "Vegetables";
            } else {
                searchstr = nutri;
            }
        }
    }

    public void savedata(final String str) {
        searchstr = str;
        dbref = dbinstance.getReference("Nutritional_Info");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoList.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.child("Item").getValue().toString().contains(str)) {
                                InfoList.add(new InfoModel(snapshot1));
                            }
                        }
                    }
                } else if (InfoList.size() == 0) {
                    Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < InfoList.size(); i++) {
                    String a = InfoList.get(i).item.toString();
                    CharSequence b =highlightText(searchstr, a);
                    InfoList.get(i).setItem(b);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        rvItem = (RecyclerView) getView().findViewById(R.id.rv_itm);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvItem.setLayoutManager(manager);
        InfoAdapter adapter = new InfoAdapter(getActivity(), InfoList);
        rvItem.setAdapter(adapter);

    }

    public static CharSequence highlightText(String search, String originalText) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(search);
            if (start < 0) {
                return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(), originalText.length());
                    highlighted.setSpan(new ForegroundColorSpan(Color.parseColor("#109f02")), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(search, spanEnd);
                }
                return highlighted;
            }
        }
        return originalText;
    }
}
