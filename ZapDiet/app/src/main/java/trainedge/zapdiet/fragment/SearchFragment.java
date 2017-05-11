package trainedge.zapdiet.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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


public class SearchFragment extends Fragment {
    private String searchstr;
    private ArrayList<InfoModel> SearchList;
    private RecyclerView rvItem;
    private View view;
    private String search;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String str) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("search", str);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            if (!getArguments().isEmpty()) {
                search = getArguments().getString("search");
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item, container, false);
        rvItem = (RecyclerView) view.findViewById(R.id.rv_itm);
        searchData();
        return view;
    }

    public void searchData() {
        searchstr = search;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvItem.setLayoutManager(manager);
        SearchList = new ArrayList<>();
        InfoAdapter adapter = new InfoAdapter(getActivity(), SearchList);
        rvItem.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference("Nutritional_Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SearchList.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.child("Item").getValue().toString().contains(search)) {
                                SearchList.add(new InfoModel(snapshot1));
                            }
                        }
                    }
                    dataSnapshot.getRef().removeEventListener(this);
                } else if (SearchList.size() == 0) {
                    Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < SearchList.size(); i++) {
                    String a = SearchList.get(i).item.toString();
                    CharSequence b = highlightText(searchstr, a);
                    SearchList.get(i).setItem(b);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
