package trainedge.zapdiet.fragment;


import android.content.Context;
import android.net.Uri;
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


public class NutritionalFragment extends Fragment {

    private List<HeadingsModel> CategoryList;
    private RecyclerView rvCategory;


    private NutritionalFragment.OnFragmentInterActionListener mListener;


    public NutritionalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutritional, container, false);

        CategoryList = new ArrayList<>();

        rvCategory = (RecyclerView) view.findViewById(R.id.myrecycle);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvCategory.setLayoutManager(manager);

        generateCategories();

        return view;
    }

    private void generateCategories() {
        CategoryList.add(new HeadingsModel("Cereal Grains and Pasta", R.drawable.pasta));
        CategoryList.add(new HeadingsModel("Dairy and Egg Products", R.drawable.egg));
        CategoryList.add(new HeadingsModel("Fats and Oils", R.drawable.fats));
        CategoryList.add(new HeadingsModel("Fruits and Fruits Juices", R.drawable.fruits));
        CategoryList.add(new HeadingsModel("Legume Products", R.drawable.legumes));
        CategoryList.add(new HeadingsModel("Non-Veg Items", R.drawable.meat));
        CategoryList.add(new HeadingsModel("Nuts and Seeds", R.drawable.nuts));
        CategoryList.add(new HeadingsModel("Spices and Herbs", R.drawable.herb));
        CategoryList.add(new HeadingsModel("Vegetables", R.drawable.vegetable));
        HeadingsAdapter adapter = new HeadingsAdapter(getActivity(), CategoryList);
        rvCategory.setAdapter(adapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInterActionListener) {
            mListener = (NutritionalFragment.OnFragmentInterActionListener) context;
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
