package trainedge.zapdiet.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trainedge.zapdiet.R;


class HeadingsAdapter extends RecyclerView.Adapter<HeadingHolder> {
    private List<HeadingsModel> CategoryList;
    Context context;
    private SharedPreferences preferences;

    public HeadingsAdapter(Context context, List<HeadingsModel> categoryList) {
        CategoryList = categoryList;
        this.context = context;
    }

    @Override
    public HeadingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = ((LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.simple_nutritional_headings, parent, false);
        return new HeadingHolder(row);
    }

    @Override
    public void onBindViewHolder(HeadingHolder holder, int position) {
        final HeadingsModel categoryModel = CategoryList.get(position);
        holder.itmnam.setText(categoryModel.label);
        holder.itmimg.setImageResource(categoryModel.img);
        final String str = categoryModel.label;
        holder.rcview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                String inthrough=preferences.getString("InThrough","NutritionalFragment").toString();
                if(inthrough.contains("NutritionalFragment")){
                    itemfragment item = itemfragment.newInstance(str);
                    FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, item);
                    transaction.addToBackStack("list");
                    transaction.commit();
                }
                else if(inthrough.contains("ChartFragment")){
                    ChartitemFragment item = ChartitemFragment.newInstance(str);
                    FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, item);
                    transaction.addToBackStack("list");
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return CategoryList.size();
    }
}
