package trainedge.zapdiet.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import trainedge.zapdiet.R;


public class InfoHolder extends RecyclerView.ViewHolder {

    TextView itmnam;
    TextView fat;
    TextView kcal;
    TextView proteins;
    TextView carb;

    public InfoHolder(View itemView) {
        super(itemView);
        itmnam = (TextView) itemView.findViewById(R.id.itm_name);
        fat = (TextView) itemView.findViewById(R.id.itm_fat);
        kcal = (TextView) itemView.findViewById(R.id.itm_kcal);
        proteins = (TextView) itemView.findViewById(R.id.itm_protein);
        carb = (TextView) itemView.findViewById(R.id.itm_carb);
    }
}
