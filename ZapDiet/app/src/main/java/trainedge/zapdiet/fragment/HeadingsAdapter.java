package trainedge.zapdiet.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trainedge.zapdiet.R;

class HeadingsAdapter extends RecyclerView.Adapter<HeadingHolder>{
    private List<HeadingsModel> CategoryList;

    public HeadingsAdapter(List<HeadingsModel> categoryList) {
        CategoryList = categoryList;
    }

    @Override
    public HeadingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row=((LayoutInflater)parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.simple_nutritional_headings,parent,false);
        return new HeadingHolder(row);
    }

    @Override
    public void onBindViewHolder(HeadingHolder holder, int position) {
        final HeadingsModel categoryModel = CategoryList.get(position);
        holder.itmnam.setText(categoryModel.label);
        holder.itmimg.setImageResource(categoryModel.img);
    }

    @Override
    public int getItemCount() {

        return CategoryList.size();
    }
}
