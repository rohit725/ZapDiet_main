package trainedge.zapdiet.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trainedge.zapdiet.R;

import static android.content.Context.MODE_PRIVATE;


public class ChartFragment extends Fragment {
    private List<HeadingsModel> CategoryList;
    private RecyclerView rvCategory;


    private ChartFragment.OnFragmentInterActionListener mListener;
    private SharedPreferences preferences;

    public ChartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        CategoryList = new ArrayList<>();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("InThrough","ChartFragment");
        editor.apply();

        rvCategory = (RecyclerView) view.findViewById(R.id.myrecycle1);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvCategory.setLayoutManager(manager);


        generateCategories();

        return view;
    }

    private void generateCategories() {
        CategoryList.add(new HeadingsModel("Obesity", R.drawable.dietchart));
        CategoryList.add(new HeadingsModel("Weight Gain", R.drawable.dietchart));
        CategoryList.add(new HeadingsModel("Weight Loss", R.drawable.dietchart));
        HeadingsAdapter adapter = new HeadingsAdapter(getActivity(), CategoryList);
        rvCategory.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChartFragment.OnFragmentInterActionListener) {
            mListener = (ChartFragment.OnFragmentInterActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInterActionListener {
    }

}
