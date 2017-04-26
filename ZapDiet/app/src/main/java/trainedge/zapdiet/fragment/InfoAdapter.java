package trainedge.zapdiet.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import trainedge.zapdiet.R;


public class InfoAdapter extends RecyclerView.Adapter<InfoHolder> {

    private ArrayList<InfoModel> infolst;
    Context context;

    public InfoAdapter(Context context,ArrayList<InfoModel> infolist){
        this.infolst = infolist;
        this.context =context;
    }

    @Override
    public InfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row=((LayoutInflater)parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.simple_nutritional_item,parent,false);
        return new InfoHolder(row);
    }

    @Override
    public void onBindViewHolder(InfoHolder holder, int position) {
        //still remained
        final InfoModel InfoModel = infolst.get(position);
        holder.itmnam.setText(InfoModel.item);
        holder.fat.setText(InfoModel.fat+"gm");
        holder.kcal.setText(InfoModel.cal);
        holder.proteins.setText(InfoModel.protein+"gm");
        holder.carb.setText(InfoModel.carb+"gm");
    }

    @Override
    public int getItemCount() {
        return infolst.size();
    }
}
